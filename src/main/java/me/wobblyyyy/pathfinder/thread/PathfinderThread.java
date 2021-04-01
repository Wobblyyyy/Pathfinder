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

package me.wobblyyyy.pathfinder.thread;

import me.wobblyyyy.pathfinder.robot.Odometry;

import java.util.ArrayList;

/**
 * Thread used in updating pathfinder values and controlling motors.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class PathfinderThread implements Runnable {
    private boolean shouldExecute = true;
    private final ArrayList<Odometry> odometryArrayList;
    private final Thread thread = new Thread(this::repeatRun);

    public void repeatRun() {
        while (shouldExecute) {
            run();
        }
    }

    @Override
    public void run() {
        for (Odometry odometry : odometryArrayList) {
            odometry.update();
        }
    }

    public void start() {
        shouldExecute = true;
        thread.start();
    }

    public void stop() {
        shouldExecute = false;
    }

    public PathfinderThread(ArrayList<Odometry> odometryArrayList) {
        this.odometryArrayList = odometryArrayList;
    }

    public void close() {
        shouldExecute = false;
    }
}
