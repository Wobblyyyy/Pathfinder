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

import me.wobblyyyy.intra.ftc2.utils.math.PidController;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * A type of path follower based on the {@code LinearFollower} class. This
 * follower integrates a PID controller for the speed of the chassis, meaning
 * that the robot will move at different speeds while following the path.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class PidFollower extends LinearFollower {
    /**
     * The PID controller that's used for running all sorts of cool math
     * things. Not really, actually - it's pretty lame, but it hopefully gets
     * the job done just as well as we need it to.
     */
    private final PidController speedController;

    /**
     * Create a new PID follower. This constructor calls the constructor of the
     * {@link LinearFollower} class.
     *
     * @param drive       the robot's drivetrain.
     * @param odometry    the robot's odometry system.
     * @param start       the start position.
     * @param end         the end position.
     * @param coefficient the speed PID controller's proportional coefficient.
     */
    public PidFollower(Drive drive,
                       Odometry odometry,
                       HeadingPoint start,
                       HeadingPoint end,
                       double coefficient) {
        super(drive, odometry, start, end, 1.0);

        double distance = distanceToTarget();
        double kP = getKpForSpeed(coefficient);
        speedController = new PidController(kP, 0.0, 0.0);
        speedController.setSetpoint(distance);
    }

    /**
     * Get a valid proportional coefficient for the speed PID controller based
     * on an inputted coefficient. This method gets the reciprocal of the
     * distance to target and multiplies it by the given coefficient.
     *
     * @param coefficient the PID controller's relativistic coefficient.
     * @return a valid kP value based on the inputted coefficient.
     */
    private double getKpForSpeed(double coefficient) {
        return (1 / distanceToTarget()) * coefficient;
    }

    /**
     * Get the current distance to the target point.
     *
     * @return the current distance to the target point. This distance is
     * calculated using the odometer's current position and the target position.
     */
    private double distanceToTarget() {
        return Distance.getDistance(
                super.getOdometry().getPos(),
                super.getTargetPos()
        );
    }

    /**
     * Drive the robot. This method attempts to power the robot by calling
     * the drive method of the drivetrain. If the drivetrain hasn't implemented
     * the {@link Drive#drive(HeadingPoint, HeadingPoint, double)} method,
     * the robot can't effectively be driven.
     */
    @Override
    public void drive() {
        /*
         * Calculate a new DRIVE SPEED coefficient based on the PID
         * controller's calculated value.
         */
        double calculatedCoefficient =
                speedController.calculate(distanceToTarget());

        /*
         * Set the linear follower's coefficient to match the calculated
         * coefficient.
         */
        super.setCoefficient(calculatedCoefficient);

        /*
         * Call the linear follower's drive method to actually drive the robot
         * as intended.
         */
        super.drive();
    }
}
