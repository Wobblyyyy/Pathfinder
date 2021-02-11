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

package me.wobblyyyy.pathfinder.shapes;

import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.geometry.Rectangle;

/**
 * Shapes used for FRC fields.
 *
 * @author Colin Robertson
 */
public class FieldShapeFRC {
    /**
     * An empty field with dimensions 320x650 inches.
     */
    public static Rectangle EMPTY = new Rectangle(
            Rectangle.Corners.BACK_LEFT,
            Rectangle.Corners.CENTER,
            new Point(0, 0),
            320,
            650,
            0
    );
}
