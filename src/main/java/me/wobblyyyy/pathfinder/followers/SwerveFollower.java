/*
 *  ======================================================================
 *  || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 *  ||                                                                  ||
 *  || This file is part of the "Pathfinder" project, which is licensed ||
 *  || and distributed under the GPU General Public License V3.         ||
 *  ||                                                                  ||
 *  || Pathfinder is available on GitHub:                               ||
 *  || https://github.com/Wobblyyyy/Pathfinder                          ||
 *  ||                                                                  ||
 *  || Pathfinder's license is available:                               ||
 *  || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 *  ||                                                                  ||
 *  || Re-distribution of this, or any other files, is allowed so long  ||
 *  || as this same copyright notice is included and made evident.      ||
 *  ======================================================================
 */

package me.wobblyyyy.pathfinder.followers;

import jaci.pathfinder.PathfinderJNI;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.DistanceFollower;
import jaci.pathfinder.modifiers.SwerveModifier;
import me.wobblyyyy.pathfinder.core.Follower;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.drive.swerve.Swerve;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.tracking.Tracker;
import me.wobblyyyy.pathfinder.util.Distance;
import me.wobblyyyy.pathfinder.util.Jaci;
import me.wobblyyyy.pathfinder.util.RobotProfile;

import java.util.ArrayList;

/**
 * An implementation of Jaci's distance follower.
 *
 * <p>
 * Unlike every other type of follower, this one should take in as many
 * waypoints as possible and follow all of those. The purpose of this type
 * of path follower is to reduce stopping time to 0 - meaning the robot should
 * not stop at any point during the path.
 * </p>
 *
 * <p>
 * The configuration for this follower might need to be tweaked as well.
 * Ideally, we can add a parameter in the PathfinderConfig for a custom
 * configuration for this bad boy?
 * </p>
 *
 * <p>
 * This will probably be a debugging hotspot, at least for a little while,
 * following Pathfinder's launch. The most common issues are indubitably going
 * to be issues surrounding unit conversion. We have to, at different points in
 * this follower class, convert between...
 * <ul>
 *     <li>Inches and meters.</li>
 *     <li>Meters and inches.</li>
 *     <li>Degrees and radians.</li>
 *     <li>Radians and degrees.</li>
 *     <li>HeadingPoint instances and Waypoint instances.</li>
 *     <li>Odometry positions and trajectory distances.</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 */
public class SwerveFollower implements Follower {
    /**
     * How many samples the trajectory should have.
     */
    private static final int SAMPLES = 500;

    /**
     * The trajectory follower's default fit method.
     */
    private static final Trajectory.FitMethod FIT =
            Trajectory.FitMethod.HERMITE_CUBIC;

    /**
     * I'm not entirely sure what this variable is - I couldn't find any
     * documentation on it anywhere.
     *
     * <p>
     * It would appear that this has to do with the frequency of sampling -
     * the closer a DT value is to 0, the more points are generated in a
     * trajectory. I also saw the word "time scale" thrown around in the
     * original C code for Jaci's pathfinder.
     * </p>
     */
    private static final double DT = 0.05;

    /**
     * A generic trajectory follower.
     *
     * <p>
     * These should be re-generated based on robot values.
     * </p>
     */
//    private final Trajectory.Config CONFIG = new Trajectory.Config(
//            FIT,
//            SAMPLES,
//            DT,
//            VEL,
//            ACE,
//            JER
//    );
    private final RobotProfile profile;

    /**
     * The trajectory's configuration.
     */
    private final Trajectory.Config trajectoryConfig;

    /**
     * What type of swerve modifier should be used.
     */
    private static final SwerveModifier.Mode SWERVE_MODE =
            SwerveModifier.Mode.SWERVE_DEFAULT;

    /**
     * A reference to the positional tracker.
     */
    private final Tracker odometry;

    /**
     * A reference to the robot's drivetrain.
     */
    private final Swerve drive;

    /**
     * The gap, in terms of X, between the front wheel pair.
     */
    private final double gapX;

    /**
     * The gap, in terms of Y, between any side wheel pair.
     */
    private final double gapY;

    /**
     * An array of the waypoints to follow.
     */
    private final Waypoint[] waypoints;

    /**
     * The swerve chassis' follower.
     */
    private DistanceFollower follower;

    /**
     * The swerve chassis' FR follower.
     */
    private DistanceFollower frf;

    /**
     * The swerve chassis' FL follower.
     */
    private DistanceFollower flf;

    /**
     * The swerve chassis' BR follower.
     */
    private DistanceFollower brf;

    /**
     * The swerve chassis' BL follower.
     */
    private DistanceFollower blf;

    /**
     * The point of origin - in other words, what point the robot actually
     * starts its journey at.
     */
    private Point origin;

    /**
     * Distance (m) traveled by one of the swerve modules.
     */
    private double distance_fr;

    /**
     * The suggested drive power for one of the swerve modules.
     */
    private double power_fr;

    /**
     * The suggested heading for one of the swerve modules.
     */
    private double heading_fr;

    /**
     * Distance (m) traveled by one of the swerve modules.
     */
    private double distance_fl;

    /**
     * The suggested drive power for one of the swerve modules.
     */
    private double power_fl;

    /**
     * The suggested heading for one of the swerve modules.
     */
    private double heading_fl;

    /**
     * Distance (m) traveled by one of the swerve modules.
     */
    private double distance_br;

    /**
     * The suggested drive power for one of the swerve modules.
     */
    private double power_br;

    /**
     * The suggested heading for one of the swerve modules.
     */
    private double heading_br;

    /**
     * Distance (m) traveled by one of the swerve modules.
     */
    private double distance_bl;

    /**
     * The suggested drive power for one of the swerve modules.
     */
    private double power_bl;

    /**
     * The suggested heading for one of the swerve modules.
     */
    private double heading_bl;

    /**
     * Create a new swerve follower.
     *
     * <p>
     * Exceptions are thrown if illegal arguments are passed - a non-swerve
     * drivetrain, or a non-tracker odometry system, just as some examples.
     * </p>
     *
     * @param points   the swerve follower's waypoints. This list should be
     *                 ALL of the points that the robot needs to run by, not
     *                 just the start and the end.
     * @param odometry the robot's odometry subsystem, used for getting the
     *                 position of the robot and estimating the percent
     *                 completion of the trajectory.
     * @param drive    the robot's drivetrain - used, quite obviously, for
     *                 driving purposes.
     * @param gapX     wheelbase horizontal.
     * @param gapY     wheelbase vertical.
     * @param profile  the robot's motion profile.
     */
    public SwerveFollower(ArrayList<HeadingPoint> points,
                          Odometry odometry,
                          Drive drive,
                          double gapX,
                          double gapY,
                          RobotProfile profile) {
        if (!(odometry instanceof Tracker)) throw new
                IllegalArgumentException("You didn't pass a tracker!");
        if (!(drive instanceof Swerve)) throw new IllegalArgumentException(
                "You didn't pass SwerveFollower a Swerve drivetrain!");

        /*
         * Create an ArrayList of all of the waypoints Jaci's pathfinder
         * needs as input parameters.
         */
        ArrayList<Waypoint> wps = new ArrayList<>();

        /*
         * For each of the points in the path to follow from our Pathfinder,
         * create a waypoint using the getWaypoint method from the Jaci class
         * under the utils package.
         *
         * If there's any issues with math, or this function returning wildly
         * inaccurate values, you should go check out that message.
         */
        for (HeadingPoint p : points) {
            wps.add(Jaci.getWaypoint(p));
        }

        /*
         * Set the waypoint array to be a new array of the size of the
         * waypoint array list.
         */
        waypoints = new Waypoint[wps.size()];

        /*
         * For each of the waypoints, set an array object to be that waypoint.
         */
        for (Waypoint w : wps) {
            waypoints[wps.indexOf(w)] = w;
        }

        /*
         * Initialize constructor values.
         *
         * Remember, gapX and gapY must be converted to meters.
         */
        this.odometry = (Tracker) odometry;
        this.drive = (Swerve) drive;
        this.gapX = Distance.inchesToMeters(gapX);
        this.gapY = Distance.inchesToMeters(gapY);

        this.profile = profile;
        this.trajectoryConfig = new Trajectory.Config(
                FIT,
                SAMPLES,
                DT,
                profile.getMaxSpeed(),
                profile.getAccelerationTime(),
                profile.getJerk()
        );
    }

    /**
     * Update the swerve follower.
     *
     * <p>
     * Because we're not entirely sure that the distance, power, and heading
     * values can be determined yet, we surround our logic in a try-catch
     * statement.
     * </p>
     */
    @Override
    public void update() {
        /*
         * Try performing all of the calculations that we need to do.
         *
         * If the calculations don't work - meaning an error is thrown from
         * anywhere in this method - absolutely nothing happens and the
         * exception is ignored.
         *
         * We know that there will be NullPointerException issues with this
         * code at some point, because the update() code is run before the
         * calculation code may have finished, as that code is run on its
         * own thread to improve runtime performance.
         *
         * If you're debugging and trying to figure out what's going on here,
         * you may want to print the stacktrace of the exception that's thrown.
         */
        try {
            /*
             * Try actually calculating all of the values.
             *
             * If there's a math issue, there's a pretty decent chance that
             * its happening here - all of the conversions between degrees,
             * radians, inches, meters, etc, can be quite confusing.
             */

            /*
             * Distance, power, and heading for the FR.
             */
            distance_fr = Distance.inchesToMeters(
                    Distance.getDistance(origin, odometry.getFrPos()));
            power_fr = frf.calculate(distance_fr);
            heading_fr = Math.toRadians(frf.getHeading());

            /*
             * Distance, power, and heading for the FL.
             */
            distance_fl = Distance.inchesToMeters(
                    Distance.getDistance(origin, odometry.getFlPos()));
            power_fl = flf.calculate(distance_fl);
            heading_fl = Math.toRadians(flf.getHeading());

            /*
             * Distance, power, and heading for the BR.
             */
            distance_br = Distance.inchesToMeters(
                    Distance.getDistance(origin, odometry.getBrPos()));
            power_br = brf.calculate(distance_br);
            heading_br = Math.toRadians(brf.getHeading());

            /*
             * Distance, power, and heading for the BL.
             */
            distance_bl = Distance.inchesToMeters(
                    Distance.getDistance(origin, odometry.getBlPos()));
            power_bl = blf.calculate(distance_bl);
            heading_bl = Math.toRadians(blf.getHeading());
        } catch (Exception ignored) {
        }
    }

    /**
     * Asynchronously calculate the path's optimal trajectory.
     *
     * <p>
     * Trajectory generation is INCREDIBLY expensive, especially when the
     * sample size gets larger. To compensate, and to make sure the robot
     * doesn't slow to a halt during operation, calculations are done in
     * a separate thread and logic in the main thread is surrounded by try
     * and catch statements to ensure nothing goes wrong.
     * </p>
     */
    @Override
    public void calculate() {
        Thread calculator = new Thread(
                () -> {
                    /*
                     * Generate the primary trajectory and enable it.
                     */
                    Trajectory t = PathfinderJNI.generateTrajectory(
                            waypoints,
                            trajectoryConfig
                    );

                    /*
                     * Create a new over-follower, for the entire chassis.
                     *
                     * This is "wrong" and shouldn't actually be used - rather,
                     * the fr, fl, br, bl, etc, distance followers are much
                     * more accurate.
                     */
                    follower = new DistanceFollower(t);

                    /*
                     * Create the swerve modifier for the four independent
                     * trajectories and followers.
                     */
                    SwerveModifier mod = new SwerveModifier(t).modify(
                            /*
                             * Robot's wheelbase X
                             */
                            gapX,

                            /*
                             * Robot's wheelbase Y
                             */
                            gapY,

                            /*
                             * Swerve mode - default mode, don't rotate
                             * while turning. This can be changed later, but
                             * for now we should focus on just getting it
                             * working as intended.
                             */
                            SWERVE_MODE
                    );

                    /*
                     * Generate trajectories, and later distance followers, for
                     * each of the swerve drivetrain's swerve modules.
                     *
                     * Although having one trajectory is pretty cool and all,
                     * it isn't anywhere near as effective as having an
                     * individual trajectory and follower for each of the
                     * swerve modules. Yay!
                     */
                    Trajectory frT = mod.getFrontRightTrajectory();
                    Trajectory flT = mod.getFrontLeftTrajectory();
                    Trajectory brT = mod.getBackRightTrajectory();
                    Trajectory blT = mod.getBackLeftTrajectory();

                    frf = new DistanceFollower(frT);
                    flf = new DistanceFollower(flT);
                    brf = new DistanceFollower(brT);
                    blf = new DistanceFollower(blT);

                    /*
                     * If the origin is null, which it should be, the trajectory
                     * origin position is where the robot is currently located.
                     */
                    if (origin == null) origin = odometry.getPos();
                },

                /*
                 * The thread's name shouldn't really matter all that much
                 * because only one of them should be running at a time.
                 *
                 * If there's an issue, however, the thread's name can be
                 * removed, changed, etc - maybe use hash codes?
                 */
                "SwerveFollowerCalculator"
        );

        /*
         * Start the calculation thread.
         *
         * Threads are auto-closing, meaning that the thread will mark itself
         * for garbage collection following its execution.
         *
         * The thread only executes a single time, meaning that it should be
         * marked for garbage collection as soon as its done.
         */
        calculator.start();
    }

    /**
     * Move the drivetrain.
     *
     * <p>
     * Because we're not entirely sure that the distance, power, and heading
     * values can be determined yet, we surround our logic in a try-catch
     * statement.
     * </p>
     *
     * <p>
     * Any exceptions thrown here are, by default, ignored - we know we'll
     * encounter several NullPointerException issues by the very nature of
     * attempting to modify values that haven't been defined yet.
     * </p>
     */
    @Override
    public void drive() {
        try {
            drive.driveModule(
                    Swerve.Modules.FR,
                    heading_fr,
                    power_fr
            );
            drive.driveModule(
                    Swerve.Modules.FL,
                    heading_fl,
                    power_fl
            );
            drive.driveModule(
                    Swerve.Modules.BR,
                    heading_br,
                    power_br
            );
            drive.driveModule(
                    Swerve.Modules.BL,
                    heading_bl,
                    power_bl
            );
        } catch (Exception ignored) {
        }
    }

    /**
     * Has the follower finished its trajectory yet?
     *
     * @return whether or not the follower's execution is over.
     */
    @Override
    public boolean isDone() {
        /*
         * If we can't determine whether or not the follower has finished
         * its execution, we assume false to negate any potential null
         * pointer exceptions we may otherwise have.
         */
        try {
            return follower.isFinished();
        } catch (Exception ignored) {
            return false;
        }
    }
}
