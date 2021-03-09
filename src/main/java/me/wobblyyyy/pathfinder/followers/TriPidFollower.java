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
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * An extension of the two-PID variant that uses an additional PID controller
 * to modify the speed of the robot based on the distance from the target
 * point. This is most effectively utilized when traveling large distances
 * when precision in target point is still required.
 *
 * <p>
 * By default, this is configured to use a direct PID controller so that the
 * starting distance from the target point is the PID's maximum speed at that
 * a distance of 0 from the target point is 0, meaning the robot is stopped.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class TriPidFollower extends DualPidFollower {
    private PidController speedController;

    @Sync
    public TriPidFollower(Drive drive,
                          Odometry odometry,
                          HeadingPoint targetPosition,
                          double driveSpeedCoefficient) {
        super(drive, odometry, targetPosition, driveSpeedCoefficient);
    }

    private double distanceFromTarget() {
        return Distance.getDistance(
                current(),
                target()
        );
    }

    /**
     * Update the follower's drive values.
     */
    @Sync
    @Override
    public void update() {
        double calculatedPower = speedController
                .calculate(distanceFromTarget());
        double inverseCalculatedPower = calculatedPower * -1;

        setDriveSpeedCoefficient(inverseCalculatedPower);

        super.update();
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
        double distanceFromTarget = distanceFromTarget();
        double kP_speed = 1 / distanceFromTarget;

        speedController = new PidController(kP_speed, 0, 0);
        speedController.setSetpoint(0);

        super.calculate();
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
        super.drive();
    }
}
