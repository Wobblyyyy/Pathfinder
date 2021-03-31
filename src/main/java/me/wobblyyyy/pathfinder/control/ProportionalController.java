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
 * A simple single-coefficient controller that uses the distance from the
 * target point as well as a coefficient for that proportion to determine
 * an output value.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class ProportionalController implements Controller {
    /**
     * The controller's coefficient.
     */
    private Coefficient proportionalCoefficient;

    /**
     * The controller's target point.
     */
    private double target;

    /**
     * Create a new {@code ProportionalController}.
     *
     * @param coefficient the controller's coefficient.
     */
    public ProportionalController(double coefficient) {
        proportionalCoefficient = new Coefficient(coefficient);
    }

    /**
     * Create a new {@code ProportionalController}.
     *
     * @param coefficient the controller's coefficient.
     */
    public ProportionalController(Coefficient coefficient) {
        proportionalCoefficient = coefficient;
    }

    /**
     * Create a new {@code ProportionalController}.
     *
     * @param coefficient the controller's coefficient.
     * @param target      the controller's target point.
     */
    public ProportionalController(Coefficient coefficient,
                                  double target) {
        proportionalCoefficient = coefficient;
        this.target = target;
    }

    /**
     * Set the controller's coefficient.
     *
     * @param coefficient the controller's coefficient.
     */
    public void setProportionalCoefficient(Coefficient coefficient) {
        proportionalCoefficient = coefficient;
    }

    /**
     * Get the controller's coefficient.
     *
     * @return the controller coefficient.
     */
    public Coefficient getProportionalCoefficient() {
        return proportionalCoefficient;
    }

    /**
     * Set the controller's target. The controller's target point is the
     * point that the calculated values will attempt to invert.
     *
     * @param target the controller's target value.
     */
    @Override
    public void setTarget(double target) {
        this.target = target;
    }

    /**
     * Get the controller's target value.
     *
     * @return the controller's target value.
     */
    @Override
    public double getTarget() {
        return target;
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
        return (target - current) * proportionalCoefficient.getCoefficient();
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
