package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.drive.swerve.Swerve;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.robot.Motor;

public class CoolDrive extends Swerve {
    /**
     * Create a new swerve drivetrain.
     *
     * @param fr_drive     the front-right drive motor.
     * @param fl_drive     the front-left drive motor.
     * @param br_drive     the back-right drive motor.
     * @param bl_drive     the back-left drive motor.
     * @param fr_turn      the front-right turn motor.
     * @param fl_turn      the front-left turn motor.
     * @param br_turn      the back-right turn motor.
     * @param bl_turn      the back-left turn motor.
     * @param fr_drive_enc the front-right drive encoder.
     * @param fl_drive_enc the front-left drive encoder.
     * @param br_drive_enc the back-right drive encoder.
     * @param bl_drive_enc the back-left drive encoder.
     * @param fr_turn_enc  the front-right turn encoder.
     * @param fl_turn_enc  the front-left turn encoder.
     * @param br_turn_enc  the back-right turn encoder.
     * @param bl_turn_enc  the back-left turn encoder.
     */
    public CoolDrive(Motor fr_drive, Motor fl_drive, Motor br_drive, Motor bl_drive, Motor fr_turn, Motor fl_turn, Motor br_turn, Motor bl_turn, Encoder fr_drive_enc, Encoder fl_drive_enc, Encoder br_drive_enc, Encoder bl_drive_enc, Encoder fr_turn_enc, Encoder fl_turn_enc, Encoder br_turn_enc, Encoder bl_turn_enc) {
        super(fr_drive, fl_drive, br_drive, bl_drive, fr_turn, fl_turn, br_turn, bl_turn, fr_drive_enc, fl_drive_enc, br_drive_enc, bl_drive_enc, fr_turn_enc, fl_turn_enc, br_turn_enc, bl_turn_enc);
    }

    @Override
    public void drive(HeadingPoint start, HeadingPoint end, double power) {
//        System.out.println(
//                "Start: (" + start.getX() + ", " + start.getY() + ")"
//        );
//        System.out.println(
//                "End: (" + end.getX() + ", " + end.getY() + ")"
//        );
    }

    @Override
    public void drive(double power, double angle) {

    }

    @Override
    public void enableUserControl() {

    }

    @Override
    public void disableUserControl() {

    }
}
