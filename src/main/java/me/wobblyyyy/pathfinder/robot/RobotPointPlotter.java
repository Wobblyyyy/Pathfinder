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
import java.util.concurrent.atomic.AtomicLong;

/**
 * A simulated-robot related utility class that uses Java's swing and awt
 * libraries to (rather inefficiently) record the movement of a simulated
 * (or real) robot. Please note that I'm entirely aware this is really messy
 * and really inefficient code - I waive all responsibility for your braincells
 * if you decide to read the source code for this wretched beast.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class RobotPointPlotter {
    /**
     * The plotter's odometry system. This odometry is polled for the robot's
     * position while the plotter is capturing points.
     */
    private final Odometry odometry;

    /**
     * Should the plotter currently be capturing points? This is atomic
     * because multiple threads are used here.
     */
    private final AtomicBoolean shouldCapture = new AtomicBoolean(true);

    /**
     * An array of all of the captured points. Hey, would you look at this -
     * using a {@code DynamicArray} over an {@code ArrayList} might actually
     * have some pretty big performance benefits here!
     */
    private DynamicArray<HeadingPoint> captured;

    /**
     * Create a new {@code RobotPointPlotter} that can record the position
     * of the robot using the inputted odometry.
     *
     * @param odometry the odometry system that will be used to record the
     *                 robot's position.
     */
    public RobotPointPlotter(Odometry odometry) {
        this.odometry = odometry;
    }

    /**
     * Draw a set of points, show the drawn points, and then wait until the
     * desired time (wait) has elapsed in ms before continuing.
     *
     * @param points the points to display.
     * @param wait   how long, in milliseconds, to wait.
     */
    public void drawPoints(DynamicArray<HeadingPoint> points,
                           int wait) {
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

    /**
     * Call the {@link #drawPoints(DynamicArray, int)} method in another
     * thread. This ensures that the main thread (the thread that you're
     * using for everything else) doesn't get locked up in order to display
     * the points.
     *
     * @param points the points to draw.
     * @param waitMs how long to wait (ms).
     */
    public void asyncDrawPoints(DynamicArray<HeadingPoint> points,
                                int waitMs) {
        new Thread(() -> drawPoints(points, waitMs)).start();
    }

    /**
     * Start capturing the robot's position in another thread.
     *
     * @param msBetweenCaptures how long (in milliseconds) between captures.
     *                          This method works by checking to see if the
     *                          time since last capture exceeds this parameter.
     *                          If it does, it'll capture the robot's position
     *                          again - otherwise, it'll idle.
     */
    public void startCapturingPoints(int msBetweenCaptures) {
        captured = new DynamicArray<>();

        shouldCapture.set(true);

        final AtomicLong sinceLast = new AtomicLong(msBetweenCaptures);
        final AtomicLong last = new AtomicLong(System.currentTimeMillis());

        Thread thread = new Thread(() -> {
            while (shouldCapture.get()) {
                if (sinceLast.get() >= msBetweenCaptures) {
                    captured.add(odometry.getPos());
                    sinceLast.set(0);
                    last.set(System.currentTimeMillis());
                } else {
                    sinceLast.set(System.currentTimeMillis() - last.get());
                }
            }
        });

        thread.start();
    }

    /**
     * Stop capturing points. If the {@link #startCapturingPoints(int)} method
     * hasn't been called yet, this method will effectively do nothing at all.
     */
    public void stopCapturingPoints() {
        shouldCapture.set(false);
    }

    /**
     * Show the captured points for a specified amount of time, notated in
     * milliseconds.
     *
     * @param forHowLongMs how long the points should be displayed for (in
     *                     milliseconds).
     */
    public void showCapturedPoints(int forHowLongMs) {
        drawPoints(captured, forHowLongMs);
    }

    /**
     * Show the captured points for a specified amount of time, notated in
     * milliseconds. This method operates asynchronously to ensure the
     * calling thread doesn't get locked up.
     *
     * @param forHowLongMs how long the points should be displayed for (in
     *                     milliseconds).
     */
    public void asyncShowCapturedPoints(int forHowLongMs) {
        asyncDrawPoints(captured, forHowLongMs);
    }

    /**
     * Mostly internal class for drawing points. I'm genuinely impressed by the
     * fact that code this bad compiles - I feel like javac should give me
     * a warning saying "this sucks" or something.
     */
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

            points.itr().forEach(point -> draw(g2, point));
        }
    }
}
