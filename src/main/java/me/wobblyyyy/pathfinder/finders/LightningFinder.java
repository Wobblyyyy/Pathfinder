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
import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.ArrayList;

/**
 * The fastest pathfinder available. Or something like that.
 *
 * <p>
 * Generally speaking, we want to attempt to use this finder whenever possible.
 * Using this, compared to the SpeedFinder or especially the actually
 * algorithmic finders, is significantly faster. And we know what that means -
 * more speed means more swag. It's a 1-1 correlation.
 * </p>
 *
 * <p>
 * This generator works by checking to see if the area in question of the
 * field is empty or not. If the area is empty, this returns the start and
 * the end points in an array. If the area is not empty, this returns an
 * empty array, signifying that the next path generator in the queue
 * should try to find a path instead.
 * </p>
 *
 * <p>
 * When looking at performance analysis, we can see that the average execution
 * time for the calculation of a given path is reduced dramatically when using
 * this type of finder over another type, such as the Speed or the Xygum finder.
 * <ul>
 *     <li>Average Lightning: 5ms</li>
 *     <li>Average Speed: 7ms</li>
 *     <li>Average Xygum: 20-133ms (depends on size)</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 */
public class LightningFinder implements Generator {
    /**
     * A reference to the pathfinder's configuration class.
     */
    private final PathfinderConfig config;

    /**
     * Create a new lightning finder.
     *
     * @param config the Pathfinder library's configuration.
     */
    public LightningFinder(PathfinderConfig config) {
        this.config = config;
    }

    /**
     * Get a "path" from point A to B.
     *
     * <p>
     * This generator works by checking to see if the area in question of the
     * field is empty or not. If the area is empty, this returns the start and
     * the end points in an array. If the area is not empty, this returns an
     * empty array, signifying that the next path generator in the queue
     * should try to find a path instead.
     * </p>
     *
     * @param start the start coordinate.
     * @param end   the end coordinate.
     * @return a path (or not) from A to B.
     */
    @Override
    public ArrayList<Point> getCoordinatePath(Point start,
                                              Point end) {
        /*
         * Start and end test points.
         *
         * If the path is clear, we add the start and end points.
         *
         * If it's not, we don't.
         */
        ArrayList<Point> points = new ArrayList<>();

        /*
         * Check if the area is empty.
         */
        if (MapTools.isAreaEmpty(
                config.getMap(),
                new MapTools.Area(
                        start,
                        end
                )
        )) {
            /*
             * The area's empty!
             *
             * We can add our start and end points to the array list.
             */
            points.add(start);
            points.add(end);
        }

        /*
         * Return the points we've gotten.
         *
         * If this list is of size 0, no points have been added, meaning no
         * path has been found. The GeneratorManager class should handle this
         * for us.
         */
        return points;
    }
}
