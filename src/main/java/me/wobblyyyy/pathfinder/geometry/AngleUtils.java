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

package me.wobblyyyy.pathfinder.geometry;

import me.wobblyyyy.pathfinder.math.Range;

/**
 * Utility class for dealing with angles in both radians and degrees.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class AngleUtils {
    public static final double ZERO = 0;
    public static final double PI = Math.PI;
    public static final double PI_TIMES_2 = PI * 2;
    public static final double PI_OVER_2 = PI / 2;
    public static final double PI_OVER_3 = PI / 3;
    public static final double PI_OVER_4 = PI / 4;
    public static final double PI_OVER_5 = PI / 5;
    public static final double PI_OVER_6 = PI / 6;
    public static final double PI_OVER_7 = PI / 7;
    public static final double PI_OVER_8 = PI / 8;

    public static final Range<Double> RANGE_DEG = new Range<>(
            0D,
            360D
    );

    public static final Range<Double> RANGE_RAD = new Range<>(
            toPiRads(0),
            toPiRads(2)
    );

    private AngleUtils() {

    }

    public static double toPiRads(double radsWithoutPi) {
        return radsWithoutPi * PI;
    }

    public static double toPiRads2(double radsWithoutPi) {
        return toPiRads(radsWithoutPi) * 2;
    }

    /**
     * Ensure a radian measure fits within the bounds of 0 to 2 pi.
     *
     * @param rad the radian measure to check.
     * @return the fixed radian measure.
     */
    public static double fixRad(double rad) {
        while (rad < toPiRads(0)) rad += toPiRads(2);
        while (rad > toPiRads(2)) rad -= toPiRads(2);

        return rad;
    }

    /**
     * Ensure a degree measure fits within the bounds of 0 to 360. If a degree
     * measure is below 0, it'll be increased until it's above 0. If a degree
     * measure is above 360, it'll be decreased until it's below 360. Note that
     * these measurements are inclusive, meaning you can have a degree that's
     * exactly 360 degrees or exactly 0 degrees.
     *
     * @param deg the degree measure to check.
     * @return the fixed degree measure.
     */
    public static double fixDeg(double deg) {
        while (deg < 0) deg += 360;   // ensure above 0
        while (deg > 360) deg -= 360; // ensure below 360

        return deg;
    }

    /**
     * Get the current difference in angle between the odometry's reported
     * angle and the target angle. This value will be fed into the turn
     * controller and used to determine turn speed.
     *
     * @return the difference between the target and current angle, in degrees.
     * If the distance is above 180deg, it'll be "normalized" by adjusting both
     * angles down by 180, "fixing" them, and recalculating the delta.
     */
    public static double minimumAngleDelta(double currentAngle,
                                           double targetAngle) {
        // Normalize both the target and current values with fixDeg
        double current = fixDeg(currentAngle);
        double target = fixDeg(targetAngle);

        // Declare delta, initialize it later.
        final double delta;

        // If the target is more than 180 degrees away from the current, we
        // know we can further optimize the angle delta.
        if (target - current > 180) {
            // Re-calculate the current and target points by subtracting 180
            // from each of them and fixing the degree measure. This ensures
            // that the maximum possible angle delta is +- 0.
            current = fixDeg(current - 180);
            target = fixDeg(current - 180);
        }

        // Initialize delta. Very fun.
        delta = target - current;

        // Now we're done!
        // Note that we don't use the fixDeg method here because this angle
        // delta is relative, whereas target and current positions are both
        // absolute.
        return delta;
    }
}
