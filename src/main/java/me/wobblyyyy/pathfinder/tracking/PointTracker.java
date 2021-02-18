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
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.tracking;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * Track a single point in a two-dimensional environment.
 *
 * <p>
 * All of the math behind this is fairly easy to find - however, as a
 * very quick overview...
 * </p>
 *
 * <p>
 * The movement of a single point can be expressed as follows.
 * <pre>
 * M_xyz = T_x + T_y + R_z
 * </pre>
 * where...
 * <ul>
 *     <li>
 *         x is the horizontal component of the X/Y plane.
 *     </li>
 *     <li>
 *         y is the vertical component of the X/Y plane.
 *     </li>
 *     <li>
 *         z is a measure of rotation or direction.
 *     </li>
 *     <li>
 *         M is a representation of the point's movement, which is a
 *         combination of the point's (a) X and Y translation and (b)
 *         rotation or direction.
 *     </li>
 *     <li>
 *         T is a measure of the point's translational movement along
 *         a given axis.
 *     </li>
 *     <li>
 *         R is a measure of the point's rotational movement.
 *     </li>
 * </ul>
 * </p>
 *
 * <p>
 * Thus, the X and Y coordinates can be tracked as follows.
 * <ul>
 *     <li>x_n = x_o * M_xy(cos(theta))</li>
 *     <li>y_n = y_o * M_xy(sin(theta))</li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class PointTracker {
    /**
     * A reference to the encoder used for positional tracking.
     *
     * <p>
     * This encoder is for the DRIVE wheel on a swerve module - NOT the
     * TURN wheel.
     * </p>
     */
    private final Encoder encoder;

    /**
     * The last-recorded angle.
     */
    private double lastAngle;

    /**
     * The last-recorded count.
     */
    private int lastCount;

    /**
     * An estimate of the point's current position.
     */
    private HeadingPoint position;

    /**
     * The point's starting position/offset.
     */
    private final Point startingPoint;

    /**
     * The encoder's CPR (counts per rotation).
     */
    private final double cpr;

    /**
     * The diameter of the drive wheel.
     */
    private final double diameter;

    /**
     * The circumference of the drive wheel.
     */
    private final double circumference;

    /**
     * TPI = Ticks per Inch.
     */
    private final double tpi;

    /**
     * Create a new point tracker.
     *
     * <p>
     * Unless you have a very good reason to, you should use the other
     * constructor with 3 arguments instead of 2.
     * </p>
     *
     * @param encoder  the point tracker's internally-used encoder.
     * @param diameter the diameter of the point tracker's drive wheel.
     */
    public PointTracker(Encoder encoder,
                        double diameter) {
        this(encoder, diameter, new HeadingPoint(0, 0, 0));
    }

    /**
     * Create a new point tracker.
     *
     * @param encoder       the point tracker's internally-used encoder.
     * @param diameter      the diameter of the point tracker's drive wheel.
     * @param startingPoint the tracker's starting position. Typically, this
     *                      value corresponds to a drive wheel's physical
     *                      positioning relative to the center of a given
     *                      wheeled vehicle.
     */
    public PointTracker(Encoder encoder,
                        double diameter,
                        Point startingPoint) {
        /*
         * Set all of the constructor values we have.
         */

        this.startingPoint = startingPoint;
        this.position = new HeadingPoint(
                startingPoint.getX(),
                startingPoint.getY(),
                0
        );
        this.encoder = encoder;
        this.cpr = encoder.getCpr();
        this.diameter = diameter;
        this.circumference = Math.PI * diameter;
        this.tpi = cpr / circumference;
    }

    /**
     * Convert between ticks and inches.
     *
     * <p>
     * If either the CPR (counts per rotation) or diameter (of a drive
     * wheel, of course) are inaccurate, this value will also be entirely
     * inaccurate.
     * </p>
     *
     * @param ticks the encoder's ticks.
     * @return how many inches have been travelled.
     */
    public double ticksToInches(int ticks) {
        /*
         * Ticks to inches is a very easy conversion - it's as simple as
         * dividing the number of ticks by the number of ticks per inch.
         */
        return ticks / tpi;
    }

    /**
     * Update the positional tracker according to an angle and count value.
     *
     * <p>
     * This method needs to be run as frequently as possible in order for it
     * to work effectively. Because this method tracks position based on
     * angle and distance, any issue with the angle will totally mess up the
     * point's perceived position.
     * </p>
     *
     * <p>
     * If, for example, you had a 45 degree turn with 1,000 encoder counts,
     * and you only updated the encoder once, the tracker would see that
     * it's facing 45deg and it's ticked 1,000 times - meaning the tracker
     * thinks that its currently 1,000 ticks ahead of 45deg from the point's
     * center. Basically, my POINT is you should update this very often.
     * </p>
     *
     * <p>
     * In addition to updating the robot's position as accurately as possible,
     * this updates the lastCount double for use in future updates.
     * </p>
     *
     * @param angle the angle which the point is currently facing. This angle
     *              should be notated in degrees, not radians. Additionally,
     *              this angle should be determined externally - you shouldn't
     *              pass a raw encoder value to this method, ever.
     * @see PointTracker#update(double, int)
     */
    public void update(double angle) {
        /*
         * This is an overloaded method here - we need to offload the
         * real calculations to the real update method.
         */
        update(angle, encoder.getCount());
    }

    /**
     * Update the positional tracker according to an angle and count value.
     *
     * <p>
     * This method needs to be run as frequently as possible in order for it
     * to work effectively. Because this method tracks position based on
     * angle and distance, any issue with the angle will totally mess up the
     * point's perceived position.
     * </p>
     *
     * <p>
     * If, for example, you had a 45 degree turn with 1,000 encoder counts,
     * and you only updated the encoder once, the tracker would see that
     * it's facing 45deg and it's ticked 1,000 times - meaning the tracker
     * thinks that its currently 1,000 ticks ahead of 45deg from the point's
     * center. Basically, my POINT is you should update this very often.
     * </p>
     *
     * <p>
     * In addition to updating the robot's position as accurately as possible,
     * this updates the lastCount double for use in future updates.
     * </p>
     *
     * @param angle the angle which the point is currently facing. This angle
     *              should be notated in degrees, not radians. Additionally,
     *              this angle should be determined externally - you shouldn't
     *              pass a raw encoder value to this method, ever.
     * @param count the encoder's current count. This should be the value
     *              directly reported from the encoder, not any other value
     *              or offset. This method already accounts for the difference
     *              in value and offsets.
     */
    public void update(double angle,
                       int count) {
        /*
         * The position of the point can be determined by using the Distance
         * class' inDirection method, which returns a point a given distance
         * and direction away from an origin point.
         */
        position = (HeadingPoint) Distance.inDirection(
                /*
                 * We need to use a HeadingPoint here instead of a point.
                 *
                 * The inDirection method accepts a HeadingPoint as an input
                 * parameter, and not a point and an angle.
                 */
                HeadingPoint.withNewHeading(
                        this.position,
                        angle
                ),

                /*
                 * To determine the distance that the point has travelled since
                 * the point's position was last updated is quite simple.
                 *
                 * The distance which the point has travelled can be best
                 * represented as the amount of ticks that have elapsed since
                 * the last time the encoder's value was queried.
                 *
                 * These elapsed ticks can then be converted into inches, thus
                 * giving us the point's position.
                 */
                ticksToInches(count - lastCount)
        );

        /*
         * Update the local last count field.
         */
        this.lastCount = count;
    }

    /**
     * Get the point's X value.
     *
     * @return the point's X value.
     */
    public double getX() {
        return getPosition().getX();
    }

    /**
     * Get the point's Y value.
     *
     * @return the point's Y value.
     */
    public double getY() {
        return getPosition().getY();
    }

    /**
     * Get the point's current heading.
     *
     * <p>
     * Just as a note, you probably shouldn't be using this method. Rather,
     * odometric tracking of a point's heading can be accomplished by simply
     * querying whatever encoder, gyroscope, or tracker is used to change
     * the angle of the wheel.
     * </p>
     *
     * @return the point's heading.
     */
    public double getHeading() {
        return getPosition().getHeading();
    }

    /**
     * Get the point positional tracker's current position.
     *
     * <p>
     * This position is an estimate of the point's position based on the math
     * highlighted earlier in the Java file - the class' JavaDoc, actually.
     * </p>
     *
     * @return the tracker's current position.
     */
    public HeadingPoint getPosition() {
        return position;
    }

    /**
     * Get the internally-used encoder.
     *
     * @return the point tracker's encoder.
     */
    public Encoder getEncoder() {
        return encoder;
    }

    /**
     * Get the diameter of the wheel.
     *
     * @return the wheel's diameter.
     */
    public double getDiameter() {
        return diameter;
    }
}
