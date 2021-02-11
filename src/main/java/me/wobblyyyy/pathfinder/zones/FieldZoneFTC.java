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

package me.wobblyyyy.pathfinder.zones;

import me.wobblyyyy.pathfinder.geometry.Shape;
import me.wobblyyyy.pathfinder.geometry.Zone;
import me.wobblyyyy.pathfinder.shapes.FieldShapeFTC;

/**
 * A generic zone representing an FTC field.
 *
 * @author Colin Robertson
 */
public class FieldZoneFTC implements Zone {
    @Override
    public String getName() {
        return "Field";
    }

    @Override
    public Shape getParentShape() {
        return FieldShapeFTC.EMPTY;
    }

    @Override
    public int getZonePriority() {
        return 0;
    }

    @Override
    public double getDriveSpeedMultiplier() {
        return 0;
    }

    @Override
    public int getComponents() {
        return 0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
