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

package me.wobblyyyy.pathfinder.math;

/**
 * An inclusive numerical range.
 *
 * @param <T> the type of number that the range uses.
 */
public class Range<T extends Number> {
    /**
     * The range's minimum value.
     */
    private T minimum;

    /**
     * The range's maximum value.
     */
    private T maximum;

    /**
     * Create a new {@code Range}.
     *
     * @param minimum the range's minimum value (inclusive).
     * @param maximum the range's maximum value (inclusive).
     */
    public Range(T minimum,
                 T maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Get the size of the range.
     *
     * @return the range's size.
     */
    public double getSize() {
        return dMax() - dMin();
    }

    /**
     * Get the range's minimum.
     *
     * @return the range's minimum.
     */
    public T getMin() {
        return minimum;
    }

    /**
     * Get the range's maximum.
     *
     * @return the range's maximum.
     */
    public T getMax() {
        return maximum;
    }

    /**
     * Set the range's minimum.
     *
     * @param minimum the range's minimum.
     */
    public void setMin(T minimum) {
        this.minimum = minimum;
    }

    /**
     * Set the range's maximum.
     *
     * @param maximum the range's maximum.
     */
    public void setMax(T maximum) {
        this.maximum = maximum;
    }

    /**
     * Clip a value to ensure it fits inside of the given range. If the value
     * provided is less than the {@link #minimum} value, the minimum value will
     * be returned. If the value provided is greater than the {@link #maximum}
     * value, the maximum value will be returned.
     *
     * @param value the value to clip.
     * @return the clipped value.
     */
    public T clip(T value) {
        if (value.doubleValue() < dMin()) {
            return minimum;
        } else if (value.doubleValue() > dMax()) {
            return maximum;
        } else {
            return value;
        }
    }

    /**
     * "Fix" a number so that it fits within the range. If the number is below
     * the range's minimum, it'll be added with the size of the range until
     * it's within the range. If the number is above the range's maximum, it'll
     * have the range's size subtracted from it until it's within the range.
     *
     * @param target the number to "fix".
     * @return a fixed number.
     */
    public double fix(T target) {
        double v = target.doubleValue();

        while (v < dMin()) v += getSize();
        while (v > dMax()) v -= getSize();

        return v;
    }

    /**
     * Check to see if a given number fits within the range.
     *
     * @param number the number to check.
     * @return whether or not the number fits within the range.
     */
    public boolean isInsideRange(Number number) {
        final double min = minimum.doubleValue();
        final double max = maximum.doubleValue();
        final double num = number.doubleValue();

        return num >= min && num <= max;
    }

    /**
     * Check to see if a number does not fit within the range.
     *
     * @param number the number to check.
     * @return whether or not the number is outside of the range.
     */
    public boolean isOutsideRange(Number number) {
        return !isInsideRange(number);
    }

    /**
     * Get the range's minimum as a double.
     *
     * @return the range's minimum as a double.
     */
    public double dMin() {
        return minimum.doubleValue();
    }

    /**
     * Get the range's maximum as a double.
     *
     * @return the range's maximum as a double.
     */
    public double dMax() {
        return maximum.doubleValue();
    }

    /**
     * Get the range's minimum as an integer.
     *
     * @return the range's minimum as an integer.
     */
    public int iMin() {
        return minimum.intValue();
    }

    /**
     * Get the range's maximum as an integer.
     *
     * @return the range's maximum as an integer.
     */
    public int iMax() {
        return maximum.intValue();
    }

    /**
     * Get the range's minimum as a float.
     *
     * @return the range's minimum as a float.
     */
    public float fMin() {
        return minimum.floatValue();
    }

    /**
     * Get the range's maximum as a float.
     *
     * @return the range's maximum as a float.
     */
    public float fMax() {
        return maximum.floatValue();
    }
}
