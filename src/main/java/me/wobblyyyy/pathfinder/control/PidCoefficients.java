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

package me.wobblyyyy.pathfinder.control;

/**
 * A coefficient container for a PID controller. These coefficients can be
 * dynamically set during operation of the PID controller.
 *
 * @author Colin Robertson
 * @see #kP
 * @see #kI
 * @see #kD
 * @since 0.6.1
 */
public class PidCoefficients {
    /**
     * The "proportional" component of the PID.
     *
     * <p>
     * The obvious method is proportional control: the motor current is set in
     * proportion to the existing error. However, this method fails if, for
     * instance, the arm has to lift different weights: a greater weight
     * needs a greater force applied for the same error on the down side,
     * but a smaller force if the error is on the upside. That's where the
     * integral and derivative terms play their part.
     * </p>
     *
     * <p>
     * Term P is proportional to the current value of the SP-PV error e(t). For
     * example, if the error is large and positive, the control output will be
     * proportionately large and positive, taking into account the gain factor
     * "K". Using proportional control alone will result in an error between
     * the setpoint and the actual process value because it requires an error
     * to generate the proportional response. If there is no error, there
     * is no corrective response.
     * </p>
     *
     * <p>
     * A high proportional gain results in a large change in the output for a
     * given change in the error. If the proportional gain is too high, the
     * system can become unstable (see the section on loop tuning). In contrast,
     * a small gain results in a small output response to a large input error,
     * and a less responsive or less sensitive controller. If the proportional
     * gain is too low, the control action may be too small when responding to
     * system disturbances. Tuning theory and industrial practice indicate that
     * the proportional term should contribute the bulk of the output change.
     * </p>
     */
    private Coefficient kP;

    /**
     * The "integral" component of the PID.
     *
     * <p>
     * An integral term increases action in relation not only to the error but
     * also the time for which it has persisted. So, if the applied force is
     * not enough to bring the error to zero, this force will be increased as
     * time passes. A pure "I" controller could bring the error to zero, but
     * it would be both slow reacting at the start (because the action would be
     * small at the beginning, needing time to get significant) and brutal (the
     * action increases as long as the error is positive, even if the error has
     * started to approach zero).
     * </p>
     *
     * <p>
     * Term I accounts for past values of the SP-PV error and integrates them
     * over time to produce the I term. For example, if there is a residual
     * SP-PV error after the application of proportional control, the integral
     * term seeks to eliminate the residual error by adding a control effect
     * due to the historic cumulative value of the error. When the error is
     * eliminated, the integral term will cease to grow. This will result
     * in the proportional effect diminishing as the error decreases,
     * but this is compensated for by the growing integral effect.
     * </p>
     *
     * <p>
     * The integral term accelerates the movement of the process towards
     * setpoint and eliminates the residual steady-state error that occurs
     * with a pure proportional controller. However, since the integral term
     * responds to accumulated errors from the past, it can cause the present
     * value to overshoot the setpoint value (see the section on loop tuning).
     * </p>
     */
    private Coefficient kI;

    /**
     * The "derivative" component of the PID.
     *
     * <p>
     * A derivative term does not consider the error (meaning it cannot bring
     * it to zero: a pure D controller cannot bring the system to its setpoint),
     * but the rate of change of error, trying to bring this rate to zero. It
     * aims at flattening the error trajectory into a horizontal line, damping
     * the force applied, and so reduces overshoot (error on the other side
     * because of too great applied force). Applying too much impetus when the
     * error is small and decreasing will lead to overshoot. After overshooting,
     * if the controller were to apply a large correction in the opposite
     * direction and repeatedly overshoot the desired position, the output
     * would oscillate around the setpoint in either a constant, growing, or
     * decaying sinusoid. If the amplitude of the oscillations increases with
     * time, the system is unstable. If they decrease, the system is stable.
     * If the oscillations remain at a constant magnitude, the system is
     * considered marginally stable.
     * </p>
     *
     * <p>
     * Term D is a best estimate of the future trend of the SP-PV error, based
     * on its current rate of change. It is sometimes called "anticipatory
     * control", as it is effectively seeking to reduce the effect of the SP-PV
     * error by exerting a control influence generated by the rate of error
     * change. The more rapid the change, the greater the controlling or
     * damping effect.
     * </p>
     *
     * <p>
     * Derivative action predicts system behavior and thus improves settling
     * time and stability of the system. An ideal derivative is not causal, so
     * that implementations of PID controllers include an additional low-pass
     * filtering for the derivative term to limit the high-frequency gain and
     * noise. Derivative action is seldom used in practice though – by one
     * estimate in only 25% of deployed controllers – because of its variable
     * impact on system stability in real-world applications.
     * </p>
     */
    private Coefficient kD;

    /**
     * Create a new {@code PidCoefficients} instance.
     *
     * @param proportional see: {@link #kP}
     * @param integral     see: {@link #kI}
     * @param derivative   see: {@link #kD}
     */
    public PidCoefficients(double proportional,
                           double integral,
                           double derivative) {
        this(
                new Coefficient(proportional),
                new Coefficient(integral),
                new Coefficient(derivative)
        );
    }

    /**
     * Create a new {@code PidCoefficients} instance.
     *
     * @param proportional see: {@link #kP}
     * @param integral     see: {@link #kI}
     * @param derivative   see: {@link #kD}
     */
    public PidCoefficients(Coefficient proportional,
                           Coefficient integral,
                           Coefficient derivative) {
        kP = proportional;
        kI = integral;
        kD = derivative;
    }

    public void setP(double coefficient) {
        setP(new Coefficient(coefficient));
    }

    public void setP(Coefficient coefficient) {
        kP = coefficient;
    }

    public void setI(double coefficient) {
        setI(new Coefficient(coefficient));
    }

    public void setI(Coefficient coefficient) {
        kI = coefficient;
    }

    public void setD(double coefficient) {
        setD(new Coefficient(coefficient));
    }

    public void setD(Coefficient coefficient) {
        kD = coefficient;
    }

    public Coefficient getP() {
        return kP;
    }

    public Coefficient getI() {
        return kI;
    }

    public Coefficient getD() {
        return kD;
    }

    public double getPV() {
        return kP.getCoefficient();
    }

    public double getIV() {
        return kI.getCoefficient();
    }

    public double getDV() {
        return kD.getCoefficient();
    }
}
