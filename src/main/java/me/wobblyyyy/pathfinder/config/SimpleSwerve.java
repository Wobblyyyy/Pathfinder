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

package me.wobblyyyy.pathfinder.config;

import me.wobblyyyy.pathfinder.core.Followers;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.map.Map;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.RobotProfile;

/**
 * A simple pathfinder configuration that relies on a lot less input
 * parameters.
 *
 * <p>
 * It's worth noting that the more abstract a configuration is, the less control
 * you have over how Pathfinder functions. If you find that you don't feel as
 * if you have enough control over Pathfinder as-is, you should check out
 * lower-level configurations.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class SimpleSwerve extends PathfinderConfig {
    private static final int SPECIFICITY = 2;
    private static final boolean LIGHTNING = true;
    private static final boolean FAST = true;
    private static final boolean THETA = true;
    private static final Followers FOLLOWER = Followers.SWERVE;

    /**
     * Create a new PathfinderConfig to be fed to a Pathfinder.
     *
     * <p>
     * For your own sake - please go read the field descriptions in this Java
     * file if you're confused about what any of these means. I've written out
     * nice and long descriptions for everything, so there shouldn't be all that
     * much confusion about what's going on.
     * </p>
     *
     * @param odometry      the odometry subsystem that's used by the pathfinder
     *                      in determining the robot's position. This odometry
     *                      system should be as accurate as possible and maintain
     *                      contact with the ground at all times.
     * @param fieldWidth    the fieldWidth of the field, in whatever units you'd like.
     *                      Although the units don't matter much, you need to be
     *                      sure to keep the units consistent.
     * @param fieldHeight   the fieldHeight of the field, in whatever units you'd like.
     *                      Although the units don't matter much, you need to be
     *                      sure to keep the units consistent.
     * @param robotX        the robot's X dimension. X/Y are the same thing here.
     *                      That's not to say that you shouldn't measure X and Y
     *                      values - accuracy is still just as important as ever.
     * @param robotY        the robot's Y dimension. X/Y are the same thing here.
     *                      That's not to say that you shouldn't measure X and Y
     *                      values - accuracy is still just as important as ever.
     * @param gapX          the distance (in inches) between the pair of front
     *                      right and front left wheel centers.
     * @param gapY          the distance (in inches) between the pair of front
     *                      right and back right wheel centers.
     * @param profile       the robot's motion profiling profile. This profile
     *                      should provide (at least somewhat accurate) info
     *                      on the robot's motion in real life.
     * @param drive         the robot's drivetrain. The drivetrain is (rather
     *                      obviously) used to actually drive the robot.
     * @param map           a virtual map of something. In most cases, this is
     *                      a game field with all your different obstacles and
     *                      what not.
     */
    public SimpleSwerve(Odometry odometry,
                        int fieldWidth,
                        int fieldHeight,
                        double robotX,
                        double robotY,
                        double gapX,
                        double gapY,
                        RobotProfile profile,
                        Drive drive,
                        Map map) {
        super(
                odometry,
                fieldWidth,
                fieldHeight,
                SPECIFICITY,
                robotX,
                robotY,
                gapX,
                gapY,
                profile,
                drive,
                map,
                FOLLOWER,
                0.5,
                LIGHTNING,
                FAST,
                THETA
        );
    }
}
