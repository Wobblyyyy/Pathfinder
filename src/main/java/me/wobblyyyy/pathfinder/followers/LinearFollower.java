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
 *
 * <p>
 * Unlike a follower such as the PIDFollower, the LinearFollower does nothing
 * fancy. It takes an input point and outputs some motor power to get there.
 * It ain't fast. It ain't pretty. It ain't cool. But it gets the job done.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class LinearFollower implements Follower {
    private final Drive drive;
    private final Odometry odometry;
    private final HeadingPoint start;
    private final HeadingPoint end;
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
     * needs to get done is done through the drivetrain itself.
     */
    @Override
    public void update() {

    }

    /**
     * In this case, we don't need to do anything - there are no calculations
     * needed to drive the robot.
     */
    @Override
    public void calculate() {

    }

    /**
     * Drive the robot.
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
     * Has the follower finished yet?
     *
     * @return whether or not the follower has finished.
     */
    @Override
    public boolean isDone() {
        return Distance.isNearPoint(
                odometry.getPos(),
                end,
                4
        );
    }
}
