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
 * Circles, much like rectangles, and literally any other shape, are...
 * cool? Maybe?
 *
 * <p>
 * Circles are a lot more complex mathematically than their linear
 * counterparts. I'm writing a lot of code that doesn't really do much
 * right now and I'm not near any pencils and paper so I don't feel like
 * working out the math to figure out how to detect things like line-circle
 * collisions and even circle-circle collisions, but I'm sure I'll get
 * around to that somewhere in the near enough future.
 * </p>
 *
 * <p>
 * If you'll notice, this circle actually has two component circles contained
 * within it - one labelled "circle," and the other; "hitbox." My reasoning
 * behind doing this has nothing to do with liking to type out GigaArc (GigaArc
 * GigaArc GigaArc GigaArc GigaArc GigaArc) and more to do with the fact that
 * circles are a tad bit finicky and I have a feeling the hitbox might
 * come in handy for future collision detection (and hopefully avoidance).
 * </p>
 *
 * <p>
 * Note that circles are a lot less expensive than rectangles and they have
 * significantly fewer components. If you have a situation in which a circle
 * could serve as a substitute for a rectangle...
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Circle implements Shape {
    /**
     * The internal component for the circle.
     */
    private final GigaArc circle;

    /**
     * The internal component for the circle's hitbox.
     */
    private final GigaArc hitbox;

    /**
     * The center-point of the circle.
     */
    public final Point center;

    /**
     * The radius of the circle itself.
     */
    public final double radius;

    /**
     * The radius of the circle's hitbox.
     */
    public final double hitboxRadius;

    /**
     * Create a new circle.
     *
     * @param center       the center-point of the circle.
     * @param radius       the radius of the circle. The radius of the circle
     *                     isn't as important as the hitbox radius, which
     *                     you'll see in a moment.
     * @param hitboxRadius the radius of the circle's hitbox. Hitboxes, in the
     *                     context of circles, are what the map sees as a
     *                     collidable object. By default, the regular circle
     *                     radius doesn't do much for collisions. However, the
     *                     hitbox radius does. So cool!
     */
    public Circle(Point center,
                  double radius,
                  double hitboxRadius) {
        circle = new GigaArc(center, radius);
        hitbox = new GigaArc(center, hitboxRadius);

        this.center = center;
        this.radius = radius;
        this.hitboxRadius = hitboxRadius;
    }

    /**
     * See whether or not a point is inside of a shape.
     *
     * @param point the point to check. This could be, for example, one of the end points
     *              of a rectangle, a square, the mid-point of a circle, the mid-point
     *              (or even quarter-point (or even eighth-point)) of a line.
     * @return whether the point is in the circle.
     */
    @Override
    public boolean isPointInShape(Point point) {
        return hitbox.isPointInCircle(point);
    }

    /**
     * Check whether or not a line enters a shape.
     *
     * <p>
     * TODO implement this method!!!
     * </p>
     *
     * @param line the line to check.
     * @return whether or not the line enters the shape.
     */
    @Override
    public boolean isLineInShape(Line line) {
        return false;
    }

    /**
     * See whether or not a point is near (within the hitbox of) a
     * certain circle.
     *
     * @param point the point to test.
     * @return whether or not the point is within the circle's hitbox area.
     */
    public boolean isPointNearShape(Point point) {
        return hitbox.isPointInCircle(point);
    }

    /**
     * Get whether or not a given point is within a certain distance
     * of a circle.
     *
     * @param point     the point to check for.
     * @param tolerance how far away the point can be (max).
     * @return whether the point meets our qualifications and wins a grand prize.
     */
    public boolean isPointNearShape(Point point,
                                    double tolerance) {
        return circle.isPointNearCircle(point, tolerance) ||
                circle.isPointInCircle(point);
    }

    /**
     * A method to determine whether or not a line intersects with a
     * circle.
     *
     * <p>
     * Just to pre-write some ideas for future use, this could possibly be
     * attempted by doing the following, assuming math is out of the question.
     * <pre>
     * 1. Create another (larger) hitbox circle to determine if it's worth
     *    even looking for an intersection in the first place.
     * 2. Find the closest point on the line to the radius. If that point is
     *    less than or equal to the radius of the circle (not the hitbox),
     *    then that line must be touching.
     * 3. Check on the line to make sure the point in question is in fact
     *    on the line and not floating off in space. We'll have to see how
     *    this plays out, however - oh well.
     * </pre>
     * </p>
     *
     * @return whether or not a line intersects with a circle.
     */
    public boolean doesLineIntersectWithCircle(Line line) {
        Line workingLine = line;
        int checkLimit = 15;
        double tolerance = 0.1;
        while (checkLimit > 0) {
            Point a = workingLine.getA(),
                    b = workingLine.getB(),
                    midpoint = workingLine.getMidpoint();
            // If any of the points - whether it be the midpoint,
            // or either of the end points - are inside of the
            // circle, we automatically know that the line must
            // intersect with the circle at some point - otherwise,
            // how exactly would that point get there?
            if (isPointNearShape(a, tolerance) ||
                    isPointNearShape(b, tolerance) ||
                    isPointNearShape(midpoint, tolerance)) {
                return true;
            }
            // We have two possible conditions coming up.
            // Distance from A is the greatest.
            // Distance from B is the greatest.
            // Regardless of what it is, we're going to make another
            // line out of whichever is closer and the current line's
            // midpoint and set workingLine to that and then repeat the
            // process all over again. Exciting, right?!
            double distanceFromA = Distance.getDistance(a, circle.center);
            double distanceFromB = Distance.getDistance(b, circle.center);
            if (distanceFromA > distanceFromB) {
                // A is greater - we want to use B.
                workingLine = new Line(b, midpoint);
            } else {
                // B is greater - we want to use A.
                workingLine = new Line(a, midpoint);
            }
            checkLimit--;
        }
        return false;
    }

    /**
     * Get a count of the components in the circle.
     *
     * @return 4. Because 4.
     */
    @Override
    public int getComponents() {
        return 4;
    }
}
