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

package me.wobblyyyy.pathfinder.followers;

import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.trajectory.Trajectory;
import me.wobblyyyy.pathfinder.geometry.Distance;

/**
 * A lovely follower designed for trajectories. Normal followers, although
 * quite cool, aren't exactly as lovely as trajectory followers. Trajectories
 * are at least 25% more epic than normal lines.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class TrajectoryFollower implements Follower {
    private final double power;
    private final Trajectory trajectory;
    private final Odometry odometry;
    private final Drive drive;
    private boolean hasFinished = false;

    public TrajectoryFollower(Trajectory trajectory,
                              Odometry odometry,
                              Drive drive,
                              PathfinderConfig config) {
        this.trajectory = trajectory;
        this.odometry = odometry;
        this.drive = drive;
        this.power = config.getSpeed();
    }

    @Override
    public void update() {

    }

    @Override
    public void calculate() {

    }

    /**
     * Get a robot transformation based on current position, target position,
     * and current angle.
     *
     * @param current the robot's current position.
     * @param target  the robot's target position.
     * @param angle   the angle the robot should face.
     * @return a new transformation based on the current and target points, as
     * well as the provided angle.
     */
    public RTransform getTransform(Point current,
                                   Point target,
                                   Angle angle) {
        /*
         * Basically a subtraction operation - target minus current.
         */
        Point difference = Point.add(
                target,
                Point.scale(current, -1)
        );

        /*
         * Get the angle to the target point from the current point.
         */
        double thetaDegrees = difference.getTheta();

        /*
         * Create a point, representing where the target is at the power the
         * follower should operate at.
         */
        Point targetPoint = Distance.inDirection(
                new Point(0, 0), // origin point
                thetaDegrees,    // degrees to target point
                power            // the robot's drivetrain power
        );

        /*
         * Create a new transformation and return it.
         */
        return new RTransform(
                new Point(0, 0), // (0, 0) - default origin
                targetPoint,     // crafted target point
                angle            // angle the robot should face
        );
    }

    @Override
    public void drive() {
        HeadingPoint position = odometry.getPos();
        Point start = trajectory.getCurrentSegment().start();
        Point end = trajectory.getCurrentSegment().end();

        if (Distance.isNearPoint(end, position, 0.25)) {
            if (trajectory.getNextSegment() == null) {
                hasFinished = true;
            }

            trajectory.completeSegment();
        } else {
            double totalDistance = Distance.distanceX(start, end);
            double step = totalDistance / 100;
            final Point target;

            if (position.getX() > end.getX()) {
                target = trajectory.getCurrentSegment().interpolateFromX(
                        position.getX() + step
                );
            } else {
                target = trajectory.getCurrentSegment().interpolateFromX(
                        position.getX() - step
                );
            }

            drive.drive(getTransform(
                    position,
                    target,
                    position.getAngle()
            ));
        }
    }

    @Override
    public boolean isDone() {
        return hasFinished;
    }
}
