package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.tracking.Tracker;

public class CoolOdometry implements Tracker {
    static final HeadingPoint d = new HeadingPoint(0, 0, 0);
    @Override
    public HeadingPoint getPos() {
        return new HeadingPoint(0, 0, 0);
    }

    @Override
    public void update() {

    }

    @Override
    public HeadingPoint getFrPos() {
        return d;
    }

    @Override
    public HeadingPoint getFlPos() {
        return d;
    }

    @Override
    public HeadingPoint getBrPos() {
        return d;
    }

    @Override
    public HeadingPoint getBlPos() {
        return d;
    }
}
