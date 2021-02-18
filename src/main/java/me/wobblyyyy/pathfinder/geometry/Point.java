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

import me.wobblyyyy.intra.ftc2.utils.math.Math;

/**
 * The most simple geometry of all - a singular point.
 *
 * <p>
 * Now, in case you have serious brain damage, a point is just an X and a Y
 * value put together. I'm assuming you don't have serious brain damage, so
 * you can continue reading. Have fun.
 * </p>
 *
 * <p>
 * Points are the core of any of the geometry in this library. Everything in
 * this class should (hopefully) be pretty well-documented. Also, I'd like
 * to suggest to you that you check out the static methods in the latter
 * half of this file - they're incredibly useful for point manipulation.
 * </p>
 *
 * <p>
 * Although points are incredibly simple as a concept - I mean, come on, man.
 * It's a point! How hard can point things really be? Not very is your answer.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Point {
    /**
     * The point's X value.
     */
    private double x;

    /**
     * The point's Y value.
     */
    private double y;

    /**
     * Create a new point.
     *
     * <p>
     * Units aren't important for field-mapping and geometry, as all units
     * are relative. However, I'd strongly suggest that you keep everything in
     * inches. I'm not suggesting this because I feel like it - rather, if
     * you don't use inches, some of the critical pathfinding code won't work.
     * </p>
     *
     * @param x the point's X value.
     * @param y the point's Y value.
     */
    public Point(double x,
                 double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the point's X value.
     *
     * <p>
     * Points are final - this value should not ever change after it has been
     * initialized. If you'd like to modify a point's values, you can create
     * another point or use one of the static methods in this file.
     * </p>
     *
     * @return the point's X value.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the point's Y value.
     *
     * <p>
     * Points are final - this value should not ever change after it has been
     * initialized. If you'd like to modify a point's values, you can create
     * another point or use one of the static methods in this file.
     * </p>
     *
     * @return the point's Y value.
     */
    public double getY() {
        return y;
    }

    /**
     * Get the theta measurement, from the origin of the plane, to this point.
     *
     * <p>
     * This is essentially a fancy wrapper for Math.atan2, which, in case
     * you weren't aware, calculates the angle a point is at based on that
     * point's tangent. Although the method returns a measurement in radians,
     * it's converted to degrees prior to being returned.
     * </p>
     *
     * @return the theta measurement, in degrees.
     */
    public double getTheta() {
        /*
         * Use Math.atan2 to get the theta and convert it to degrees.
         */
        return Math.atan2(getY(), getX()) * (180 / Math.PI);
    }

    /**
     * Scale a point based a given value.
     *
     * @param scale the point's scale factor.
     */
    public void scale(double scale) {
        scale(scale, scale);
    }

    /**
     * Scale a point based on two scale factors.
     *
     * @param xScale the X axis scale factor.
     * @param yScale the Y axis scale factor.
     */
    public void scale(double xScale,
                      double yScale) {
        this.x = getX() * xScale;
        this.y = getY() * yScale;
    }

    /**
     * Blend (average) two points together to form another point.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the result blended point.
     */
    public static Point blend(Point a,
                              Point b) {
        /*
         * Return a new point, which, in reality, is the average of the
         * two inputted points.
         */
        return new Point(
                Math.average(a.getX(), b.getX()),
                Math.average(a.getY(), b.getY())
        );
    }

    /**
     * Add two points together to form another point.
     *
     * <p>
     * This is NOT the same as blending! Blending is averaging, whereas this
     * method is simply addition.
     * </p>
     *
     * @param a one of the two points.
     * @param b one of the two points.
     * @return the sum of the two points.
     */
    public static Point add(Point a,
                            Point b) {
        /*
         * Return the simple sum of the two points.
         */
        return new Point(
                a.getX() + b.getX(),
                a.getY() + b.getY()
        );
    }

    /**
     * Scale a point upwards or downwards by a specific factor.
     *
     * <p>
     * This method does not independently scale the X and Y values - to do
     * that, you can take a look at any of these methods:
     * <ul>
     *     <li>Scale only X: {@link #scaleX(Point, double)}</li>
     *     <li>Scale only Y: {@link #scaleY(Point, double)}</li>
     *     <li>Scale both: {@link #scale(Point, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the factor to scale the point by.
     * @return a new, scaled, point.
     */
    public static Point scale(Point a,
                              double multiplier) {
        /*
         * Scale a point - scale the X and Y values the same.
         */
        return new Point(
                a.getX() * multiplier,
                a.getY() * multiplier
        );
    }

    /**
     * Scale the X and Y values of a point separately.
     *
     * <p>
     * If you're interested in scaling these axes uniformly, you can use the
     * simpler {@link #scale(Point, double)} method.
     * </p>
     *
     * @param a           the point to be scaled.
     * @param xMultiplier the value by which the X value is multiplied.
     * @param yMultiplier the value by which the Y value is multiplier.
     * @return a newly-scaled value.
     */
    public static Point scale(Point a,
                              double xMultiplier,
                              double yMultiplier) {
        return new Point(
                a.getX() * xMultiplier,
                a.getY() * yMultiplier
        );
    }

    /**
     * Scale only the X value of a point.
     *
     * <p>
     * If you'd like to scale both axes at the same time, you can check out
     * either of these two methods.
     * <ul>
     *     <li>X and Y together: {@link #scale(Point, double)}</li>
     *     <li>X and Y separate: {@link #scale(Point, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the value by which the point should be scaled by.
     * @return a newly-scaled point.
     */
    public static Point scaleX(Point a,
                               double multiplier) {
        return new Point(
                a.getX() * multiplier,
                a.getY()
        );
    }

    /**
     * Scale only the Y value of a point.
     *
     * <p>
     * If you'd like to scale both axes at the same time, you can check out
     * either of these two methods.
     * <ul>
     *     <li>X and Y together: {@link #scale(Point, double)}</li>
     *     <li>X and Y separate: {@link #scale(Point, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the value by which the point should be scaled by.
     * @return a newly-scaled point.
     */
    public static Point scaleY(Point a,
                               double multiplier) {
        return new Point(
                a.getX(),
                a.getY() * multiplier
        );
    }

    /**
     * Blend several points together.
     *
     * <p>
     * There's no weight or bias to this - each of the inputted points is
     * balanced evenly with all of the other points.
     * </p>
     *
     * @param points the points to blend together.
     * @return the blended/averaged points.
     */
    public static Point average(Point... points) {
        Point t = new Point(0, 0);

        for (Point p : points) {
            add(t, p);
        }

        return scale(t, points.length);
    }

    /**
     * Get the string value of the point.
     *
     * <p>
     * Although Java's default toString method is quite lovely, it doesn't
     * actually do a great job of converting a point to a string. So, all
     * we have to do is override it, and we're all good! Yay!
     * </p>
     *
     * @return the point, expressed as a string.
     */
    @Override
    public String toString() {
        return "{(" + getX() + ", " + getY() + ")}";
    }

    /**
     * Are two points exactly identical?
     *
     * <p>
     * Points are considered to be identical or non-identical based solely on
     * the point's X and Y values. If they're all the same, the
     * points are identical. If they're not, they're not identical.
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return whether or not the points are identical.
     */
    public static boolean isSame(Point a,
                                 Point b) {
        return a.getX() == b.getX() &&
                a.getY() == b.getY();
    }

    /**
     * Get a clone of this point with a heading.
     *
     * @param a       the point to clone.
     * @param heading the heading the point should have.
     * @return a new heading point based on this one.
     */
    public static HeadingPoint withHeading(Point a,
                                           double heading) {
        return new HeadingPoint(
                a.getX(),
                a.getY(),
                heading
        );
    }

    /**
     * Get a clone of this instanced point with a new heading.
     *
     * @param heading the new heading to add.
     * @return a HeadingPoint based off this point.
     */
    public HeadingPoint withHeading(double heading) {
        return new HeadingPoint(
                this.getX(),
                this.getY(),
                heading
        );
    }

    /**
     * Creates a copied point.
     *
     * <p>
     * This is a DEEP, not a SHALLOW clone. Deep clones are entirely new
     * objects - no shared hash codes, etc. Shallow clones aren't new objects.
     * This is essentially the clone() method, but cooler.
     * </p>
     *
     * @return a cloned version of this point.
     */
    public Point copy() {
        return new Point(
                this.getX(),
                this.getY()
        );
    }
}
