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
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
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

import me.wobblyyyy.edt.DynamicArray;

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
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
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
     * Front-right distance travelled.
     */
    private double distance_fr;

    /**
     * Power for the front-right wheel.
     */
    private double power_fr;

    /**
     * Heading for the front-right wheel.
     */
    private double heading_fr;

    /**
     * Front-left distance travelled.
     */
    private double distance_fl;

    /**
     * Power for the front-left wheel.
     */
    private double power_fl;

    /**
     * Heading for the front-left wheel.
     */
    private double heading_fl;

    /**
     * Back-right distance travelled.
     */
    private double distance_br;

    /**
     * Power for the back-right wheel.
     */
    private double power_br;

    /**
     * Heading for the back-right wheel.
     */
    private double heading_br;

    /**
     * Back-left distance travelled.
     */
    private double distance_bl;

    /**
     * Power for the back-left wheel.
     */
    private double power_bl;

    /**
     * Heading for the back-left wheel.
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
     */
    public SwerveFollower(DynamicArray<HeadingPoint> points,
                          Odometry odometry,
                          Drive drive,
                          double gapX,
                          double gapY,
                          RobotProfile profile) {
        if (!(odometry instanceof Tracker)) throw new
                IllegalArgumentException("You didn't pass a tracker!");
        if (!(drive instanceof Swerve)) throw new IllegalArgumentException(
                "You didn't pass SwerveFollower a Swerve drivetrain!");

        DynamicArray<Waypoint> wps = new DynamicArray<>();

        points.itr().forEach(point -> wps.add(Jaci.getWaypoint(point)));

        waypoints = new Waypoint[wps.size()];

        wps.itr().forEach(point -> waypoints[wps.itr().index()] = point);

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
     *
     * @see Distance#getDistance(Point, Point)
     * @see DistanceFollower#calculate(double)
     * @see DistanceFollower#getHeading()
     */
    @Override
    public void update() {
        try {
            distance_fr = Distance.getDistance(origin, odometry.getFrPos());
            power_fr = frf.calculate(distance_fr);
            heading_fr = frf.getHeading();
            distance_fl = Distance.getDistance(origin, odometry.getFlPos());
            power_fl = flf.calculate(distance_fl);
            heading_fl = flf.getHeading();
            distance_br = Distance.getDistance(origin, odometry.getBrPos());
            power_br = brf.calculate(distance_br);
            heading_br = brf.getHeading();
            distance_bl = Distance.getDistance(origin, odometry.getBlPos());
            power_bl = blf.calculate(distance_bl);
            heading_bl = blf.getHeading();
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
     *
     * @see Trajectory
     * @see DistanceFollower
     * @see SwerveModifier
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

                    follower = new DistanceFollower(t);

                    /*
                     * Generate individual trajectories and enable them.
                     */
                    SwerveModifier mod = new SwerveModifier(t).modify(
                            gapX,
                            gapY,
                            SWERVE_MODE
                    );

                    Trajectory frT = mod.getFrontRightTrajectory();
                    Trajectory flT = mod.getFrontLeftTrajectory();
                    Trajectory brT = mod.getBackRightTrajectory();
                    Trajectory blT = mod.getBackLeftTrajectory();

                    frf = new DistanceFollower(frT);
                    flf = new DistanceFollower(flT);
                    brf = new DistanceFollower(brT);
                    blf = new DistanceFollower(blT);

                    if (origin == null) origin = odometry.getPos();
                },
                "SwerveFollowerCalculator"
        );

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
     * @see DistanceFollower#isFinished()
     */
    @Override
    public boolean isDone() {
        try {
            return follower.isFinished();
        } catch (Exception ignored) {
            return false;
        }
    }
}
