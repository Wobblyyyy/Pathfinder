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

import me.wobblyyyy.pathfinder.math.Range;

/**
 * An abstract implementation of the {@code Controller} interface designed to
 * streamline controller implementations by abstracting common methods (getters
 * and setters for target, minimum, maximum, etc) and handling output range
 * clipping. This abstract class can be extended by a controller implementation
 * so that there's less code required to write a controller.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public abstract class AbstractController implements Controller {
    /**
     * The controller's output range. By default, this range is negative to
     * positive infinity, meaning there aren't any outputs that are invalid.
     * This can be changed using the {@link #setMax(double)} and the
     * {@link #setMin(double)} methods.
     */
    public Range<Double> outputRange = new Range<>(
            Double.NEGATIVE_INFINITY,
            Double.POSITIVE_INFINITY
    );

    /**
     * The controller's target.
     *
     * @see #setTarget(double)
     * @see #getTarget()
     */
    public double target = 0;

    /**
     * Clip a value using the controller's output range.
     *
     * @param value the value to clip.
     * @return the clipped value.
     * @see Range#clip(Number)
     */
    public double clip(double value) {
        return outputRange.clip(value);
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
     * Set the controller's maximum output value.
     *
     * @param max the controller's maximum output value.
     */
    @Override
    public void setMax(double max) {
        outputRange.setMax(max);
    }

    /**
     * Get the controller's maximum output value.
     *
     * @return the controller's maximum output value.
     */
    @Override
    public double getMax() {
        return outputRange.getMax();
    }

    /**
     * Set the controller's minimum output value.
     *
     * @param min the controller's minimum output value.
     */
    @Override
    public void setMin(double min) {
        outputRange.setMin(min);
    }

    /**
     * Get the controller's minimum output value.
     *
     * @return the controller's minimum output value.
     */
    @Override
    public double getMin() {
        return outputRange.getMin();
    }
}
