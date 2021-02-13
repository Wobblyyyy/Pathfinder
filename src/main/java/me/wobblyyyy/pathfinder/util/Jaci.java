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

import jaci.pathfinder.Waypoint;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Utility methods used in implementing Jaci's pathfinder.
 *
 * @author Colin Robertson
 */
public class Jaci {
    /**
     * Get a waypoint based on a point.
     *
     * <p>
     * Remember, Pathfinder's measurements are in INCHES, and Jaci's
     * measurements are in (a) meters and (b) radians.
     * </p>
     *
     * @param p the input point.
     * @return the output waypoint.
     */
    public static Waypoint getWaypoint(Point p) {
        if (p instanceof HeadingPoint) return new Waypoint(
                Distance.inchesToMeters(p.getX()),
                Distance.inchesToMeters(p.getY()),
                Math.toRadians(((HeadingPoint) p).getHeading())
        );
        else return new Waypoint(
                Distance.inchesToMeters(p.getX()),
                Distance.inchesToMeters(p.getY()),
                0
        );
    }

    /**
     * Get a waypoint based on a point and an angle.
     *
     * <p>
     * Remember, Pathfinder's measurements are in INCHES, and Jaci's
     * measurements are in (a) meters and (b) radians.
     * </p>
     *
     * @param p the original point.
     * @param a the angle to use.
     * @return output waypoint.
     */
    public static Waypoint getWaypoint(Point p,
                                       double a) {
        return getWaypoint(
                HeadingPoint.withNewHeading(
                        Point.scale(p, Distance.INCHES_TO_METERS),
                        Math.toRadians(a)
                )
        );
    }
}
