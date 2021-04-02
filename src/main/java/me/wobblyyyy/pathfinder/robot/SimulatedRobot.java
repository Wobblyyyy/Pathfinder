/*
 * ======================================================================
 * || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 * ||                                                                  ||
 * || This file is part of the "Pathfinder" project, which is licensed ||
 * || and distributed under the GPU General Public License V3.         ||
 * ||                                                                  ||
 * || Pathfinder is available on GitHub:                               ||
 * || https://github.com/Wobblyyyy/Pathfinder                          ||
 * ||                                                                  ||
 * || Pathfinder's license is available:                               ||
 * || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 * ||                                                                  ||
 * || Re-distribution of this, or any other files, is allowed so long  ||
 * || as this same copyright notice is included and made evident.      ||
 * ||                                                                  ||
 * || Unless required by applicable law or agreed to in writing, any   ||
 * || software distributed under the license is distributed on an "AS  ||
 * || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 * || express or implied. See the license for specific language        ||
 * || governing permissions and limitations under the license.         ||
 * ||                                                                  ||
 * || Along with this file, you should have received a license file,   ||
 * || containing a copy of the GNU General Public License V3. If you   ||
 * || did not receive a copy of the license, you may find it online.   ||
 * ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.robot;

import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.time.Time;
import me.wobblyyyy.pathfinder.time.TimeUnit;

/**
 * A simulated robot. A robot that you can, in fact, simulate. You get the
 * point. This class allows you to simulate the movement of a robot by using
 * the inputted drive translations, a provided coefficient, and a constant
 * time factor to simulate movement, thus updating the simulated position
 * of the robot.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class SimulatedRobot implements Drive, Odometry {
    /**
     * Use milliseconds internally. Although ms is used as the time unit,
     * we end up converting ms to seconds later on. I don't know why I chose
     * to do it this way, but it works like this, and there's no point in
     * breaking it for no good reason.
     */
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECOND;

    /**
     * The position of the robot. The robot's position could actually be
     * represented better as a {@link me.wobblyyyy.pathfinder.geometry.Point}
     * instead of a {@code HeadingPoint}, but using a point here instead and
     * having to add a heading to that point for interfacing purposes
     * ({@link Odometry#getPos()} specifically) would be inefficient.
     */
    private HeadingPoint position = new HeadingPoint(0, 0, 0);

    /**
     * The last recorded timestamp. This timestamp is measured in ms, but is
     * converted to seconds during transformation.
     */
    private double lastTime = Time.relativeTime(TIME_UNIT);

    /**
     * The coefficient to use here.
     */
    private final double coefficient;

    /**
     * Create a new {@code SimulatedRobot} instance.
     *
     * @param coefficient the coefficient to multiply the robot's simulated
     *                    translational values by.
     */
    public SimulatedRobot(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Simulate robot movement. This method works by determining the time
     * elapsed since the last movement update, converting that to seconds,
     * and multiplying the inputted translation X/Y values by the coefficient
     * provided by the constructor.
     *
     * @param transform the robot's desired transformation. It's important to
     *                  note that this transformation's angle doesn't mean how
     *                  much the robot should be turning, it means the angle
     */
    @Override
    public void drive(RTransform transform) {
        double time = Time.relativeTime(TIME_UNIT);
        double elapsedTime = time - lastTime;

        /*
         * 1. Convert time (ms) to time (s).
         * 2. Multiply the transformation's values by time in seconds,
         * 3. Multiply the transformation's values by the coefficient.
         */
        double distanceX = transform.getX() * (time / 1000) * coefficient;
        double distanceY = transform.getY() * (time / 1000) * coefficient;

        /*
         * Simulated robot position doesn't have anything to do with angles.
         * It was mostly created so I can streamline testing paths without
         * having to physically test the path.
         */
        position = position.transform(
                distanceX,
                distanceY,
                Angle.fromDegrees(0)
        );

        /*
         * Update the last timestamp.
         */
        lastTime = time;
    }

    /**
     * Do nothing - just needed for interfacing.
     */
    @Override
    public void enableUserControl() {

    }

    /**
     * Do nothing - just needed for interfacing.
     */
    @Override
    public void disableUserControl() {

    }

    /**
     * Get the simulated position of the robot. Check out the class
     * {@link SimulatedRobot} to learn a bit more about how robot simulation
     * works.
     *
     * @return the simulated position of the robot.
     */
    @Override
    public HeadingPoint getPos() {
        return position;
    }

    /**
     * Do nothing - just needed for interfacing.
     */
    @Override
    public void update() {

    }
}
