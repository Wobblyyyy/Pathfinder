package me.wobblyyyy.pathfinder.core;

import me.wobblyyyy.pathfinder.CoolConfig;
import me.wobblyyyy.pathfinder.finders.Xygum;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.junit.jupiter.api.Test;

public class XygumTest {
    @Test
    public void test() {
        Xygum x = new Xygum(new CoolConfig(), Xygum.Finders.A);

        System.out.println(x.getCoordinatePath(
                new Point(
                        200, 200
                ),
                new Point(
                        100, 100
                )
        ));
    }
}
