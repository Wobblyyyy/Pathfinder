package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PathfinderTest {
    Pathfinder pathfinder;

    @Test
    public void testPathfinder() throws InterruptedException {
        ArrayList<HeadingPoint> points = new ArrayList<>() {{
            add(new HeadingPoint(
                    0,
                    0,
                    0
            ));
            add(new HeadingPoint(
                    20,
                    30,
                    19
            ));
            add(new HeadingPoint(
                    78,
                    103,
                    24
            ));
            add(new HeadingPoint(
                    201,
                    201,
                    201
            ));
        }};

        long execPID = 0;
        long execTRA = 0;
        pathfinder = new Pathfinder(new CoolConfig());

        execTRA = System.currentTimeMillis();
        pathfinder.followPath(points);
        execTRA = System.currentTimeMillis() - execTRA;

        ArrayList<Point> path = pathfinder.getManager().getWaypointPath(points);

        for (Point p : path) {
            if (p instanceof HeadingPoint) System.out.println(((HeadingPoint) p).toString());
            else System.out.println(p.toString());
        }

        System.out.println(execTRA);
    }
}
