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

package me.wobblyyyy.pathfinder.robot;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.concurrent.atomic.AtomicBoolean;

public class RobotPointPlotter {
    public static class PointSetDrawer extends JComponent {
        private final DynamicArray<HeadingPoint> points;

        public PointSetDrawer(DynamicArray<HeadingPoint> points) {
            this.points = points;
        }

        protected void draw(Graphics2D graphics, HeadingPoint point) {
            graphics.draw(new Line2D.Double(
                    point.getX() + 100,
                    point.getY() + 100,
                    point.getX() + 100,
                    point.getY() + 100
            ));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            points.itr().forEach(point -> {
                draw(g2, point);
            });
        }
    }

    public void drawPoints(DynamicArray<HeadingPoint> points, int wait) {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        PointSetDrawer drawer = new PointSetDrawer(points);

        frame.setLayout(new BorderLayout());
        frame.add(drawer);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(wait);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DynamicArray<HeadingPoint> captured;
    private final Odometry odometry;
    private final AtomicBoolean shouldCapture = new AtomicBoolean(true);

    public RobotPointPlotter(Odometry odometry) {
        this.odometry = odometry;
    }

    public void startCapturingPoints() {
        captured = new DynamicArray<>();

        shouldCapture.set(true);

        long sinceLast = 10000000;
        long last = System.currentTimeMillis();
        while (shouldCapture.get()) {
            if (sinceLast > 50) {
                captured.add(odometry.getPos());
                sinceLast = 0;
                last = System.currentTimeMillis();
            } else {
                sinceLast = System.currentTimeMillis() - last;
            }
        }
    }

    public void stopCapturingPoints() {
        shouldCapture.set(false);
    }

    public void showCapturedPoints(int forHowLongMs) {
        drawPoints(captured, forHowLongMs);
    }
}
