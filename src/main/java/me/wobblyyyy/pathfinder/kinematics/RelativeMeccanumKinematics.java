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

package me.wobblyyyy.pathfinder.kinematics;

import me.wobblyyyy.pathfinder.geometry.AngleUtils;

import java.util.function.Function;

/**
 * Kinematics designed for using relative rather than absolute transformations.
 *
 * @author Colin Robertson
 * @since 0.7.1
 */
public class RelativeMeccanumKinematics {
    private static final double[] ANGLES = new double[]{
            45,  // FRONT LEFT
            315, // FRONT RIGHT
            315, // BACK LEFT
            45   // BACK RIGHT
    };

    /**
     * The kinematics' maximum allowable magnitude.
     */
    private final double maxMagnitude;

    /**
     * The kinematics' minimum allowable magnitude.
     */
    private final double minMagnitude;

    /**
     * An angle that's added to calculated movement angles. This should only
     * be used when 0 doesn't work.
     */
    private final double movementAngleOffset;

    /**
     * Create a new instance of the {@code RelativeMeccanumKinematics} class.
     */
    public RelativeMeccanumKinematics() {
        this(
                1,
                0,
                0
        );
    }

    /**
     * Create a new {@code RelativeMeccanumKinematics} with some added bonuses.
     * Very cool, right?
     *
     * @param maxMagnitude        the maximum calculated magnitude value.
     * @param minMagnitude        the minimum calculated magnitude value.
     * @param movementAngleOffset an angle that's added to calculated movement
     *                            angles to offset the outputted movement
     *                            direction.
     */
    public RelativeMeccanumKinematics(double maxMagnitude,
                                      double minMagnitude,
                                      double movementAngleOffset) {
        this.maxMagnitude = maxMagnitude;
        this.minMagnitude = minMagnitude;
        this.movementAngleOffset = movementAngleOffset;
    }

    /**
     * Apply a single trig transform.
     *
     * @param trig          trig function to use.
     * @param movementAngle movement angle.
     * @param wheelAngle    wheel angle.
     * @param magnitude     magnitude.
     * @return transformed value.
     */
    private static double transform(Function<Double, Double> trig,
                                    double movementAngle,
                                    double wheelAngle,
                                    double magnitude) {
        double trigMovementAngle = trig.apply(movementAngle);
        double trigWheelAngle = trig.apply(wheelAngle);

        return trigMovementAngle * trigWheelAngle * magnitude;
    }

    /**
     * Calculate power for a single wheel based on the wheel's direction of
     * power application and the desired movement angle.
     *
     * @param movementAngle the angle at which the wheel moves in.
     * @param wheelAngle    the angle at which the wheel applies force.
     * @param magnitude     the "speed" multiplier that determines how fast
     *                      (or how slow) your robot will go.
     * @return a calculated power value for a single wheel.
     */
    private static double calculatePower(double movementAngle,
                                         double wheelAngle,
                                         double magnitude) {
        double xTransform = transform(
                Math::sin,
                Math.toRadians(movementAngle),
                Math.toRadians(wheelAngle),
                magnitude
        );

        double yTransform = transform(
                Math::cos,
                Math.toRadians(movementAngle),
                Math.toRadians(wheelAngle),
                magnitude
        );

        return xTransform + yTransform;
    }

    /**
     * Create a {@code MeccanumState} instance containing four wheel power
     * values based on a desired transformation.
     *
     * @param transform the desired transformation.
     * @return a set of power values for a meccanum drivetrain.
     */
    public MeccanumState toMeccanumState(RTransform transform) {
        double movementAngle = AngleUtils.fixDeg(Math.toDegrees(Math.atan2(
                transform.getY(),
                transform.getX()
        )) + movementAngleOffset);
        double magnitude = Math.max(
                Math.min(
                        Math.hypot(transform.getX(), transform.getY()),
                        maxMagnitude),
                minMagnitude
        );

        double fl = calculatePower(movementAngle, ANGLES[0], magnitude);
        double fr = calculatePower(movementAngle, ANGLES[1], magnitude);
        double bl = calculatePower(movementAngle, ANGLES[2], magnitude);
        double br = calculatePower(movementAngle, ANGLES[3], magnitude);

        fl += transform.getTurn();
        fr -= transform.getTurn();
        bl += transform.getTurn();
        br -= transform.getTurn();

        MeccanumState state = new MeccanumState(fl, fr, bl, br);
        state.normalizeFromMaxUnderOne();

        state = new MeccanumState(
                state.flPower() * magnitude,
                state.frPower() * magnitude,
                state.blPower() * magnitude,
                state.brPower() * magnitude
        );

        return state;
    }
}
