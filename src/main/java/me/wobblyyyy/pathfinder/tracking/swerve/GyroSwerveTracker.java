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
import me.wobblyyyy.pathfinder.robot.Encoder;
import me.wobblyyyy.pathfinder.robot.Gyroscope;

/**
 * A combination of a gyroscopic tracker and a swerve chassis tracker!
 *
 * <p>
 * In order to actually use the gyroscope, you need to pass it as a parameter
 * to one of the two constructors for this class. It doesn't matter how the
 * gyroscope is initialized - so long as it gets a (fairly accurate) heading
 * for the robot, it should be all good.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class GyroSwerveTracker extends SwerveChassisTracker {
    private final Gyroscope gyroscope;

    /**
     * Create a new gyroscope-enabled swerve chassis tracker.
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
     * @param gyroscope     the gyroscope interface used for updating the
     *                      angle/heading of the swerve tracker.
     */
    public GyroSwerveTracker(Encoder frTurn,
                             Encoder frDrive,
                             Encoder flTurn,
                             Encoder flDrive,
                             Encoder brTurn,
                             Encoder brDrive,
                             Encoder blTurn,
                             Encoder blDrive,
                             double wheelDiameter,
                             double gapX,
                             double gapY,
                             Gyroscope gyroscope) {
        super(
                frTurn,
                frDrive,
                flTurn,
                flDrive,
                brTurn,
                brDrive,
                blTurn,
                blDrive,
                wheelDiameter,
                gapX,
                gapY
        );

        this.gyroscope = gyroscope;
    }

    /**
     * Create a new gyroscope-enabled swerve chassis tracker.
     *
     * @param fr        front-right tracker.
     * @param fl        front-left tracker.
     * @param br        back-right tracker.
     * @param bl        back-left tracker.
     * @param gyroscope the gyroscope interface used for updating the
     *                  angle/heading of the swerve tracker.
     */
    public GyroSwerveTracker(SwerveModuleTracker fr,
                             SwerveModuleTracker fl,
                             SwerveModuleTracker br,
                             SwerveModuleTracker bl,
                             Gyroscope gyroscope) {
        super(fr, fl, br, bl);

        this.gyroscope = gyroscope;
    }

    /**
     * Get the position of the chassis.
     *
     * <p>
     * The heading component of this HeadingPoint is determined exclusively
     * based on the gyroscope's reported angle. If this angle is at all off,
     * so will this heading.
     * </p>
     *
     * @return the chassis's position and heading.
     */
    @Override
    public HeadingPoint getPos() {
        return HeadingPoint.withNewHeading(
                super.getPos(),
                gyroscope.getAngle()
        );
    }
}
