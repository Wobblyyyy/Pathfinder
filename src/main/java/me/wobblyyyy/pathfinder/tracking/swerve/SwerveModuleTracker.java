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

package me.wobblyyyy.pathfinder.tracking.swerve;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.tracking.AngleTracker;
import me.wobblyyyy.pathfinder.tracking.PointTracker;

/**
 * Track the position of a swerve module.
 *
 * <p>
 * In essence, this is a wrapper of a combination of AngleTracker and
 * PointTracker - that's all that a swerve module needs, in reality.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @since 0.1.0
 * @see AngleTracker
 * @see PointTracker
 */
public class SwerveModuleTracker {
    /**
     * The angle tracker - used for tracking the angle of the drive wheel.
     */
    private final AngleTracker tTurn;

    /**
     * The point tracker - used for tracking the position of the point.
     */
    private final PointTracker tDrive;

    /**
     * The module's offset.
     */
    private final Point offset;

    /**
     * Create a new swerve module tracker! Woot-woot!
     *
     * @param turnEncoder        the encoder that tracks the angle of the drive
     *                           wheel. In most swerve modules, a non-motor encoder
     *                           tracks this for you - you just need to find its
     *                           CPR and get everything set up, and this should handle
     *                           the rest for you.
     * @param driveEncoder       the encoder that tracks the movement of the drive
     *                           wheel. Because encoders are unidirectional,
     *                           the turnEncoder is needed to accurately keep
     *                           track of the position of the point.
     * @param driveWheelDiameter the diameter of the drive wheel, preferably
     *                           measured in inches.
     * @param offset             the swerve module's offset from the center of
     *                           the robot. This value is INCREDIBLY important
     *                           to accurately tracking the position of the
     *                           robot - if this value is off, the chassis
     *                           position will be as well.
     */
    public SwerveModuleTracker(Encoder turnEncoder,
                               Encoder driveEncoder,
                               double driveWheelDiameter,
                               Point offset) {
        /*
         * Set up constructor variables.
         *
         * I don't like how there's varying levels of abstraction in this
         * constructor - ideally, tTurn and tDrive should both take the
         * same number of parameters.
         *
         * Maybe we should make an object to fix this?
         */
        tTurn = new AngleTracker(turnEncoder);
        tDrive = new PointTracker(
                driveEncoder,
                driveWheelDiameter,
                offset
        );

        /*
         * Set the swerve module's offset.
         */
        this.offset = offset;
    }

    /**
     * Update the turn tracker and the drive tracker.
     *
     * <p>
     * As stressed in the PointTracker class, this needs to be updated as
     * frequently as possible.
     * </p>
     *
     * <p>
     * The more frequently this method is called, the more accurate the
     * robot's position will be. X and Y values don't suffer all that much,
     * but the angle they're adjusted at will.
     * </p>
     *
     * <p>
     * Generally, it's a wise idea to have a thread dedicated to updating
     * the odometric position of the robot.
     * </p>
     *
     * <p>
     * None of the math that's responsible for tracking the chassis' position
     * is stored in this class. Rather, it's available in the point and
     * angle tracker classes, both of which have an `@see` tag.
     * </p>
     *
     * @see AngleTracker#update()
     * @see PointTracker#update(double)
     */
    public void update() {
        /*
         * Update the turn tracker first.
         *
         * The turn tracker doesn't take any parameters - the encoder that's
         * used by the turn tracker is stored inside of the class.
         */
        tTurn.update();

        /*
         * Update the drive tracker next.
         *
         * This is updated after the turn tracker to ensure that the turn
         * tracker provides the most updated angle possible.
         *
         * Unlike the turn tracker, the point tracker needs a single parameter
         * passed to it - that, of course, being the angle of the angle
         * tracker.
         */
        tDrive.update(tTurn.getAngle());
    }

    /**
     * Get the position of the module tracker.
     *
     * <p>
     * In order to make the heading more accurate, we opt to use the
     * turn's position rather than the drive's position. They should be the
     * same anyways, but... y'know. Just in case.
     * </p>
     *
     * @return the module's position.
     */
    public HeadingPoint getPosition() {
        return tDrive.getPosition();
    }

    /**
     * Get the turn encoder.
     *
     * @return the turn encoder.
     */
    protected Encoder gt() {
        return tTurn.getEncoder();
    }

    /**
     * Get the drive encoder.
     *
     * @return the drive encoder.
     */
    protected Encoder gd() {
        return tDrive.getEncoder();
    }

    /**
     * Get the drive wheel's diameter.
     *
     * @return the drive wheel's diameter.
     */
    protected double gwd() {
        return tDrive.getDiameter();
    }

    /**
     * Get the swerve module's X offset.
     *
     * @return the swerve module's X offset.
     */
    protected double gxo() {
        return offset.getX();
    }

    /**
     * Get the swerve module's Y offset.
     *
     * @return the swerve module's Y offset.
     */
    protected double gyo() {
        return offset.getY();
    }
}
