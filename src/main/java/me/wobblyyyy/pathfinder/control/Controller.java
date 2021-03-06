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
 * Interface to be extended by {@code Controller} instances. Each controller
 * should be able to calculate a power value based on certain inputs, such
 * as distance from target, etc.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public interface Controller {
    /**
     * Set the controller's target. The controller's target point is the
     * point that the calculated values will attempt to invert.
     *
     * @param target the controller's target value.
     */
    void setTarget(double target);

    /**
     * Get the controller's target value.
     *
     * @return the controller's target value.
     */
    double getTarget();

    /**
     * Use the controller to calculate a value based on a current measurement.
     * This method assumes that the target point has already been set prior
     * to this method's calling.
     *
     * @param current the current measurement to calculate from.
     * @return a value calculated by the controller.
     */
    double calculate(double current);

    /**
     * Use the controller to calculate a value based on a current measurement.
     * This method uses the inputted target - in essence, this method should
     * call the {@link #setTarget(double)} method and then the
     * {@link #calculate(double)} method.
     *
     * @param current the current measurement to calculate from.
     * @param target the target point that the controller should attempt to
     *               reach. This point is set to the controller's target
     *               point when this method is called.
     * @return a value calculated by the controller.
     */
    double calculate(double current, double target);

    /**
     * Set the controller's maximum output value.
     *
     * @param max the controller's maximum output value.
     */
    void setMax(double max);

    /**
     * Get the controller's maximum output value.
     *
     * @return the controller's maximum output value.
     */
    double getMax();

    /**
     * Set the controller's minimum output value.
     *
     * @param min the controller's minimum output value.
     */
    void setMin(double min);

    /**
     * Get the controller's minimum output value.
     *
     * @return the controller's minimum output value.
     */
    double getMin();
}
