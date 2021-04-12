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
 * A number with a specified tolerance.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class Tolerance {
    /**
     * The tolerance's tolerance. Yup.
     */
    private final double tolerance;

    /**
     * Create a new {@code Tolerance} instance.
     *
     * @param tolerance the {@code Tolerance} instance's internal tolerance.
     *                  This tolerance is defined as the maximum distance away
     *                  from the reference number that a target number can be
     *                  while still being considered within the tolerance's
     *                  acceptable range.
     */
    public Tolerance(double tolerance) {
        this.tolerance = tolerance;
    }

    /**
     * Compare two numbers - a reference and a target.
     *
     * @param reference the reference number. This number is the number which
     *                  the target number is compared against.
     * @param target    the target number. This number is the number which is
     *                  compared to the reference number.
     * @return true if the target number fits within the reference number's
     * acceptable tolerance, false if it does not. This condition is determined
     * by calculating a minimum and maximum value based on the tolerance (add
     * and subtract the tolerance to the reference number) and verifying that
     * the target number fits between the minimum and maximum.
     */
    public boolean compare(double reference,
                           double target) {
        double l = reference - tolerance; // MIN bound
        double h = reference + tolerance; // MAX bound

        return l <= target && target <= h;
    }

    /**
     * Compare several numbers.
     *
     * <p>
     * This method is essentially a way to run the
     * {@link #compare(double, double)} method on an array of doubles instead
     * of a single double. This method calls the {@code compare} method
     * for each of the provided numbers. It will return true if all of the
     * numbers fit within the given tolerance. It will return false if that's
     * not the case.
     * </p>
     *
     * @param reference the reference number. This number is the number which
     *                  the target number is compared against.
     * @param targets   the target numbers. These are the numbers that will
     *                  be compared to the reference number.
     * @return true if the target number fits within the reference number's
     * acceptable tolerance, false if it does not. This condition is determined
     * by calculating a minimum and maximum value based on the tolerance (add
     * and subtract the tolerance to the reference number) and verifying that
     * the target number fits between the minimum and maximum.
     * @see #compare(double, double)
     */
    public boolean compare(double reference,
                           double[] targets) {
        for (double target : targets) {
            if (!compare(reference, target)) return false;
        }

        return true;
    }
}
