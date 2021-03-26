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

package me.wobblyyyy.pathfinder.time;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Spline;
import me.wobblyyyy.pathfinder.math.SplineInterpolator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

/**
 * A specialization of an interpolatable spline designed specifically for
 * time-based double measurements - ex. the angle a swerve module should be
 * facing over a given period of time, how fast the robot should be moving
 * over a given period of time, etc.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class TimeSpline {
    private final TimeUnit timeUnit;
    private final List<Double> timestamps;
    private final List<Double> values;
    private SplineInterpolator interpolator;
    private RelativeTime relativeTime;

    /**
     * Create a new {@code TimeSpline}. By default, {@code TimeSpline}s do not
     * start tracking relative time on construction. In order to track relative
     * time, the {@link #start()} method must be called, or a boolean flag
     * should be set in the four-parameter constructor.
     *
     * @param timestamps          an array of time measurements, measured in
     *                            the time unit you provided.
     * @param values              an array of values that correspond to the
     *                            inputted time measurements. This array should
     *                            be the same size as the "values" array and
     *                            will be interpolated later.
     */
    public TimeSpline(double[] timestamps,
                      double[] values) {
        this(
                TimeUnit.MILLISECOND,
                timestamps,
                values
        );
    }

    /**
     * Create a new {@code TimeSpline}. By default, {@code TimeSpline}s do not
     * start tracking relative time on construction. In order to track relative
     * time, the {@link #start()} method must be called, or a boolean flag
     * should be set in the four-parameter constructor.
     *
     * @param timeUnit            the unit of time that the spline will be
     *                            based on. In almost all cases, it's a good
     *                            idea to use milliseconds as your time
     *                            unit, but it's entirely up to you.
     * @param timestamps          an array of time measurements, measured in
     *                            the time unit you provided.
     * @param values              an array of values that correspond to the
     *                            inputted time measurements. This array should
     *                            be the same size as the "values" array and
     *                            will be interpolated later.
     */
    public TimeSpline(TimeUnit timeUnit,
                      double[] timestamps,
                      double[] values) {
        this(
                timeUnit,
                timestamps,
                values,
                false
        );
    }

    /**
     * Create a new {@code TimeSpline}. By default, {@code TimeSpline}s do not
     * start tracking relative time on construction. In order to track relative
     * time, the {@link #start()} method must be called, or a boolean flag
     * should be set in the four-parameter constructor.
     *
     * @param timeUnit            the unit of time that the spline will be
     *                            based on. In almost all cases, it's a good
     *                            idea to use milliseconds as your time
     *                            unit, but it's entirely up to you.
     * @param timestamps          an array of time measurements, measured in
     *                            the time unit you provided.
     * @param values              an array of values that correspond to the
     *                            inputted time measurements. This array should
     *                            be the same size as the "values" array and
     *                            will be interpolated later.
     * @param startOnConstruction should the {@code TimeSpline} immediately
     *                            start tracking relative time on construction?
     *                            If true, it will. If false, you'll have to
     *                            call the {@link #start()} method before
     *                            relative time is tracked.
     */
    public TimeSpline(TimeUnit timeUnit,
                      double[] timestamps,
                      double[] values,
                      boolean startOnConstruction) {
        this.timeUnit = timeUnit;

        interpolator = SplineInterpolator.monotoneCubic(
                listPrimitives(timestamps),
                listPrimitives(values)
        );

        if (startOnConstruction) start();

        add(timestamps, values);
    }

    {
        timestamps = new ArrayList<>();
        values = new ArrayList<>();
    }

    private List<Double> listPrimitives(double[] primitiveArray) {
        return Arrays.asList(boxPrimitives(primitiveArray));
    }

    private Double[] boxPrimitives(double[] primitiveArray) {
        return DoubleStream.of(primitiveArray).boxed().toArray(Double[]::new);
    }

    /**
     * Interpolate a value from the {@code TimeSpline} based on the inputted
     * time measurement. This measurement should be in the same unit as the
     * unit inputted into the constructor during this instance of
     * {@code TimeSpline}'s instantiation.
     *
     * <p>
     * It's typically preferable to use the {@link #interpolate()} method
     * rather than this one, as it will automatically take care of timing for
     * you - you just need to worry about the values then.
     * </p>
     *
     * @param time a time unit to interpolate a value from.
     * @return an interpolated value from the specified time unit.
     */
    public double interpolate(double time) {
        return interpolator.interpolateFromX(time);
    }

    /**
     * Interpolate a value from the {@code TimeSpline}'s values based on the
     * relative time since the {@link #start()} method was called. If the
     * start method has not been called, this method will not function
     * correctly. The start method is called during construction with the
     * {@link #TimeSpline(TimeUnit, double[], double[], boolean)} constructor
     * if the boolean value is true.
     *
     * @return an interpolated value from the {@code TimeSpline}.
     */
    public double interpolate() {
        return interpolate(relativeTime.relativeTime());
    }

    /**
     * Add a singular timestamp and value pair to the TimeSpline. Please note
     * that this requires the entire spline to be re-computed, which can be
     * incredibly expensive. You should avoid calling this method whenever
     * possible - values should be inserted into the spline on construction.
     *
     * @param timestamp the timestamp of the pair.
     * @param value     the value of the pair.
     */
    public void add(double timestamp, double value) {
        add(
                new double[]{timestamp},
                new double[]{value}
        );
    }

    /**
     * Add several timestamp and value pairs to the TimeSpline. Please note
     * that this requires the entire spline to be re-computed, which can be
     * incredibly expensive. You should avoid calling this method whenever
     * possible - values should be inserted into the spline on construction.
     *
     * @param timestamps the timestamp array of the pairs.
     * @param values     the value array of the pairs.
     */
    public void add(double[] timestamps, double[] values) {
        this.timestamps.addAll(listPrimitives(timestamps));
        this.values.addAll(listPrimitives(values));

        interpolator = SplineInterpolator.monotoneCubic(
                this.timestamps,
                this.values
        );
    }

    /**
     * Enable the {@code TimeSpline}'s relative time tracking. This method
     * <b>MUST</b> be called before the {@link #interpolate()} method will
     * work. Note that the {@link #interpolate(double)} method will still
     * function as intended.
     */
    public void start() {
        relativeTime = RelativeTime.now(timeUnit);
    }

    /**
     * Get the {@code TimeSpline}'s {@link RelativeTime} instance.
     *
     * @return the {@code TimeSpline}'s {@link RelativeTime} instance.
     */
    public RelativeTime getRelativeTime() {
        return relativeTime;
    }

    public List<Double> timestamps() {
        return timestamps;
    }

    public List<Double> values() {
        return values;
    }

    public static Spline toSpline(TimeSpline spline) {
        DynamicArray<HeadingPoint> pointArray = new DynamicArray<>();

        for (int i = 0; i < spline.timestamps().size(); i += 1) {
            double timestamp = spline.timestamps().get(i);
            double value = spline.values().get(i);
            pointArray.add(
                    new HeadingPoint(
                            timestamp,
                            value,
                            0
                    )
            );
        }

        return new Spline(pointArray);
    }

    public Spline toSpline() {
        return toSpline(this);
    }
}
