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

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

/**
 * Interface used for standardizing odometry.
 *
 * @author Colin Robertson
 */
public interface Odometry {
    /**
     * Get the robot's position and heading.
     *
     * @return the robot's position and heading.
     */
    HeadingPoint getPos();

    /**
     * Update the odometry system.
     */
    void update();
}
