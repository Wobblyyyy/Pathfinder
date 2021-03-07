package me.wobblyyyy.pathfinder;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.junit.jupiter.api.Test;

public class PathfinderTest {
    Pathfinder pathfinder;

    @Test
    public void testPathfinder() throws InterruptedException {
        DynamicArray<HeadingPoint> points = new DynamicArray<>() {{
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
//        pathfinder.lock();
        execTRA = System.currentTimeMillis() - execTRA;

        DynamicArray<Point> path = pathfinder.getManager().getWaypointPath(points);

        path.itr().forEach(point -> System.out.println(point.toString()));

        System.out.println(execTRA);
    }
}
