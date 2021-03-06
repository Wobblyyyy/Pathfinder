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

import me.wobblyyyy.pathfinder.util.Distance;

/**
 * A combination of two points, forming a line. Incredible, truly!
 *
 * <p>
 * You should NOT use the Line class as a method of measuring the distance
 * between two points. Rather, the Distance class has a lovely little static
 * method named "getDistance" that'll do exactly that for you.
 * </p>
 *
 * <p>
 * Lines are a core component of many shapes, namely rectangles and squares.
 * Aside from using those shapes, it's fairly unlikely you're going to need to
 * use this class. But it's here anyways!
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Line {
    /**
     * One of the two points of a line.
     */
    private final Point a;

    /**
     * One of the two points of a line.
     */
    private final Point b;

    /**
     * The midpoint of the line.
     */
    private final Point midpoint;

    /**
     * The midpoint between the midpoint and A.
     */

    private final Point aPoint;

    /**
     * The midpoint between the midpoint and B.
     */
    private final Point bPoint;

    /**
     * Create a new line.
     *
     * <p>
     * Lines have five major points:
     * <ul>
     *     <li>A (you provide this one)</li>
     *     <li>B (you provide this one)</li>
     *     <li>Midpoint (in between A and B)</li>
     *     <li>A-Point (in between A and the midpoint)</li>
     *     <li>B-Point (in between B and the midpoint)</li>
     * </ul>
     * </p>
     *
     * @param a one of the line's two points.
     * @param b one of the line's two points.
     */
    public Line(Point a,
                Point b) {
        this.a = a;
        this.b = b;
        this.midpoint = Point.blend(a, b);
        this.aPoint = Point.blend(a, midpoint);
        this.bPoint = Point.blend(b, midpoint);
    }

    /**
     * Create a new line from a center point, extending outwards at a given
     * angle for a given length.
     *
     * <p>
     * Lines have five major points:
     * <ul>
     *     <li>A (you provide this one)</li>
     *     <li>B (you provide this one)</li>
     *     <li>Midpoint (in between A and B)</li>
     *     <li>A-Point (in between A and the midpoint)</li>
     *     <li>B-Point (in between B and the midpoint)</li>
     * </ul>
     * </p>
     *
     * @param a      the line's center-point.
     * @param angle  the angle at which the line should be drawn.
     * @param length the length of the line to draw.
     */
    public Line(Point a,
                double angle,
                double length) {
        this(a, Distance.inDirection(
                new HeadingPoint(
                        a.getX(),
                        a.getY(),
                        angle
                ),
                length
        ));
    }

    /**
     * Get the A point.
     *
     * @return the A point.
     */
    public Point getA() {
        return a;
    }

    /**
     * Get the B point.
     *
     * @return the B point.
     */
    public Point getB() {
        return b;
    }

    /**
     * Get the midpoint.
     *
     * @return the midpoint.
     */
    public Point getMidpoint() {
        return midpoint;
    }

    /**
     * Get the point between A and the midpoint.
     *
     * @return the point between A and the midpoint.
     */
    public Point getAPoint() {
        return aPoint;
    }

    /**
     * Get the point between B and the midpoint.
     *
     * @return the point between B and the midpoint.
     */
    public Point getBPoint() {
        return bPoint;
    }

    /**
     * Get the length of the line, in units.
     *
     * <p>
     * Mapping has nothing to do with units - whatever unit you put in,
     * you'll get out.
     * </p>
     *
     * @return the length of the line in (?) units.
     */
    public double getLength() {
        return Distance.getDistance(
                this.a,
                this.b
        );
    }

    /**
     * Check whether or not two lines collide at any point.
     *
     * <p>
     * Prior to reaching this stage of virtual collision detection,
     * check to make sure none of the following conditions are true...
     * <ul>
     *     <li>Lines are parallel.</li>
     *     <li>Line intersection is theoretically impossible due to distance.</li>
     *     <li>Line intersection is, for another reason, theoretically impossible.</li>
     * </ul>
     * </p>
     * <p>
     * A, B, and C are numbers which define each of the lines. Given two sets
     * of points, (x1, y1) and (x2, y2) we can determine the point of intersection
     * between the two lines. After finding this point of intersection, however,
     * we still have to make sure that that point of intersection is actually on
     * both of the lines, ensuring a virtual collision is indeed happening.
     * <pre>
     * A = y2-y1;
     * B = x1-x2;
     * C = Ax1+By1;
     * </pre>
     * </p>
     *
     * @param a the first line
     * @param b the second line
     * @return whether or not the lines intersect
     */
    public static boolean doesIntersect(Line a,
                                        Line b) {
        // I'm sure none of the below code makes
        // any sense at all, but I'll do my best
        // to ensure I explain at least a little
        // bit of what's going on here.
        // Note that these arent' my formulas; so
        // if anything is wrong, it's not my fault.
        // Each of the first six doubles is a part
        // of the A, B, C trio mentioned in the
        // JavaDoc for this method.
        // The first letter, such as (A_a) or (B_a)
        // indicates whether the next letter, following
        // the underscore, is a part of the FIRST (a) or
        // the SECOND (b) group of A, B, and C.
        double a_a = a.b.getY() - a.a.getY();
        double a_b = a.a.getX() - a.b.getX();
        double a_c = (a_a * a.a.getX()) + (a_b * a.a.getY());
        double b_a = b.b.getY() - b.a.getY();
        double b_b = b.a.getX() - b.b.getX();
        double b_c = (b_a * b.a.getX()) + (b_b * b.a.getY());
        // "det" is a pretty cool number. I have absolutely
        // no clue what the fuck "det" means, and I'm way
        // too lazy to figure out the meaning of it - but,
        // anyways...
        // If det is zero, both of the lines are parallel and
        // do not intersect at any point. If they're parallel,
        // there's no point in continuing, so we just return
        // false automatically.
        double det = (a_a * b_b) - (b_a * a_b);
        if (det == 0) {
            // Lines are parallel, no point in continuing.
            return false;
        } else {
            // Steps...
            // 1. Determine point of intersection with these formulas.
            // 2. Check if the X and Y coordinates exist on each of the lines.
            // 3. That's it! Yay!
            // Once again, these formulas are not mine - I
            // can't and don't accept any responsibility if
            // this doesn't work. However, in the unlikely event
            // that this ends up working on the first try, this
            // was all code I came up with while I was sleeping.
            // That's right - I'm so good at coding I can do math
            // I learned in 9th grade and forgot about later while
            // I'm not even awake.
            double i_x = ((b_b * a_c) - (a_b * b_c)) / det; // x coord
            double i_y = ((a_a * b_c) - (b_a * a_c)) / det; // y coord
            // Create a new coordinate based on our X and Y values.
            Point pointOfIntersection = new Point(i_x, i_y);
            // Evaluate whether the point we've found is on
            // both of our lines. If it is, there is a collision.
            // If it's not, there is no collision/intersection.
            return a.isPointOnLine(pointOfIntersection) &&
                    b.isPointOnLine(pointOfIntersection);
        }
    }

    /**
     * Check whether or not a given ordered pair lies on
     * the instanced line.
     *
     * <p>
     * "If your line segment goes from (x1, y1) to (x2, y2), then to
     * check if (x, y) is on that segment, you just need to check that
     * min(x1, x2) <= x <= max(x1, x2), and do the same thing for Y."
     * </p>
     *
     * <p>
     * In order to check whether or not two lines collide at any
     * point, we have to first ensure that there is indeed a point
     * of collision. This may, in the near future, need to be adjusted
     * using comparators to allow for certain degrees of tolerance
     * to ensure that points are very nearly on the line rather than
     * on the line - simply checking if a point is on a given line could
     * sometimes be inaccurate.
     * </p>
     *
     * @param point a given pair
     * @return whether or not that point is on this line
     */
    public boolean isPointOnLine(Point point) {
        boolean xPass =
                Math.min(a.getX(), b.getX()) <= point.getX() &&
                        point.getX() <= Math.max(a.getX(), b.getX());
        boolean yPass =
                Math.min(a.getY(), b.getY()) <= point.getY() &&
                        point.getY() <= Math.max(a.getY(), b.getY());
        return xPass && yPass;
    }
}
