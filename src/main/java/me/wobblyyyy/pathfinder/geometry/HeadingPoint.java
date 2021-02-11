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

package me.wobblyyyy.pathfinder.geometry;

import me.wobblyyyy.intra.ftc2.utils.math.Math;

/**
 * An extension of the default Point class - this time with heading.
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
 * separate point without the heading, you can use the static getPoint() method
 * included in this class.
 * </p>
 *
 * @author Colin Robertson
 */
public class HeadingPoint extends Point {
    /**
     * The point's heading.
     */
    private final double heading;

    /**
     * Create a new heading point.
     *
     * <p>
     * Because this class extends point, you're still left with a plain
     * old point if you'd like. Actually, I'd suggest that you only use these
     * special heading points when absolutely needed - representing the
     * position of a robot, for example.
     * </p>
     *
     * <p>
     * Sticking simply with points makes code significantly easier to generify
     * and thus debug. Ultimately, I can't really do much - and I don't really
     * care - what you do, I'm just giving you a suggestion.
     * </p>
     *
     * @param x       the point's X value.
     * @param y       the point's Y value.
     * @param heading the point's heading. All headings, at least all of the
     *                headings that I've written, are notated in DEGREES. It
     *                would be a FANTASTIC idea to do the same, so you don't
     *                end up forgetting and messing something up. Just a
     *                suggestion or something.
     */
    public HeadingPoint(double x,
                        double y,
                        double heading) {
        super(x, y);

        this.heading = heading;
    }

    /**
     * Get the heading of the point.
     *
     * @return the point's heading.
     */
    public double getHeading() {
        return heading;
    }

    /**
     * Get a new Point, without the heading.
     *
     * @return a new Point, without the heading.
     */
    public Point getPoint() {
        return new Point(
                getX(),
                getY()
        );
    }

    /**
     * Get a HeadingPoint with a new heading.
     *
     * @param original   the original point.
     * @param newHeading the point's new heading.
     * @return a new HeadingPoint with an updated heading.
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
     * Scale a point (heading included) by a certain factor.
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
     * @param headingMultiplier the multiplier for the point's heading.
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
        HeadingPoint t = new HeadingPoint(0, 0, 0);

        for (HeadingPoint p : points) {
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
        return "{(" + getX() + ", " + getY() + ") @ " + getHeading() + "}";
    }

    /**
     * Are two heading points exactly identical?
     *
     * <p>
     * Points are considered to be identical or non-identical based solely on
     * the point's X, Y, and heading values. If they're all the same, the
     * points are identical. If they're not, they're not identical.
     * </p>
     *
     * @param a the first of the two points.
     * @param b the second of the two points.
     * @return whether or not the points are identical.
     */
    public static boolean isSame(HeadingPoint a,
                                 HeadingPoint b) {
        return a.getX() == b.getX() &&
                a.getY() == b.getY() &&
                a.getHeading() == b.getHeading();
    }
}
