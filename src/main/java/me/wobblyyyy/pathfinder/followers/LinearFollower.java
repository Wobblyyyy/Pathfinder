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

package me.wobblyyyy.pathfinder.followers;

import me.wobblyyyy.pathfinder.control.Controller;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.geometry.Distance;

/**
 * The most basic path follower. This follower can follow a single segment
 * defined by two {@link HeadingPoint} instances. The first of the two points
 * (the start point) and the second of the two points (the end point) are used
 * in tandem with the robot's current position to dynamically correct for any
 * potential movement mishaps, meaning the robot will go to where it's supposed
 * to, or it'll die trying.
 *
 * @author Colin Robertson
 * @since 0.1.0
 */
public class LinearFollower implements Follower {
    /**
     * A reference to the follower's drivetrain.
     */
    private final Drive drive;

    /**
     * The follower's odometry system. This system is only used for determining
     * if the follower is close to the target position.
     */
    private final Odometry odometry;

    /**
     * The follower's start point.
     */
    @SuppressWarnings("FieldCanBeLocal")
    private final HeadingPoint start;

    /**
     * The follower's end/target point.
     */
    private final HeadingPoint end;

    /**
     * The follower's speed multiplier. The higher this value is, the faster
     * the {@code LinearFollower} will execute. This value can be changed
     * through either configuration options or the {@code Factory} class.
     */
    private double coefficient;

    /**
     * The follower's turn {@code Controller}.
     */
    private final Controller turnController;

    /**
     * Create a new linear follower.
     *
     * @param drive       the robot's drivetrain.
     * @param odometry    the robot's odometry system.
     * @param start       the start position.
     * @param end         the end position.
     * @param coefficient the robot's speed (0 to 1).
     */
    public LinearFollower(Drive drive,
                          Odometry odometry,
                          HeadingPoint start,
                          HeadingPoint end,
                          double coefficient,
                          Controller turnController) {
        this.start = start;
        this.end = end;
        this.drive = drive;
        this.odometry = odometry;
        this.coefficient = coefficient;
        this.turnController = turnController;
    }

    /**
     * Get the linear follower's drive speed coefficient.
     *
     * @return the linear follower's drive speed coefficient.
     */
    public double getCoefficient() {
        return coefficient;
    }

    /**
     * Set the linear follower's drive speed coefficient.
     *
     * @param coefficient the linear follower's new drive speed coefficient.
     */
    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    /**
     * Get a reference to the odometry system used by the follower.
     *
     * @return the follower's odometry system.
     */
    public Odometry getOdometry() {
        return odometry;
    }

    /**
     * Get the follower's target/goal/end position.
     *
     * @return the follower's target/goal/end position.
     */
    public HeadingPoint getTargetPos() {
        return end;
    }

    /**
     * We don't need to do anything here, either - all of the driving that
     * needs to get done is done through the drivetrain itself. This method
     * will do absolutely nothing, but it has to be called anyways.
     */
    @Override
    public void update() {

    }

    /**
     * In this case, we don't need to do anything - there are no calculations
     * needed to drive the robot. All of the translational calculations that
     * are needed should be handled by the internal kinematics of the
     * implemented drivetrain of choice.
     */
    @Override
    public void calculate() {

    }

    /**
     * Get the current difference in angle between the odometry's reported
     * angle and the target angle. This value will be fed into the turn
     * controller and used to determine turn speed.
     *
     * @return the difference between the target and current angle, in degrees.
     * If the distance is above 180deg, it'll be "normalized" by adjusting both
     * angles down by 180, "fixing" them, and recalculating the delta.
     */
    public double getAngleDelta() {
        // This method call returns a number that can be either positive or
        // negative. We don't fix the angle because this is a RELATIVE
        // measurement, unlike an absolute measurement, such as current and
        // target headings.
        return AngleUtils.minimumAngleDelta(
                odometry.getPos().getHeading(), // current heading
                end.getHeading()                // target heading
        );
    }

    /**
     * Drive the robot. This method attempts to power the robot by calling
     * the drive method of the drivetrain.
     */
    @Override
    public void drive() {
        /*
         * We create a theoretical target point by using the inDirection
         * method to create a point that's COEFFICIENT away from 0 in whatever
         * direction we determine by calculating the angle between the
         * robot's current position and the robot's target position.
         */
        Point target = Distance.inDirection(
                Point.ZERO,                               // origin point
                Point.angleOfDeg(                         // angle to go at
                        odometry.getPos(),                // current position
                        end                               // target position
                ),                                        // angle A to B
                coefficient                               // speed to move at
        );

        /*
         * Now we create a transformation from the theoretical target point.
         * This transformation is a transformation between 0 and the target
         * point, thus capable of instructing the robot to move how we want.
         *
         * The turn controller is utilized here. We determine the current
         * angle delta (can be positive or negative) and use the controller
         * to determine the rate at which the robot should move. Because this
         * can be a positive or negative number as well, the robot can turn
         * in either direction. Lovely, isn't it?
         */
        RTransform transformation = new RTransform(
                Point.ZERO,                               // origin
                target,                                   // "target" point
                turnController.calculate(getAngleDelta()) // turn rate
        );

        drive.drive(transformation);
    }

    /**
     * Has the follower finished yet? The follower's finish qualification is
     * determined by whether or not the robot is close enough to the target
     * position. This tolerance is, by default, 0.5 units - often inches.
     *
     * @return whether or not the follower has finished.
     */
    @Override
    public boolean isDone() {
        /*
         * If the robot's position and the target position are very nearly
         * the same, we're done. We can now return true and set power to the
         * motors. Power, of course, meaning 0 - the robot should stop at this
         * point entirely.
         *
         * The distance I've selected here - 0.5 - is really low, which might
         * cause some issues with the robot overshooting the target. If this
         * is an issue, this can be changed.
         */
        if (Distance.isNearPoint(
                odometry.getPos(),
                end,
                0.5
        )) {
            drive.drive(RTransform.ZERO);
            return true;
        }

        /*
         * If we aren't done, however, return false, indicating that the
         * follower should continue its execution.
         */
        return false;
    }

    /**
     * Return a string representation of the {@code LinearFollower}.
     *
     * <p>
     * This string takes following form:
     * {@code "LinearFollower{{(%f, %f) @ %f}, {(%f, %f) @ %f}}"}. Each of the
     * {@code %f} values will be replaced by a number. In this case, the order
     * of these numbers is as follows:
     * <ul>
     *     <li> start point's x value (1) </li>
     *     <li> start point's y value (2) </li>
     *     <li> start point's heading (3) </li>
     *     <li>   end point's x value (4) </li>
     *     <li>   end point's y value (5) </li>
     *     <li>   end point's heading (6) </li>
     * </ul>
     * </p>
     *
     * @return the {@code LinearFollower} in {@code String} form.
     */
    @Override
    public String toString() {
        return String.format(
                "LinearFollower{{(%f, %f) @ %f}, {(%f, %f) @ %f}}",
                start.getX(),          // start point's x value (1)
                start.getY(),          // start point's y value (2)
                start.getHeading(),    // start point's heading (3)
                end.getX(),            // end point's x value   (4)
                end.getY(),            // end point's y value   (5)
                end.getHeading()       // end point's heading   (6)
        );
    }
}
