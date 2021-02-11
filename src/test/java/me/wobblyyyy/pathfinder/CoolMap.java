package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.geometry.Rectangle;
import me.wobblyyyy.pathfinder.geometry.Shape;
import me.wobblyyyy.pathfinder.geometry.Zone;
import me.wobblyyyy.pathfinder.map.Map;

import java.util.ArrayList;

public class CoolMap extends Map {
//    private static final ArrayList<Zone> zones = new ArrayList<>() {{
//        add(
//        new Zone() {
//            @Override
//            public String getName() {
//                return "test";
//            }
//
//            @Override
//            public Shape getParentShape() {
//                return new Rectangle(
//                        Rectangle.Corners.FRONT_LEFT,
//                        Rectangle.Corners.CENTER,
//                        new Point(72, 72),
//                        18,
//                        18,
//                        0,
//                        false,
//                        false
//                );
//            }
//
//            @Override
//            public int getZonePriority() {
//                return 0;
//            }
//
//            @Override
//            public boolean isPointInZone(Point point) {
//                return false;
//            }
//
//            @Override
//            public double getDriveSpeedMultiplier() {
//                return 0;
//            }
//
//            @Override
//            public int getComponents() {
//                return 0;
//            }
//
//            @Override
//            public boolean isSolid() {
//                return false;
//            }
//        });
//    }};

    public CoolMap() {
        super(
                new ArrayList<Zone>() {{
                    add(new BlockZone());
                }}
        );
//        super(zones);
    }
}
