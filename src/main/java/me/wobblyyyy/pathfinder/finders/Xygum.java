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
import org.xguzm.pathfinding.Heuristic;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
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
 * @author Colin Robertson
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
         */
        private static final boolean dontCrossCorners = true;

        /**
         * A way to calculate the distance between two nodes on some form of
         * a navigation graph.
         */
        private static final Heuristic heuristic = new ManhattanDistance();

        /**
         * When false, (0,0) is located at the bottom left of the grid. When true, (0,0) is located
         * at the top left of the grid
         *
         * <p>
         * Default value is false.
         */
        private static final boolean isYDown = false;

        /**
         * The cost of moving one cell over the x or y axis.
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
     */
    private class Nav {
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
     */
    private class Finder {
        Finders f;
        ThetaStarGridFinder<GridCell> thetaFinder;
        AStarGridFinder<GridCell> aStarFinder;

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

        Point fix(Point p,
                  int minX,
                  int minY,
                  int maxX,
                  int maxY) {
            int x = (int) (p.getX() < minX ? minX :
                    (p.getX() > maxX ? maxX : p.getX()));
            int y = (int) (p.getY() < minY ? minY :
                    (p.getY() > maxY ? maxY : p.getY()));

            return new Point(x, y);
        }

        ArrayList<GridCell> getPath(Point start,
                                    Point end) {
            int minX = (int) Math.floor(Math.min(start.getX(), end.getX()));
            int minY = (int) Math.floor(Math.min(start.getY(), end.getY()));
            int maxX = (int) Math.floor(Math.max(start.getX(), end.getX()));
            int maxY = (int) Math.floor(Math.max(start.getY(), end.getY()));

            start = Point.scale(
                    start,
                    config.getSpecificity()
            );
            end = Point.scale(
                    end,
                    config.getSpecificity()
            );

            NavigationGrid<GridCell> grid = nav.getNav(
                    minX,
                    minY,
                    maxX,
                    maxY
            );

            int specificity = config.getSpecificity();
            int xOffset = minX * specificity;
            int yOffset = minY * specificity;

            minX *= specificity;
            minY *= specificity;
            maxX *= specificity;
            maxY *= specificity;

            start = fix(start, minX, minY, maxX, maxY);
            end = fix(end, minX, minY, maxX, maxY);

            int startX = (int)
                    (start.getX() - xOffset);
            int startY = (int)
                    (start.getY() - yOffset);
            int endX = (int)
                    (end.getX() - xOffset);
            int endY = (int)
                    (end.getY() - yOffset);

            if (f != null) {
                switch (f) {
                    case T:
                        return new ArrayList<>(
                                thetaFinder.findPath(
                                        startX,
                                        startY,
                                        endX,
                                        endY,
                                        grid
                                )
                        );
                    default:
                    case A:
                        return new ArrayList<>(
                                aStarFinder.findPath(
                                        startX,
                                        startY,
                                        endX,
                                        endY,
                                        grid
                                )
                        );
                }
            } else {
                return null;
            }
        }
    }

    /**
     * Create a new instance of Xavier's pathfinder.
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
     * Meccanum robots (or at least those that use this library) don't actually read GridCells - rather,
     * they read coordinates. Although this is a public function, it's rather unlikely you'll ever
     * need to use it. Rather, you should go check out...
     * </p>
     *
     * @param start the start position (notated as a double coordinate).
     * @param end   the end position (notated as a double coordinate).
     * @return a list of scaled-up (1440x1440) {@link GridCell} instances.
     */
    private ArrayList<GridCell> getCellPath(Point start,
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
     */
    @Override
    public ArrayList<Point> getCoordinatePath(Point start, Point end) {
        ArrayList<GridCell> cells = getCellPath(start, end);
        ArrayList<Point> points = new ArrayList<>();

        for (GridCell c : cells) {
            points.add(
                    new Point(
                            c.getX(),
                            c.getY()
                    )
            );
        }

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
