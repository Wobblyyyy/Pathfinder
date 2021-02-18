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

package me.wobblyyyy.pathfinder.finders;

import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.core.Generator;
import me.wobblyyyy.pathfinder.core.MapTools;
import me.wobblyyyy.pathfinder.geometry.Line;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.geometry.Zone;
import me.wobblyyyy.pathfinder.util.Distance;

import java.util.ArrayList;

/**
 * A relatively quick path generator - think of it as an extended version of
 * the LightningFinder.
 *
 * <p>
 * This finder checks for any zones within the given pathfinding area, and, if
 * it doesn't find any, just generates a path. However, if it DOES find more
 * than one solid zone in the given area, it'll double-check to ensure that
 * the robot can't possibly collide with any of the objects on its path.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @since 0.1.0
 */
public class SpeedFinder implements Generator {
    /**
     * Robot X.
     */
    private final double rX;

    /**
     * Robot Y.
     */
    private final double rY;

    /**
     * Robot radius.
     */
    private final double rR;

    /**
     * The pathfinder's configuration.
     */
    private final PathfinderConfig config;

    /**
     * Create a new SpeedFinder.
     *
     * @param config the Pathfinder library's configuration.
     */
    public SpeedFinder(PathfinderConfig config) {
        /*
         * Set all of the constructor variables.
         *
         * These values include:
         * - Configuration
         * - Robot X
         * - Robot Y
         */

        this.config = config;

        rX = config.getRobotX();
        rY = config.getRobotY();
        rR = Math.hypot(rX, rY);
    }

    /**
     * Get a newly-generated coordinate path between two points.
     *
     * <p>
     * This path is INCREDIBLY linear - it's quite literally just two points.
     * If this method of pathfinding doesn't end up working, we can instead
     * use the ThetaStar pathfinder, which, unless there's not a single path
     * available, will generate a valid path.
     * </p>
     *
     * @param start the start coordinate.
     * @param end   the end coordinate.
     * @return either no or two points for a path.
     */
    @Override
    public ArrayList<Point> getCoordinatePath(Point start,
                                              Point end) {
        /*
         * An ArrayList of zones that are considered to be "in the way."
         *
         * A zone which is in the way, in this case, is in the same area as
         * the pathfinder's active search area. The lightning finder is much
         * faster than this finder because it only finds paths for areas where
         * there are no solid zones.
         *
         * Any zone that is in the same area as the pathfinder is searching
         * will be added to this list of zones, which are used later to ensure
         * there aren't any solid collisions.
         */
        ArrayList<Zone> itw = MapTools.getZonesInArea(
                config.getMap(),
                new MapTools.Area(
                        start,
                        end
                )
        );

        /*
         * A measurement of distance between the start and end positions of
         * a coordinate path.
         */
        double distance = Distance.getDistance(start, end);

        /*
         * The difference between the two points.
         *
         * We only need this value for calculating the atan2 of the path.
         */
        Point difference = new Point(
                end.getX() - start.getX(),
                end.getY() - start.getY()
        );

        /*
         * The angle which the robot will be moving in is important.
         *
         * This angle is used, in conjunction with lines, to detect any
         * potential collisions, and, if possible, avoid them.
         */
        double angle = difference.getTheta();

        /*
         * Left and right "base" points.
         *
         * These base points are where the lines that are drawn start.
         *
         * These points are calculated using the Distance#inDirection method
         * in combination to the robot's X and Y values.
         */

        Point lBase = Distance.inDirection(
                start,
                angle + 90,
                rR
        );
        Point rBase = Distance.inDirection(
                start,
                angle - 90,
                rR
        );

        /*
         * Finally, we get to drawing the lines themselves.
         *
         * These lines should be drawn from the respective base points (left
         * is obviously on the left, right is obviously on the right) and
         * drawn for the amount of distance that would be traveled if the
         * path was followed as directly as possible.
         */

        Line left = new Line(
                lBase,
                Distance.inDirection(
                        lBase,
                        angle,
                        distance
                )
        );
        Line right = new Line(
                rBase,
                Distance.inDirection(
                        rBase,
                        angle,
                        distance
                )
        );

        /*
         * For each of the zones that have been flagged as "in the way," we
         * run a test to see if the line enters the zone.
         *
         * If the line DOES enter the zone, no path can be found. This means
         * that the robot will collide with a solid object if it follows
         * the path as it originally intended.
         *
         * However, if the line DOES NOT enter ANY of the zones, the path is
         * valid as is.
         */
        for (Zone z : itw) {
            if (z.getParentShape().isLineInShape(left) ||
                    z.getParentShape().isLineInShape(right))
                return new ArrayList<>();
        }

        /*
         * Create a new ArrayList of points, just to return the value.
         */

        ArrayList<Point> points = new ArrayList<>();
        points.add(start);
        points.add(end);

        /*
         * Would you look at that! We're done with our speed finding!
         */
        return points;
    }
}
