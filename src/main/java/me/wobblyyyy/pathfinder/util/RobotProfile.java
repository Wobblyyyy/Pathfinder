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

package me.wobblyyyy.pathfinder.util;

/**
 * A robot's movement profile.
 *
 * <p>
 * Motion profiles are still a work-in-progress here. With that being said,
 * motion profiles SHOULD (hopefully, that is) allow the robot to accurately
 * follow pre-defined paths and routes.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class RobotProfile {
    /**
     * How long, in seconds, the robot takes to accelerate.
     *
     * <p>
     * Acceleration time is defined as the interval of time that passes
     * between when acceleration begins and when the robot reaches its
     * maximum speed. This maximum speed is up to you to decide - however,
     * in order for certain follower profiles to work, this value must be as
     * close to accurate as possible.
     * </p>
     */
    private final double accelerationTime;

    /**
     * How long, in seconds, the robot takes to decelerate.
     *
     * <p>
     * Deceleration time, like acceleration time, is defined as the interval
     * of time between two of the robot's states of motions. In the case of
     * deceleration, the amount of time between when the robot, at full speed,
     * stops accelerating, and when the robot actually reaches a complete stop.
     * </p>
     */
    private final double decelerationTime;

    /**
     * How much distance the robot takes to accelerate.
     *
     * <p>
     * Acceleration distance is defined as the amount of space, in inches, that
     * the robot requires to drive in a straight line and accelerate to its
     * maximum speed.
     * </p>
     *
     * <p>
     * Although I can't force you to, these measurements should be in inches.
     * Doing them in any other measurement of distance might break something.
     * </p>
     */
    private final double accelerationDistance;

    /**
     * How much distance the robot takes to decelerate.
     *
     * <p>
     * Deceleration distance is defined as the amount of distance the robot
     * requires to go from its maximum speed to a full and complete stop.
     * </p>
     *
     * <p>
     * Although I can't force you to, these measurements should be in inches.
     * Doing them in any other measurement of distance might break something.
     * </p>
     */
    private final double decelerationDistance;

    /**
     * The maximum speed of the robot, measured as feet per second.
     *
     * <p>
     * Max speed should be the most accurate possible estimate of the robot's
     * maximum speed.
     * </p>
     */
    private final double maxSpeed;

    /**
     * The robot's jerk value.
     *
     * <p>
     * In all honesty, I have absolutely no idea what the hell jerk is. I'll
     * update this documentation when I eventually do figure out what that
     * means.
     * </p>
     */
    private final double jerk;

    /**
     * Create a robot profile.
     *
     * <p>
     * If you're confused about what any of this means, you should go
     * read the JavaDoc in this file - it contains everything you need to know.
     * </p>
     *
     * @param accelerationTime     acceleration time (stop to max speed),
     *                             measured in seconds.
     * @param decelerationTime     deceleration time (max speed to stop),
     *                             measured in seconds.
     * @param accelerationDistance how far the robot travels while its
     *                             accelerating.
     * @param decelerationDistance how far the robot travels while its
     *                             decelerating.
     * @param maxSpeed             the maximum speed of the robot, measured
     *                             in terms of feet per second.
     */
    public RobotProfile(double accelerationTime,
                        double decelerationTime,
                        double accelerationDistance,
                        double decelerationDistance,
                        double maxSpeed,
                        double jerk) {
        this.accelerationTime = accelerationTime;
        this.decelerationTime = decelerationTime;
        this.accelerationDistance = accelerationDistance;
        this.decelerationDistance = decelerationDistance;
        this.maxSpeed = maxSpeed;
        this.jerk = jerk;
    }

    /**
     * Get the robot's acceleration time. (seconds)
     *
     * @return the robot's acceleration time. (in second)
     */
    public double getAccelerationTime() {
        return accelerationTime;
    }

    /**
     * Get the robot's deceleration time. (seconds)
     *
     * @return the robot's deceleration time. (in seconds)
     */
    public double getDecelerationTime() {
        return decelerationTime;
    }

    /**
     * Get the robot's acceleration distance. (feet)
     *
     * @return the amount of distance the robot needs to cover to accelerate.
     */
    public double getAccelerationDistance() {
        return accelerationDistance;
    }

    /**
     * Get the robot's deceleration distance. (feet)
     *
     * @return the amount of distance the robot needs to cover to decelerate.
     */
    public double getDecelerationDistance() {
        return decelerationDistance;
    }

    /**
     * Get the robot's maximum speed. (feet per second)
     *
     * @return the robot's maximum speed. (feet per second)
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Get the robot's jerk value.
     *
     * @return the robot's maximum jerk.
     */
    public double getJerk() {
        return jerk;
    }
}
