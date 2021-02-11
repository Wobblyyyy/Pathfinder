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
