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

package me.wobblyyyy.pathfinder.drive;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

/**
 * An interface used to make sure different types of drive systems are
 * fully integrated and working.
 *
 * <p>
 * If you're trying to make a Pathfinder configuration and you want to add
 * a drivetrain (have to, actually) you SHOULD NOT implement this class.
 * UNLESS you have a drivetrain that's not a swerve drive, a meccanum drive,
 * or a tank drive, you should use a pre-made class instead of creating your
 * own. Unless you like debugging...
 * </p>
 *
 * @author Colin Robertson
 */
public interface Drive {
    /**
     * Drive the drivetrain.
     *
     * <p>
     * This doesn't actually drive the drivetrain all the way from point A to
     * point B. Rather, it tells the motors of the drivetrain how to get there,
     * if it were to drive linearly between the two points. When following
     * curved trajectories, or when following powered trajectories, this method
     * is called dozens of time per second, with different values every time.
     * </p>
     *
     * @param start the start point.
     * @param end the end point.
     * @param power the percent (0 to 1) of power the drivetrain should
     *              operate at.
     */
    void drive(HeadingPoint start, HeadingPoint end, double power);

    /**
     * Drive the drivetrain.
     *
     * <p>
     * This method should drive the drivetrain with a given power and a given
     * angle, meaning that the drivetrain should move in the given angle at
     * the given power.
     * </p>
     *
     * @param power the power at which the motors should operate.
     * @param angle the angle at which the motors should point.
     */
    void drive(double power, double angle);

    /**
     * Allow the drivetrain to be controlled by a user.
     *
     * <p>
     * User control needs to be enabled in order for the user to actually
     * control the robot manually, such as via a joystick. Although
     * Pathfinder is pretty lovely, sometimes you need to control the robot.
     * </p>
     *
     * <p>
     * If this isn't implemented properly, you may see very funky things going
     * on with the motors - spasms, for example. In order to counter this, it's
     * a good idea to... well, actually implement this method properly.
     * </p>
     */
    void enableUserControl();

    /**
     * Stop allowing the drivetrain to be controlled by a user.
     *
     * <p>
     * In order for Pathfinder to actually function, and in order for the
     * user to not miserably mess up absolutely everything, user control
     * needs to be disabled prior to controlling a motor.
     * </p>
     *
     * <p>
     * If this isn't implemented properly, you may see very funky things going
     * on with the motors - spasms, for example. In order to counter this, it's
     * a good idea to... well, actually implement this method properly.
     * </p>
     */
    void disableUserControl();
}
