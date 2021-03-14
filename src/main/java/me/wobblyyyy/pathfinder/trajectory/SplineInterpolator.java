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

import java.util.ArrayList;
import java.util.List;

/**
 * Interpolate splines by using the Fritsch-Carlson computational method for
 * determining control points for a spline. This class isn't really all that
 * useful on its own - rather, you should go check out the much more usable
 * {@link Spline} class - that'll get what you need done.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
@SuppressWarnings("DuplicatedCode")
public class SplineInterpolator {
    private final List<Double> xValues;
    private final List<Double> yValues;
    private final double[] mValues;

    protected SplineInterpolator(List<Double> x,
                                 List<Double> y,
                                 double[] m) {
        xValues = x;
        yValues = y;
        mValues = m;
    }

    /**
     * Create a new inverted monotone cubic spline.
     *
     * @param xList the X components of the spline.
     * @param yList the Y components of the spline.
     * @return a newly-created inverted spline that passes through each of the
     * provided points.
     */
    public static SplineInterpolator invertedMonotoneCubic(
            List<Double> xList,
            List<Double> yList) {
        ArrayList<Double> reflectedX = new ArrayList<>();
        double centerX = xList.get(0);

        for (double x : xList) {
            double distanceFromCenter = Math.abs(centerX - x);

            reflectedX.add(distanceFromCenter + centerX);
        }

        return monotoneCubic(reflectedX, yList);
    }

    /**
     * Create a new monotone cubic spline.
     *
     * @param x the X components of the spline.
     * @param y the Y components of the spline.
     * @return a newly-created spline that passes through each of the provided
     * X/Y control points - very cool, I know.
     */
    public static SplineInterpolator monotoneCubic(List<Double> x,
                                                   List<Double> y) {
        final int n = x.size();
        double[] d = new double[n - 1];
        double[] m = new double[n];

        for (int i = 0; i < n - 1; i++) {
            double h = x.get(i + 1) - x.get(i);
            d[i] = (y.get(i + 1) - y.get(i)) / h;
        }

        m[0] = d[0];

        for (int i = 1; i < n - 1; i++) {
            m[i] = (d[i - 1] + d[i]) * 0.5;
        }

        m[n - 1] = d[n - 2];

        for (int i = 0; i < n - 1; i++) {
            if (d[i] == 0) {
                m[i] = 0.0;
                m[i + 1] = 0.0;
            } else {
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                double h = Math.hypot(a, b);
                if (h > 3.0) {
                    double t = 3.0 / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }

        return new SplineInterpolator(x, y, m);
    }

    public double interpolateFromX(double x) {
        final int n = xValues.size();

        if (Double.isNaN(x)) return x;
        if (x <= xValues.get(0)) return yValues.get(0);
        if (x >= xValues.get(n - 1)) return yValues.get(n - 1);

        int i = 0;
        while (x >= xValues.get(i + 1)) {
            i += 1;

            if (x == xValues.get(i)) return yValues.get(i);
        }

        double h = xValues.get(i + 1) - xValues.get(i);
        double t = (x - xValues.get(i)) / h;

        return (yValues.get(i) * (1 + 2 * t) + h * mValues[i] * t) *
                (1 - t) *
                (1 - t) +
                (yValues.get(i + 1) *
                        (3 - 2 * t) + h *
                        mValues[i + 1] *
                        (t - 1)
                ) * t * t;
    }

    public double interpolateFromY(double y) {
        final int n = yValues.size();

        if (Double.isNaN(y)) return y;
        if (y <= yValues.get(0)) return xValues.get(0);
        if (y >= yValues.get(n - 1)) return xValues.get(n - 1);

        int i = 0;
        while (y >= yValues.get(i + 1)) {
            i += 1;

            if (y == yValues.get(i)) return xValues.get(i);
        }

        double h = yValues.get(i + 1) - yValues.get(i);
        double t = (y - yValues.get(i)) / h;

        return (xValues.get(i) * (1 + 2 * t) + h * mValues[i] * t) *
                (1 - t) *
                (1 - t) +
                (xValues.get(i + 1) *
                        (3 - 2 * t) + h *
                        mValues[i + 1] *
                        (t - 1)
                ) * t * t;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        final int n = xValues.size();

        builder.append("{");

        for (int i = 0; i < n; i++) {
            if (i != 0) builder.append(", ");

            builder
                    .append("(")
                    .append(xValues.get(i))
                    .append(", ")
                    .append(yValues.get(i))
                    .append(": ")
                    .append(mValues[i])
                    .append(")");
        }

        builder.append("}");

        return builder.toString();
    }
}
