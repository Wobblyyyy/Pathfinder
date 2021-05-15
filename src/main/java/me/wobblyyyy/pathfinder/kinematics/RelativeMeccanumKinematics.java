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
//    private static final double SIN_45 = Math.sin(Math.toDegrees(45));
//    private static final double SIN_315 = Math.sin(Math.toDegrees(315));
//    private static final double COS_45 = Math.cos(Math.toDegrees(45));
//    private static final double COS_315 = Math.cos(Math.toDegrees(315));

    private static final double[] ANGLES = new double[] {
            45,  // FRONT LEFT
            315, // FRONT RIGHT
            315, // BACK LEFT
            45   // BACK RIGHT
    };

    public RelativeMeccanumKinematics() {

    }

    private static double transform(Function<Double, Double> trig,
                                    double movementAngle,
                                    double wheelAngle,
                                    double magnitude) {
        double trigMovementAngle = trig.apply(movementAngle);
        double trigWheelAngle = trig.apply(wheelAngle);

        return trigMovementAngle * trigWheelAngle * magnitude;
    }

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

    public MeccanumState toMeccanumState(RTransform transform) {
        double movementAngle = AngleUtils.fixDeg(Math.toDegrees(Math.atan2(
                transform.getY(),
                transform.getX()
        )) + 270);
        double magnitude = Math.min(Math.hypot(transform.getX(), transform.getY()), 1);

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
