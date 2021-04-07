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

import me.wobblyyyy.pathfinder.math.Invertable;
import me.wobblyyyy.pathfinder.math.functional.Sum;

import java.util.function.Supplier;

/**
 * An implementation of the well-known proportional integral derivative
 * controller. This implementation is rather barebones, but it gets the
 * job done. You're more than welcome to create your own PID controller
 * or whatever - I won't get offended if you don't use this one. But it's
 * here if you want it!
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class PidController extends AbstractController {
    /**
     * The controller's coefficients.
     */
    private PidCoefficients coefficients;

    /**
     * An accessor for the P coefficient.
     */
    private Supplier<Double> kP;

    /**
     * An accessor for the I coefficient.
     */
    private Supplier<Double> kI;

    /**
     * An accessor for the D coefficient.
     */
    private Supplier<Double> kD;

    /**
     * A cumulative sum of all of the errors.
     */
    private double cumulativeError;

    /**
     * The last actual value - used by the D coefficient.
     */
    private double lastActual;

    /**
     * Is the controller reversed? If yes, all of the outputs will be
     * swapped. If no, the outputs are normal.
     */
    private final boolean isReversed;

    /**
     * Create a new PID controller.
     *
     * @param coefficients the controller's coefficients.
     */
    public PidController(PidCoefficients coefficients) {
        this(coefficients, false);
    }

    /**
     * Create a new PID controller.
     *
     * @param coefficients the controller's coefficients.
     * @param isReversed   is the controller reversed?
     */
    public PidController(PidCoefficients coefficients,
                         boolean isReversed) {
        setCoefficients(coefficients);
        this.isReversed = isReversed;
    }

    public void setCoefficients(PidCoefficients coefficients) {
        this.coefficients = coefficients;

        kP = coefficients::getPV;
        kI = coefficients::getIV;
        kD = coefficients::getDV;
    }

    public void setCoefficients(double proportional,
                                double integral,
                                double derivative) {
        setCoefficients(new PidCoefficients(
                proportional,
                integral,
                derivative
        ));
    }

    public PidCoefficients getCoefficients() {
        return coefficients;
    }

    /**
     * Use the controller to calculate a value based on a current measurement.
     * This method assumes that the target point has already been set prior
     * to this method's calling.
     *
     * @param current the current measurement to calculate from.
     * @return a value calculated by the controller.
     */
    @Override
    public double calculate(double current) {
        double error = target - current;

        double outputP = kP.get() * error;
        double outputI = kI.get() * cumulativeError;
        double outputD = -(kD.get() * (current - lastActual));
        double output = Sum.of(outputP, outputI, outputD);

        lastActual = current;
        cumulativeError += error;

        return clip(Invertable.apply(output, isReversed));
    }

    /**
     * Use the controller to calculate a value based on a current measurement.
     * This method uses the inputted target - in essence, this method should
     * call the {@link #setTarget(double)} method and then the
     * {@link #calculate(double)} method.
     *
     * @param current the current measurement to calculate from.
     * @param target  the target point that the controller should attempt to
     *                reach. This point is set to the controller's target
     *                point when this method is called.
     * @return a value calculated by the controller.
     */
    @Override
    public double calculate(double current, double target) {
        setTarget(target);

        return calculate(current);
    }
}
