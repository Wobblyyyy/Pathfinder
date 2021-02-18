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

package me.wobblyyyy.pathfinder.robot;

/**
 * Interface used for interacting with gyroscopes.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Gyroscope {
    /**
     * Gets the angle at which the gyroscope is pointing.
     *
     * <p>
     * This angle should be in DEGREES, not RADIANS, otherwise some pathfinding
     * math (or more specifically, drivetrain math) may be slightly messed up.
     * Or significantly messed up, I'm not really sure. In addition to
     * returning an angle that's notated in degrees, the angle should
     * (preferably) be within the range of (0) to (360).
     * </p>
     *
     * @return the gyroscope's angle.
     */
    double getAngle();
}
