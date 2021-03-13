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

/**
 * A variety of mathemtical functionality related to kinematics for a swerve
 * chassis. Both forwarads and backwards (inverse) kinematic utilities are
 * provided here, allowing for translation from desired translation to
 * desired swerve module states as well as translation from desired swerve
 * module states. The math behind this class is largely based upon the
 * Efficient Java Matrix Library {@code SimpleMatrix} with a bit of fancy
 * intervention here and there on our part. This class DOES NOT modify the
 * drivetrain's states - this class simply provides a mathematical interface
 * for determining module states and translations for a swerve chassis with
 * a set of manually inputted wheel positions.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
@SuppressWarnings("unused")
public class SwerveKinematics {
    /**
     * Forwards kinematics - used for getting module states from a translation.
     */
    private final SimpleMatrix kinematicsForwards;
    
    /**
     *  Inverse kinematics - used for odometry purposes.
     */
    private final SimpleMatrix kinematicsBackwards;

    /**
     * A container for each of the wheel positions on the robot.
     */
    private final Arrayable<Point> wheelPositions;

    /**
     * Create a new {@code SwerveKinematics} instance. This class doesn't actually
     * do anything other than provide a mathematical interface for kinematics.
     *
     * @param wheelPositions an {@code Arrayable} instance containing relative
     *                       swerve module positions. This position is defined as
     *                       the center axis on which the swerve module both drives
     *                       and rotates. This array must be greater than two in
     *                       size. Please note: the order of this array determines
     *                       the order in which kinematic operations output power
     *                       or positional values - remember the order you put these
     *                       modules in, such as FR/FL/BR/BL.
     */
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

    /**
     * Get swerve module states based on the desired transformation and the
     * wheel positions passed to this instance of {@code SwerveKinematics} on
     * construction. These states are outputted in the SAME order as the wheels
     * are inputted in upon construction. For example, if you created this instance
     * of swerve kinematics with the order of FR/FL/BR/BL, the outputted states
     * will be outputted in the same order.
     *
     * @param transform the robot's desired transformation. In most cases, this
     *                  transformation should fit within the range of (-1, 1).
     * @return a {@code StaticArray} of each of the module states that would represent
     * the desired translation. These module states are returned in the same order as
     * the wheel positions that were inputted upon this instance's construction.
     */
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

    /**
     * Get a positional transformation based on a set of swerve module states.
     * If the size of the provided state array doesn't match the size of the
     * internally-stored array of wheel positions, an exception will be thrown,
     * indicating that no transformation could be generated as the size of the
     * arrays did not match. 
     *
     * @param states a {@code StaticArray} of swerve module states. This array
     *               should be the same size as the internal array of wheel
     *               positions or an exception will be thrown on this method's
     *               invocation. Not cool.
     */
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
