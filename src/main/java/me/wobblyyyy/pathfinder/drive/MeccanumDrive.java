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

package me.wobblyyyy.pathfinder.drive;

import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.MeccanumKinematics;
import me.wobblyyyy.pathfinder.kinematics.MeccanumState;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.robot.Motor;

public class MeccanumDrive implements Drive {
    public final Motor frontLeft;
    public final Motor frontRight;
    public final Motor backLeft;
    public final Motor backRight;

    public final Point flPos;
    public final Point frPos;
    public final Point blPos;
    public final Point brPos;

    public final MeccanumKinematics kinematics;

    public MeccanumDrive(Motor frontLeft,
                         Motor frontRight,
                         Motor backLeft,
                         Motor backRight,
                         double wheelBaseGapX,
                         double wheelBaseGapY) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;

        flPos = new Point(-wheelBaseGapX / 2, +wheelBaseGapY / 2);
        frPos = new Point(+wheelBaseGapX / 2, +wheelBaseGapY / 2);
        blPos = new Point(-wheelBaseGapX / 2, -wheelBaseGapY / 2);
        brPos = new Point(+wheelBaseGapX / 2, -wheelBaseGapY / 2);

        this.kinematics = new MeccanumKinematics(
                flPos,
                frPos,
                blPos,
                brPos
        );
    }

    private Motor fl() {
        return frontLeft;
    }

    private Motor fr() {
        return frontRight;
    }

    private Motor bl() {
        return backLeft;
    }

    private Motor br() {
        return backRight;
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
        MeccanumState state = kinematics.toMeccanumState(transform);

        state.normalizeFromMaxUnderOne();

        fl().setPower(state.flPower(), false);
        fr().setPower(state.frPower(), false);
        bl().setPower(state.blPower(), false);
        br().setPower(state.brPower(), false);
    }

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
    @Override
    public void enableUserControl() {
        frontLeft.enableUserControl();
        frontRight.enableUserControl();
        backLeft.enableUserControl();
        backRight.enableUserControl();
    }

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
    @Override
    public void disableUserControl() {
        frontLeft.disableUserControl();
        frontRight.disableUserControl();
        backLeft.disableUserControl();
        backRight.disableUserControl();
    }
}
