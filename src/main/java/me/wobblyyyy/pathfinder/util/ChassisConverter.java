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

package me.wobblyyyy.pathfinder.util;

import me.wobblyyyy.pathfinder.drive.swerve.Swerve;
import me.wobblyyyy.pathfinder.tracking.swerve.SwerveChassisTracker;

/**
 * Convert between drivetrains and trackers.
 *
 * <p>
 * The process of manually going through and converting dozens of different
 * encoders and all of that nonsense is painstaking and boring. Instead of
 * doing that, why not just use one of these VERY COOL and VERY AWESOME
 * methods? I know you're impressed. I just know it.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class ChassisConverter {
    /**
     * Get a brand-new Swerve tracker, based on a Swerve drivetrain.
     *
     * @param swerve   the original swerve drivetrain.
     * @param diameter the diameter of the wheels.
     * @param gapX     see: {@link SwerveChassisTracker#gapX}
     * @param gapY     see: {@link SwerveChassisTracker#gapY}
     * @return a new {@link SwerveChassisTracker}
     */
    public static SwerveChassisTracker getSwerveTracker(Swerve swerve,
                                                        double diameter,
                                                        double gapX,
                                                        double gapY) {
        return new SwerveChassisTracker(
                swerve.getFr_turn_enc(),
                swerve.getFr_drive_enc(),
                swerve.getFl_turn_enc(),
                swerve.getFl_drive_enc(),
                swerve.getBr_turn_enc(),
                swerve.getBr_drive_enc(),
                swerve.getBl_turn_enc(),
                swerve.getBl_drive_enc(),
                diameter,
                gapX,
                gapY
        );
    }
}
