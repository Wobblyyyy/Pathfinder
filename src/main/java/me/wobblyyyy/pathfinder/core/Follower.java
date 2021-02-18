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

package me.wobblyyyy.pathfinder.core;

/**
 * Interface used for different types of followers.
 *
 * <p>
 * Followers are one of the lowest-levels of pathfinding utilities - given a
 * target position and a start position, determine how to control the robot
 * so that it optimally reaches its target position.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Follower {
    /**
     * Update the follower's drive values.
     */
    void update();

    /**
     * Calculate a list of all the instructions needed to follow the path.
     *
     * <p>
     * This should (needs to, really) be run before the robot makes any sort
     * of movement in any direction - otherwise, you're defeating the whole
     * purpose of having a pre-planned route.
     * </p>
     */
    void calculate();

    /**
     * Drive the robot itself.
     *
     * <p>
     * The drive method should almost always call another drive method, a
     * drive method that's contained in a Drive class, actually. Driving the
     * robot within the follower is a shitty idea, and you shouldn't do it.
     * </p>
     */
    void drive();

    /**
     * Whether or not the follower has finished its execution.
     *
     * @return whether or not the follower has finished its execution.
     */
    boolean isDone();
}
