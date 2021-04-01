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

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Reflect a value across a specific value.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class Reflection {
    /**
     * Private constructor - don't allow utility classes to
     * be used as objects.
     */
    private Reflection() {
        
    }
    
    /**
     * Reflect a number across 0 as an axis.
     *
     * @param value the number that should be reflected.
     * @return the reflected number.
     */
    public static double of(double value) {
        return of(value, 0);
    }

    /**
     * Reflect a number across a specified axis.
     *
     * @param value  the number that should be reflected.
     * @param center the axis to reflect over.
     * @return the reflected number.
     */
    public static double of(double value,
                            double center) {
        if (value > center) return center - (Math.abs(center - value));
        else return center + (Math.abs(center - value));
    }

    /**
     * Reflect a set of points over the Y axis. This could better be
     * phrased as 'inverting' each of the X values.
     *
     * @param points the points to reflect.
     * @param y      the value that each of the points
     *               should be reflected over.
     * @return an array of reflected points.
     */
    public static DynamicArray<Point> reflectPointsOverY(
            Arrayable<Point> points,
            double y) {
        DynamicArray<Point> newPoints = new DynamicArray<>();

        points.itr().forEach(point -> {
            newPoints.add(new Point(
                    of(point.getX(), y),
                    point.getY()
            ));
        });

        return newPoints;
    }

    /**
     * Reflect a set of points over the X axis. This could better be
     * phrased as 'inverting' each of the Y values.
     *
     * @param points the points to reflect.
     * @param y      the value that each of the points
     *               should be reflected over.
     * @return an array of reflected points.
     */
    public static DynamicArray<Point> reflectPointsOverX(
            Arrayable<Point> points,
            double x) {
        DynamicArray<Point> newPoints = new DynamicArray<>();

        points.itr().forEach(point -> {
            newPoints.add(new Point(
                    point.getX(),
                    of(point.getY(), x)
            ));
        });

        return newPoints;
    }
}
