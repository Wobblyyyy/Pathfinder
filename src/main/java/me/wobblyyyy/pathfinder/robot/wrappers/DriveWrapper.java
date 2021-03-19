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

package me.wobblyyyy.pathfinder.robot.wrappers;

import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.robot.Drive;

/**
 * Wrapper surrounding user-inputted drivetrains that's designed to allow users
 * to invert the X or Y transform values that are fed to their drivetrains.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class DriveWrapper implements Drive {
    /**
     * The wrapper's drivetrain.
     */
    private final Drive drive;

    /**
     * Should X and Y transformations be swapped?
     */
    private final boolean driveSwapXY;

    /**
     * Should transformational X values be swapped?
     */
    private final boolean driveInvertX;

    /**
     * Should transformational Y values be swapped?
     */
    private final boolean driveInvertY;

    /**
     * Create a new drive wrapper.
     *
     * @param drive        the {@code Drive} to use for the wrapper.
     * @param driveSwapXY  should X and Y transforms be swapped?
     * @param driveInvertX should X transforms be inverted?
     * @param driveInvertY should Y transforms be inverted?
     */
    public DriveWrapper(Drive drive,
                        boolean driveSwapXY,
                        boolean driveInvertX,
                        boolean driveInvertY) {
        this.drive = drive;
        this.driveSwapXY = driveSwapXY;
        this.driveInvertX = driveInvertX;
        this.driveInvertY = driveInvertY;
    }

    /**
     * Drive the robot according to a specified transformation. Transformations
     * are made up of several components, most notably X, Y, and angle. It's
     * important to note that the angle component of these transformations
     * represents the angle that the robot SHOULD be facing, not by how much
     * the robot needs to turn.
     *
     * @param transform the robot's desired transformation. It's important to
     *                  note that this transformation's angle doesn't mean how
     *                  much the robot should be turning, it means the angle
     *                  that the robot should currently be facing.
     */
    @Override
    public void drive(RTransform transform) {
        transform = driveSwapXY ?
                new RTransform(
                        transform.getStart(),
                        transform.getStop(),
                        transform.getTurn()
                ) : transform;

        if (driveInvertX) transform.invertX();
        if (driveInvertY) transform.invertY();

        drive.drive(transform);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void enableUserControl() {
        drive.enableUserControl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disableUserControl() {
        drive.disableUserControl();
    }
}
