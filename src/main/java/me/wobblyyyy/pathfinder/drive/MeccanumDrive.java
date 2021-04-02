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

/**
 * An implementation of the meccanum holonomic drivetrain. If you're looking
 * at this class, chances are you using a meccanum drivetrain already and
 * want to have an easier time implementing one. If you don't know what
 * a meccanum drivetrain is, but you want to know oh-so-badly, you can just
 * Google it. Yeah. That's all.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class MeccanumDrive implements Drive {
    /**
     * The drivetrain's front-left motor (FL)
     */
    public final Motor frontLeft;

    /**
     * The drivetrain's front-right motor (FR)
     */
    public final Motor frontRight;

    /**
     * The drivetrain's back-left motor (BL)
     */
    public final Motor backLeft;

    /**
     * The drivetrain's back-right motor (BR)
     */
    public final Motor backRight;

    /**
     * The position of the FL wheel.
     */
    public final Point flPos;

    /**
     * The position of the FR wheel.
     */
    public final Point frPos;

    /**
     * The position of the BL wheel.
     */
    public final Point blPos;

    /**
     * The position of the BR wheel.
     */
    public final Point brPos;

    /**
     * The drivetrain's internal kinematics class.
     *
     * @see MeccanumKinematics
     */
    public final MeccanumKinematics kinematics;

    /**
     * Create a new {@code MeccanumDrive} by using four inputted motors and
     * two parameters specifying the gap in between the wheels.
     *
     * <p>
     * This constructor will also set the {@code MeccanumDrive}'s four
     * positional points based on the provided doubles.
     * <ul>
     *     <li>{@link #flPos} is (-x/2, +y/2)</li>
     *     <li>{@link #frPos} is (+x/2, +y/2)</li>
     *     <li>{@link #blPos} is (-x/2, -y/2</li>
     *     <li>{@link #frPos} is (+x/2, -y/2</li>
     * </ul>
     * </p>
     *
     * @param frontLeft     the front-left (FL) motor.
     * @param frontRight    the front-right (FR) motor.
     * @param backLeft      the back-left (BL) motor.
     * @param backRight     the back-right (BR) motor.
     * @param wheelBaseGapX the horizontal gap between the right side and
     *                      the left side of the drivetrain's wheels.
     * @param wheelBaseGapY the vertical gap between the front and back pairs
     *                      of wheels of the drivetrain.
     */
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

        /*
         * Determine the position of each of the wheels. This is quite simple,
         * really. Figure this:
         *
         * FL is quad 2 : negative X, positive Y
         * FR is quad 1 : positive X, positive Y
         * BL is quad 3 : negative X, negative Y
         * BR is quad 4 : positive X, negative Y
         */
        flPos = new Point(-wheelBaseGapX / 2, +wheelBaseGapY / 2);
        frPos = new Point(+wheelBaseGapX / 2, +wheelBaseGapY / 2);
        blPos = new Point(-wheelBaseGapX / 2, -wheelBaseGapY / 2);
        brPos = new Point(+wheelBaseGapX / 2, -wheelBaseGapY / 2);

        // Create a new kinematics instance from the provided points.
        // Order is always FL, FR, BL, BR.
        this.kinematics = new MeccanumKinematics(
                flPos,
                frPos,
                blPos,
                brPos
        );
    }

    /**
     * Internal method to access one of the motors.
     *
     * @return the front-left motor.
     */
    private Motor fl() {
        return frontLeft;
    }

    /**
     * Internal method to access one of the motors.
     *
     * @return the front-right motor.
     */
    private Motor fr() {
        return frontRight;
    }

    /**
     * Internal method to access one of the motors.
     *
     * @return the back-left motor.
     */
    private Motor bl() {
        return backLeft;
    }

    /**
     * Internal method to access one of the motors.
     *
     * @return the back-right motor.
     */
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
        // Use the drivetrain's kinematics to convert between a desired
        // transformation and a valid meccanum drive state.
        MeccanumState state = kinematics.toMeccanumState(transform);

        // Normalize the power of each of the module states. If the power
        // values of any of the modules exceeds 1, all of the module states
        // will be proportionally scaled down as to preserve movement
        // direction and force.
        state.normalizeFromMaxUnderOne();

        // Set the normalized states to each of the motors.
        // In this case, we assume that the user is not controlling the robot.
        // If they are, sucks for them. That reminds me, actually:
        // TODO implement user and non-user meccanum control
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
