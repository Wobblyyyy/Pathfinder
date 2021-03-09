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
 * A collection of different types of path followers.
 *
 * <p>
 * The pros and cons for each type of follower are listed in each of the
 * follower's respective JavaDocs.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public enum Followers {
    /**
     * Basic proportional integral derivative follower.
     *
     * <p>
     * PROS:
     * <ul>
     *     <li>Fast generation, PID doesn't require much math.</li>
     *     <li>(Usually) accurate following.</li>
     *     <li>Linear paths are easy.</li>
     * </ul>
     * </p>
     *
     * <p>
     * CONS:
     * <ul>
     *     <li>Non-linear or multi-stage paths are very slow.</li>
     *     <li>Potential inaccuracy based on the robot's profile.</li>
     * </ul>
     * </p>
     */
    PID,

    /**
     * Linear follower without proportion.
     *
     * <p>
     * PROS:
     * <ul>
     *     <li>Can't go wrong. Unless you go too fast.</li>
     *     <li>Very fast generation - can be run whenever.</li>
     * </ul>
     * </p>
     *
     * <p>
     * CONS:
     * <ul>
     *     <li>Very slow and not at all optimized.</li>
     *     <li>Can over-shoot targets pretty easily.</li>
     * </ul>
     * </p>
     */
    LINEAR,

    /**
     * Linear follower with distance proportion speed.
     *
     * <p>
     * PROS:
     * <ul>
     *     <li>Stable and reliable.</li>
     *     <li>Immediate generation.</li>
     * </ul>
     * </p>
     *
     * <p>
     * CONS:
     * <ul>
     *     <li>Not fully optimized.</li>
     *     <li>Can follow inaccurately.</li>
     *     <li>Proportional curves can be difficult to tweak.</li>
     * </ul>
     * </p>
     */
    PROPORTIONAL,

    /**
     * Use JaciBrunning's Pathfinder library to generate a swerve
     * trajectory and follow it.
     *
     * <p>
     * PROS:
     * <ul>
     *     <li>Incredibly efficient path generation.</li>
     *     <li>Fantastic support for multi-stage paths.</li>
     *     <li>Very, very, very fast.</li>
     *     <li>Designed specifically for swerve drivetrains.</li>
     * </ul>
     * </p>
     *
     * <p>
     * CONS:
     * <ul>
     *     <li>Path generation is VERY expensive.</li>
     *     <li>Trajectories are also very expensive, and you need 5.</li>
     * </ul>
     * </p>
     *
     * <p>
     * Pathfinder can detect potential collisions that might occur based on
     * the generated trajectory, however, doing so can be slow.
     * </p>
     */
    SWERVE,

    /**
     * A follower based on an X and Y PID controller. These controllers
     * attempt to get the X and Y positions of the robot to the target
     * positions. This type of controller moves at a constant speed that is
     * set upon the follower's construction.
     */
    DUAL_PID,

    /**
     * A follower based on an X, Y, and SPEED PID controller. This type of
     * follower is an extension of the {@link #DUAL_PID} follower and seeks
     * to optimize precise movement by using an additional PID controller to
     * control the speed of the robot.
     */
    TRI_PID
}
