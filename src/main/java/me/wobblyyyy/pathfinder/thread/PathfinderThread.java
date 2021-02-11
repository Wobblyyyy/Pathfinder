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

package me.wobblyyyy.pathfinder.thread;

import me.wobblyyyy.pathfinder.robot.Odometry;

import java.util.ArrayList;

/**
 * Thread used in updating pathfinder values and controlling motors.
 *
 * @author Colin Robertson
 */
public class PathfinderThread extends Thread {
    private final ArrayList<Odometry> odometryArrayList;

    @Override
    public void run() {
        for (Odometry odometry : odometryArrayList) {
            odometry.update();
        }
    }

    public PathfinderThread(ArrayList<Odometry> odometryArrayList) {
        this.odometryArrayList = odometryArrayList;
    }
}
