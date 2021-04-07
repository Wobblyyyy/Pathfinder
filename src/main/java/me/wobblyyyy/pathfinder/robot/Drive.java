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

import me.wobblyyyy.pathfinder.kinematics.RTransform;

/**
 * An interface used to make sure different types of drive systems are
 * fully integrated and working.
 *
 * <p>
 * If you're trying to make a Pathfinder configuration and you want to add
 * a drivetrain (have to, actually) you SHOULD NOT implement this class.
 * UNLESS you have a drivetrain that's not a swerve drive, a meccanum drive,
 * or a tank drive, you should use a pre-made class instead of creating your
 * own. Unless you like debugging...
 * </p>
 *
 * @author Colin Robertson
 * @version 2.1.0
 * @since 0.1.0
 */
public interface Drive {
    /**
     * Drive the robot according to a specified transformation. Transformations
     * are made up of several components, most notably X, Y, and angle.
     *
     * @param transform the robot's desired transformation. The X and Y
     *                  components of this transformation are fairly
     *                  self-explanatory. The "turn" component of this
     *                  transformation represents how fast the drivetrain
     *                  should turn.
     */
    void drive(RTransform transform);

    /**
     * Allow the drivetrain to be controlled by a user.
     *
     * <p>
     * User control needs to be enabled in order for the user to actually
     * control the robot manually, such as via a joystick. Although
     * Pathfinder is pretty lovely, sometimes you need to control the robot.
     * </p>
     *
     * <p>
     * If this isn't implemented properly, you may see very funky things going
     * on with the motors - spasms, for example. In order to counter this, it's
     * a good idea to... well, actually implement this method properly.
     * </p>
     */
    void enableUserControl();

    /**
     * Stop allowing the drivetrain to be controlled by a user.
     *
     * <p>
     * In order for Pathfinder to actually function, and in order for the
     * user to not miserably mess up absolutely everything, user control
     * needs to be disabled prior to controlling a motor.
     * </p>
     *
     * <p>
     * If this isn't implemented properly, you may see very funky things going
     * on with the motors - spasms, for example. In order to counter this, it's
     * a good idea to... well, actually implement this method properly.
     * </p>
     */
    void disableUserControl();
}
