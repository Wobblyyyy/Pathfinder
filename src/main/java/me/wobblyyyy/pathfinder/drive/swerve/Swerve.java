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

package me.wobblyyyy.pathfinder.drive.swerve;

import me.wobblyyyy.intra.ftc2.utils.math.Comparator;
import me.wobblyyyy.intra.ftc2.utils.math.Math;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.robot.Motor;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * A beautiful class used for operating a swerve drivetrain.
 *
 * <p>
 * Moving and turning is certainly a topic that needs to get some further
 * research into in the future. Having to stop entirely for every minor wheel
 * adjustment would be incredibly slow. Although it would, in fact, provide a
 * significantly higher degree of accuracy in linear paths, trajectories, which
 * is where speed optimization really happens in the first place, would
 * struggle a fair bit.
 * </p>
 *
 * <p>
 * As with a couple of the other files in this project, this file is absolutely
 * gigantic. There's little reason for it's size other than my self-enforced
 * requirement to document absolutely everything, so I do apologize.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @since 0.1.0
 */
public class Swerve implements Drive {
    /**
     * Front-right drive motor.
     */
    private final Motor fr_drive;

    /**
     * Front-left drive motor.
     */
    private final Motor fl_drive;

    /**
     * Back-right drive motor.
     */
    private final Motor br_drive;

    /**
     * Back-left drive motor.
     */
    private final Motor bl_drive;

    /**
     * Front-right turn motor.
     */
    private final Motor fr_turn;

    /**
     * Front-left turn motor.
     */
    private final Motor fl_turn;

    /**
     * Back-right turn motor.
     */
    private final Motor br_turn;

    /**
     * Back-left turn motor.
     */
    private final Motor bl_turn;

    /**
     * Front-right drive encoder.
     */
    private final Encoder fr_drive_enc;

    /**
     * Front-left drive encoder.
     */
    private final Encoder fl_drive_enc;

    /**
     * Back-right drive encoder.
     */
    private final Encoder br_drive_enc;

    /**
     * Back-left drive encoder.
     */
    private final Encoder bl_drive_enc;

    /**
     * Front-right turn encoder.
     */
    private final Encoder fr_turn_enc;

    /**
     * Front-left turn encoder.
     */
    private final Encoder fl_turn_enc;

    /**
     * Back-right turn encoder.
     */
    private final Encoder br_turn_enc;

    /**
     * Back-left turn encoder.
     */
    private final Encoder bl_turn_enc;

    /**
     * Ahh... a good old comparator. I didn't think I'd be getting much
     * use out of one of these any time soon, but you never know.
     */
    private final Comparator ac;

    /**
     * Calculator used in determining swerve module power based on current
     * and target positions. The values behind this PID controller were
     * determined experimentally while I was working on my high school team's
     * swerve drive chassis. This is only the default turn calculator - it
     * can be customized however you like.
     */
    private final SwerveTurn turnCalculator = SwerveModulePid::calculate;

    /**
     * Create a new swerve drivetrain.
     *
     * <p>
     * Although yes, there are a ton of parameters to pass here - it's really
     * not that difficult. You just need to make sure that you're passing the
     * right parameters in the right places, otherwise, you'll end up with
     * a poorly or not at all functioning drivetrain, which, obviously, is
     * never a good thing to have.
     * </p>
     *
     * <p>
     * Little else, save for the setting that's done here, is accomplished
     * by this constructor. The initialization and other management of
     * the motors and encoders that are passed to this method should
     * be handled elsewhere, prior to them getting to this method.
     * </p>
     *
     * @param fr_drive     the front-right drive motor.
     * @param fl_drive     the front-left drive motor.
     * @param br_drive     the back-right drive motor.
     * @param bl_drive     the back-left drive motor.
     * @param fr_turn      the front-right turn motor.
     * @param fl_turn      the front-left turn motor.
     * @param br_turn      the back-right turn motor.
     * @param bl_turn      the back-left turn motor.
     * @param fr_drive_enc the front-right drive encoder.
     * @param fl_drive_enc the front-left drive encoder.
     * @param br_drive_enc the back-right drive encoder.
     * @param bl_drive_enc the back-left drive encoder.
     * @param fr_turn_enc  the front-right turn encoder.
     * @param fl_turn_enc  the front-left turn encoder.
     * @param br_turn_enc  the back-right turn encoder.
     * @param bl_turn_enc  the back-left turn encoder.
     */
    public Swerve(Motor fr_drive,
                  Motor fl_drive,
                  Motor br_drive,
                  Motor bl_drive,
                  Motor fr_turn,
                  Motor fl_turn,
                  Motor br_turn,
                  Motor bl_turn,
                  Encoder fr_drive_enc,
                  Encoder fl_drive_enc,
                  Encoder br_drive_enc,
                  Encoder bl_drive_enc,
                  Encoder fr_turn_enc,
                  Encoder fl_turn_enc,
                  Encoder br_turn_enc,
                  Encoder bl_turn_enc) {
        /*
         * Set all of the variables that we've received through the
         * constructor.
         *
         * Little else, save for the setting that's done here, is accomplished
         * by this constructor. The initialization and other management of
         * the motors and encoders that are passed to this method should
         * be handled elsewhere, prior to them getting to this method.
         */

        this.fr_drive = fr_drive;
        this.fl_drive = fl_drive;
        this.br_drive = br_drive;
        this.bl_drive = bl_drive;
        this.fr_turn = fr_turn;
        this.fl_turn = fl_turn;
        this.br_turn = br_turn;
        this.bl_turn = bl_turn;
        this.fr_drive_enc = fr_drive_enc;
        this.fl_drive_enc = fl_drive_enc;
        this.br_drive_enc = br_drive_enc;
        this.bl_drive_enc = bl_drive_enc;
        this.fr_turn_enc = fr_turn_enc;
        this.fl_turn_enc = fl_turn_enc;
        this.br_turn_enc = br_turn_enc;
        this.bl_turn_enc = bl_turn_enc;

        /*
         * Create a new Comparator, used for determining whether or not
         * the current angle and the target angle are similar or the same.
         */
        ac = new Comparator(3);
    }

    /**
     * Get the front-right drive motor.
     *
     * @return the front-right drive motor.
     */
    public Motor getFr_drive() {
        return fr_drive;
    }

    /**
     * Get the front-left drive motor.
     *
     * @return the front-left drive motor.
     */
    public Motor getFl_drive() {
        return fl_drive;
    }

    /**
     * Get the back-right drive motor.
     *
     * @return the back-right drive motor.
     */
    public Motor getBr_drive() {
        return br_drive;
    }

    /**
     * Get the back-left drive motor.
     *
     * @return the back-left drive motor.
     */
    public Motor getBl_drive() {
        return bl_drive;
    }

    /**
     * Get the front-right turn motor.
     *
     * @return the front-right turn motor.
     */
    public Motor getFr_turn() {
        return fr_turn;
    }

    /**
     * Get the front-left turn motor.
     *
     * @return the front-left turn motor.
     */
    public Motor getFl_turn() {
        return fl_turn;
    }

    /**
     * Get the back-right turn motor.
     *
     * @return the back-right turn motor.
     */
    public Motor getBr_turn() {
        return br_turn;
    }

    /**
     * Get the back-left turn motor.
     *
     * @return the back-left turn motor.
     */
    public Motor getBl_turn() {
        return bl_turn;
    }

    /**
     * Get the front-right drive encoder.
     *
     * @return the front-right drive encoder.
     */
    public Encoder getFr_drive_enc() {
        return fr_drive_enc;
    }

    /**
     * Get the front-left drive encoder.
     *
     * @return the front-left drive encoder.
     */
    public Encoder getFl_drive_enc() {
        return fl_drive_enc;
    }

    /**
     * Get the back-right drive encoder.
     *
     * @return the back-right drive encoder.
     */
    public Encoder getBr_drive_enc() {
        return br_drive_enc;
    }

    /**
     * Get the back-left drive encoder.
     *
     * @return the back-left drive encoder.
     */
    public Encoder getBl_drive_enc() {
        return bl_drive_enc;
    }

    /**
     * Get the front-right turn encoder.
     *
     * @return the front-right turn encoder.
     */
    public Encoder getFr_turn_enc() {
        return fr_turn_enc;
    }

    /**
     * Get the front-left turn encoder.
     *
     * @return the front-left turn encoder.
     */
    public Encoder getFl_turn_enc() {
        return fl_turn_enc;
    }

    /**
     * Get the back-right turn encoder.
     *
     * @return the back-right turn encoder.
     */
    public Encoder getBr_turn_enc() {
        return br_turn_enc;
    }

    /**
     * Get the back-left turn encoder.
     *
     * @return the back-left turn encoder.
     */
    public Encoder getBl_turn_enc() {
        return bl_turn_enc;
    }

    /**
     * Turn to a given angle.
     *
     * <p>
     * This turning method is slow and doesn't allow for user configuration.
     * This should be a target for upgrades / revamps in the near future.
     * </p>
     *
     * @param target  the target angle.
     * @param encoder the motor's encoder.
     * @param motor   the motor itself.
     */
    public void turnTo(double target,
                       Encoder encoder,
                       Motor motor) {
        /*
         * Current is the motor's current angle.
         *
         * Target is obviously the target angle.
         */
        double current = getDeg(encoder);

        /*
         * Hand off our current information to the swerve module turn calculator
         * defined earlier in this class.
         */
        double power = turnCalculator.calculate(current, target);

        /*
         * Finally set power to the motor. Epic sauce.
         */
        motor.setPower(power);
    }

    /**
     * Get the angle of a wheel.
     *
     * @param encoder the wheel's encoder.
     * @return the wheel's angle.
     */
    public double getDeg(Encoder encoder) {
        /*
         * The encoder's count.
         */
        double count = encoder.getCount();

        /*
         * The encoder's CPR.
         */
        double cpr = encoder.getCpr();

        /*
         * Get the encoder's degrees per tick.
         */
        double dpt = 360 / cpr;

        /*
         * Get the degree count of the encoder.
         */
        double deg = dpt * count;

        /*
         * Normalize the values generated by the encoder's count.
         *
         * This can get expensive as the encoder counts get higher and
         * higher. To normalize this, we can try to set the offset of the
         * encoder as frequently as possible.
         */

        while (deg > 360) deg -= 360;
        while (deg < 0) deg += 360;

        /*
         * Return the degrees measurement of the encoder.
         */
        return deg;
    }

    /**
     * Are the wheels aligned properly?
     *
     * @param target the target angle.
     * @return whether or not the wheels are aligned closely enough.
     */
    public boolean isNearTarget(double target) {
        /*
         * Compare all four of the motors individually.
         *
         * If any of the motors ARE NOT at the target angle, we need to adjust
         * in order to make sure that they are.
         */

        boolean fr = ac.compare(getDeg(this.fr_turn_enc), target);
        boolean fl = ac.compare(getDeg(this.fl_turn_enc), target);
        boolean br = ac.compare(getDeg(this.br_turn_enc), target);
        boolean bl = ac.compare(getDeg(this.bl_turn_enc), target);

        /*
         * Check to see whether or not all of the values are close to their
         * targets.
         */
        return fr && fl && br && bl;
    }

    /**
     * Check if the wheels are aligned, and then turn them.
     */
    public void turnModules(double target) {
        /*
         * If we're not near the target, we need to turn to it.
         *
         * It's shocking, it's crazy. I know.
         */
        if (!isNearTarget(target)) {
            turnTo(target, fr_turn_enc, fr_turn);
            turnTo(target, fl_turn_enc, fl_turn);
            turnTo(target, br_turn_enc, br_turn);
            turnTo(target, bl_turn_enc, bl_turn);
        }
    }

    /**
     * Set power to motors based on positions. This chassis will entirely stop
     * all movement until the modules are at their target angle. This is done
     * to ensure precision. In most situations, this won't impact you at all.
     * Unless the trajectory you're following is more a zig-zag than a spline,
     * waiting for the system to be at the correct position shouldn't harm
     * anything TOO much.
     *
     * <p>
     * If the turn wheels are properly aligned, we can just go straight to
     * powering the chassis. Otherwise, we have to turn the wheels first.
     * </p>
     *
     * @param start the start point.
     * @param end   the end point.
     * @param power the power the drivetrain should operate at.
     */
    @Override
    public void drive(HeadingPoint start,
                      HeadingPoint end,
                      double power) {
        double angle = HeadingPoint.angleOfDeg(start, end);

        if (isNearTarget(angle)) {
            fr_drive.setPower(power, false);
            fl_drive.setPower(power, false);
            br_drive.setPower(power, false);
            bl_drive.setPower(power, false);
        } else {
            fr_drive.setPower(0, false);
            fl_drive.setPower(0, false);
            br_drive.setPower(0, false);
            bl_drive.setPower(0, false);
            turnModules(angle);
        }
    }

    /**
     * Operate the drivetrain. Woot-woot!
     *
     * @param power the power at which the motors should operate.
     * @param angle the angle at which the motors should point.
     */
    @Override
    public void drive(double power,
                      double angle) {
        /*
         * o = origin point.
         *
         * Its goal is to provide a basic point that we have to work with
         * for angle measurement, etc.
         */
        HeadingPoint o = new HeadingPoint(0, 0, 0);

        /*
         * Create a point in the direction that we intend to move.
         *
         * This point is, like the last one, used for angle measurement.
         */
        HeadingPoint t = (HeadingPoint) Distance.inDirection(o, angle, power);

        /*
         * Drive the drivetrain towards a certain direction.
         */
        drive(o, t, power);
    }

    /**
     * Drive a single module based on angle and power.
     */
    public void driveModule(Modules module,
                            double angle,
                            double power) {
        Encoder e;
        Motor m;
        Motor d;

        switch (module) {
            default:
            case FR:
                e = getFr_turn_enc();
                m = getFr_turn();
                d = getFr_drive();
                break;
            case FL:
                e = getFl_turn_enc();
                m = getFl_turn();
                d = getFl_drive();
                break;
            case BR:
                e = getBr_turn_enc();
                m = getBr_turn();
                d = getBr_drive();
                break;
            case BL:
                e = getBl_turn_enc();
                m = getBl_turn();
                d = getBl_drive();
                break;
        }

        turnTo(
                angle,
                e,
                m
        );

        d.setPower(power);
    }

    /**
     * Allow the drivetrain to be user-controlled.
     */
    @Override
    public void enableUserControl() {
        /*
         * Enable user control for all of the drive motors.
         */
        fr_drive.enableUserControl();
        fl_drive.enableUserControl();
        br_drive.enableUserControl();
        bl_drive.enableUserControl();

        /*
         * Enable user control for all of the turn motors.
         */
        fr_turn.enableUserControl();
        fl_turn.enableUserControl();
        br_turn.enableUserControl();
        bl_turn.enableUserControl();
    }

    /**
     * Give the user a giant "frick you" and no longer allow them to control
     * the drivetrain.
     */
    @Override
    public void disableUserControl() {
        /*
         * Disable user control for all of the drive motors.
         */
        fr_drive.disableUserControl();
        fl_drive.disableUserControl();
        br_drive.disableUserControl();
        bl_drive.disableUserControl();

        /*
         * Disable user control for all of the turn motors.
         */
        fr_turn.disableUserControl();
        fl_turn.disableUserControl();
        br_turn.disableUserControl();
        bl_turn.disableUserControl();
    }

    /**
     * The swerve drivetrain's different modules.
     */
    public enum Modules {
        /**
         * Front-right.
         */
        FR,

        /**
         * Front-left.
         */
        FL,

        /**
         * Back-right.
         */
        BR,

        /**
         * Back-left.
         */
        BL
    }
}
