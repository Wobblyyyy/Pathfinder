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

package me.wobblyyyy.pathfinder.test.drawer;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.config.PathfinderConfigurationBuilder;
import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.geometry.CircleInterpolator;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.robot.RobotPointPlotter;
import me.wobblyyyy.pathfinder.robot.SimulatedRobot;
import me.wobblyyyy.pathfinder.trajectory.Arcs;
import org.junit.jupiter.api.Test;

public class TestDrawingRobot {
    private SimulatedRobot robot = new SimulatedRobot(1);
    private PathfinderConfig config = PathfinderConfigurationBuilder
            .newConfiguration()
            .drive(robot)
            .odometry(robot)
            .followerType(Followers.LINEAR)
            .speed(0.0001)
            .build();
    private final Pathfinder pathfinder = new Pathfinder(config);

    @Test
    public void testDrawPointsForLine() {
        HeadingPoint target1 = new HeadingPoint(10, 10, 0);
        HeadingPoint target2 = new HeadingPoint(20, 10, 0);
        HeadingPoint target3 = new HeadingPoint(30, 30, 0);
        RobotPointPlotter plotter = new RobotPointPlotter(robot);
        (new Thread(plotter::startCapturingPoints)).start();
        pathfinder.goToPosition(target1);
        pathfinder.tickUntil();
        pathfinder.goToPosition(target2);
        pathfinder.tickUntil();
        pathfinder.goToPosition(target3);
        pathfinder.tickUntil();
        plotter.stopCapturingPoints();
        plotter.showCapturedPoints(10000);
    }

    @Test
    public void testCircleThing() {
        CircleInterpolator interpolator = new CircleInterpolator(
                new Point(0, 0),
                90,
                180,
                50,
                50
        );
        RobotPointPlotter plotter = new RobotPointPlotter(robot);
        (new Thread(plotter::startCapturingPoints)).start();
        DynamicArray<Point> pointsWOH = interpolator.getPointsFromEnd();
        DynamicArray<HeadingPoint> points = new DynamicArray<>() {{
            pointsWOH.itr().forEach(point -> add(point.withHeading(0)));
        }};
        pathfinder.followPath(points);
        pathfinder.tickUntil();
        plotter.stopCapturingPoints();
        plotter.showCapturedPoints(100000);
    }

    @Test
    public void testSlalomPath() {
        DynamicArray<HeadingPoint> slalomPath = new DynamicArray<>(
                new HeadingPoint(10.1, 0.1, 0),
                new HeadingPoint(72.2, -90.2, 0),
                new HeadingPoint(210.3, -90.3, 0),
                new HeadingPoint(210.4, 4.4, 0),
                new HeadingPoint(290.5, 4.5, 0),
                new HeadingPoint(290.6, -90.6, 0),
                new HeadingPoint(210.7, -90.7, 0),
                new HeadingPoint(210.8, 4.8, 0),
                new HeadingPoint(72.9, 4.9, 0),
                new HeadingPoint(20.1, -60.1, 0),
                new HeadingPoint(0.2, -60.2, 0)
        );
        RobotPointPlotter plotter = new RobotPointPlotter(robot);
        (new Thread(plotter::startCapturingPoints)).start();
        pathfinder.followPath(slalomPath);
        pathfinder.tickUntil();
        plotter.stopCapturingPoints();
        plotter.showCapturedPoints(100000);
    }

    @Test
    public void testBarrelPath() {
        DynamicArray<HeadingPoint> barrelPath =
                new DynamicArray<>() {{
                    final Point D5_CENTER = new Point(0, 150);
                    final double D5_RADIUS = 24;
                    final Point B8_CENTER = new Point(120, 240);
                    final double B8_RADIUS = 24;
                    final Point D10_CENTER = new Point(0, 300);
                    final double D10_RADIUS = 24;

                    DynamicArray<HeadingPoint> D5_A = Arcs.decreasingFrom(
                            Arcs.INTERPOLATED_QUAD_1, D5_RADIUS, D5_CENTER);
                    DynamicArray<HeadingPoint> D5_B = Arcs.decreasingFrom(
                            Arcs.INTERPOLATED_QUAD_2, D5_RADIUS, D5_CENTER);
                    DynamicArray<HeadingPoint> D5_C = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_3, D5_RADIUS, D5_CENTER);
                    DynamicArray<HeadingPoint> D5_D = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_4, D5_RADIUS, D5_CENTER);

                    DynamicArray<HeadingPoint> B8_A = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_2, B8_RADIUS, B8_CENTER);
                    DynamicArray<HeadingPoint> B8_B = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_1, B8_RADIUS, B8_CENTER);
                    DynamicArray<HeadingPoint> B8_C = Arcs.decreasingFrom(
                            Arcs.INTERPOLATED_QUAD_4, B8_RADIUS, B8_CENTER);

                    DynamicArray<HeadingPoint> D10_A = new DynamicArray<>(
                            new HeadingPoint(0, 275, 0));
                    DynamicArray<HeadingPoint> D10_B = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_2, D10_RADIUS, D10_CENTER);
                    DynamicArray<HeadingPoint> D10_C = Arcs.increasingFrom(
                            Arcs.INTERPOLATED_QUAD_1, D10_RADIUS, D10_CENTER);

                    DynamicArray<HeadingPoint> RETURN_TO_START = new DynamicArray<>(
                            new HeadingPoint(24, 275, 0),
                            new HeadingPoint(22, 150, 0),
                            new HeadingPoint(20, 0, 0),
                            new HeadingPoint(0, 0, 0)
                    );

                    D5_A.itr().forEach(this::add);
                    D5_B.itr().forEach(this::add);
                    D5_C.itr().forEach(this::add);
                    D5_D.itr().forEach(this::add);
                    B8_A.itr().forEach(this::add);
                    B8_B.itr().forEach(this::add);
                    B8_C.itr().forEach(this::add);
                    D10_A.itr().forEach(this::add);
                    D10_B.itr().forEach(this::add);
                    D10_C.itr().forEach(this::add);
                    RETURN_TO_START.itr().forEach(this::add);
                }};
        RobotPointPlotter plotter = new RobotPointPlotter(robot);
        (new Thread(plotter::startCapturingPoints)).start();
        pathfinder.followPath(barrelPath);
        pathfinder.tickUntil();
        plotter.stopCapturingPoints();
        plotter.showCapturedPoints(100000);
    }
}
