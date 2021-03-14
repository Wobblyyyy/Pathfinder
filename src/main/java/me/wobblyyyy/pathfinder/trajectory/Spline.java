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

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Ahh... splines. Basically gay lines. I swear I'm not homophobic, I like men
 * too. Anyways. Straight lines are very linear and very boring. Splines, on
 * the other hand, are basically straight lines that aren't at all straight -
 * rather, they curve. Splines are incredibly useful in making complex movement
 * trajectories and profiles. Unlike regular lines, splines, as you know, have
 * an element of curvature. This element of curvature allows for your robot to
 * continually move without ever having to come to a stop or a hard direction
 * change. Splines themselves are representations of a concept, not fully
 * fledged implementations of a trajectory following algorithm.
 *
 * <p>
 * Splines, or at least these splines, make use of the Fritsch-Carlson method
 * for computing internal spline parameters. This method isn't incredibly
 * computationally expensive, but as a best practice, it's suggested that you
 * do as little runtime execution of spline initialization.
 * </p>
 *
 * <p>
 * Interpolation is handled by yet another rather complex algorithm that nobody
 * quite understands - including me, probably. Anyways, if you really are quite
 * sincerely interested in learning how spline interpolation works, you can
 * go check out the {@link SplineInterpolator} class - that has all the cool
 * and sexy math you might want to look at.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class Spline implements Segment {
    /**
     * The internal spline interpolator. All of the hard math behind splines
     * is handled in a separate class as to reduce code clutter and make the
     * {@code Spline} class a bit more readable, especially for those without
     * much experience in building splines.
     */
    private final SplineInterpolator interpolator;

    /**
     * A set of each of the points that the spline should hit. This is used
     * mostly for metadata about the spline.
     */
    private final StaticArray<HeadingPoint> points;

    /**
     * An array of all of the angles contained on the spline's path.
     * TODO implement angles
     */
    private final DynamicArray<Angle> angles;

    /**
     * The minimum X value of the spline.
     */
    private double xMinimum;

    /**
     * The minimum Y value of the spline.
     */
    private double yMinimum;

    /**
     * The maximum X value of the spline.
     */
    private double xMaximum;

    /**
     * The maximum Y value of the spline.
     */
    private double yMaximum;

    /**
     * Is the spline's internal interpolator inverted?
     */
    private final boolean isInverted;

    /**
     * Create a new {@code Spline} that will hit each of the required points.
     * Splines are created so that they hit each and every one of the target
     * points, meaning that if you tell the robot to pass through (10, 10),
     * the robot will pass through (10, 10) no matter what.
     *
     * @param points a container that contains each of the required points.
     *               Each and every one of these points will be passed through
     *               directly by the spline computational system, meaning that
     *               the robot will go exactly where you tell it to go.
     */
    public Spline(Arrayable<HeadingPoint> points) {
        this.points = new StaticArray<>(points);

        int size = points.size();

        DynamicArray<Double> xValues = new DynamicArray<>(size);
        DynamicArray<Double> yValues = new DynamicArray<>(size);
        angles = new DynamicArray<>(size);

        xMinimum = points.get(0).getX();
        yMaximum = points.get(0).getY();
        xMaximum = xMinimum;
        yMaximum = yMinimum;

        /*
         * For each of the values contained in the arrays of points, check
         * to see if those values are technically new minimums or maximums.
         *
         * If they're either of those, re-assign the min and max values.
         */
        points.itr().forEach(point -> {
            double x = point.getX();
            double y = point.getY();

            xValues.add(x);
            yValues.add(y);
            angles.add(point.getAngle());

            xMinimum = Math.min(xMinimum, x);
            yMinimum = Math.min(yMinimum, y);
            xMaximum = Math.max(xMaximum, x);
            yMaximum = Math.max(yMaximum, y);
        });

        ArrayList<Double> xList = fromDynamicArray(xValues);
        ArrayList<Double> yList = fromDynamicArray(yValues);

        /*
         * Inline conditional assignment, I'm sorry. Oh well.
         *
         * isInverted is set to INVERSE of onlyIncreasing (x values)
         *
         * if isInverted is false:
         *   create a new monotone cubic spline
         * if isInverted is true:
         *   create a new inverted monotone cubic spline
         */
        interpolator = !(isInverted = !onlyIncreasing(xList)) ?
                SplineInterpolator.monotoneCubic(xList, yList) :
                SplineInterpolator.invertedMonotoneCubic(xList, yList);
    }

    /**
     * Check to see if an {@code ArrayList} only has a sequence of increasing
     * values - used for spline inversion.
     *
     * @param values the values to check.
     * @return whether or not the values exclusively sequentially increase.
     */
    private static boolean onlyIncreasing(ArrayList<Double> values) {
        double lastValue = values.get(0);

        for (double value : values) {
            if (value != lastValue) {
                if (!(value > lastValue)) return false;
            }

            lastValue = value;
        }

        return true;
    }

    /**
     * Convert an {@code DynamicArray} into an {@code ArrayList}.
     *
     * @param array the {@code DynamicArray} to convert.
     * @return the {@code ArrayList} that's been created.
     */
    private ArrayList<Double> fromDynamicArray(DynamicArray<Double> array) {
        Double[] doubleArray = array.toDoubleArray();

        return new ArrayList<>(Arrays.asList(doubleArray));
    }

    /**
     * Get a point from the segment based on an X value. This point should fit
     * within the bounds of the segment and should be represented partially by
     * the provided value, which provides a basis for interpolation from there.
     *
     * @param xValue the value that the segment should interpolate from. This
     *               means, in essence, that the segment should determine the
     *               nearest possible point to the requested X value.
     * @return an interpolated point based on an inputted value. This point has
     * no regard for heading - rather, it's a simple and plain point that can
     * be used for following the trajectory or just figuring out where a
     * certain value on the trajectory lies.
     */
    @Override
    public Point interpolateFromX(double xValue) {
        return new Point(
                xValue,
                interpolator.interpolateFromX(!isInverted ?
                        xValue :
                        (Math.abs(xMaximum - xValue)) + xMaximum)
        );
    }

    /**
     * Get a point from the segment based on an Y value. This point should fit
     * within the bounds of the segment and should be represented partially by
     * the provided value, which provides a basis for interpolation from there.
     *
     * @param yValue the value that the segment should interpolate from. This
     *               means, in essence, that the segment should determine the
     *               nearest possible point to the requested Y value.
     * @return an interpolated point based on an inputted value. This point has
     * no regard for heading - rather, it's a simple and plain point that can
     * be used for following the trajectory or just figuring out where a
     * certain value on the trajectory lies.
     */
    @Override
    public Point interpolateFromY(double yValue) {
        return new Point(
                interpolator.interpolateFromY(!isInverted ?
                        yValue :
                        (Math.abs(yMaximum - yValue)) + yMaximum),
                yValue
        );
    }

    /**
     * Get the angle which the robot should be facing at the given trajectory.
     * This angle is handled separately from the interpolation methods to
     * increase the degree of flexibility the {@code Segment} interface will
     * provide both internally and externally.
     *
     * @param point the point that will serve as a basis for fetching the
     *              desired robot heading.
     * @return the most closely interpolated heading/angle at a given point.
     * This angle typically corresponds to how far along the segment the robot
     * has travelled, but it's up to the implementations of the segment class
     * to decide how exactly this is handled. Yay interfaces!
     */
    @Override
    public Angle angleAt(Point point) {
        return points.get(points.size() - 1).getAngle();
    }

    /**
     * Get a combination of the minimum X and Y values for the segment. This
     * point is used to reduce primitive clutter - instead of returning an
     * individual X and Y double, we can return a single point that represents
     * our requested information.
     *
     * @return a combination of the requested X and Y values. This is used
     * mostly in segment interpolation, as individual segments are predicated
     * on raw X and Y coordinates, not interpolatable coordinates.
     */
    @Override
    public Point minimum() {
        return new Point(
                xMinimum,
                yMinimum
        );
    }

    /**
     * Get a combination of the maximum X and Y values for the segment. This
     * point is used to reduce primitive clutter - instead of returning an
     * individual X and Y double, we can return a single point that represents
     * our requested information.
     *
     * @return a combination of the requested X and Y values. This is used
     * mostly in segment interpolation, as individual segments are predicated
     * on raw X and Y coordinates, not interpolatable coordinates.
     */
    @Override
    public Point maximum() {
        return new Point(
                xMaximum,
                yMaximum
        );
    }

    /**
     * Convert the spline to a string representation of the spline.
     *
     * @return the interpolator's representation of the spline in the very
     * readable String form.
     */
    @Override
    public String toString() {
        return interpolator.toString();
    }

    /**
     * Get the point where the segment starts. This is defined as the position
     * that represents 0 percent of the segment's completion.
     *
     * @return the point at which the segment begins.
     */
    @Override
    public Point start() {
        return points.get(0);
    }

    /**
     * Get the point where the segment ends. This is defined as the position
     * that represents 100 percent of the segment's completion.
     *
     * @return the point at which the segment ends.
     */
    @Override
    public Point end() {
        return points.get(points.size() - 1);
    }
}
