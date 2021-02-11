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

package me.wobblyyyy.pathfinder.robot;

/**
 * Simple interface used for communicating with encoders.
 *
 * @author Colin Robertson
 */
public interface Encoder {
    /**
     * Get the encoder's current count.
     *
     * @return the encoder's count.
     */
    int getCount();

    /**
     * Get the encoder's CPR (counts per rotation).
     *
     * @return the encoder's counts per rotation.
     */
    double getCpr();
}
