package me.wobblyyyy.pathfinder.frc;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import me.wobblyyyy.pathfinder.robot.Motor;

/**
 * Pathfinder-enabled Talon.
 *
 * @author Colin Robertson
 */
public class PfTalon implements Motor {
    private boolean isUserControlled = true;
    private double power = 0.0;
    private final TalonSRX talon;

    /**
     * Create a new PfTalon.
     *
     * @param id the talon's ID.
     */
    public PfTalon(int id) {
        talon = new TalonSRX(id);
    }

    /**
     * Allow the device to be controlled by a user.
     */
    @Override
    public void enableUserControl() {
        isUserControlled = true;
    }

    /**
     * Stop allowing the device to be controlled by a user.
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
        if (!(isUserControlled && user)) {
            talon.set(ControlMode.PercentOutput, power);
        }
        this.power = power;
    }

    /**
     * Get the motor's power.
     *
     * @return the motor's power value.
     */
    @Override
    public double getPower() {
        return power;
    }
}
