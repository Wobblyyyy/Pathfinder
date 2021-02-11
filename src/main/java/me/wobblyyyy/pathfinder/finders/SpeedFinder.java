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
 *  ======================================================================
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
 */
public class SpeedFinder implements Generator {
    private final double rX;
    private final double rY;
    private final PathfinderConfig config;

    /**
     * Create a new SpeedFinder.
     *
     * @param config the Pathfinder library's configuration.
     */
    public SpeedFinder(PathfinderConfig config) {
        this.config = config;

        rX = config.getRobotX();
        rY = config.getRobotY();
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
        ArrayList<Zone> itw = MapTools.getZonesInArea(
                config.getMap(),
                new MapTools.Area(
                        start,
                        end
                )
        );

        double distance = Distance.getDistance(start, end);

        Point difference = new Point(
                end.getX() - start.getX(),
                end.getY() - start.getY()
        );

        double angle = difference.getTheta();

        Point lBase = Distance.inDirection(
                start,
                angle + 90,
                Math.hypot(rX, rY)
        );
        Point rBase = Distance.inDirection(
                start,
                angle - 90,
                Math.hypot(rX, rY)
        );

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

        for (Zone z : itw) {
            if (z.getParentShape().isLineInShape(left) ||
                    z.getParentShape().isLineInShape(right))
                return new ArrayList<>();
        }

        ArrayList<Point> points = new ArrayList<>();
        points.add(start);
        points.add(end);

        return points;
    }
}
