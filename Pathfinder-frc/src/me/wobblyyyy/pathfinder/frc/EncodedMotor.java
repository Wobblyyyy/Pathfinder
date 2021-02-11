package me.wobblyyyy.pathfinder.frc;

import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.robot.Motor;
import org.roxbotix.elibs2.motor.MotorConfig;

/**
 * A combination of both an encoder and a motor - how crazy!
 *
 * <p>
 * There's no practical advantage to choosing to use this rather than using
 * a separate motor and a separate encoder, but the option's here in case
 * you ever want to.
 * </p>
 *
 * @author Colin Robertson
 */
public class EncodedMotor implements Motor, Encoder {
    private PfMotor motor;
    private PfEncoder encoder;

    /**
     * Create a new encoder-enabled Pathfinder motor.
     *
     * <p>
     * You should certainly go read the documentation for both the PfMotor and
     * the PfEncoder class before continuing, as it might save you quite a bit
     * of confusion later down the line.
     * </p>
     *
     * @param motorConfig the motor's configuration.
     * @param channelA    the encoder's A channel.
     * @param channelB    the encoder's B channel.
     * @param inverted    is the encoder inverted?
     * @param cpr         the encoder's counts per rotation.
     */
    public EncodedMotor(MotorConfig motorConfig,
                        int channelA,
                        int channelB,
                        boolean inverted,
                        double cpr) {
        motor = new PfMotor(motorConfig);
        encoder = new PfEncoder(
                channelA,
                channelB,
                false,
                cpr
        );
    }

    /**
     * Get the encoder's count.
     *
     * @return the encoder's count.
     */
    @Override
    public int getCount() {
        return encoder.getCount();
    }

    /**
     * Get the encoder's counts per rotation.
     *
     * @return the encoder's counts per rotation.
     */
    @Override
    public double getCpr() {
        return encoder.getCpr();
    }

    /**
     * Allow the motor to be user-controlled.
     */
    @Override
    public void enableUserControl() {
        motor.enableUserControl();
    }

    /**
     * Stop allowing the motor to be user-controlled.
     */
    @Override
    public void disableUserControl() {
        motor.disableUserControl();
    }

    /**
     * Set power to the motor.
     *
     * @param power the power to set to the motor.
     */
    @Override
    public void setPower(double power) {
        motor.setPower(power);
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
        motor.setPower(power, user);
    }

    /**
     * Get the motor's power.
     *
     * @return the motor's power.
     */
    @Override
    public double getPower() {
        return motor.getPower();
    }
}
