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

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.intra.ftc2.utils.math.Math;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.ejml.simple.SimpleMatrix;

@SuppressWarnings("unused")
public class SwerveKinematics {
    private final SimpleMatrix kinematicsForwards;
    private final SimpleMatrix kinematicsBackwards;

    private final Arrayable<Point> wheelPositions;

    @SuppressWarnings("PointlessArithmeticExpression")
    public SwerveKinematics(final Arrayable<Point> wheelPositions) {
        if (wheelPositions.size() < 2) {
            throw new IllegalArgumentException(
                    "Swerve drivetrains require at least 2 wheel positions!"
            );
        }

        int moduleCount = wheelPositions.size();
        this.wheelPositions = new StaticArray<>(wheelPositions);
        kinematicsBackwards = new SimpleMatrix(moduleCount * 2, 3);

        wheelPositions.itr().forEach(position -> {
            int i = wheelPositions.itr().index();

            kinematicsBackwards.setRow(i * 2 + 0, 0, 1, 0, -position.getX());
            kinematicsBackwards.setRow(i * 2 + 1, 0, 0, 1, +position.getY());
        });

        kinematicsForwards = kinematicsBackwards.pseudoInverse();
    }

    private SimpleMatrix getTransformationVector(RTransform transform) {
        return new SimpleMatrix(3, 1) {{
            setColumn(
                    0,
                    0,
                    transform.getX(),
                    transform.getY(),
                    transform.getTurn().getRadians()
            );
        }};
    }

    private SimpleMatrix getModuleMatrix(SimpleMatrix transformationVector) {
        return kinematicsBackwards.mult(transformationVector);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private StaticArray<SwerveModuleState> getSwerveModuleStates(
            SimpleMatrix moduleMatrix) {
        StaticArray<SwerveModuleState> states = new StaticArray<>();

        wheelPositions.itr().forEach(position -> {
            int i = wheelPositions.itr().index();

            double x = moduleMatrix.get(i * 2 + 0, 0);
            double y = moduleMatrix.get(i * 2 + 1, 0);
            double speed = Math.hypot(x, y);
            Angle angle = Angle.fromRadians(Math.atan2(y, x));

            states.set(i, new SwerveModuleState(speed, angle));
        });

        return states;
    }

    public StaticArray<SwerveModuleState> getStates(RTransform transform) {
        SimpleMatrix transformationVector = getTransformationVector(transform);
        SimpleMatrix moduleMatrix = getModuleMatrix(transformationVector);
        return getSwerveModuleStates(moduleMatrix);
    }

    @SuppressWarnings("PointlessArithmeticExpression")
    private SimpleMatrix getModuleStateMatrix(
            StaticArray<SwerveModuleState> states) {
        return new SimpleMatrix(wheelPositions.size() * 2, 1) {{
            states.itr().forEach(state -> {
                int i = states.itr().index();

                set(
                        i * 2 + 0,
                        0,
                        state.getPower() * state.getTurnAngle().getCos()
                );
                set(
                        i * 2 + 1,
                        0,
                        state.getPower() * state.getTurnAngle().getSin()
                );
            });
        }};
    }

    private SimpleMatrix getChassisTransformVector(SimpleMatrix stateMatrix) {
        return kinematicsForwards.mult(stateMatrix);
    }

    public RTransform getTransform(StaticArray<SwerveModuleState> states) {
        if (states.size() != wheelPositions.size()) {
            throw new IllegalArgumentException(
                    "Error while attempting to get RTransform from a set of " +
                            "swerve module states - the size of the array " +
                            "of input states does not match the size of the " +
                            "original array of states."
            );
        }

        SimpleMatrix stateMatrix = getModuleStateMatrix(states);
        SimpleMatrix chassisMatrix = getChassisTransformVector(stateMatrix);

        return new RTransform(
                chassisMatrix.get(0, 0),
                chassisMatrix.get(1, 0),
                Angle.fromRadians(chassisMatrix.get(2, 0))
        );
    }
}
