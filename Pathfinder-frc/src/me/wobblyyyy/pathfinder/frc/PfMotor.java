package me.wobblyyyy.pathfinder.frc;

import org.roxbotix.elibs2.motor.Motor;
import org.roxbotix.elibs2.motor.MotorConfig;

/**
 * An extension of elibs2's already-expansive Motor class, designed to be
 * compatible with Pathfinder. Quite lovely, isn't it?
 *
 * @author Colin Robertson
 */
public class PfMotor extends Motor
        implements me.wobblyyyy.pathfinder.robot.Motor {
    private boolean isUserControlled = true;

    /**
     * Create a new motor.
     *
     * @param motorConfig the motor's configuration.
     */
    public PfMotor(MotorConfig motorConfig) {
        super(motorConfig);
    }

    /**
     * Enable user control for this motor.
     */
    @Override
    public void enableUserControl() {
        isUserControlled = true;
    }

    /**
     * Disable user control for this motor.
     */
    @Override
    public void disableUserControl() {
        isUserControlled = false;
    }

    /**
     * Set power to the motor.
     *
     * @param power the power to set to the motor.
     */
    @Override
    public void setPower(double power) {
        setPower(power, true);
    }

    /**
     * Set power to the motor.
     *
     * @param power the power to set to the motor.
     * @param user  whether or not this power change is the result of a user
     *              or non-user. true means that a user made the change, while
     */
    @Override
    public void setPower(double power, boolean user) {
        if (!(isUserControlled && user)) super.set(power);
    }

    /**
     * Set power to the motor.
     *
     * @param power the power to set to the motor.
     */
    @Override
    public void set(double power) {
        setPower(power);
    }
}
