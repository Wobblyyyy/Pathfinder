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

package me.wobblyyyy.pathfinder.math.functional.one;

/**
 * Math functionality for finding the reciprocal of a number.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class Reciprocal {
    /**
     * Get the reciprocal of a specified number.
     *
     * @param input the number to get the reciprocal of.
     * @return the number's reciprocal.
     */
    public static double of(double input) {
        return 1 / input;
    }

    /**
     * Get the reciprocal of a specified number and multiply it by the provided
     * scale coefficient.
     *
     * @param input the number to get the reciprocal of.
     * @param scale the number to multiply by.
     * @return the reciprocal of the specified number, multiplied by the
     * provided scale value.
     */
    public static double of(double input, double scale) {
        return of(input) * scale;
    }
}
