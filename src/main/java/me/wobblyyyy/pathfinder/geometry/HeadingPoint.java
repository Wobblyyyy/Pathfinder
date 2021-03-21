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
 * An extension of the default Point class - this time with z.
 *
 * <p>
 * For the sake of consistency, every single angle measure in this entire
 * project should be written in degrees. I don't care how you do whatever
 * you do - that's up to you - but all of the angles which I'm using are
 * all written, notated, and stored in degrees.
 * </p>
 *
 * <p>
 * Due to the nature of inheritance in Java, HeadingPoint can be used as a
 * substitute for just about any situation. If you'd prefer to get an entirely
 * separate point without the z, you can use the static getPoint() method
 * included in this class.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings("unused")
public class HeadingPoint extends Point {
    /**
     * The point's z.
     */
    private final double z;

    /**
     * The point's {@link Angle}.
     */
    private final transient Angle angle;

    /**
     * Create a new z point.
     *
     * <p>
     * Because this class extends point, you're still left with a plain
     * old point if you'd like. Actually, I'd suggest that you only use these
     * special z points when absolutely needed - representing the
     * position of a robot, for example.
     * </p>
     *
     * <p>
     * Sticking simply with points makes code significantly easier to generify
     * and thus debug. Ultimately, I can't really do much - and I don't really
     * care - what you do, I'm just giving you a suggestion.
     * </p>
     *
     * @param x the point's X value.
     * @param y the point's Y value.
     * @param z the point's z. All headings, at least all of the
     *          headings that I've written, are notated in DEGREES. It
     *          would be a FANTASTIC idea to do the same, so you don't
     *          end up forgetting and messing something up. Just a
     *          suggestion or something.
     */
    public HeadingPoint(double x,
                        double y,
                        double z) {
        /*
         * Super, of course, because we're clean like that.
         */
        super(x, y);

        /*
         * Assign the z value so we're all good.
         */
        this.z = z;

        /*
         * Assign the angle based on the z.
         */
        this.angle = Angle.fromDegrees(z);
    }

    /**
     * Create a new {@code HeadingPoint}.
     *
     * @param x     the point's X value.
     * @param y     the point's Y value.
     * @param angle the point's angle.
     */
    public HeadingPoint(double x,
                        double y,
                        Angle angle) {
        this(x, y, angle.getDegrees());
    }

    /**
     * Create a new HeadingPoint based on a point. Note that this constructor
     * will set the z point's z to 0.
     *
     * @param point the point to base the new z point on.
     */
    public HeadingPoint(Point point) {
        this(point.getX(), point.getY(), 0);
    }

    /**
     * Create a new HeadingPoint based on a point.
     *
     * @param point the point that the z point should be based on.
     * @param z     the point's z. All headings, at least all of the
     *              headings that I've written, are notated in DEGREES. It
     *              would be a FANTASTIC idea to do the same, so you don't
     *              end up forgetting and messing something up. Just a
     *              suggestion or something.
     */
    public HeadingPoint(Point point,
                        double z) {
        this(point.getX(), point.getY(), z);
    }

    /**
     * Create a new HeadingPoint based on a point.
     *
     * @param point the point that the z point should be based on.
     * @param angle the point's z. All headings, at least all of the
     *              headings that I've written, are notated in DEGREES. It
     *              would be a FANTASTIC idea to do the same, so you don't
     *              end up forgetting and messing something up. Just a
     *              suggestion or something.
     */
    public HeadingPoint(Point point,
                        Angle angle) {
        this(point.getX(), point.getY(), angle.getDegrees());
    }

    /**
     * Create a new {@code HeadingPoint} based off the current point and a
     * desired translation.
     *
     * @param deltaX     the difference in X value.
     * @param deltaY     the difference in Y value.
     * @param deltaTheta the difference in angle. NOT the current angle, not
     *                   the new angle - the DIFFERENCE in angle.
     * @return a newly created {@code HeadingPoint} based off the calling point
     * and the given delta values.
     */
    public HeadingPoint transform(double deltaX,
                                  double deltaY,
                                  Angle deltaTheta) {
        return new HeadingPoint(
                this.getX() + deltaX,
                this.getY() + deltaY,
                this.getAngle().plus(deltaTheta)
        );
    }

    /**
     * Get the z of the point.
     *
     * @return the point's z.
     * @see #getAngle()
     */
    public double getHeading() {
        return z;
    }

    /**
     * Get the point's angle.
     *
     * @return the point's angle.
     * @see #getHeading()
     */
    public Angle getAngle() {
        return angle;
    }

    /**
     * Get a new Point, without the z.
     *
     * @return a new Point, without the z.
     */
    public Point getPoint() {
        return new Point(
                getX(),
                getY()
        );
    }

    /**
     * Get a HeadingPoint with a new z.
     *
     * @param original   the original point.
     * @param newHeading the point's new z.
     * @return a new HeadingPoint with an updated z.
     */
    public static HeadingPoint withNewHeading(Point original,
                                              double newHeading) {
        return new HeadingPoint(
                original.getX(),
                original.getY(),
                newHeading
        );
    }

    /**
     * Blend (average) two points together to form another point.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the result blended point.
     */
    public static HeadingPoint blend(HeadingPoint a,
                                     HeadingPoint b) {
        return new HeadingPoint(
                Math.average(a.getX(), b.getX()),
                Math.average(a.getY(), b.getY()),
                Math.average(a.getHeading(), b.getHeading())
        );
    }

    /**
     * Add two points together to form another point.
     *
     * @param a one of the two points.
     * @param b one of the two points.
     * @return the sum of the two points.
     */
    public static HeadingPoint add(HeadingPoint a,
                                   HeadingPoint b) {
        return new HeadingPoint(
                a.getX() + b.getX(),
                a.getY() + b.getY(),
                a.getHeading() + b.getHeading()
        );
    }

    /**
     * Subtract the value of point {@code B} from point {@code A} and get the
     * difference between the two points.
     *
     * @param a the first of the two points - this is the point that will
     *          be subtracted from.
     * @param b the second of the two points - this is the point that will
     *          get subtracted.
     * @return the difference between the two points.
     */
    public static HeadingPoint subtract(HeadingPoint a,
                                        HeadingPoint b) {
        return add(a, scale(b, -1));
    }

    /**
     * Get the angle from point A to point B. This method inversely subtracts
     * the two points and uses the {@link Math#atan2(double, double)} method
     * to determine the angle between them.
     *
     * <p>
     * The angle between two points is defined as follows:
     * <code>
     * atan2(y2 - y1, x2 - x1)
     * </code>
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the angle between the two points, notated in radians.
     */
    public static double angleOfRad(HeadingPoint a,
                                    HeadingPoint b) {
        double angleInRadians = Math.atan2(
                b.getY() - a.getY(),
                b.getX() - a.getX()
        );

        return AngleUtils.fixRad(angleInRadians);
    }

    /**
     * Get the angle from point A to point B. This method inversely subtracts
     * the two points and uses the {@link Math#atan2(double, double)} method
     * to determine the angle between them.
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return the angle between the two points, notated in radians.
     */
    public static double angleOfDeg(HeadingPoint a,
                                    HeadingPoint b) {
        return Math.toDegrees(angleOfRad(a, b));
    }

    /**
     * Scale a point (z included) by a certain factor.
     *
     * <p>
     * Other lovely scale methods include...
     * <ul>
     *     <li>
     *         Scale only X: {@link #scaleX(HeadingPoint, double)}
     *     </li>
     *     <li>
     *         Scale only Y: {@link #scaleY(HeadingPoint, double)}
     *     </li>
     *     <li>
     *         Scale both: {@link #scale(HeadingPoint, double, double)}
     *     </li>
     *     <li>
     *         Scale both by the same factor: {@link #scale(HeadingPoint, double)}
     *     </li>
     * </ul>
     * </p>
     *
     * @param a                 the point to scale.
     * @param xMultiplier       the multiplier for the point's X value.
     * @param yMultiplier       the multiplier for the point's Y value.
     * @param headingMultiplier the multiplier for the point's z.
     * @return a newly-scaled point.
     */
    public static HeadingPoint scale(HeadingPoint a,
                                     double xMultiplier,
                                     double yMultiplier,
                                     double headingMultiplier) {
        return new HeadingPoint(
                a.getX() * xMultiplier,
                a.getY() * yMultiplier,
                a.getHeading() * headingMultiplier
        );
    }

    /**
     * Scale a point upwards or downwards by a specific factor.
     *
     * <p>
     * This method does not independently scale the X and Y values - to do
     * that, you can take a look at any of these methods:
     * <ul>
     *     <li>Scale only X: {@link #scaleX(HeadingPoint, double)}</li>
     *     <li>Scale only Y: {@link #scaleY(HeadingPoint, double)}</li>
     *     <li>Scale both: {@link #scale(HeadingPoint, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the factor to scale the point by.
     * @return a new, scaled, point.
     */
    public static HeadingPoint scale(HeadingPoint a,
                                     double multiplier) {
        return new HeadingPoint(
                a.getX() * multiplier,
                a.getY() * multiplier,
                a.getHeading()
        );
    }

    /**
     * Scale the X and Y values of a point separately.
     *
     * <p>
     * If you're interested in scaling these axes uniformly, you can use the
     * simpler {@link #scale(HeadingPoint, double)} method.
     * </p>
     *
     * @param a           the point to be scaled.
     * @param xMultiplier the value by which the X value is multiplied.
     * @param yMultiplier the value by which the Y value is multiplier.
     * @return a newly-scaled value.
     */
    public static HeadingPoint scale(HeadingPoint a,
                                     double xMultiplier,
                                     double yMultiplier) {
        return new HeadingPoint(
                a.getX() * xMultiplier,
                a.getY() * yMultiplier,
                a.getHeading()
        );
    }

    /**
     * Scale only the X value of a point.
     *
     * <p>
     * If you'd like to scale both axes at the same time, you can check out
     * either of these two methods.
     * <ul>
     *     <li>X and Y together: {@link #scale(HeadingPoint, double)}</li>
     *     <li>X and Y separate: {@link #scale(HeadingPoint, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the value by which the point should be scaled by.
     * @return a newly-scaled point.
     */
    public static HeadingPoint scaleX(HeadingPoint a,
                                      double multiplier) {
        return new HeadingPoint(
                a.getX() * multiplier,
                a.getY(),
                a.getHeading()
        );
    }

    /**
     * Scale only the Y value of a point.
     *
     * <p>
     * If you'd like to scale both axes at the same time, you can check out
     * either of these two methods.
     * <ul>
     *     <li>X and Y together: {@link #scale(HeadingPoint, double)}</li>
     *     <li>X and Y separate: {@link #scale(HeadingPoint, double, double)}</li>
     * </ul>
     * </p>
     *
     * @param a          the point to be scaled.
     * @param multiplier the value by which the point should be scaled by.
     * @return a newly-scaled point.
     */
    public static HeadingPoint scaleY(HeadingPoint a,
                                      double multiplier) {
        return new HeadingPoint(
                a.getX(),
                a.getY() * multiplier,
                a.getHeading()
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
    public static HeadingPoint average(HeadingPoint... points) {
        /*
         * The cumulative point - the point to be averaged.
         */
        HeadingPoint t = new HeadingPoint(0, 0, 0);

        /*
         * Add to the main point.
         */
        for (HeadingPoint p : points) {
            t = add(t, p);
        }

        /*
         * Scale the point, emulating averaging functionality.
         */
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
        /*
         * The current string format looks a bit like this...
         * {(0, 0) @ 0}
         * Meaning (x, y) at (@) z.
         */
        return "{(" + getX() + ", " + getY() + ") @ " + getHeading() + "}";
    }

    /**
     * Are two z points exactly identical?
     *
     * <p>
     * Points are considered to be identical or non-identical based solely on
     * the point's X, Y, and z values. If they're all the same, the
     * points are identical. If they're not, they're not identical.
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return whether or not the points are identical.
     */
    public static boolean isSame(HeadingPoint a,
                                 HeadingPoint b) {
        /*
         * Check to ensure that the points are exactly identical.
         *
         * Because the sub-values of a z point are all in double form,
         * simple, this is as simple as running three comparisons.
         */
        return a.getX() == b.getX() &&
                a.getY() == b.getY() &&
                a.getHeading() == b.getHeading();
    }
}
