/*
 * ======================================================================
 * || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 * ||                                                                  ||
 * || This file is part of the "Pathfinder" project, which is licensed ||
 * || and distributed under the GPU General Public License V3.         ||
 * ||                                                                  ||
 * || Pathfinder is available on GitHub:                               ||
 * || https://github.com/Wobblyyyy/Pathfinder                          ||
 * ||                                                                  ||
 * || Pathfinder's license is available:                               ||
 * || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 * ||                                                                  ||
 * || Re-distribution of this, or any other files, is allowed so long  ||
 * || as this same copyright notice is included and made evident.      ||
 * ||                                                                  ||
 * || Unless required by applicable law or agreed to in writing, any   ||
 * || software distributed under the license is distributed on an "AS  ||
 * || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 * || express or implied. See the license for specific language        ||
 * || governing permissions and limitations under the license.         ||
 * ||                                                                  ||
 * || Along with this file, you should have received a license file,   ||
 * || containing a copy of the GNU General Public License V3. If you   ||
 * || did not receive a copy of the license, you may find it online.   ||
 * ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.geometry.Spline;
import me.wobblyyyy.pathfinder.math.functional.PointRotation;
import me.wobblyyyy.pathfinder.geometry.Distance;

/**
 * A utility class, containing a wide variety of points, midpoints, splines,
 * trajectories, and sets of interpolated points. This class doesn't provide
 * much functionality on its own - rather, the fields and methods in this class
 * allow you to easily use all sorts of arcs in your own code.
 *
 * <p>
 * This class was originally created to contain various 90 degree arcs that can
 * be transformed and adjusted to whatever specification you want. These arcs
 * can be accessed in two ways - firstly, you can use the pre-interpolated paths,
 * named INTERPOLATED_QUAD_(1, 2, 3, or 4), or you can use the trajectories for
 * each of these arcs, manually interpolate them, and then transform them from
 * there. You should go check out some examples for how this class can be
 * utilized - it's a lot easier to explain like that.
 * </p>
 *
 * <p>
 * One of the core concepts at play here is quadrants. These quadrants are like
 * those on a cartesian coordinate plane.
 * <ul>
 * <li>Quadrant 1: positive x, positive y</li>
 * <li>Quadrant 2: negative x, positive y</li>
 * <li>Quadrant 3: negative x, negative y</li>
 * <li>Quadrant 4: positive x, negative y</li>
 * </ul>
 * </p>
 *
 * <p>
 * As mentioned above, it's a good idea to check out some of the documentation
 * to get a better picture of what's going on here.
 * </p>
 *
 * <p>
 * As an example, here's how a semi-circle with a radius of 10 units with a
 * center point of (0, 0) would be constructed.
 * <pre>
 * <code>
 * DynamicArray<HeadingPoint> allPoints = new DynamicArray<>();
 * double RADIUS_SEMICIRCLE = 10;
 * Point ORIGIN_SEMICIRCLE = new Point(0, 0);
 * DynamicArray<HeadingPoint> SEMICIRCLE_A = Arcs.ensureIncreasingOrder(
 *     Arcs.transformPoints(
 *         Arcs.INTERPOLATED_QUAD_2,
 *         RADIUS_SEMICIRCLE,
 *         ORIGIN_SEMICIRCLE
 *     )
 * );
 * DynamicArray<HeadingPoint> SEMICIRCLE_B = Arcs.ensureIncreasingOrder(
 *     Arcs.transformPoints(
 *         Arcs.INTERPOLATED_QUAD_1,
 *         RADIUS_SEMICIRCLE,
 *         ORIGIN_SEMICIRCLE
 *     )
 * );
 * SEMICIRCLE_A.itr().forEach(allPoints::add);
 * SEMICIRCLE_B.itr().forEach(allPoints::add);
 * </code>
 * </pre>
 * By the end of this example, the {@code allPoints} array will contain
 * points forming a semicircle, starting at (-10, 0) and eventually ending
 * up at (10, 0). Very cool, very epic, I know.
 * </p>
 *
 * <p>
 * As stated previously, and just to re-iterate, if you're at all confused
 * about how the methods and fields in this class operate, you should check
 * out the examples in the Pathfinder/example/ directory - those should
 * hopefully clarify what this class does.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
@SuppressWarnings("DuplicatedCode")
public class Arcs {
    /**
     * The "origin point" of any cartesian plane. This is used mostly
     * internally to provide a reference for midpoints and more.
     * This point has an X, Y, and heading values, all of zero.
     */
    public static final HeadingPoint ORIGIN = new HeadingPoint(0, 0, 0);

    /**
     * A point drawn straight upwards. This point has the following X and Y
     * values: (0, 1)
     */
    public static final HeadingPoint QUADRANTAL_UP =
            new HeadingPoint(+0, +1, +0);

    /**
     * A point drawn straight rightwards. This point has the following X and Y
     * values: (1, 0)
     */
    public static final HeadingPoint QUADRANTAL_RIGHT =
            new HeadingPoint(+1, +0, +0);

    /**
     * A point drawn straight downwards. This point has the following X and Y
     * values: (0, -1)
     */
    public static final HeadingPoint QUADRANTAL_DOWN =
            new HeadingPoint(+0, -1, +0);

    /**
     * A point drawn straight leftwards. This point has the following X and Y
     * values: (-1, 0)
     */
    public static final HeadingPoint QUADRANTAL_LEFT =
            new HeadingPoint(-1, +0, +0);

    /**
     * A point with the coordinates (sqrt2/2, sqrt2/2) in the specified
     * quadrant. This point is the middle point between two of the quadrantal
     * angles defined in the {@code Arc} class. This point is drawn at
     * a 45 degree angle with a length of 1.
     */
    public static final HeadingPoint MIDPOINT_QUAD_1 = Distance
            .inDirection(ORIGIN, 45, 1)
            .withHeading(0);

    /**
     * A point with the coordinates (sqrt2/2, sqrt2/2) in the specified
     * quadrant. This point is the middle point between two of the quadrantal
     * angles defined in the {@code Arc} class. This point is drawn at
     * a 135 degree angle with a length of 1.
     */
    public static final HeadingPoint MIDPOINT_QUAD_2 = Distance
            .inDirection(ORIGIN, 135, 1)
            .withHeading(0);

    /**
     * A point with the coordinates (sqrt2/2, sqrt2/2) in the specified
     * quadrant. This point is the middle point between two of the quadrantal
     * angles defined in the {@code Arc} class. This point is drawn at
     * a 225 degree angle with a length of 1.
     */
    public static final HeadingPoint MIDPOINT_QUAD_3 = Distance
            .inDirection(ORIGIN, 225, 1)
            .withHeading(0);

    /**
     * A point with the coordinates (sqrt2/2, sqrt2/2) in the specified
     * quadrant. This point is the middle point between two of the quadrantal
     * angles defined in the {@code Arc} class. This point is drawn at
     * a 315 degree angle with a length of 1.
     */
    public static final HeadingPoint MIDPOINT_QUAD_4 = Distance
            .inDirection(ORIGIN, 315, 1)
            .withHeading(0);

    /**
     * A spline formed between three points - two quadrantals and a midpoint
     * between those two quadrantals.
     *
     * <p>
     * In this case, those are as follows:
     * <ul>
     *     <li>Upwards quadrantal</li>
     *     <li>Quadrant 1 midpoint</li>
     *     <li>Rightwards quadrantal</li>
     * </ul>
     * </p>
     */
    public static final Spline SPLINE_QUAD_1 = new Spline(new StaticArray<>(
            QUADRANTAL_UP,
            MIDPOINT_QUAD_1,
            QUADRANTAL_RIGHT
    ));

    /**
     * A spline formed between three points - two quadrantals and a midpoint
     * between those two quadrantals.
     *
     * <p>
     * In this case, those are as follows:
     * <ul>
     *     <li>Leftwards quadrantal</li>
     *     <li>Quadrant 2 midpoint</li>
     *     <li>Upwards quadrantal</li>
     * </ul>
     * </p>
     */
    public static final Spline SPLINE_QUAD_2 = new Spline(new StaticArray<>(
            QUADRANTAL_LEFT,
            MIDPOINT_QUAD_2,
            QUADRANTAL_UP
    ));

    /**
     * A spline formed between three points - two quadrantals and a midpoint
     * between those two quadrantals.
     *
     * <p>
     * In this case, those are as follows:
     * <ul>
     *     <li>Leftwards quadrantal</li>
     *     <li>Quadrant 3 midpoint</li>
     *     <li>Downwards quadrantal</li>
     * </ul>
     * </p>
     */
    public static final Spline SPLINE_QUAD_3 = new Spline(new StaticArray<>(
            QUADRANTAL_LEFT,
            MIDPOINT_QUAD_3,
            QUADRANTAL_DOWN
    ));

    /**
     * A spline formed between three points - two quadrantals and a midpoint
     * between those two quadrantals.
     *
     * <p>
     * In this case, those are as follows:
     * <ul>
     *     <li>Downwards quadrantal</li>
     *     <li>Quadrant 4 midpoint</li>
     *     <li>Rightwards quadrantal</li>
     * </ul>
     * </p>
     */
    public static final Spline SPLINE_QUAD_4 = new Spline(new StaticArray<>(
            QUADRANTAL_DOWN,
            MIDPOINT_QUAD_4,
            QUADRANTAL_RIGHT
    ));

    /**
     * A single-segment trajectory composed of {@link #SPLINE_QUAD_1}
     */
    public static final Trajectory TRAJECTORY_QUAD_1 = new Trajectory(
            new StaticArray<>(SPLINE_QUAD_1));

    /**
     * A single-segment trajectory composed of {@link #SPLINE_QUAD_2}
     */
    public static final Trajectory TRAJECTORY_QUAD_2 = new Trajectory(
            new StaticArray<>(SPLINE_QUAD_2));

    /**
     * A single-segment trajectory composed of {@link #SPLINE_QUAD_3}
     */
    public static final Trajectory TRAJECTORY_QUAD_3 = new Trajectory(
            new StaticArray<>(SPLINE_QUAD_3));

    /**
     * A single-segment trajectory composed of {@link #SPLINE_QUAD_4}
     */
    public static final Trajectory TRAJECTORY_QUAD_4 = new Trajectory(
            new StaticArray<>(SPLINE_QUAD_4));

    /**
     * The amount of samples to use for each path. Samples are best defined
     * as how many points each curve will have.
     */
    public static final int SAMPLES = 10;

    /**
     * A set of interpolated points based on {@link #TRAJECTORY_QUAD_1}.
     * By default, there are 10 interpolation samples. If you'd like to
     * customize how many samples there are, check out the "see" tags.
     *
     * @see #interpolateQuad1(int)
     * @see #interpolateQuad2(int)
     * @see #interpolateQuad3(int)
     * @see #interpolateQuad4(int)
     */
    public static final DynamicArray<HeadingPoint> INTERPOLATED_QUAD_1 =
            PathGenerator.toPath(TRAJECTORY_QUAD_1, SAMPLES);

    /**
     * A set of interpolated points based on {@link #TRAJECTORY_QUAD_2}.
     * By default, there are 10 interpolation samples. If you'd like to
     * customize how many samples there are, check out the "see" tags.
     *
     * @see #interpolateQuad1(int)
     * @see #interpolateQuad2(int)
     * @see #interpolateQuad3(int)
     * @see #interpolateQuad4(int)
     */
    public static final DynamicArray<HeadingPoint> INTERPOLATED_QUAD_2 =
            PathGenerator.toPath(TRAJECTORY_QUAD_2, SAMPLES);

    /**
     * A set of interpolated points based on {@link #TRAJECTORY_QUAD_3}.
     * By default, there are 10 interpolation samples. If you'd like to
     * customize how many samples there are, check out the "see" tags.
     *
     * @see #interpolateQuad1(int)
     * @see #interpolateQuad2(int)
     * @see #interpolateQuad3(int)
     * @see #interpolateQuad4(int)
     */
    public static final DynamicArray<HeadingPoint> INTERPOLATED_QUAD_3 =
            PathGenerator.toPath(TRAJECTORY_QUAD_3, SAMPLES);

    /**
     * A set of interpolated points based on {@link #TRAJECTORY_QUAD_4}.
     * By default, there are 10 interpolation samples. If you'd like to
     * customize how many samples there are, check out the "see" tags.
     *
     * @see #interpolateQuad1(int)
     * @see #interpolateQuad2(int)
     * @see #interpolateQuad3(int)
     * @see #interpolateQuad4(int)
     */
    public static final DynamicArray<HeadingPoint> INTERPOLATED_QUAD_4 =
            PathGenerator.toPath(TRAJECTORY_QUAD_4, SAMPLES);

    /**
     * Private constructor - utility classes should not instantiatable.
     */
    private Arcs() {

    }

    /**
     * Get an interpolated set of points for the arc in a specified quadrant.
     *
     * @param samples how many times the point should be interpolated. The
     *                higher this number is, the more accurate the arc will
     *                be. However, having a large sample size may also decrease
     *                runtime performance.
     * @return a set of interpolated points from the specified quadrant. This
     * {@code DynamicArray} will have a size equal to the amount of samples
     * you input.
     */
    public static DynamicArray<HeadingPoint> interpolateQuad1(int samples) {
        return PathGenerator.toPath(TRAJECTORY_QUAD_1, samples);
    }

    /**
     * Get an interpolated set of points for the arc in a specified quadrant.
     *
     * @param samples how many times the point should be interpolated. The
     *                higher this number is, the more accurate the arc will
     *                be. However, having a large sample size may also decrease
     *                runtime performance.
     * @return a set of interpolated points from the specified quadrant. This
     * {@code DynamicArray} will have a size equal to the amount of samples
     * you input.
     */
    public static DynamicArray<HeadingPoint> interpolateQuad2(int samples) {
        return PathGenerator.toPath(TRAJECTORY_QUAD_2, samples);
    }

    /**
     * Get an interpolated set of points for the arc in a specified quadrant.
     *
     * @param samples how many times the point should be interpolated. The
     *                higher this number is, the more accurate the arc will
     *                be. However, having a large sample size may also decrease
     *                runtime performance.
     * @return a set of interpolated points from the specified quadrant. This
     * {@code DynamicArray} will have a size equal to the amount of samples
     * you input.
     */
    public static DynamicArray<HeadingPoint> interpolateQuad3(int samples) {
        return PathGenerator.toPath(TRAJECTORY_QUAD_3, samples);
    }

    /**
     * Get an interpolated set of points for the arc in a specified quadrant.
     *
     * @param samples how many times the point should be interpolated. The
     *                higher this number is, the more accurate the arc will
     *                be. However, having a large sample size may also decrease
     *                runtime performance.
     * @return a set of interpolated points from the specified quadrant. This
     * {@code DynamicArray} will have a size equal to the amount of samples
     * you input.
     */
    public static DynamicArray<HeadingPoint> interpolateQuad4(int samples) {
        return PathGenerator.toPath(TRAJECTORY_QUAD_4, samples);
    }

    /**
     * Ensure all of the provided points are in increasing X order.
     *
     * <p>
     * This method is most applicable when the robot should follow a
     * specified trajectory in a given order. If, for example, your
     * robot is at point (0, 0) and you want it to follow an arc that
     * will get it to point (10, 10), you could use this method on the
     * generated points to verify that the first point is the one with
     * the lowest X value and the last point is the one with the highest
     * X value.
     * </p>
     *
     * @param points the set of points you'd like to use.
     * @return the points in increasing X order.
     */
    public static DynamicArray<HeadingPoint> ensureIncreasingOrder(
            DynamicArray<HeadingPoint> points) {
        double fX = points.get(0).getX();
        double lX = points.get(points.size() - 1).getX();

        if (fX > lX) {
            // original is decreasing
            DynamicArray<HeadingPoint> newPoints = new DynamicArray<>();
            points.itr().forEach(point -> newPoints.add(0, point));
            return newPoints;
        } else {
            // original is increasing
            return points;
        }
    }

    /**
     * Ensure all of the provided points are in decreasing X order.
     *
     * <p>
     * This method is most applicable when the robot should follow a
     * specified trajectory in a given order. If, for example, your
     * robot is at point (10, 10) and you want it to follow an arc that
     * will get it to point (0, 0), you could use this method on the
     * generated points to verify that the first point is the one with
     * the highest X value and the last point is the one with the lowest
     * X value.
     * </p>
     *
     * @param points the set of points you'd like to use.
     * @return the points in decreasing X order.
     */
    public static DynamicArray<HeadingPoint> ensureDecreasingOrder(
            DynamicArray<HeadingPoint> points) {
        double fX = points.get(0).getX();
        double lX = points.get(points.size() - 1).getX();

        if (fX < lX) {
            // original is increasing
            DynamicArray<HeadingPoint> newPoints = new DynamicArray<>();
            points.itr().forEach(point -> newPoints.add(0, point));
            return newPoints;
        } else {
            // original is decreasing
            return points;
        }
    }

    /**
     * Scale a set of points.
     *
     * @param points the points to scale.
     * @param scale  the multiplier to multiply each of the points by
     * @return a scaled set of points.
     */
    public static DynamicArray<HeadingPoint> scalePoints(
            DynamicArray<HeadingPoint> points,
            double scale) {
        return new DynamicArray<>() {{
            points.itr().forEach(point ->
                    add(HeadingPoint.scale(point, scale)));
        }};
    }

    /**
     * Add a center point to each of the entries in the provided array.
     *
     * @param points the array of points that will be displaced.
     * @param center the desired center point.
     * @return displaced points.
     */
    public static DynamicArray<HeadingPoint> displacePoints(
            DynamicArray<HeadingPoint> points,
            Point center) {
        return new DynamicArray<>() {{
            points.itr().forEach(point ->
                    add(Point.add(point, center).withHeading(0)));
        }};
    }

    /**
     * Transform an inputted set of points based on the provided radius
     * and center point. The entire array of points is scaled up using the
     * radius as a multiplier. Then, the entire array gets the center point
     * added to it, thus making an arc around the center point.
     *
     * <p>
     * As an example, take the following.
     * <pre>
     * <code>
     * // We want a set of points that form a 90 degree arc.
     * // Center of (10, 10).
     * // Extends to (0, 10) and (10, 20).
     * // Visualized, it would look something like this...
     * // ________####
     * // ______###___
     * // _____#______
     * // ____##______
     * // ___#________
     * // __##________
     * // _##_________
     * // _#__________
     * // Obviously, that drawing is a bit off, but you get the point.
     *
     * // Get the points of an interpolated arc in quadrant 2
     * DynamicArray<HeadingPoint> originalPoints =
     *     Arcs.INTERPOLATED_QUAD_2;
     *
     * // Apply a transformation to these points
     * DynamicArray<HeadingPoint> transformedPoints = Arcs.transformPoints(
     *     originalPoints,   // the points to transform
     *     10,               // the radius of the arc
     *     new Point(10, 10) // the center of the arc
     * );
     * </code>
     * </pre>
     * </p>
     *
     * @param points the points to scale and move.
     * @param radius the radius of the arc.
     * @param center the center point of the arc.
     * @return a fresh set of transformed points.
     * @see #scalePoints(DynamicArray, double)
     * @see #displacePoints(DynamicArray, Point)
     */
    public static DynamicArray<HeadingPoint> transformPoints(
            DynamicArray<HeadingPoint> points,
            double radius,
            Point center) {
        DynamicArray<HeadingPoint> scaled = scalePoints(points, radius);

        return displacePoints(scaled, center);
    }

    /**
     * Apply the from operation and then apply the increasing operation.
     * This method performs two operations - first, it transforms the points
     * using the {@link #transformPoints(DynamicArray, double, Point) method.
     * After transforming the points, code to ensure that all of the points
     * are in a increasing X order is run.
     *
     * <p>
     * Both of the methods used in this operation are already documented.
     * If you're confused about what exactly this method does, or would just
     * like to learn more about how it works, you should check out the
     * methods linked in the "see" tags of this JavaDoc.
     * </p>
     *
     * <p>
     * This method specifically can be used to ensure that, on a cartesian
     * coordinate plane where positive X moves rightwards, that the points
     * in the trajectory/arc/whatever you inputted to this method are moving
     * in a rightwards direction. If, say, you'd like your robot to move
     * that way, this method can be used. Ultimately, much of this will
     * come down to testing, just to ensure that everything works well with
     * your robot specifically.
     * </p>
     *
     * <p>
     * An example of this method's use can be found in the main JavaDoc
     * for the arcs class: {@link Arcs}.
     * </p>
     *
     * @param points the points that should be transformed.
     * @param radius the radius of the arc.
     * @param center the center point of the arc.
     * @return a newly-transformed curve.
     * @see #ensureIncreasingOrder(DynamicArray)
     * @see #transformPoints(DynamicArray, double, Point)
     */
    public static DynamicArray<HeadingPoint> increasingFrom(
            DynamicArray<HeadingPoint> points,
            double radius,
            Point center) {
        return ensureIncreasingOrder(transformPoints(points, radius, center));
    }

    /**
     * Apply the from operation and then apply the decreasing operation.
     * This method performs two operations - first, it transforms the points
     * using the {@link #transformPoints(DynamicArray, double, Point) method.
     * After transforming the points, code to ensure that all of the points
     * are in a decreasing X order is run.
     *
     * <p>
     * Both of the methods used in this operation are already documented.
     * If you're confused about what exactly this method does, or would just
     * like to learn more about how it works, you should check out the
     * methods linked in the "see" tags of this JavaDoc.
     * </p>
     *
     * <p>
     * This method specifically can be used to ensure that, on a cartesian
     * coordinate plane where positive X moves leftwards, that the points
     * in the trajectory/arc/whatever you inputted to this method are moving
     * in a leftwards direction. If, say, you'd like your robot to move
     * that way, this method can be used. Ultimately, much of this will
     * come down to testing, just to ensure that everything works well with
     * your robot specifically.
     * </p>
     *
     * <p>
     * An example of this method's use can be found in the main JavaDoc
     * for the arcs class: {@link Arcs}.
     * </p>
     *
     * @param points the points that should be transformed.
     * @param radius the radius of the arc.
     * @param center the center point of the arc.
     * @return a newly-transformed curve.
     * @see #ensureDecreasingOrder(DynamicArray)
     * @see #transformPoints(DynamicArray, double, Point)
     */
    public static DynamicArray<HeadingPoint> decreasingFrom(
            DynamicArray<HeadingPoint> points,
            double radius,
            Point center) {
        return ensureDecreasingOrder(transformPoints(points, radius, center));
    }

    /**
     * Rotate a set of points by a specified amount of degrees.
     *
     * <p>
     * Rotating points is about as simple as it sounds - the points you input
     * will be rotated by the degrees you input. As an example:
     * <ul>
     * <li>(0, 1) is rotated by 90 degrees, becomes (-1, 0).</li>
     * <li>(1, 0) is rotated by 180 degrees, becomes (-1, 0).</li>
     * <li>(1, 0) is rotated by 330 degrees, becomes (sqrt3/2, 0.5)</li>
     * </ul>
     * </p>
     *
     * <p>
     * The points that are rotated in this method are rotated through the
     * {@link PointRotation#rotatePointsWithHeading(DynamicArray, double)}
     * method, which accepts a set of points and a measurement, in degrees,
     * to rotate these points by. This measurement is deltaDegrees, not
     * absolute degrees - if you have a point at a 90 degree angle and rotate
     * it by 90 degrees, that point will then be at a 180 degree angle.
     * </p>
     *
     * @param points          the points to rotate.
     * @param degreesToRotate the degrees to rotate the points by.
     * @return the rotated points.
     */
    public static DynamicArray<HeadingPoint> rotatePoints(
            DynamicArray<HeadingPoint> points,
            double degreesToRotate) {
        return PointRotation.rotatePointsWithHeading(points, degreesToRotate);
    }
}
