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
import java.util.Collections;

/**
 * Wrapper class used to manage threads.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class PathfinderThreadManager {
    /**
     * The pathfinder thread that's used here.
     */
    PathfinderThread thread;

    public PathfinderThreadManager(Odometry odometry) {
        this(new ArrayList<>(Collections.singletonList(odometry)));
    }

    /**
     * Create a new pathfinder thread manager.
     *
     * @param odometryArrayList a list of all of the robot's odometry systems.
     */
    public PathfinderThreadManager(ArrayList<Odometry> odometryArrayList) {
        thread = new PathfinderThread(odometryArrayList);
    }

    /**
     * Start the pathfinder thread.
     */
    public void start() {
        thread.start();
    }

    /**
     * Stop the pathfinder thread.
     */
    @SuppressWarnings("deprecation")
    public void stop() {
        thread.stop();
    }

    /**
     * Gently shut down the pathfinder thread.
     */
    public void close() {
        thread.close();
    }
}
