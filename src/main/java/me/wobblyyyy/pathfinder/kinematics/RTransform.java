/*
 * ======================================================================
 * || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 * ||                                                                  ||
 * || This file is part of the "Pathfinder" project, which is licensed ||
 * || and distributed under the GPU General Public License V3.         ||
 * ||                                                                  ||
 * || Pathfinder is available on GitHub:                               ||
 * || https://github.com/Wobblyyyy/Pathfinder                          ||
 * ||                                                                  ||
 * || Pathfinder's license is available:                               ||
 * || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 * ||                                                                  ||
 * || Re-distribution of this, or any other files, is allowed so long  ||
 * || as this same copyright notice is included and made evident.      ||
 * ||                                                                  ||
 * || Unless required by applicable law or agreed to in writing, any   ||
 * || software distributed under the license is distributed on an "AS  ||
 * || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 * || express or implied. See the license for specific language        ||
 * || governing permissions and limitations under the license.         ||
 * ||                                                                  ||
 * || Along with this file, you should have received a license file,   ||
 * || containing a copy of the GNU General Public License V3. If you   ||
 * || did not receive a copy of the license, you may find it online.   ||
 * ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.kinematics;

import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.math.functional.one.Reflection;
import me.wobblyyyy.pathfinder.geometry.Distance;

/**
 * A representation of a robot's transformation. This representation doesn't
 * include units or rates - rather, everything is measured as a generic unit
 * that can be customized to your likings. As another note, this transformation
 * doesn't include a rotation rate - rather, it includes a desired angle. The
 * robot's movement direction is determined by the two points that represent
 * the robot's transformation. The robot's angle is defined by whatever angle
 * is inputted. The robot should attempt to turn to this angle whenever it can.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class RTransform {
    /**
     * Zero transformation - no movement whatsoever.
     */
    public static RTransform ZERO = new RTransform(0, 0, fd(0));

    /**
     * Forwards transformation - (0, 1)
     */
    public static RTransform FORWARDS = new RTransform(0, 1, fd(0));

    /**
     * Rightwards transformation - (1, 0)
     */
    public static RTransform RIGHT = new RTransform(1, 0, fd(0));

    /**
     * Backwards transformation - (0, -1)
     */
    public static RTransform BACKWARDS = new RTransform(0, -1, fd(0));

    /**
     * Leftwards transformation - (0, -1)
     */
    public static RTransform LEFT = new RTransform(-1, 0, fd(0));

    /**
     * The transformation's start point.
     */
    private final Point start;

    /**
     * The transformation's stop point.
     */
    private Point stop;

    /**
     * The transformation's facing. This facing should represent the angle that
     * the robot as a whole should be facing.
     */
    private final Angle turn;

    /**
     * X distance between start and stop points.
     */
    private double x;

    /**
     * Y distance between start and stop points.
     */
    private double y;

    /**
     * Create a new robot transformation instance by using two points and an
     * angle. This angle represents the angle that the chassis should be facing.
     * It DOES NOT represent the rate at which the robot should turn.
     *
     * @param start the transformation's start point.
     * @param stop  the transformation's stop point.
     * @param turn  the angle that the robot should be facing.
     */
    public RTransform(Point start,
                      Point stop,
                      Angle turn) {
        this.start = start;
        this.stop = stop;
        this.turn = turn;

        this.x = Distance.distanceX(start, stop);
        this.y = Distance.distanceY(start, stop);
    }

    /**
     * Create a new robot transformation instance by using an X and a Y
     * component translation rather than a pair of points. The angle required
     * by this constructor should represent the angle that the robot is facing,
     * not the angle that the robot should be moving in.
     *
     * @param x    the translation's X component.
     * @param y    the translation's Y component.
     * @param turn the translation's angle.
     */
    public RTransform(double x,
                      double y,
                      Angle turn) {
        this(
                new Point(0, 0),
                new Point(x, y),
                turn
        );
    }

    /**
     * Internally-used method to get an angle from a degrees measurement.
     *
     * @param d degrees of angle.
     * @return angle from degrees.
     */
    @SuppressWarnings("SameParameterValue")
    private static Angle fd(double d) {
        return Angle.fromDegrees(d);
    }

    /**
     * Create a new robot transformation based on a given component X and Y
     * translation as well as a gyroscope angle. This allows you to use field
     * relative control instead of the default control system (which is
     * relative to the robot, not the field).
     *
     * @param start the translation's start point.
     * @param stop  the translation's stop point.
     * @param turn  the angle the robot should be facing.
     * @param gyro  the gyroscope's angle.
     * @return a newly-created robot transformation that makes use of the gyro
     * angle provided to transform the robot as specified.
     */
    public static RTransform fromGyro(Point start,
                                      Point stop,
                                      Angle turn,
                                      Angle gyro) {
        double vX = Distance.distanceX(start, stop);
        double vY = Distance.distanceY(start, stop);

        return fromGyro(vX, vY, turn, gyro);
    }

    /**
     * Create a new robot transformation based on a given component X and Y
     * translation as well as a gyroscope angle. This allows you to use field
     * relative control instead of the default control system (which is
     * relative to the robot, not the field).
     *
     * @param vX   the translation's component X value.
     * @param vY   the translation's component Y value.
     * @param turn the angle the robot should be facing.
     * @param gyro the gyroscope's angle.
     * @return a newly-created robot transformation that makes use of the gyro
     * angle provided to transform the robot as specified.
     */
    public static RTransform fromGyro(double vX,
                                      double vY,
                                      Angle turn,
                                      Angle gyro) {
        return new RTransform(
                (+vX * gyro.cos()) + (+vY * gyro.sin()),
                (-vX * gyro.sin()) + (+vY * gyro.cos()),
                turn
        );
    }

    /**
     * Get the transformation's start point.
     *
     * @return the transformation's start point.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Get the transformation's stop point.
     *
     * @return the transformation's stop point.
     */
    public Point getStop() {
        return stop;
    }

    /**
     * Get the robot's desired heading. This is not a measurement of desired
     * rotation, this is a measurement of desired facing.
     *
     * @return the robot's desired heading turn value.
     */
    public Angle getTurn() {
        return turn;
    }

    /**
     * Get the transformation's X value.
     *
     * @return the transformation's X value.
     */
    public double getX() {
        return x;
    }

    /**
     * Get the transformation's Y value.
     *
     * @return the transformation's Y value.
     */
    public double getY() {
        return y;
    }

    /**
     * Internal method to update the transformation's distance values.
     */
    private void updateTransformDistances() {
        this.x = Distance.distanceX(start, stop);
        this.y = Distance.distanceY(start, stop);
    }

    /**
     * Invert the transformation's X value.
     *
     * <p>
     * This change is reflected in both the X distance and the stop point. The
     * stop point has its X value literally flipped over the start point's X
     * value, thus giving you an inverted value.
     * </p>
     */
    public void invertX() {
        stop = new Point(
                Reflection.of(stop.getX(), start.getX()),
                stop.getY()
        );

        updateTransformDistances();
    }

    /**
     * Invert the transformation's Y value.
     *
     * <p>
     * This change is reflected in both the Y distance and the stop point. The
     * stop point has its Y value literally flipped over the start point's Y
     * value, thus giving you an inverted value.
     * </p>
     */
    public void invertY() {
        stop = new Point(
                stop.getX(),
                Reflection.of(stop.getY(), start.getY())
        );

        updateTransformDistances();
    }

    /**
     * Convert the transformation to a string.
     *
     * @return a String representation of the transformation.
     */
    @Override
    public String toString() {
        return String.format(
                "Robot Transform: {(vX: %.2f), (vY: %.2f), (vT: %.2f deg)}",
                x, y, turn.getDegrees()
        );
    }
}
