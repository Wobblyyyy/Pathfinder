/*
 *  ======================================================================
 *  || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 *  ||                                                                  ||
 *  || This file is part of the "Pathfinder" project, which is licensed ||
 *  || and distributed under the GPU General Public License V3.         ||
 *  ||                                                                  ||
 *  || Pathfinder is available on GitHub:                               ||
 *  || https://github.com/Wobblyyyy/Pathfinder                          ||
 *  ||                                                                  ||
 *  || Pathfinder's license is available:                               ||
 *  || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 *  ||                                                                  ||
 *  || Re-distribution of this, or any other files, is allowed so long  ||
 *  || as this same copyright notice is included and made evident.      ||
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.geometry;

import me.wobblyyyy.intra.ftc2.utils.math.Comparator;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * The core component behind a circle.
 *
 * <p>
 * Just to clarify - a GigaArc IS a circle. However, circles have actual
 * circles and hitboxes - using a GigaArc thus allows us to have two
 * "circles" where we'd normally only be able to have one.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class GigaArc {
    public final Point center;
    public final double radius;
    public final double diameter;
    public final double circumference;
    public final double area;

    /**
     * Create a new GigaArc.
     *
     * @param center the center-point of the GigaArc.
     * @param radius the radius of the arc.
     */
    public GigaArc(Point center,
                   double radius) {
        this.center = center;
        this.radius = radius;
        diameter = radius * 2;
        circumference = 2 * Math.PI * radius;
        area = Math.PI * Math.pow(radius, 2);
    }

    /**
     * Is a given point contained within the GigaArc?
     *
     * @param point the point to test.
     * @return whether or not the point is contained in the circle.
     */
    public boolean isPointInCircle(Point point) {
        return Distance.getDistance(point, center) < radius;
    }

    /**
     * Is a given point ON the radius of a circle?
     *
     * @param point the point to test.
     * @return whether or not the point is ON the circle.
     */
    public boolean isPointOnCircle(Point point) {
        return isPointOnCircle(point, 0.1);
    }

    /**
     * Is a given point ON the radius of a circle?
     *
     * @param point     the point to test.
     * @param tolerance the tolerance of what's defined as being on the
     *                  edge of the circle.
     * @return whether or not the given point is on the edge of the circle.
     */
    public boolean isPointOnCircle(Point point,
                                   double tolerance) {
        Comparator comparator = new Comparator(0.1);
        return comparator.compare(
                Distance.getDistance(point, center),
                radius
        );
    }

    /**
     * Is a given point close to a circle?
     *
     * @param point     the point to test.
     * @param tolerance the maximum allowable distance a point can be before
     *                  it's no longer considered to be "close."
     * @return whether or not a given point is near the circle.
     */
    public boolean isPointNearCircle(Point point,
                                     double tolerance) {
        GigaArc circle = new GigaArc(center, radius + tolerance);
        return isPointInCircle(point);
    }
}
