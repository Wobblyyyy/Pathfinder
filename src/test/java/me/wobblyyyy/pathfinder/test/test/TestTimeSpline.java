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

package me.wobblyyyy.pathfinder.test.test;

import me.wobblyyyy.pathfinder.geometry.Spline;
import me.wobblyyyy.pathfinder.test.trajectory.SplineTest;
import me.wobblyyyy.pathfinder.time.TimeSpline;
import me.wobblyyyy.pathfinder.time.TimeUnit;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class TestTimeSpline {
    private final TimeUnit unit = TimeUnit.MILLISECOND;

    @Test
    public void testInterpolation() {
        TimeSpline spline = new TimeSpline(
                unit,
                new double[]{
                        1000,
                        2000,
                        3000,
                        4000,
                        5000
                },
                new double[]{
                        1,
                        2,
                        3,
                        4,
                        5
                },
                true
        );

        for (double i = 0; i <= 5000; i += 500) {
            System.out.println(
                    "Interpolated from " + i + ": " +
                            spline.interpolate(i)
            );
        }
    }

    @Test
    public void testTimeBasedInterpolation() {
        TimeSpline spline = new TimeSpline(
                unit,
                new double[]{
                        1000 / 100D,
                        2000 / 100D,
                        3000 / 100D,
                        4000 / 100D,
                        5000 / 100D
                },
                new double[]{
                        1,
                        2,
                        3,
                        4,
                        5
                },
                true
        );

        double interpolated;
        double lastInterpolated = 0;
        while ((interpolated = spline.interpolate()) < 5D) {
            if (interpolated != lastInterpolated) {
                double relative = spline.getRelativeTime().relativeTime();
                System.out.println(
                        "Interpolated from " + relative + ": " + interpolated
                );
                lastInterpolated = interpolated;
            }
        }
    }

    @SuppressWarnings("OctalInteger")
    @Test
    public void testFunkyInterpolation() {
        TimeSpline spline = new TimeSpline(
                unit,
                new double[]{
                        0000 / 100D,
                        1000 / 100D,
                        2000 / 100D,
                        3000 / 100D,
                        4000 / 100D,
                        5000 / 100D,
                        6000 / 100D,
                        7000 / 100D,
                        8000 / 100D,
                        9000 / 100D
                },
                new double[]{
                        1,
                        2,
                        3,
                        4,
                        5,
                        4,
                        3,
                        2,
                        1,
                        0
                },
                true
        );

        double interpolated;
        double lastInterpolated = 0;
        while ((interpolated = spline.interpolate()) != 0) {
            if (interpolated != lastInterpolated) {
                double relative = spline.getRelativeTime().relativeTime();
                System.out.println(
                        "Interpolated from " + relative + ": " + interpolated
                );
                lastInterpolated = interpolated;
            }
        }

        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        Spline asSpline = spline.toSpline();

        SplineTest.DrawableSpline drawable = new SplineTest.DrawableSpline(asSpline);
        SplineTest.PlottableSpline plottable = new SplineTest.PlottableSpline(drawable);

        frame.setLayout(new BorderLayout());
        frame.add(plottable);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
//            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
