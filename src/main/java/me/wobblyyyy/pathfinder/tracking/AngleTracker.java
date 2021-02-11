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
 *  ======================================================================
 */

package me.wobblyyyy.pathfinder.tracking;

import me.wobblyyyy.pathfinder.robot.Encoder;

/**
 * Track an angle based on an encoder.
 *
 * <p>
 * Angle tracking is actually incredibly simple - use the encoder's CPR and the
 * encoder's count to determine how many ticks equals how many degrees.
 * </p>
 *
 * @author Colin Robertson
 */
public class AngleTracker {
    /**
     * A reference to the encoder that's used in tracking.
     */
    private final Encoder encoder;

    /**
     * The encoder's CPR.
     */
    private final double cpr;

    /**
     * TPD = Ticks per Degrees.
     */
    private final double tpd;

    /**
     * The current angle.
     */
    private double angle;

    /**
     * Create a new angle tracker.
     *
     * @param encoder the encoder to be used.
     */
    public AngleTracker(Encoder encoder) {
        this.encoder = encoder;
        this.cpr = encoder.getCpr();
        this.tpd = this.cpr / 360;
    }

    /**
     * Update the tracker based on the encoder's count.
     */
    public void update() {
        angle = encoder.getCount() * tpd;

        while (angle > 360) angle -= 360;
        while (angle <  0 ) angle += 360;
    }

    /**
     * Get angle.
     *
     * <p>
     * Hit da mf Nae-Nae? Hell to the yeah.
     * </p>
     *
     * @return angle.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Get the encoder used in the tracker.
     *
     * @return the tracker's encoder.
     */
    public Encoder getEncoder() {
        return encoder;
    }
}
