package me.wobblyyyy.pathfinder.core;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.followers.DualPidFollower;
import me.wobblyyyy.pathfinder.followers.LinearFollower;
import me.wobblyyyy.pathfinder.followers.TriPidFollower;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class FollowerFactory {
    public static DualPidFollower dualPid(PathfinderConfig config,
                                          DynamicArray<HeadingPoint> points) {
        return new DualPidFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(1),
                config.getSpeed()
        );
    }

    public static TriPidFollower triPid(PathfinderConfig config,
                                        DynamicArray<HeadingPoint> points) {
        return new TriPidFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(1),
                config.getSpeed()
        );
    }

    public static LinearFollower linear(PathfinderConfig config,
                                        DynamicArray<HeadingPoint> points) {
        return new LinearFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(0),
                points.get(1),
                config.getSpeed()
        );
    }
}
