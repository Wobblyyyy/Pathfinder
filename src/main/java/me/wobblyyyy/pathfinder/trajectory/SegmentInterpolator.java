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

import me.wobblyyyy.intra.ftc2.utils.math.Range;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Instantiable class used in interpreting a segment based on percentage X
 * or percentage Y values. Segments themselves are very lovely, but are based
 * on raw coordinate values, which can be rather challenging to work with when
 * attempting to follow along multiple segments. To counteract this issue, we
 * can use interpolators for each segment that allow us to use a percent of
 * completion value to determine the next target point, allowing us to smoothly
 * follow a lovely segment.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class SegmentInterpolator {
    /**
     * The segment which will be interpolated.
     */
    private final Segment segment;

    /**
     * The minimum X value contained in the segment.
     */
    private final double minX;

    /**
     * The minimum Y value contained in the segment.
     */
    private final double minY;

    /**
     * The size of the range between the minimum and maximum X values.
     */
    private final double sizeX;

    /**
     * The size of the range between the minimum and maximum Y values.
     */
    private final double sizeY;

    /**
     * A range of X values.
     */
    private final Range xRange;

    /**
     * A range of Y values.
     */
    private final Range yRange;

    /**
     * Create a new {@code SegmentInterpolator} that will interpolate the
     * provided segment to the best of its ability.
     *
     * @param segment the segment that should be interpolated.
     */
    public SegmentInterpolator(Segment segment) {
        this.segment = segment;

        minX = segment.minimum().getX();
        minY = segment.minimum().getY();
        double maxX = segment.maximum().getX();
        double maxY = segment.maximum().getY();

        sizeX = maxX - minX;
        sizeY = maxY - minY;

        xRange = new Range(minX, maxX);
        yRange = new Range(minY, maxY);
    }

    /**
     * Check to see if the provided value fits within the range of the minimum
     * to the maximum X value.
     *
     * @param x the X value to check.
     * @return whether or not the provided X value is within the range of the
     * minimum and maximum values. This value is "valid" if it fits within the
     * range and "invalid" if it does not.
     */
    public boolean validX(double x) {
        return xRange.inRange(x);
    }

    /**
     * Check to see if the provided value fits within the range of the minimum
     * to the maximum Y value.
     *
     * @param y the Y value to check.
     * @return whether or not the provided Y value is within the range of the
     * minimum and maximum values. This value is "valid" if it fits within the
     * range and "invalid" if it does not.
     */
    public boolean validY(double y) {
        return yRange.inRange(y);
    }

    /**
     * Check to see if the provided point is valid. Point validity is based
     * on whether or not the respective X and Y values are "valid", which is
     * determined using the {@link #validX(double)} and {@link #validY(double)}
     * methods.
     *
     * @param point the point that will be checked for validity.
     * @return whether or not the given point is "valid," meaning each the X
     * and Y values are in-range.
     */
    public boolean valid(Point point) {
        return validX(point.getX()) && validY(point.getY());
    }

    /**
     * Determine what percentage of completion a specific X value is associated
     * with on the segment.
     *
     * @param x the X value to get the percentage of completion from.
     * @return the percentage of completion based on the specified X value.
     * If the requested X value is not contained in the segment, this method
     * will return -1, rather than a percentage of completion. If the requested
     * value IS contained on the segment, however, this method will return a
     * number from 0 to 1 indicating the percentage that specified value
     * corresponds to on the segment.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public double percentX(double x) {
        if (!validX(x)) return -1;

        double withoutMin = x - minX;
        double percentOf = withoutMin / sizeX;

        return percentOf;
    }

    /**
     * Determine what percentage of completion a specific Y value is associated
     * with on the segment.
     *
     * @param y the Y value to get the percentage of completion from.
     * @return the percentage of completion based on the specified Y value.
     * If the requested Y value is not contained in the segment, this method
     * will return -1, rather than a percentage of completion. If the requested
     * value IS contained on the segment, however, this method will return a
     * number from 0 to 1 indicating the percentage that specified value
     * corresponds to on the segment.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public double percentY(double y) {
        if (!validY(y)) return -1;

        double withoutMin = y - minY;
        double percentOf = withoutMin / sizeY;

        return percentOf;
    }

    /**
     * Get an X-Y point based a percentage of completion of the total X axis
     * range. This percentage should always fit within the range of 0 to 1.
     *
     * <p>
     * This method works by multiplying the inputted percentage value by the
     * total size of the requested axis and then adding the minimum value of
     * that axis to the product of the percentage and the total size. Such,
     * this percentage will always return a real solution, assuming it fits
     * within the defined range of 0 to 1. Anything outside of that range will
     * be interpreted by the segment handler.
     * </p>
     *
     * @param percentX the percentage of completion to get a point from. This
     *                 percentage should always fit within the range of 0 to 1,
     *                 can not be negative, and should be greater than 0 in
     *                 nearly all cases.
     * @return a point at the given percentage of completion along the
     * requested axis. The return value of this method is left up to the
     * segment's implementation of the {@link Segment#interpolateFromX(double)}
     * method.
     */
    public Point atPercentX(double percentX) {
        double realX = (percentX * sizeX) + minX;

        return segment.interpolateFromX(realX);
    }

    /**
     * Get an X-Y point based a percentage of completion of the total Y axis
     * range. This percentage should always fit within the range of 0 to 1.
     *
     * <p>
     * This method works by multiplying the inputted percentage value by the
     * total size of the requested axis and then adding the minimum value of
     * that axis to the product of the percentage and the total size. Such,
     * this percentage will always return a real solution, assuming it fits
     * within the defined range of 0 to 1. Anything outside of that range will
     * be interpreted by the segment handler.
     * </p>
     *
     * @param percentY the percentage of completion to get a point from. This
     *                 percentage should always fit within the range of 0 to 1,
     *                 can not be negative, and should be greater than 0 in
     *                 nearly all cases.
     * @return a point at the given percentage of completion along the
     * requested axis. The return value of this method is left up to the
     * segment's implementation of the {@link Segment#interpolateFromY(double)}
     * method.
     */
    public Point atPercentY(double percentY) {
        double realY = (percentY * sizeY) + minY;

        return segment.interpolateFromY(realY);
    }

    /**
     * Get a point based on a completion percentage, 0 to 1.
     *
     * @param percent the percentage to check for.
     * @return the point at the completion percentage.
     * @deprecated This method's return values can be inaccurate for certain
     * types of segments, specifically monotone cubic splines. Use either the
     * {@link #atPercentX(double)} or {@link #atPercentY(double)} methods
     * instead of this one.
     */
    @Deprecated(forRemoval = true)
    public Point atPercent(double percent) {
        Point basedOnX = atPercentX(percent);
        Point basedOnY = atPercentY(percent);

        return new Point(
                ((basedOnX.getX() + basedOnY.getX()) / 2),
                ((basedOnX.getY() + basedOnY.getY()) / 2)
        );
    }
}
