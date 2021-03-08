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

import me.wobblyyyy.pathfinder.core.Follower;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * The most incredibly simple trajectory follower you could possible imagine.
 * This follower works by linearly driving the drivetrain in a given direction.
 * The follower's speed coefficient (how fast the robot moves out of a possible
 * 100% power) can be modified on construction, allowing the follower to be
 * either faster or slower. After the follower's execution has finished
 * entirely, the follower should stop the robot by setting power to each of the
 * drivetrain's motors to 0. Please note that this follower doesn't have any
 * dynamic correction - if the robot is in the wrong place, the follower won't
 * work, and your pathfinder will have a hard time... pathfinding.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class LinearFollower implements Follower {
    /**
     * A reference to the follower's drivetrain.
     */
    private final Drive drive;

    /**
     * The follower's odometry system. This system is only used for determining
     * if the follower is close to the target position.
     */
    private final Odometry odometry;

    /**
     * The follower's start point.
     */
    private final HeadingPoint start;

    /**
     * The follower's end/target point.
     */
    private final HeadingPoint end;

    /**
     * The follower's speed multiplier. The higher this value is, the faster
     * the {@code LinearFollower} will execute. This value can be changed
     * through either configuration options or the {@code Factory} class.
     */
    private final double coefficient;

    /**
     * Create a new linear follower.
     *
     * @param drive       the robot's drivetrain.
     * @param odometry    the robot's odometry system.
     * @param start       the start position.
     * @param end         the end position.
     * @param coefficient the robot's speed (0 to 1).
     */
    public LinearFollower(Drive drive,
                          Odometry odometry,
                          HeadingPoint start,
                          HeadingPoint end,
                          double coefficient) {
        this.start = start;
        this.end = end;
        this.drive = drive;
        this.odometry = odometry;
        this.coefficient = coefficient;
    }

    /**
     * We don't need to do anything here, either - all of the driving that
     * needs to get done is done through the drivetrain itself. This method
     * will do absolutely nothing, but it has to be called anyways.
     */
    @Override
    public void update() {

    }

    /**
     * In this case, we don't need to do anything - there are no calculations
     * needed to drive the robot. All of the translational calculations that
     * are needed should be handled by the internal kinematics of the
     * implemented drivetrain of choice.
     */
    @Override
    public void calculate() {

    }

    /**
     * Drive the robot. This method attempts to power the robot by calling
     * the drive method of the drivetrain. If the drivetrain hasn't implemented
     * the {@link Drive#drive(HeadingPoint, HeadingPoint, double)} method,
     * the robot can't effectively be driven.
     */
    @Override
    public void drive() {
        drive.drive(
                start,
                end,
                coefficient
        );
    }

    /**
     * Has the follower finished yet? The follower's finish qualification is
     * determined by whether or not the robot is close enough to the target
     * position. This tolerance is, by default, 4 units - often inches.
     *
     * @return whether or not the follower has finished.
     */
    @Override
    public boolean isDone() {
        /*
         * If the robot's position and the target position are very nearly
         * the same, we're done. We can now return true and set power to the
         * motors. Power, of course, meaning 0 - the robot should stop at this
         * point entirely.
         */
        if (Distance.isNearPoint(
                odometry.getPos(),
                end,
                4
        )) {
            drive.drive(0.0, 0.0);
            return true;
        }

        /*
         * If we aren't done, however, return false, indicating that the
         * follower should continue its execution.
         */
        return false;
    }
}
