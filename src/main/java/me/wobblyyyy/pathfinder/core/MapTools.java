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

package me.wobblyyyy.pathfinder.core;

import me.wobblyyyy.pathfinder.geometry.*;
import me.wobblyyyy.pathfinder.map.Map;
import org.xguzm.pathfinding.grid.GridCell;

import java.util.ArrayList;

/**
 * A wide variety of different tools for playing around with maps.
 *
 * <p>
 * Unless you're really interested in exactly HOW Pathfinder works, you can
 * entirely ignore this file. The only method that does anything you need
 * to be concerned with is the getCells() method.
 * </p>
 *
 * <p>
 * This, like much of the other code in the revamped version of Pathfinder,
 * is a little bit outdated and purposeless. If you have the time to, it would
 * be (very greatly) appreciated if you could go through this file, clean
 * everything up a bit, and maybe add a little bit of that oh-so-sexy
 * documentation you love to see.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class MapTools {
    /**
     * Scale a map so that the zones are compatible with the pathfinder.
     *
     * <p>
     * By default, zones are... well, their normal sizes. However, if we were
     * to use the pathfinding system with these normally-sized zones, we'd run
     * into tons upon tons of different collisions. By scaling the zones up
     * so that they can't possibly touch the robot, we prevent these collisions,
     * which, is, in fact, pretty darn cool.
     * </p>
     *
     * @param map    the original map.
     * @param length the robot's LENGTH.
     * @param width  the robot's WIDTH.
     * @return a new map with scaled zones.
     */
    public static Map scaleMap(Map map,
                               double length,
                               double width) {
        ArrayList<Zone> newZones = new ArrayList<>();

        for (Zone z : map.zones) {
            if (z.getParentShape() instanceof Rectangle) {
                newZones.add(
                        createNewZone(
                                z,
                                createNewRectangle(
                                        z,
                                        width,
                                        length
                                )
                        )
                );
            }

            if (z.getParentShape() instanceof Circle) {
                newZones.add(
                        createNewZone(
                                z,
                                createNewCircle(
                                        z,
                                        width,
                                        length
                                )
                        )
                );
            }
        }

        return new Map(newZones);
    }

    /**
     * Create a (much smaller) two-dimensional array of grid cells for use in
     * finding a single path.
     *
     * @param map   the field's map.
     * @param minX  the path's minimum X value.
     * @param minY  the path's minimum Y value.
     * @param maxX  the path's maximum X value.
     * @param maxY  the path's maximum Y value.
     * @param scale the field's scale/specificity.
     * @return a two-dimensional array for use in a navigation grid.
     */
    public static GridCell[][] createSmallCells(Map map,
                                                int minX,
                                                int minY,
                                                int maxX,
                                                int maxY,
                                                int scale) {
        minX = minX < 1 ? minX : minX - 1;
        minY = minY < 1 ? minY : minY - 1;
        maxX += 1;
        maxY += 1;

        int x = maxX - minX;
        int y = maxY - minY;

        GridCell[][] cells = new GridCell[(x * scale)][(y * scale)];

        ArrayList<Zone> solids = new ArrayList<>();
        for (Zone z : map.zones) if (z.isSolid()) solids.add(z);

        for (int i = 0; i < x * scale; i++) {
            for (int j = 0; j < y * scale; j++) {
                boolean isWalkable = true;

                for (Zone z : solids) {
                    if (z.isPointInZone(
                            new Point(
                                    (i * scale) + (minX * scale),
                                    (j * scale) + (minY * scale)
                            )
                    )) isWalkable = false;
                }

                cells[i][j] = new GridCell(
                        i,
                        j,
                        isWalkable
                );
            }
        }

        return cells;
    }

    /**
     * Create a new two-dimensional array of GridCell elements based on a
     * provided field map.
     *
     * @param map         a map of the field. Note that this map MUST be
     *                    properly scaled prior to this method - if the
     *                    map isn't properly scaled, you're not gonna get
     *                    the results you want.
     * @param sizeX       the field's X dimension (array size).
     * @param sizeY       the field's Y dimension (array size).
     * @param specificity the field's integer scale factor.
     * @return a two-dimensional array of grid cells representing the map.
     */
    public static GridCell[][] createCells(Map map,
                                           int sizeX,
                                           int sizeY,
                                           int specificity) {
        GridCell[][] cells = new GridCell[sizeX * specificity][sizeY * specificity];

        ArrayList<Zone> solids = new ArrayList<>();
        for (Zone z : map.zones) if (z.isSolid()) solids.add(z);

        for (int i = 0; i < sizeX * specificity; i++) {
            for (int j = 0; j < sizeY * specificity; j++) {
                boolean isWalkable = true;

                for (Zone z : solids) {
                    if (z.isPointInZone(
                            new Point(
                                    i * specificity,
                                    j * specificity
                            )
                    )) isWalkable = false;
                }

                cells[i][j] = new GridCell(i, j, isWalkable);
            }
        }

        return cells;
    }

    /**
     * Get a 2d GridCell array based on an inputted map.
     *
     * @param map              an inputted field map.
     * @param fieldX           the X size of the cell array.
     * @param fieldY           the Y size of the cell array.
     * @param fieldSpecificity the field's specific scalar factor.
     * @param robotX           the robot's X size. X/Y are the same here.
     * @param robotY           the robot's Y size. X/Y are the same here.
     * @return a 2d GridCell array representing the field.
     */
    public static GridCell[][] getCells(Map map,
                                        int fieldX,
                                        int fieldY,
                                        int fieldSpecificity,
                                        double robotX,
                                        double robotY) {
        return createCells(
                scaleMap(
                        map,
                        robotX,
                        robotY * 1.0
                ),
                fieldX,
                fieldY,
                fieldSpecificity
        );
    }

    /**
     * Get a 2d array of cells based on several inputted parameters.
     *
     * <p>
     * DISPOSABLE INSTANCE!
     * </p>
     *
     * @param map   the field's map.
     * @param minX  the minimum X value.
     * @param minY  the minimum Y value.
     * @param maxX  the maximum X value.
     * @param maxY  the maximum Y value.
     * @param scale the scale of the field.
     * @param rX    robot's X.
     * @param rY    robot's Y.
     * @return small grid for navigation.
     */
    public static GridCell[][] getSmallCells(Map map,
                                             int minX,
                                             int minY,
                                             int maxX,
                                             int maxY,
                                             int scale,
                                             double rX,
                                             double rY) {
        return createSmallCells(
                scaleMap(
                        map,
                        rX,
                        rY * 1.0
                ),
                minX,
                minY,
                maxX,
                maxY,
                scale
        );
    }

    /**
     * Create a new zone based on a previous zone and a new shape.
     *
     * @param zone  the original zone.
     * @param shape the new shape.
     * @return the new zone.
     */
    public static Zone createNewZone(final Zone zone, final Shape shape) {
        return new Zone() {
            @Override
            public String getName() {
                return zone.getName();
            }

            @Override
            public Shape getParentShape() {
                return shape;
            }

            @Override
            public int getZonePriority() {
                return zone.getZonePriority();
            }

            @Override
            public boolean isPointInZone(Point point) {
                return zone.isPointInZone(point);
            }

            @Override
            public double getDriveSpeedMultiplier() {
                return zone.getDriveSpeedMultiplier();
            }

            @Override
            public int getComponents() {
                return zone.getComponents();
            }

            @Override
            public boolean isSolid() {
                return false;
            }
        };
    }

    /**
     * Create a new rectangle based on an old zone and the robot's size.
     *
     * @param zone   the old rectangle zone.
     * @param width  the width of the robot.
     * @param height the height of the robot.
     * @return a new rectangle.
     */
    public static Rectangle createNewRectangle(Zone zone,
                                               double width,
                                               double height) {
        Rectangle original = (Rectangle) zone.getParentShape();
        Rectangle updated;

        Rectangle.Corners drawCorner = original.drawCorner;
        Rectangle.Corners rotateCorner = original.rotateCorner;
        Point startingPoint = original.startingPoint;
        double xDraw = original.xDraw;
        double yDraw = original.yDraw;
        double rotationalAngle = original.rotationalAngle;

        double halfRobotWidth = width / 2;
        double halfRobotHeight = height / 2;

        Point startMod;

        switch (drawCorner) {
            case FRONT_RIGHT:
                startMod = new Point(
                        halfRobotWidth,
                        halfRobotHeight
                );
                xDraw += -halfRobotWidth;
                yDraw += -halfRobotHeight;
                break;
            case FRONT_LEFT:
                startMod = new Point(
                        -halfRobotWidth,
                        halfRobotHeight
                );
                xDraw += halfRobotWidth;
                yDraw += -halfRobotHeight;
                break;
            case BACK_RIGHT:
                startMod = new Point(
                        halfRobotWidth,
                        -halfRobotHeight
                );
                xDraw += -halfRobotWidth;
                yDraw += halfRobotHeight;
                break;
            case BACK_LEFT:
                startMod = new Point(
                        -halfRobotWidth,
                        -halfRobotHeight
                );
                xDraw += halfRobotWidth;
                yDraw += halfRobotHeight;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + drawCorner);
        }

        startingPoint = Point.add(
                startingPoint,
                startMod
        );

        updated = new Rectangle(
                drawCorner,
                rotateCorner,
                startingPoint,
                xDraw,
                yDraw,
                rotationalAngle
        );

        return updated;
    }

    /**
     * Create a new circle.
     *
     * @param zone   old zone.
     * @param width  robot width.
     * @param height robot height.
     * @return new circle.
     */
    public static Circle createNewCircle(Zone zone,
                                         double width,
                                         double height) {
        Circle original = (Circle) zone.getParentShape();
        Circle updated;

        double halfRobotWidth = width / 2;

        Point center = original.center;
        double radius = original.radius;
        double hitboxRadius = original.hitboxRadius;

        radius += halfRobotWidth;
        hitboxRadius += halfRobotWidth;

        updated = new Circle(
                center,
                radius,
                hitboxRadius
        );

        return updated;
    }

    /**
     * Get a list of all of the zones contained in an area.
     *
     * <p>
     * The sampling method behind this is in need of some serious improvement.
     * Simply testing 16 points isn't anywhere near as accurate as testing to
     * see if any of the shapes are or are not inside of the given zone.
     * </p>
     *
     * @param map  the map to check against. (unscaled)
     * @param area the area to check. (unscaled)
     * @return a list of all of the zones inside a given area.
     */
    public static ArrayList<Zone> getZonesInArea(Map map,
                                                 Area area) {
        ArrayList<Point> _testPoints = new ArrayList<Point>() {{
            add(new Point(
                    area.getMinX(),
                    area.getMinY()
            ));
            add(new Point(
                    area.getMaxX(),
                    area.getMinY()
            ));
            add(new Point(
                    area.getMinX(),
                    area.getMinY()
            ));
            add(new Point(
                    area.getMaxX(),
                    area.getMaxY()
            ));
        }};
        ArrayList<Point> testPoints = new ArrayList<>();

        for (Point p : _testPoints) {
            Point half = Point.scale(p, 0.5);
            Point quarter = Point.scale(p, 0.25);

            testPoints.add(p);
            testPoints.add(half);
            testPoints.add(quarter);
        }

        ArrayList<Zone> zones = new ArrayList<>();

        for (Zone z : map.zones) {
            if (z.isSolid()) {
                Shape s = z.getParentShape();
                for (Point p : testPoints) {
                    if (s.isPointInShape(p)) zones.add(z);
                }
            }
        }

        return zones;
    }

    /**
     * Is a given area completely void of any zones?
     *
     * <p>
     * Non-solid zones aren't counted here - for example, the field, which is
     * the largest (and sometimes only) non-solid zone on the field, isn't
     * counted when deciding whether or not a zone is empty.
     * </p>
     *
     * @param map  the field's map. (unscaled)
     * @param area the area of the field to test. (unscaled)
     * @return whether or not the given area is empty.
     */
    public static boolean isAreaEmpty(Map map,
                                      Area area) {
        return getZonesInArea(map, area).size() == 0;
    }

    /**
     * Class used for representing the area of a zone.
     */
    public static class Area {
        private final double minX;
        private final double minY;
        private final double maxX;
        private final double maxY;

        /**
         * Create a new area.
         *
         * @param a one of the area's defining points.
         * @param b one of the area's defining points.
         */
        public Area(Point a,
                    Point b) {
            this(
                    Math.min(a.getX(), b.getX()),
                    Math.min(a.getY(), b.getY()),
                    Math.max(a.getX(), b.getX()),
                    Math.max(a.getY(), b.getY())
            );
        }

        /**
         * Create a new area.
         *
         * @param minX area's min x.
         * @param minY area's min y.
         * @param maxX area's max x.
         * @param maxY area's max y.
         */
        public Area(double minX,
                    double minY,
                    double maxX,
                    double maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        /**
         * Get the area's minimum X.
         *
         * @return the area's minimum X.
         */
        public double getMinX() {
            return minX;
        }

        /**
         * Get the area's minimum Y.
         *
         * @return the area's minimum Y.
         */
        public double getMinY() {
            return minY;
        }

        /**
         * Get the area's maximum X.
         *
         * @return the area's maximum X.
         */
        public double getMaxX() {
            return maxX;
        }

        /**
         * Get the area's maximum Y.
         *
         * @return the area's maximum Y.
         */
        public double getMaxY() {
            return maxY;
        }
    }
}
