/*
 *  ======================================================================
 *  || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 *  ||                                                                  ||
 *  || This file is part of the "Pathfinder" project, which is licensed ||
 *  || and distributed under the GPU General Public License V3.         ||
 *  ||                                                                  ||
 *  || Pathfinder is available on GitHub:                               ||
 *  || https://github.com/Wobblyyyy/Pathfinder                          ||
 *  ||                                                                  ||
 *  || Pathfinder's license is available:                               ||
 *  || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 *  ||                                                                  ||
 *  || Re-distribution of this, or any other files, is allowed so long  ||
 *  || as this same copyright notice is included and made evident.      ||
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.followers;

import me.wobblyyyy.intra.ftc2.utils.math.PidController;
import me.wobblyyyy.pathfinder.annotations.Sync;
import me.wobblyyyy.pathfinder.core.Follower;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * Use two proportional integral derivative controllers to determine optimal
 * X and Y translational values - move at a consistent speed according to this
 * calculated translation.
 *
 * <p>
 * This is essentially a more effective substitution for the {@code Linear}
 * follower type - using PID controllers and constantly updating the current
 * position of the robot allows for increased precision in movement.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class DualPidFollower implements Follower {
    private final Drive drive;
    private final Odometry odometry;
    private final HeadingPoint targetPosition;
    private double driveSpeedCoefficient;
    private double calculatedPowerX;
    private double calculatedPowerY;
    private PidController pidControllerX;
    private PidController pidControllerY;

    /**
     * Create a new {@code DualPidFollower}.
     *
     * @param drive                 the robot's drivetrain interface. Mostly
     *                              used for... you know... driving the robot?
     * @param odometry              the robot's odometry system. This system
     *                              will be polled every execution cycle.
     * @param targetPosition        the follower's target position and heading.
     * @param driveSpeedCoefficient the speed at which the follower should
     *                              operate at. This is a value from 0 to 1,
     *                              with 1 being full speed forwards, and 0
     *                              being not moving at all. This can be changed
     *                              with {@link #setDriveSpeedCoefficient(double)}
     */
    @Sync
    public DualPidFollower(Drive drive,
                           Odometry odometry,
                           HeadingPoint targetPosition,
                           double driveSpeedCoefficient) {
        this.drive = drive;
        this.odometry = odometry;
        this.targetPosition = targetPosition;
        this.driveSpeedCoefficient = driveSpeedCoefficient;
    }

    @Sync
    private HeadingPoint currentPosition() {
        return odometry.getPos();
    }

    /**
     * Update the follower's drive values.
     */
    @Sync
    @Override
    public void update() {
        HeadingPoint currentPosition = currentPosition();

        double distanceX = Distance.distanceX(currentPosition, targetPosition);
        double distanceY = Distance.distanceY(currentPosition, targetPosition);

        this.calculatedPowerX = pidControllerX.calculate(distanceX);
        this.calculatedPowerY = pidControllerY.calculate(distanceY);
    }

    /**
     * Calculate a list of all the instructions needed to follow the path.
     *
     * <p>
     * This should (needs to, really) be run before the robot makes any sort
     * of movement in any direction - otherwise, you're defeating the whole
     * purpose of having a pre-planned route.
     * </p>
     */
    @Sync
    @Override
    public void calculate() {
        HeadingPoint currentPosition = currentPosition();

        double distanceX = -Distance.distanceX(currentPosition, targetPosition);
        double distanceY = -Distance.distanceY(currentPosition, targetPosition);

        double kP_x = 1 / distanceX;
        double kP_y = 1 / distanceY;

        this.pidControllerX = new PidController(kP_x, 0, 0);
        this.pidControllerY = new PidController(kP_y, 0, 0);

        pidControllerX.setSetpoint(0);
        pidControllerY.setSetpoint(0);
    }

    /**
     * Drive the robot itself.
     *
     * <p>
     * The drive method should almost always call another drive method, a
     * drive method that's contained in a Drive class, actually. Driving the
     * robot within the follower is a shitty idea, and you shouldn't do it.
     * </p>
     */
    @Sync
    @Override
    public void drive() {
        HeadingPoint currentPosition = currentPosition();
        HeadingPoint calculatedTargetPoint = new HeadingPoint(
                calculatedPowerX,
                calculatedPowerY,
                targetPosition.getHeading()
        );

        drive.drive(
                currentPosition,
                calculatedTargetPoint,
                driveSpeedCoefficient
        );
    }

    /**
     * Whether or not the follower has finished its execution.
     *
     * @return whether or not the follower has finished its execution.
     */
    @Sync
    @Override
    public boolean isDone() {
        return Distance.isNearPoint(
                currentPosition(),
                targetPosition,
                2
        );
    }

    @Sync
    public HeadingPoint target() {
        return targetPosition;
    }

    @Sync
    public HeadingPoint current() {
        return currentPosition();
    }

    @Sync
    public void setDriveSpeedCoefficient(double driveSpeedCoefficient) {
        this.driveSpeedCoefficient = driveSpeedCoefficient;
    }
}
