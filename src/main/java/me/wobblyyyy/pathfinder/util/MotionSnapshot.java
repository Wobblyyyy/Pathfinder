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

package me.wobblyyyy.pathfinder.util;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

/**
 * A picture - a "snapshot," if you will, of the robot's projected motion.
 *
 * @author Colin Robertson
 */
public class MotionSnapshot {
    private final HeadingPoint start;
    private final HeadingPoint end;
    private final double power;

    /**
     * Create a new MotionSnapshot.
     *
     * @param start the start position.
     * @param end   the end position.
     * @param power the power at which the drivetrain should operate.
     */
    public MotionSnapshot(HeadingPoint start,
                          HeadingPoint end,
                          double power) {
        this.start = start;
        this.end = end;
        this.power = power;
    }

    /**
     * Get the start position.
     *
     * @return the start position.
     */
    public HeadingPoint getStart() {
        return start;
    }

    /**
     * Get the end position.
     *
     * @return the end position.
     */
    public HeadingPoint getEnd() {
        return end;
    }

    /**
     * Get the power value.
     *
     * @return the power value.
     */
    public double getPower() {
        return power;
    }
}
