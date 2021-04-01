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

/**
 * Functional utility class used to get the sum of a set of inputs.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class Sum {
    /**
     * Private constructor so this utility class can't be used
     * anywhere as an object.
     */
    private Sum() {
        
    }
    
    /**
     * Get the sum of a data set. It's worth noting that this method is
     * slower than manually adding each of the numbers, but it helps
     * with code clarity and cleanliness.
     *
     * @param inputs the data set to get the sum of.
     * @return the sum of all of the inputted numbers.
     */
    public static double of(double... inputs) {
        double i = 0;

        for (double v : inputs) {
            i += v;
        }

        return i;
    }
}
