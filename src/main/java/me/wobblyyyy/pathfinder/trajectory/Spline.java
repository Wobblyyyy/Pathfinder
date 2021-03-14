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

public class Spline implements Segment {
    private final SplineInterpolator interpolator;
    private final StaticArray<HeadingPoint> points;
    private final DynamicArray<Angle> angles;

    private double xMinimum;
    private double yMinimum;
    private double xMaximum;
    private double yMaximum;

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

        interpolator = SplineInterpolator.monotoneCubic(
                xList,
                yList
        );
    }

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
                interpolator.interpolateFromX(xValue)
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
                interpolator.interpolateFromY(yValue),
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

    @Override
    public String toString() {
        return interpolator.toString();
    }
}
