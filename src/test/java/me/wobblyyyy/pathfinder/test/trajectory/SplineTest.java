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

package me.wobblyyyy.pathfinder.test.trajectory;

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.trajectory.Spline;
import me.wobblyyyy.pathfinder.util.Distance;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class SplineTest {
    /*
     * Four points here:
     * TOP
     * RIGHT
     * BOTTOM
     * LEFT
     */

    final double radius = 10;

    Point center = new Point(20, 20);
    Point top = new Point(20, radius + 20);
    Point right = new Point(radius + 20, 20);
    Point bottom = new Point(20, -radius + 20);
    Point left = new Point(-radius + 20, 20);

    Point topToRight = Distance.inDirection(center, 45, radius);
    Point rightToBottom = Distance.inDirection(center, 315, radius);
    Point bottomToLeft = Distance.inDirection(center, 225, radius);
    Point leftToTop = Distance.inDirection(center, 135, radius);

    StaticArray<HeadingPoint> arrayQuad1 = new StaticArray<>(
            top.withHeading(0),
            topToRight.withHeading(0),
            right.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad4 = new StaticArray<>(
            right.withHeading(0),
            rightToBottom.withHeading(0),
            bottom.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad3 = new StaticArray<>(
            bottom.withHeading(0),
            bottomToLeft.withHeading(0),
            left.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad2 = new StaticArray<>(
            left.withHeading(0),
            leftToTop.withHeading(0),
            top.withHeading(0)
    );

    public Spline spline1 = new Spline(arrayQuad1);
    public Spline spline2 = new Spline(arrayQuad2);
    public Spline spline3 = new Spline(arrayQuad3);
    public Spline spline4 = new Spline(arrayQuad4);

    StaticArray<HeadingPoint> coolPoints = new StaticArray<>(
            new HeadingPoint(0, 0, 0),
            new HeadingPoint(10, 10, 0),
//            new HeadingPoint(17, 18, 0)
//            new HeadingPoint(23, 28, 0),
            new HeadingPoint(30, -18, 0),
            new HeadingPoint(40, 0, 0)
    );

    public Spline coolSpline = new Spline(coolPoints);

    StaticArray<HeadingPoint> leftPoints = new StaticArray<>(
            new HeadingPoint(0, 0, 0),
            new HeadingPoint(-10, -10, 0)
    );

    public Spline leftSpline = new Spline(leftPoints);

    @Test
    public void printSplines() {
        System.out.println("Spline 1: " + spline1.toString());
        System.out.println("Spline 2: " + spline2.toString());
        System.out.println("Spline 3: " + spline3.toString());
        System.out.println("Spline 4: " + spline4.toString());
    }

    @Test
    public void drawCircleSplines() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        DrawableSpline drawable1 = new DrawableSpline(spline1);
        DrawableSpline drawable2 = new DrawableSpline(spline2);
        DrawableSpline drawable3 = new DrawableSpline(spline3);
        DrawableSpline drawable4 = new DrawableSpline(spline4);

        PlottableSpline plottable = new PlottableSpline(
                drawable1,
                drawable2,
                drawable3,
                drawable4
        );

        frame.setLayout(new BorderLayout());
        frame.add(plottable);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void drawCoolSpline() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        DrawableSpline drawable = new DrawableSpline(coolSpline);
        PlottableSpline plottable = new PlottableSpline(drawable);

        frame.setLayout(new BorderLayout());
        frame.add(plottable);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void drawLeftSpline() {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        DrawableSpline drawable = new DrawableSpline(leftSpline);
        PlottableSpline plottable = new PlottableSpline(drawable);

        frame.setLayout(new BorderLayout());
        frame.add(plottable);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DrawableSpline {
        private final Spline spline;

        public DrawableSpline(Spline spline) {
            this.spline = spline;
        }

        public void draw(Graphics2D graphics) {
            System.out.println("min: " + spline.minimum().toString());
            System.out.println("max: " + spline.maximum().toString());
            for (double i = spline.minimum().getX();
                 i < spline.maximum().getX();
                 i += 0.1) {
                Point point = spline.interpolateFromX(i);

                point = Point.add(point, new Point(40, 40));
                point = Point.scale(point, 2);

                if (point.getX() < 20 || point.getY() < 20) {
                    System.out.println("X: " + point.getX());
                    System.out.println("Y: " + point.getY());
                }

                graphics.draw(new Line2D.Double(
                        point.getX(),
                        point.getY(),
                        point.getX(),
                        point.getY()
                ));
            }
        }
    }

    public class PlottableSpline extends JComponent {
        private final DrawableSpline[] drawable;

        public PlottableSpline(DrawableSpline... drawable) {
            this.drawable = drawable;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;

            for (DrawableSpline d : drawable) {
                d.draw(g2);
            }
        }
    }
}
