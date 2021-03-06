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

import me.wobblyyyy.pathfinder.core.Follower;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.util.Distance;
import me.wobblyyyy.pathfinder.util.MotionSnapshot;
import me.wobblyyyy.pathfinder.util.RobotProfile;

import java.util.HashMap;

/**
 * One of the more primitive types of path following systems.
 *
 * <p>
 * Due to my lack of experience with any form of math that a 3rd grader
 * couldn't handle with ease, this thing's a little bit broken as of now.
 * Shorter paths - paths that don't allow the robot to fully accelerate and
 * decelerate - are run linearly with a slower speed coefficient. This needs
 * to be addressed in the near future if this is to be a viable pathfinding
 * follower, but I don't know what to do as of now.
 * </p>
 *
 * <p>
 * As with all other types of followers, this should be instantiated for each
 * trajectory the robot is supposed to follow.
 * </p>
 *
 * <p>
 * Follow a path based on optimal acceleration and deceleration rates. These
 * values should be calculated with some form of utility and are best notated
 * as...
 * <ul>
 *     <li>Time (seconds) to go from a stop to max speed.</li>
 *     <li>Time (seconds) to go from max speed to a stop.</li>
 *     <li>Distance (inches) that the robot travels to accelerate.</li>
 *     <li>Distance (inches) that the robot travels to decelerate.</li>
 *     <li>The robot's maximum speed (feet per second).</li>
 * </ul>
 * </p>
 *
 * <p>
 * If any of these values are inaccurate, the robot's pathfinding ability
 * will be hindered significantly. The more accurate these values are, the
 * more efficient the following system will be.
 * </p>
 *
 * <p>
 * Although the name would, in fact, suggest that this is a proportional
 * integral derivative follower, this is only mostly a proportional integral
 * derivative follower. Proportional is the most important part here - the
 * robot's power is (almost) directly proportional to how far from the target
 * the robot is.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class PIDFollower implements Follower {
    /**
     * All of the follower's motion plan.
     */
    private HashMap<Long, MotionSnapshot> motion = new HashMap<>();

    /**
     * A reference to the robot's motion profile.
     */
    private final RobotProfile profile;

    /**
     * The time at which the PID follower should stop.
     */
    private long stop;

    /**
     * Whether or not the PID follower has stopped yet.
     */
    private boolean isStopped = false;

    /**
     * A reference to the drivetrain that the robot uses. csgo
     */
    private final Drive drive;

    /**
     * The follower's current start point.
     */
    private HeadingPoint start;

    /**
     * The follower's current end point.
     */
    private HeadingPoint end;

    /**
     * The total distance between the two points.
     */
    private double distance;

    /**
     * The power coefficient of the drivetrain.
     */
    private double power;

    /**
     * Create a new PID follower.
     *
     * @param profile the robot's motion profile.
     */
    public PIDFollower(Drive drive,
                       RobotProfile profile,
                       HeadingPoint start,
                       HeadingPoint end) {
        this.drive = drive;
        this.profile = profile;
        this.start = start;
        this.end = end;
    }

    /**
     * Update the follower's drive values.
     */
    @Override
    public synchronized void update() {
        HashMap<Long, MotionSnapshot> newMap = new HashMap<>();

        for (HashMap.Entry<Long, MotionSnapshot> e : motion.entrySet()) {
            if (e.getKey() < System.currentTimeMillis()) {
                MotionSnapshot s = e.getValue();
                this.start = s.getStart();
                this.end = s.getEnd();
                this.power = s.getPower();
            } else {
                newMap.put(
                        e.getKey(),
                        e.getValue()
                );
            }
        }

        motion = newMap;

        if (System.currentTimeMillis() > stop) {
            isStopped = true;
        }
    }

    /**
     * Perform all of the calculations needed to follow the trajectory.
     *
     * <p>
     * There's a 95% chance that something inside of this wretched method is
     * wrong. I don't know what it is yet. But I'm sure I'll find out soon
     * enough when everyone is asking me why the robot isn't capable of
     * human-level emotion and flight.
     * </p>
     *
     * <p>
     * Let me just say this now - there is absolutely not a chance I'm going
     * to ever look at this code for so much as a second more than I have to.
     * If you're looking through this file, and you know how to write code in
     * Java, PLEASE re-do this entire method - it sucks.
     * </p>
     */
    @Override
    public synchronized void calculate() {
        /*
         * Get the distance and angle the robot has to move at.
         *
         * The angle is NOT the heading - the angle is the angle that
         * the robot actually has to move at.
         *
         * Math.atan2() returns a measurement in radians - we want to use
         * degrees. To convert between the two, we multiply by 180/PI.
         */
        distance = Distance.getDistance(start, end);
        double angle = Math.atan2(
                end.getY() - start.getY(),
                end.getX() - start.getX()
        ) * 180 / Math.PI;

        /*
         * Minimum time and distance it takes for the robot to move based
         * on acceleration profiles.
         *
         * If the robot doesn't have enough time or space to fully accelerate
         * and decelerate, we can just use a much simpler proportional
         * controller. I'm WAY too lazy to learn physics.
         */
        double minDistance =
                profile.getAccelerationDistance() +
                        profile.getDecelerationDistance();
        double minTime =
                profile.getAccelerationTime() +
                        profile.getDecelerationTime();

        /*
         * Max speed values.
         * msDistance is a measure of how many feet are travelled while not
         * accelerating or decelerating.
         * msTime is a measure of how many seconds are needed for the
         * robot to traverse the msDistance.
         */
        double msDistance =
                distance - (
                        profile.getAccelerationDistance() +
                        profile.getDecelerationDistance());
        double msTime =
                msDistance / profile.getMaxSpeed();

        /*
         * The robot doesn't ever reach its maximum speed - its either
         * accelerating or decelerating at all points during the curve.
         * In this case, our best option is to accelerate until the
         * deceleration functionality needs to kick in for the robot
         * to stop at its target.
         */
        if (msDistance < 0) {
            long t_start = System.currentTimeMillis();
            long t_decel = (long) (t_start + (profile.getAccelerationTime() * 1000));

            HeadingPoint decelPoint = (HeadingPoint) Distance.inDirection(
                    new HeadingPoint(
                            end.getX(),
                            end.getY(),
                            end.getHeading() + 180.0
                    ),
                    profile.getDecelerationDistance()
            );

            MotionSnapshot start = new MotionSnapshot(
                    this.start,
                    decelPoint,
                    1.0
            );
            MotionSnapshot decel = new MotionSnapshot(
                    decelPoint,
                    this.end,
                    0.0
            );

            motion.put(
                    t_start,
                    start
            );
            motion.put(
                    t_decel,
                    decel
            );

            /*
             * As of now, this stopping distance is probably wildly inaccurate.
             * This just assumes the robot is traveling at 3/4 of its max speed
             * and sets a stop target time from there.
             */
            this.stop = (long) (
                    (
                            (distance / (profile.getMaxSpeed() * 0.75
                            ) * 2
                            ) * 1000
                    ) + System.currentTimeMillis());

            return;
        }

        /*
         * How long does EVERYTHING take when put together?
         */
        double totalTime = msDistance > 0 ?
                minTime + msTime :
                0;

        /*
         * Create time targets for certain events.
         *
         * All of this information is based on the RobotProfile that the
         * PathfinderConfig that's used here has. If the RobotProfile isn't
         * accurate, these won't be either - times will be off and performance
         * will likely be hindered quite a bit.
         */
        long t_start = System.currentTimeMillis();
        long t_accel = (long) (t_start + (profile.getAccelerationTime() * 1000));
        long t_decel = (long) (t_accel + (msTime * 1000));
        long t_stop  = (long) (t_decel + (profile.getDecelerationTime() * 1000));

        /*
         * Define all of the key points we have to be at.
         *
         * It took FOREVER to get these mostly correct.
         *
         * These points should be based off the ANGLE double that's defined
         * earlier in this method, NOT the ending or starting heading.
         */
        HeadingPoint p_start = this.start;
        HeadingPoint p_accel = (HeadingPoint) Distance.inDirection(
                new HeadingPoint(
                        start.getX(),
                        start.getY(),
                        angle
                ),
                profile.getAccelerationDistance()
        );
        HeadingPoint p_decel = (HeadingPoint) Distance.inDirection(
                new HeadingPoint(
                        p_accel.getX(),
                        p_accel.getY(),
                        angle
                ),
                msDistance
        );
        HeadingPoint p_stop = this.end;
        HeadingPoint p_ft = new HeadingPoint(
                p_stop.getX(),
                p_stop.getY(),
                end.getHeading()
        );

        /*
         * MotionSnapshot is a wrapper class for storing information about
         * a drivetrain's temporary drive target and power.
         */
        MotionSnapshot start = new MotionSnapshot(
                p_start,
                p_accel,
                1.0
        );
        MotionSnapshot max = new MotionSnapshot(
                p_accel,
                p_decel,
                1.0
        );
        MotionSnapshot decel = new MotionSnapshot(
                p_decel,
                p_stop,
                0.0
        );
        MotionSnapshot stop = new MotionSnapshot(
                p_stop,
                p_ft,
                0.5
        );

        /*
         * Add the motion snapshots and times to the Motion hashmap.
         */
        motion.put(
                t_start,
                start
        );
        motion.put(
                t_accel,
                max
        );
        motion.put(
                t_decel,
                decel
        );

        /*
         * Give the robot an extra 500ms (0.5 seconds) to turn after it gets
         * to its target destination.
         */
        this.stop = t_stop + 500;
    }

    /**
     * Drive the robot based on the follower's knowledge of what the
     * hell is going on.
     *
     * <p>
     * Because I can assure you - I most certainly don't know.
     * </p>
     */
    @Override
    public synchronized void drive() {
        drive.drive(
                this.start,
                this.end,
                this.power
        );
    }

    /**
     * Is the PID follower done with its execution yet?
     *
     * <p>
     * A finished follower should no longer be active - as a result, we need
     * to... well, de-activate it.
     * </p>
     *
     * @return whether or not the follower has finished its execution.
     */
    @Override
    public boolean isDone() {
        return isStopped;
    }
}
