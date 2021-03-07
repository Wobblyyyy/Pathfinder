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

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.core.Generator;
import me.wobblyyyy.pathfinder.core.MapTools;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.map.Map;
import org.xguzm.pathfinding.Heuristic;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.NavigationGridGraph;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;
import org.xguzm.pathfinding.grid.finders.JumpPointFinder;
import org.xguzm.pathfinding.grid.finders.ThetaStarGridFinder;
import org.xguzm.pathfinding.grid.heuristics.ManhattanDistance;

import java.util.ArrayList;

/**
 * An implementation of the path generation library originally witten by
 * Xavier Guzman.
 *
 * <p>
 * Although this is a relatively efficient pathfinder, and one that's more than
 * capable of performing exactly as expected, this pathfinder can sometimes
 * generate paths with way too many points. As a result, certain types of
 * followers will be incredibly slow to follow the path.
 * </p>
 *
 * <p>
 * This is really where a lot of the "magic" happens. Although Pathfinder on
 * its own handles a lot of stuff - positional tracking, multithreading,
 * path management, path generation, path following, all of that cool stuff -
 * most of the actual path generation is outsourced to another library.
 * </p>
 *
 * <p>
 * As a result, the code in this class is all tested and working extremely
 * well. Any issues with path generation that can't be resolved by opening
 * an issue on Pathfinder should be reported. As Xavier's library is hosted
 * and built on my GitHub, I can make any changes that would fix the code.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Xygum implements Generator {
    /**
     * The Pathfinder library's configuration.
     */
    private final PathfinderConfig config;

    /**
     * Navigation utilities.
     */
    private final Nav nav;

    /**
     * The finder itself.
     */
    private final Finder finder;

    /**
     * The pathfinder's configuration class.
     */
    private static class FinderConfiguration {
        /**
         * Options for a grid finder.
         */
        public static final GridFinderOptions options;

        /**
         * Whether or not diagonal movement is allowed within the grid.
         *
         * <p>
         * <b>Note:</b> This will be ignored in {@link JumpPointFinder}, as diagonal movement is required for it
         * <p>
         * <p>
         * Default value is true.
         *
         * @see GridFinderOptions#allowDiagonal
         */
        private static final boolean allowDiagonal = true;

        /**
         * When true, diagonal movement requires both neighbors to be open.
         * When false, diagonal movement can be achieved by having only one open neighbor
         * <p>
         * <p>
         * Example: To go from (1,1) to (2,2) when this is set to true, where (x) denotes a non walkable cell,
         * the following applies
         *
         * <pre>
         *                 Valid           Invalid
         *             +---+---+---+    +---+---+---+
         *             |   |   | 0 |    |   | x | 0 |
         *             +---+---+---+    +---+---+---+
         * when True   |   | 0 |   |    |   | 0 |   |
         *             +---+---+---+    +---+---+---+
         *             |   |   |   |    |   |   |   |
         *             +---+---+---+    +---+---+---+
         *
         *
         *             +---+---+---+
         *             |   | x | 0 |
         *             +---+---+---+
         * when false  |   | 0 |   |    none
         *             +---+---+---+
         *             |   |   |   |
         *             +---+---+---+
         * </pre>
         * <p>
         * If {@link #allowDiagonal} is false, this setting is ignored.
         * <p>
         * Default value is true.
         *
         * @see GridFinderOptions#dontCrossCorners
         */
        private static final boolean dontCrossCorners = true;

        /**
         * A way to calculate the distance between two nodes on some form of
         * a navigation graph.
         *
         * @see GridFinderOptions#heuristic
         */
        private static final Heuristic heuristic = new ManhattanDistance();

        /**
         * When false, (0,0) is located at the bottom left of the grid. When true, (0,0) is located
         * at the top left of the grid
         *
         * <p>
         * Default value is false.
         *
         * @see GridFinderOptions#isYDown
         */
        private static final boolean isYDown = false;

        /**
         * The cost of moving one cell over the x or y axis.
         *
         * @see GridFinderOptions#orthogonalMovementCost
         */
        private static final float orthogonalMovementCost = 1;

        /**
         * The cost of moving one cell over both the x and y axis.
         *
         * <p>
         * Determine the hypotenuse of a triangle with two sides with a side length of
         * one. This can be visualized as so:
         * <pre>
         *     a
         *   +-----/
         *   |    /
         *   |   /
         * b |  /   c
         *   | /
         *   |/
         *   /
         * a = 1.0;
         * b = 1.0;
         * c = sqrt((a^2)+(b^2));
         * </pre>
         * </p>
         *
         * @see GridFinderOptions#diagonalMovementCost
         */
        private static final float diagonalMovementCost = (float) Math.hypot(1, 1);

        static {
            // Set up the grid finder's options.
            options = new GridFinderOptions(
                    allowDiagonal,
                    dontCrossCorners,
                    heuristic,
                    isYDown,
                    orthogonalMovementCost,
                    diagonalMovementCost
            );
        }
    }

    /**
     * Navigation class, used in... well, navigating, obviously.
     *
     * <p>
     * There were initially a lot of issues with memory, given this pathfinder's
     * grid would take up practically the entire heap on some less powerful
     * systems. In order to counter this, a new array of GridCells is generated
     * every time a path needs to be found.
     * </p>
     *
     * @see GridCell[][]
     * @see NavigationGrid
     */
    private class Nav {
        /**
         * Get cells based on inputted values.
         *
         * @param minX min x val
         * @param minY min y val
         * @param maxX max x val
         * @param maxY max y val
         * @return cells based on parameters
         * @see MapTools#getSmallCells(Map, int, int, int, int, int, double, double)
         */
        GridCell[][] getCells(int minX,
                              int minY,
                              int maxX,
                              int maxY) {
            return MapTools.getSmallCells(
                    config.getMap(),
                    minX,
                    minY,
                    maxX,
                    maxY,
                    config.getSpecificity(),
                    config.getRobotX(),
                    config.getRobotY()
            );
        }

        /**
         * Get a new NavigationGrid based on the minimum and maximum X and Y
         * values.
         *
         * @param minX minimum X value.
         * @param minY minimum Y value.
         * @param maxX maximum X value.
         * @param maxY maximum Y value.
         * @return a new navigation grid based on minimum and maximum values.
         * @see Nav#getCells(int, int, int, int)
         */
        NavigationGrid<GridCell> getNav(int minX,
                                        int minY,
                                        int maxX,
                                        int maxY) {
            return new NavigationGrid<>(
                    getCells(
                            minX,
                            minY,
                            maxX,
                            maxY
                    )
            );
        }
    }

    /**
     * Wrapper class for the Theta Star Grid Finder.
     *
     * @see ThetaStarGridFinder
     * @see AStarGridFinder
     */
    private class Finder {
        /**
         * Which type of finder is used.
         *
         * <p>
         * The two options are Theta* and A*.
         * </p>
         */
        Finders f;

        /**
         * A Theta* pathfinder.
         */
        ThetaStarGridFinder<GridCell> thetaFinder;

        /**
         * An A* pathfinder.
         */
        AStarGridFinder<GridCell> aStarFinder;

        /**
         * Create a new Finder instance.
         *
         * <p>
         * As well as creating a new Finder, we also initialize both the
         * Theta* and A* pathfinders.
         * </p>
         */
        public Finder() {
            thetaFinder = new ThetaStarGridFinder<>(
                    GridCell.class,
                    FinderConfiguration.options
            );
            aStarFinder = new AStarGridFinder<>(
                    GridCell.class,
                    FinderConfiguration.options
            );
        }

        /**
         * Fix a point.
         *
         * <p>
         * Point-fixing is really just making sure it fits within the min
         * and max bounds so that we don't get any null pointer exceptions
         * from the path finding systems that we're using.
         * </p>
         *
         * @param p    the original point.
         * @param minX min x val
         * @param minY min y val
         * @param maxX max x val
         * @param maxY max y val
         * @return a new, "fixed" point
         */
        Point fix(Point p,
                  int minX,
                  int minY,
                  int maxX,
                  int maxY) {
            /*
             * Figure out a new X value - clip.
             */
            int x = (int) (p.getX() < minX ? minX :
                    (p.getX() > maxX ? maxX : p.getX()));

            /*
             * Figure out a new Y value - clip.
             */
            int y = (int) (p.getY() < minY ? minY :
                    (p.getY() > maxY ? maxY : p.getY()));

            /*
             * Return a new point.
             *
             * This point should have the X and Y values that have been
             * calculated earlier in the method.
             */
            return new Point(x, y);
        }

        /**
         * Get a path between two points.
         *
         * @param start start point.
         * @param end   end point.
         * @return a path between the two points.
         * @see ThetaStarGridFinder#findPath(int, int, int, int, NavigationGridGraph)
         * @see AStarGridFinder#findPath(int, int, int, int, NavigationGridGraph)
         */
        DynamicArray<GridCell> getPath(Point start,
                                       Point end) {
            /*
             * Declare minimum and maximum values for later use.
             *
             * This code is very ugly - how can we improve upon that in the
             * future? Declaring four variables in a row like this typically
             * isn't so pleasing, especially when abstraction would make it
             * all look so much cleaner.
             */

            int minX = (int) Math.floor(Math.min(start.getX(), end.getX()));
            int minY = (int) Math.floor(Math.min(start.getY(), end.getY()));
            int maxX = (int) Math.floor(Math.max(start.getX(), end.getX()));
            int maxY = (int) Math.floor(Math.max(start.getY(), end.getY()));

            /*
             * Start and end positions need to get scaled.
             */

            start = Point.scale(
                    start,
                    config.getSpecificity()
            );
            end = Point.scale(
                    end,
                    config.getSpecificity()
            );

            /*
             * Create a new navigation grid.
             *
             * Is creating a new navigation grid for every path we find too
             * expensive? Is there a better way to do this? I'd look into
             * it, for sure, but I'm a little bit lazy right now.
             */
            NavigationGrid<GridCell> grid = nav.getNav(
                    minX,
                    minY,
                    maxX,
                    maxY
            );

            /*
             * Declare more variables.
             *
             * This section of the code seems to have way too many variables,
             * so I'd like to ask - how can we cut down on that?
             */

            int specificity = config.getSpecificity();
            int xOffset = minX * specificity;
            int yOffset = minY * specificity;

            /*
             * Multiply everything by specificity values.
             *
             * If we don't multiply these by specificity values, the whole
             * thing breaks, and it doesn't work. And then someone has to
             * spend hours debugging it.
             *
             * In case you couldn't tell, that person is ME - and I'm a little
             * bit angry about it.
             */

            minX *= specificity;
            minY *= specificity;
            maxX *= specificity;
            maxY *= specificity;

            /*
             * Fix the start and end positions.
             *
             * If you're curious about what fixing a point entails, you're
             * more than welcome to go check out the documentation for
             * the fix method in this class.
             */

            start = fix(start, minX, minY, maxX, maxY);
            end = fix(end, minX, minY, maxX, maxY);

            /*
             * Create integer variables because I'm lazy and don't feel
             * like inlining all of them.
             */

            int startX = (int)
                    (start.getX() - xOffset);
            int startY = (int)
                    (start.getY() - yOffset);
            int endX = (int)
                    (end.getX() - xOffset);
            int endY = (int)
                    (end.getY() - yOffset);

            /*
             * If a path WAS found...
             */
            if (f != null) {
                switch (f) {
                    case T:
                        /*
                         * We use the theta star pathfinding algorithm.
                         */
                        return new DynamicArray<>(
                                new ArrayList<>(thetaFinder.findPath(
                                        startX,
                                        startY,
                                        endX,
                                        endY,
                                        grid
                                ))
                        );
                    default:
                        /*
                         * If the default option is given, for a reason I
                         * could not even possibly begin to fathom, we just
                         * use the A case.
                         *
                         * By not putting a "break" here, we continue along
                         * and move on to the next case.
                         */
                    case A:
                        /*
                         * We use the A star pathfinding algorithm.
                         */
                        return new DynamicArray<>(
                                new ArrayList<>(aStarFinder.findPath(
                                        startX,
                                        startY,
                                        endX,
                                        endY,
                                        grid
                                ))
                        );
                }
            } else {
                /*
                 * Nothing could be found - return null so everyone knows
                 * that nothing was found.
                 */
                return null;
            }
        }
    }

    /**
     * Create a new instance of the path generation wrapper.
     *
     * @param config the Pathfinder library's configuration class.
     */
    public Xygum(PathfinderConfig config,
                 Finders f) {
        this.config = config;

        nav = new Nav();
        finder = new Finder();

        finder.f = f;
    }

    /**
     * Get a path that's notated in PathfindingCore's default GridCell implementation.
     *
     * <p>
     * All of the pathfinding work done here is handled by the Finder class.
     * </p>
     *
     * @param start the start position (notated as a double coordinate).
     * @param end   the end position (notated as a double coordinate).
     * @return a list of scaled-up (1440x1440) {@link GridCell} instances.
     * @see Finder#getCellPath(Point, Point)
     */
    private DynamicArray<GridCell> getCellPath(Point start,
                                               Point end) {
        return finder.getPath(start, end);
    }

    /**
     * Get a path, from a start coordinate to an end coordinate, that's
     * directly readable by our implementation of the pathfinding system.
     *
     * @param start the start coordinate.
     * @param end   the end coordinate.
     * @return a group, composed of individual Point items.
     * @see Xygum#getCellPath(Point, Point)
     */
    @Override
    public DynamicArray<Point> getCoordinatePath(Point start, Point end) {
        /*
         * Get a path, in cells, from point A to point B.
         */
        DynamicArray<GridCell> cells = getCellPath(start, end);

        /*
         * Initialize a new DynamicArray of points.
         *
         * This DynamicArray should be contributed to later by converting
         * GridCell elements into points.
         */
        DynamicArray<Point> points = new DynamicArray<>();

        /*
         * For each cell, we add a new element to the list of points
         * based on the cell's X and Y values.
         *
         * We need to test what values the cell's X and Y values should
         * be modified by. Obviously, the GridCell X and Y values won't ever
         * correspond 100% to our values.
         */
        cells.itr().forEach(cell ->
                points.add(new Point(cell.getX(), cell.getY())));

        return points;
    }

    /**
     * The available finders.
     */
    public enum Finders {
        /**
         * Theta star finder.
         */
        T,

        /**
         * A star finder.
         */
        A
    }
}
