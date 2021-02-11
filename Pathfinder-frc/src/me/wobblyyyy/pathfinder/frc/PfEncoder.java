package me.wobblyyyy.pathfinder.frc;

import org.roxbotix.elibs2.robot.components.Encoder;

/**
 * Pathfinder-enabled encoders, designed to make your life easier than ever.
 *
 * <p>
 * If you don't know about how this bad boy works, you should go ahead and
 * check out the original encoder class' documentation.
 * </p>
 *
 * @author Colin Robertson
 */
public class PfEncoder extends Encoder
        implements me.wobblyyyy.pathfinder.robot.Encoder {
    private final double cpr;

    /**
     * Create a new Pathfinder-enabled encoder.
     *
     * @param channelA the encoder's A channel.
     * @param channelB the encoder's B channel.
     * @param inverted whether or not the encoder is inverted. This means that
     *                 the values the encoder sends are multiplied by -1: in
     *                 essence, if you're driving the robot forwards and the
     *                 encoder says you're going backwards, you might want to
     *                 mess around with this setting.
     * @param cpr      the encoder's CPR, or "counts per rotation."
     */
    public PfEncoder(int channelA,
                     int channelB,
                     boolean inverted,
                     double cpr) {
        super(
                channelA,
                channelB,
                inverted
        );

        this.cpr = cpr;
    }

    /**
     * Get the encoder's CPR.
     *
     * <p>
     * In case you're entirely illiterate, CPR stands for "counts per
     * rotation." Super cool, I know.
     * </p>
     *
     * @return the encoder's CPR.
     */
    @Override
    public double getCpr() {
        return cpr;
    }
}
