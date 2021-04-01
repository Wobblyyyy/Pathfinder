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

package me.wobblyyyy.pathfinder.math.functional;

import java.util.stream.DoubleStream;

/**
 * Additional math utilities for getting the absolute maximum of a set of data.
 * This is mostly useful in speed and state normalization.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class AbsMax {
    /**
     * Get the maximum absolute value of the provided numbers.
     *
     * <p>
     * Example A:
     * <ul>
     *     <li>
     *         Inputs: 10, -15, 20, 30
     *     </li>
     *     <li>
     *         Output: 30
     *     </li>
     * </ul>
     * </p>
     *
     * <p>
     * Example B:
     * <ul>
     *     <li>
     *         Inputs: -130, 20, 10, 30
     *     </li>
     *     <li>
     *         Output: 130
     *     </li>
     * </ul>
     * </p>
     *
     * @param values the values to find the absolute maximum of.
     * @return the absolute maximum of the provided values.
     */
    public static double getAbsoluteMax(double... values) {
        double[] newValues = new double[values.length];
        if (values.length < 1) return 0;
        for (int i = 0; i < values.length; i++) {
            newValues[i] = Math.abs(values[i]);
        }
        return DoubleStream.of(newValues).max().getAsDouble();
    }

    /**
     * Get the maximum absolute value of the provided numbers.
     * This method calls {@link #getAbsoluteMax(double...)}.
     *
     * <p>
     * Example A:
     * <ul>
     *     <li>
     *         Inputs: 10, -15, 20, 30
     *     </li>
     *     <li>
     *         Output: 30
     *     </li>
     * </ul>
     * </p>
     *
     * <p>
     * Example B:
     * <ul>
     *     <li>
     *         Inputs: -130, 20, 10, 30
     *     </li>
     *     <li>
     *         Output: 130
     *     </li>
     * </ul>
     * </p>
     *
     * @param values the values to find the absolute maximum of.
     * @return the absolute maximum of the provided values.
     */
    public static double of(double... values) {
        return getAbsoluteMax(values);
    }
}
