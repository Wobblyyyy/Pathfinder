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

package me.wobblyyyy.pathfinder.util;

import jaci.pathfinder.Waypoint;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Utility methods used in implementing Jaci's pathfinder.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Jaci {
    /**
     * Get a waypoint based on a point.
     *
     * <p>
     * Remember, Pathfinder's measurements are in INCHES, and Jaci's
     * measurements are in (a) meters and (b) radians.
     * </p>
     *
     * @param p the input point.
     * @return the output waypoint.
     */
    public static Waypoint getWaypoint(Point p) {
        if (p instanceof HeadingPoint) return new Waypoint(
                Distance.inchesToMeters(p.getX()),
                Distance.inchesToMeters(p.getY()),
                Math.toRadians(((HeadingPoint) p).getHeading())
        );
        else return new Waypoint(
                Distance.inchesToMeters(p.getX()),
                Distance.inchesToMeters(p.getY()),
                0
        );
    }

    /**
     * Get a waypoint based on a point and an angle.
     *
     * <p>
     * Remember, Pathfinder's measurements are in INCHES, and Jaci's
     * measurements are in (a) meters and (b) radians.
     * </p>
     *
     * @param p the original point.
     * @param a the angle to use.
     * @return output waypoint.
     */
    public static Waypoint getWaypoint(Point p,
                                       double a) {
        return getWaypoint(
                HeadingPoint.withNewHeading(
                        Point.scale(p, Distance.INCHES_TO_METERS),
                        Math.toRadians(a)
                )
        );
    }
}
