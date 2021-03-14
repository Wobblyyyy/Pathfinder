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

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

/**
 * An extension of the very lovely {@code Spline} class that seeks to make
 * splines even more simple by reducing the amount of input points to 2. Note
 * that using splines as the basis for all types of lines is rather inefficient,
 * as running spline calculations for a very simple line is quite expensive
 * in terms of the computational power required to do so. It may be a wise
 * idea to try to optimize this.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class Linear extends Spline {
    /**
     * Create a new linear spline, which is basically a fancy way of saying
     * a line. Yes, that's it. It's just a line. Nothing more, nothing less -
     * it's just a linear line. Yes.
     *
     * @param start the line's start point.
     * @param end   the line's end/target/destination point.
     */
    public Linear(HeadingPoint start,
                  HeadingPoint end) {
        super(new StaticArray<>(start, end));
    }
}
