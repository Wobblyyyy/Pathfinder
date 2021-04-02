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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Builder utility class that leverages functional interfaces to allow for
 * {@link Motor} instances to be constructed from a method to set power to
 * the motor and a method to get power from the motor.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class MotorBuilder {
    /**
     * Don't allow the construction of utility classes.
     */
    private MotorBuilder() {

    }

    /**
     * Create a new {@link PfMotor} (which implements {@link Motor}) from
     * a method to set power to the motor and a method to get power from the
     * motor. In addition to those two functional interfaces, add a boolean
     * that toggles whether the motor is inverted or not.
     *
     * <p>
     * Functional interfaces are a lesser-known part of Java's idiomatic
     * programming features, but they're incredibly useful, especially in
     * a situation such as this one - it allows us to create a motor without
     * having to declare intermediary variables. Ex:
     * <code>
     * <pre>
     * // Let's pretend DcMotor is a class that accepts a
     * // string as a name. This class has two methods:
     * // 1. setPower(double) ... and
     * // 2. getPower()
     * // setPower takes a single input
     * // getPower returns a single output
     * DcMotor motor = new DcMotor("motor");
     *
     * // We can then create a Pathfinder motor by
     * // using these methods as parameters: see here.
     * PfMotor realMotor = MotorBuilder.buildFrom(
     *   motor::setPower, // set power
     *   motor::getPower, // get power
     *   false            // is the motor inverted?
     * );
     * </pre>
     * </code>
     * </p>
     *
     * @param setPower   a {@link FunctionalInterface} to set power to the
     *                   motor. You should pass a method as a parameter here,
     *                   ideally, a method of the motor's core implementation.
     * @param getPower   a {@link FunctionalInterface} to get power from the
     *                   motor. You should pass a method as a parameter here,
     *                   ideally, a method of the motor's core implementation.
     * @param isInverted is the motor inverted?
     * @return a new {@code PfMotor}/{@code Motor}, created from the
     * provided methods and inversion boolean.
     */
    public static PfMotor buildFrom(Consumer<Double> setPower,
                                    Supplier<Double> getPower,
                                    boolean isInverted) {
        return new PfMotor(new Motor() {
            private boolean isUserControlled = true;

            @Override
            public void enableUserControl() {
                isUserControlled = true;
            }

            @Override
            public void disableUserControl() {
                isUserControlled = false;
            }

            @Override
            public void setPower(double power) {
                setPower(power, true);
            }

            @Override
            public void setPower(double power, boolean user) {
                setPower.accept(power);
            }

            @Override
            public double getPower() {
                return getPower.get();
            }
        }, isInverted);
    }

    /**
     * Create a new {@link PfMotor} (which implements {@link Motor}) from
     * a method to set power to the motor and a method to get power from the
     * motor.
     *
     * <p>
     * Functional interfaces are a lesser-known part of Java's idiomatic
     * programming features, but they're incredibly useful, especially in
     * a situation such as this one - it allows us to create a motor without
     * having to declare intermediary variables. Ex:
     * <code>
     * <pre>
     * // Let's pretend DcMotor is a class that accepts a
     * // string as a name. This class has two methods:
     * // 1. setPower(double) ... and
     * // 2. getPower()
     * // setPower takes a single input
     * // getPower returns a single output
     * DcMotor motor = new DcMotor("motor");
     *
     * // We can then create a Pathfinder motor by
     * // using these methods as parameters: see here.
     * PfMotor realMotor = MotorBuilder.buildFrom(
     *   motor::setPower, // set power
     *   motor::getPower, // get power
     *   false            // is the motor inverted?
     * );
     * </pre>
     * </code>
     * </p>
     *
     * @param setPower a {@link FunctionalInterface} to set power to the
     *                 motor. You should pass a method as a parameter here,
     *                 ideally, a method of the motor's core implementation.
     * @param getPower a {@link FunctionalInterface} to get power from the
     *                 motor. You should pass a method as a parameter here,
     *                 ideally, a method of the motor's core implementation.
     * @return a new {@code PfMotor}/{@code Motor}, created from the
     * provided methods.
     */
    public static PfMotor buildFrom(Consumer<Double> setPower,
                                    Supplier<Double> getPower) {
        return buildFrom(setPower, getPower, false);
    }
}
