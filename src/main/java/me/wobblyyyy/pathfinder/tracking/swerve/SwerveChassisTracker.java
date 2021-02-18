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
import me.wobblyyyy.pathfinder.tracking.Tracker;

/**
 * Track the position of a swerve chassis with four swerve modules.
 *
 * <p>
 * As with any of the trackers in this library, this is compatible with
 * the Pathfinder system out of the box. In fact, you can even convert your
 * swerve drivetrain into a swerve chassis tracker pretty easily by using
 * the {@link me.wobblyyyy.pathfinder.util.ChassisConverter} class.
 * </p>
 *
 * <p>
 * Much of the tracking math behind the swerve chassis tracker happens
 * elsewhere in the code. A lot of it happens in the SwerveModuleTracker class,
 * which is included as an `@see` tag in this doc.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @since 0.1.0
 * @see SwerveModuleTracker
 */
public class SwerveChassisTracker implements Tracker {
    /**
     * The front-right turn motor's encoder.
     */
    private final Encoder frTurn;

    /**
     * The front-right drive motor's encoder.
     */
    private final Encoder frDrive;

    /**
     * The front-left turn motor's encoder.
     */
    private final Encoder flTurn;

    /**
     * The front-left drive motor's encoder.
     */
    private final Encoder flDrive;

    /**
     * The back-right turn motor's encoder.
     */
    private final Encoder brTurn;

    /**
     * The back-right drive motor's encoder.
     */
    private final Encoder brDrive;

    /**
     * The back-left turn motor's encoder.
     */
    private final Encoder blTurn;

    /**
     * The back-left dive motor's encoder.
     */
    private final Encoder blDrive;

    /**
     * The diameter of each of the four drive wheels. As usual, this
     * value is measured in inches.
     */
    public final double wheelDiameter;

    /**
     * The X distance, measured in inches, between the FR-FL pair of swerve
     * modules. This distance should be measured from the very center of the
     * swerve module's rotational radius.
     */
    public final double gapX;

    /**
     * The Y distance, measured in inches, between the FR-FL pair of swerve
     * modules. This distance should be measured from the very center of the
     * swerve module's rotational radius.
     */
    public final double gapY;

    /**
     * Front-right swerve tracker.
     */
    private final SwerveModuleTracker fr;

    /**
     * Front-left swerve tracker.
     */
    private final SwerveModuleTracker fl;

    /**
     * Back-right swerve tracker.
     */
    private final SwerveModuleTracker br;

    /**
     * Back-left swerve tracker.
     */
    private final SwerveModuleTracker bl;

    /**
     * Create a new swerve chassis tracker! Yay!
     *
     * <p>
     * Each of these constructor parameters is detailed in their own
     * field JavaDocs in this Java file. If you're confused about what's going
     * on, or what the meaning of a term is, you should go ahead and read
     * those.
     * </p>
     *
     * @param frTurn        FR turn encoder
     * @param frDrive       FR drive encoder
     * @param flTurn        FL turn encoder
     * @param flDrive       FL drive encoder
     * @param brTurn        BR turn encoder
     * @param brDrive       BR drive encoder
     * @param blTurn        BL turn encoder
     * @param blDrive       BL drive encoder
     * @param wheelDiameter see: {@link #wheelDiameter}
     * @param gapX          see: {@link #gapX}
     * @param gapY          see: {@link #gapY}
     */
    public SwerveChassisTracker(Encoder frTurn,
                                Encoder frDrive,
                                Encoder flTurn,
                                Encoder flDrive,
                                Encoder brTurn,
                                Encoder brDrive,
                                Encoder blTurn,
                                Encoder blDrive,
                                double wheelDiameter,
                                double gapX,
                                double gapY) {
        /*
         * Set constructor parameters/fields.
         *
         * We have a ton of these. Ideally, there should be a way to cut
         * down on this at some point - methods should rarely ever exceed
         * 20 lines in length for the purpose of simplicity.
         */

        this.frTurn = frTurn;
        this.frDrive = frDrive;
        this.flTurn = flTurn;
        this.flDrive = flDrive;
        this.brTurn = brTurn;
        this.brDrive = brDrive;
        this.blTurn = blTurn;
        this.blDrive = blDrive;
        this.wheelDiameter = wheelDiameter;
        this.gapX = gapX;
        this.gapY = gapY;

        /*
         * Calculate "offset points."
         *
         * These offset points are used in swerve calculations. Because the
         * angle and position of each of the wheels can be entirely independent
         * of one another, we need to know the offset of each of these wheels
         * in order to be able to track the relative movement of the chassis.
         */

        Point frO = new Point(
                gapX / 2,
                gapY / 2
        );
        Point flO = new Point(
                gapX / 2 * -1,
                gapY / 2
        );
        Point brO = new Point(
                gapX / 2,
                gapY / 2 * -1
        );
        Point blO = new Point(
                gapX / 2 * -1,
                gapY / 2 * -1
        );

        /*
         * After calculating offset points, we have to initialize the swerve
         * module trackers.
         *
         * Although pretending to track things is cool and all, in order to
         * actually track them, we need to have access to the math in the
         * SwerveModuleTracker class. This math depends on several things:
         * - Turn motor
         * - Drive motor
         * - The diameter of the drive wheel
         * - The offset of the swerve module's center axis
         */

        SwerveModuleTracker fr = new SwerveModuleTracker(
                frTurn,
                frDrive,
                wheelDiameter,
                frO
        );
        SwerveModuleTracker fl = new SwerveModuleTracker(
                flTurn,
                flDrive,
                wheelDiameter,
                flO
        );
        SwerveModuleTracker br = new SwerveModuleTracker(
                brTurn,
                brDrive,
                wheelDiameter,
                brO
        );
        SwerveModuleTracker bl = new SwerveModuleTracker(
                blTurn,
                blDrive,
                wheelDiameter,
                blO
        );

        /*
         * Set our FR, FL, BR, and BL module trackers.
         */

        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;
    }

    /**
     * Create a new swerve chassis tracker.
     *
     * @param fr front-right tracker.
     * @param fl front-left tracker.
     * @param br back-right tracker.
     * @param bl back-left tracker.
     */
    public SwerveChassisTracker(SwerveModuleTracker fr,
                                SwerveModuleTracker fl,
                                SwerveModuleTracker br,
                                SwerveModuleTracker bl) {
        this(
                fr.gt(),
                fr.gd(),
                fl.gt(),
                fl.gd(),
                br.gt(),
                br.gd(),
                bl.gt(),
                bl.gd(),
                fr.gwd(),
                fr.gxo() * 2,
                fr.gyo() * 2
        );
    }

    /**
     * Get the chassis' position WITHOUT HEADING.
     *
     * <p>
     * This point is calculated by averaging the position of all four of
     * the swerve modules. In theory, this should give the chassis's center
     * position, as the robot's position is defined as the very center of it.
     * </p>
     *
     * @return the chassis position, without heading.
     */
    private HeadingPoint getHeadlessPoint() {
        return HeadingPoint.withNewHeading(
                Point.average(
                        fr.getPosition(),
                        fl.getPosition(),
                        br.getPosition(),
                        bl.getPosition()
                ),
                0.0
        );
    }

    /**
     * Get the robot's position and heading.
     *
     * <p>
     * THIS CLASS DOES NOT SUPPORT TRACKING THE HEADING OF THE ROBOT. If you'd
     * like to track the heading of the robot, you're going to have to do that
     * some other way. Possibly the SwerveHeadingChassisTracker class that's
     * in the very same package as this one. Just possibly.
     * </p>
     *
     * @return the robot's position and heading.
     * @see SwerveChassisTracker#getHeadlessPoint()
     */
    @Override
    public HeadingPoint getPos() {
        return getHeadlessPoint();
    }

    /**
     * Update the odometry system.
     *
     * <p>
     * As I'm sure you've seen me say a million times already, this NEEDS to
     * be called as frequently as possible. A whole thread can even be
     * dedicated to doing just this.
     * </p>
     * @see SwerveModuleTracker#update()
     */
    @Override
    public void update() {
        /*
         * Update the position of each of the swerve trackers.
         *
         * Positional updating is handled in the SwerveModuleTracker class,
         * not this class - go look there if you're angry about something.
         */

        fr.update();
        fl.update();
        br.update();
        bl.update();
    }

    /**
     * Get the position of the front right swerve module.
     *
     * @return the front right swerve module's position.
     */
    @Override
    public HeadingPoint getFrPos() {
        return fr.getPosition();
    }

    /**
     * Get the position of the front left swerve module.
     *
     * @return the front left swerve module's position.
     */
    @Override
    public HeadingPoint getFlPos() {
        return fl.getPosition();
    }

    /**
     * Get the position of the back right swerve module.
     *
     * @return the back right swerve module's position.
     */
    @Override
    public HeadingPoint getBrPos() {
        return br.getPosition();
    }

    /**
     * Get the position of the back left swerve module.
     *
     * @return the back left swerve module's position.
     */
    @Override
    public HeadingPoint getBlPos() {
        return bl.getPosition();
    }
}
