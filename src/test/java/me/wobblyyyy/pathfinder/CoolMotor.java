package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.robot.Motor;

public class CoolMotor implements Motor, Encoder {
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public double getCpr() {
        return 0;
    }

    @Override
    public void enableUserControl() {

    }

    @Override
    public void disableUserControl() {

    }

    @Override
    public void setPower(double power) {

    }

    @Override
    public void setPower(double power, boolean user) {

    }

    @Override
    public double getPower() {
        return 0;
    }
}
