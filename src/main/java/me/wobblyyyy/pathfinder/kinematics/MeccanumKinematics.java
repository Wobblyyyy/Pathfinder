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
import me.wobblyyyy.pathfinder.math.functional.one.Reciprocal;
import org.ejml.simple.SimpleMatrix;

/**
 * Forwards and inverse meccanum kinematics. Most forms of drivetrains
 * can use the states of each wheel to determine a position estimate,
 * but meccanum drivetrains have so much slip that doesn't really work.
 * Thus, this class is mostly only useful for forwards kinematics.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
@SuppressWarnings("FieldCanBeLocal")
public class MeccanumKinematics {
    private final SimpleMatrix kinematicsForwards;
    private SimpleMatrix kinematicsBackwards;

    private final Point posFl;
    private final Point posFr;
    private final Point posBl;
    private final Point posBr;

    /**
     * Create a new {@code MeccanumKinematics} instance that calculates
     * power values and transformations based on the inputted points. 
     * These points are relative to the center of the robot.
     *
     * @param posFl the position of one of the meccanum modules. This
     *              position is relative to the center of the robot.
     * @param posFr the position of one of the meccanum modules. This
     *              position is relative to the center of the robot.
     * @param posBl the position of one of the meccanum modules. This
     *              position is relative to the center of the robot.
     * @param posBr the position of one of the meccanum modules. This
     *              position is relative to the center of the robot.
     */
    public MeccanumKinematics(final Point posFl,
                              final Point posFr,
                              final Point posBl,
                              final Point posBr) {
        this.posFl = posFl;
        this.posFr = posFr;
        this.posBl = posBl;
        this.posBr = posBr;

        kinematicsBackwards = new SimpleMatrix(4, 3);
        kinematicsBackwards.setRow(0, 0, 1, -1, -(posFl.getX() + posFl.getY()));
        kinematicsBackwards.setRow(1, 0, 1, +1, +(posFr.getX() - posFr.getY()));
        kinematicsBackwards.setRow(2, 0, 1, +1, +(posBl.getX() - posBl.getY()));
        kinematicsBackwards.setRow(3, 0, 1, -1, -(posBr.getX() + posBr.getY()));
        kinematicsBackwards = kinematicsBackwards.scale(Reciprocal.of(Math.sqrt(2)));
        kinematicsForwards = kinematicsBackwards.pseudoInverse();
    }

    /**
     * Get a {@code MeccanumState} from a provided {@code RTransform}.
     *
     * @param transform the desired robot translation.
     * @return a meccanum state, representing the power values each module
     * should have to move as desired. Please note: the returned state is
     * NOT normalized, meaning the power values aren't ensured to be in
     * any valid range. It's strongly suggested that you normalize these
     * power values by using the {@link MeccanumState#normalizeFromMaxUnderOne()}
     * method - otherwise, the power values might not be very epic.
     */
    public MeccanumState toMeccanumState(RTransform transform) {
        SimpleMatrix speedVector = new SimpleMatrix(3, 1);
        speedVector.setColumn(
                0,
                0,
                transform.getX(),
                transform.getY(),
                0
        );
        SimpleMatrix moduleMatrix = kinematicsBackwards.mult(speedVector);
        return new MeccanumState(
                moduleMatrix.get(0, 0),
                moduleMatrix.get(1, 0),
                moduleMatrix.get(2, 0),
                moduleMatrix.get(3, 0)
        );
    }

    /**
     * Convert meccanum states into a robot transformation.
     *
     * @param state the meccanum state to get a transformation from.
     * @param angle yeah I don't know it's an angle or something!
     * @return a transformation representing the provided meccanum state.
     */
    public RTransform toTransform(MeccanumState state, Angle angle) {
        SimpleMatrix moduleMatrix = new SimpleMatrix(4, 1);
        moduleMatrix.setColumn(
                0,
                0,
                state.flPower(),
                state.frPower(),
                state.blPower(),
                state.brPower()
        );
        SimpleMatrix speedVector = kinematicsForwards.mult(moduleMatrix);
        return new RTransform(
                speedVector.get(0, 0),
                speedVector.get(1, 0),
                angle
        );
    }
}
