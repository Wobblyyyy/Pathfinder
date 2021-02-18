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

package me.wobblyyyy.pathfinder.tracking.threeWheel;

import com.tejasmehta.OdometryCore.OdometryCore;
import com.tejasmehta.OdometryCore.localization.EncoderPositions;
import com.tejasmehta.OdometryCore.localization.OdometryPosition;
import me.wobblyyyy.pathfinder.error.UnimplementedException;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.tracking.Tracker;

/**
 * Three-wheel odometry chassis tracking, provided as an alternative to
 * more direct methods, such as Swerve or Tank tracking.
 *
 * <p>
 * When compared to other methods of positional tracking, using three
 * non-actuated wheels is significantly more accurate than, say, getting encoder
 * positions from a swerve or tank drive.
 * </p>
 *
 * <p>
 * This portion of code depends on Tejas Mehta's OdometryCore library, which
 * handles most (all) of the math behind three-wheel positional tracking.
 * Preceding the implementation of his OdometryCore library, it has been
 * verified/confirmed to work accurately and effectively.
 * </p>
 *
 * <p>
 * If you're interested in learning more about the OdometryCore library that
 * serves as the very core of this tracker, you can go to either of these
 * two links.
 * <ul>
 *     <li>
 *         <a href="https://github.com/tmthecoder/OdometryCore">
 *             OdometryCore GitHub page
 *         </a>
 *     </li>
 *     <li>
 *         <a href="https://docs.tmthecoder.dev/OdometryCore/">
 *             OdometryCore JavaDoc portal
 *         </a>
 *     </li>
 * </ul>
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class ThreeWheelChassisTracker implements Tracker {
    /**
     * The left encoder.
     */
    private final Encoder left;

    /**
     * The right encoder.
     */
    private final Encoder right;

    /**
     * The middle/front/back encoder.
     */
    private final Encoder middle;

    /**
     * The diameter of each of the odometry wheels.
     */
    private final double wheelDiameter;

    /**
     * The left encoder's offset.
     */
    private final double leftOffset;

    /**
     * The right encoder's offset.
     */
    private final double rightOffset;

    /**
     * The middle encoder's offset.
     */
    private final double middleOffset;

    /**
     * Locally-used encoder values.
     */
    private EncoderPositions encoderPositions =
            new EncoderPositions(0, 0, 0);

    /**
     * Locally-used odometry position implementation.
     */
    private OdometryPosition odometryPosition;

    /**
     * Locally-used positional information about the robot.
     */
    private HeadingPoint position;

    /**
     * Create a new ThreeWheelChassisTracker instance.
     *
     * <p>
     * As a note, this tracker relies on math from Tejas Mehta's OdometryCore
     * library. Links to important documentation and information about his
     * library are available in the main JavaDoc for this class file.
     * </p>
     *
     * @param left          the left encoder.
     * @param right         the right encoder.
     * @param middle        the front or back encoder. It's named "middle" as
     *                      it's technically in the middle of the robot. It's
     *                      important to mount this wheel as accurately as
     *                      possible, otherwise some positional tracking math
     *                      will/might be wrong.
     * @param wheelDiameter the diameter of each of the encoder's wheels,
     *                      preferably (and hopefully) measured in inches.
     * @param leftOffset    the left wheel's offset from the center of the
     *                      robot. This is ONLY the horizontal offset.
     * @param rightOffset   the right wheel's offset from the center of the
     *                      robot. This is ONLY the horizontal offset.
     * @param middleOffset  the center wheel's offset from the center of the
     *                      robot. This is ONLY vertical offset.
     */
    public ThreeWheelChassisTracker(Encoder left,
                                    Encoder right,
                                    Encoder middle,
                                    double wheelDiameter,
                                    double leftOffset,
                                    double rightOffset,
                                    double middleOffset) {
        this.left = left;
        this.right = right;
        this.middle = middle;
        this.wheelDiameter = wheelDiameter;
        this.leftOffset = leftOffset;
        this.rightOffset = rightOffset;
        this.middleOffset = middleOffset;

        OdometryCore.initialize(
                middle.getCpr(),
                wheelDiameter,
                leftOffset,
                rightOffset,
                middleOffset
        );
    }

    /**
     * Tell the user that the method they're attempting to invoke has not
     * yet been implemented. Sucks for them, y'know?
     */
    private void throwUnimplemented() {
        try {
            throw new UnimplementedException(
                    "FR, FL, BR, BL positional tracking has not been " +
                            "implemented yet!"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is NOT implemented yet!
     *
     * @return null
     */
    @Override
    public HeadingPoint getFrPos() {
        throwUnimplemented();
        return null;
    }

    /**
     * This method is NOT implemented yet!
     *
     * @return null
     */
    @Override
    public HeadingPoint getFlPos() {
        throwUnimplemented();
        return null;
    }

    /**
     * This method is NOT implemented yet!
     *
     * @return null
     */
    @Override
    public HeadingPoint getBrPos() {
        throwUnimplemented();
        return null;
    }

    /**
     * This method is NOT implemented yet!
     *
     * @return null
     */
    @Override
    public HeadingPoint getBlPos() {
        throwUnimplemented();
        return null;
    }

    /**
     * Get the robot's position and heading.
     *
     * <p>
     * As usual, heading is signified in degrees, not radians. Also, the
     * measurements of position are measured in INCHES, not meters.
     * </p>
     *
     * @return the robot's position and heading.
     */
    @Override
    public HeadingPoint getPos() {
        return position;
    }

    /**
     * Update the odometry system.
     *
     * <p>
     * This should be run as frequently as possible.
     * </p>
     */
    @Override
    public void update() {
        encoderPositions = new EncoderPositions(
                left.getCount(),
                right.getCount(),
                middle.getCount()
        );
        odometryPosition = OdometryCore
                .getInstance()
                .getCurrentPosition(encoderPositions);
        position = new HeadingPoint(
                odometryPosition.getX(),
                odometryPosition.getY(),
                odometryPosition.getHeadingDegrees()
        );
    }
}
