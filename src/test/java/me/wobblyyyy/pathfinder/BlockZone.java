package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.geometry.Rectangle;
import me.wobblyyyy.pathfinder.geometry.Shape;
import me.wobblyyyy.pathfinder.geometry.Zone;

public class BlockZone implements Zone {
    @Override
    public String getName() {
        return "Block";
    }

    @Override
    public Shape getParentShape() {
        return new Rectangle(
                Rectangle.Corners.FRONT_LEFT,
                Rectangle.Corners.CENTER,
                new Point(200, 200),
                0,
                0,
                0
        );
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
        return true;
    }
}
