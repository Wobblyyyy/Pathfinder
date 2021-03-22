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
import me.wobblyyyy.pathfinder.math.Reciprocal;
import org.ejml.simple.SimpleMatrix;

/**
 * Kinematic utilities for a very lovely meccanum drivetrain. If you ask me,
 * who doesn't love meccanum drivetrains? Exactly. We all do.
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
