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

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.math.GearRatio;
import me.wobblyyyy.pathfinder.math.RotationalVelocity;
import me.wobblyyyy.pathfinder.math.WheelSize;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * Convert between wheel speed (-1 to 1) and velocity (in inches per second,
 * most often). Mostly used for odometry.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class SpeedConverter {
    /**
     * The wheel's rotational velocity.
     */
    private final RotationalVelocity velocity;

    /**
     * The wheel's size.
     */
    private final WheelSize wheelSize;

    /**
     * The gear ratio between the motor and the wheel.
     */
    private final GearRatio gearRatio;

    /**
     * Create a new {@code SpeedConverter} instance that can be used to convert
     * between speed values (assigned by states) and real velocities that can
     * be used for odometry purposes.
     *
     * @param velocity  the wheel velocity system.
     * @param wheelSize the size of the wheel.
     * @param gearRatio the gear ratio between the motor and the wheel.
     */
    public SpeedConverter(RotationalVelocity velocity,
                          WheelSize wheelSize,
                          GearRatio gearRatio) {
        this.velocity = velocity;
        this.wheelSize = wheelSize;
        this.gearRatio = gearRatio;
    }

    /**
     * Create a new {@code SpeedConverter} instance that can be used to convert
     * between speed values (assigned by states) and real velocities that can
     * be used for odometry purposes. This constructor is an overload for the
     * other constructor. This constructor assumes a gear ratio of 1:1.
     *
     * @param velocity  the wheel velocity system.
     * @param wheelSize the size of the wheel.
     */
    public SpeedConverter(RotationalVelocity velocity,
                          WheelSize wheelSize) {
        this(velocity, wheelSize, new GearRatio(1, 1));
    }

    /**
     * Get the speed of the wheel in inches per second.
     *
     * @return the speed of the wheel in inches per second.
     */
    public double inchesPerSecond() {
        double outputVelocity = gearRatio.howManyOut(
                velocity.getRotationsPerSecond()
        );
        return outputVelocity * wheelSize.getCircumference();
    }

    /**
     * Get the speed of the wheel in inches per minute.
     *
     * @return the speed of the wheel in inches per minute.
     */
    public double inchesPerMinute() {
        return inchesPerSecond() / 60;
    }

    /**
     * Get the speed of the wheel in feet per second.
     *
     * @return the speed of the wheel in feet per second.
     */
    public double feetPerSecond() {
        return inchesPerSecond() / 12;
    }

    /**
     * Get the speed of the wheel in feet per minute.
     *
     * @return the speed of the wheel in feet per minute.
     */
    public double feetPerMinute() {
        return feetPerSecond() / 60;
    }

    /**
     * Get the speed of the wheel in meters per second.
     *
     * @return the speed of the wheel in meters per second.
     */
    public double metersPerSecond() {
        return Distance.feetToMeters(feetPerSecond());
    }

    /**
     * Get the speed of the wheel in meters per minute.
     *
     * @return the speed of the wheel in meters per second.
     */
    public double metersPerMinute() {
        return Distance.feetToMeters(feetPerMinute());
    }

    /**
     * This one's just for fun - don't actually use it.
     *
     * @return the robot's speed, in miles per hour.
     */
    public double milesPerHour() {
        return feetPerMinute() / 5024 / 60;
    }

    /**
     * Convert a {@code StaticArray} of swerve module states to a more
     * odometrically-usable version of those same states by using another
     * {@code StaticArray} of speed converters.
     *
     * <p>
     * These speed converters work by using the rotational velocity of each
     * of the swerve chassis' wheels to determine the current movement of the
     * robot. Using real velocity is much more useful than using simulated
     * velocity. Although you could technically use the {@link SwerveOdometry}
     * class directly, it wouldn't be very accurate - using this method helps
     * to improve the accuracy of exactly that.
     * </p>
     *
     * @param originalStates  a {@code StaticArray} of original states.
     * @param speedConverters a {@code StaticArray} of speed converters. This
     *                        array must be the same size as the original states
     *                        array, or an exception will be thrown.
     * @return actually accurate swerve module states. Not based on speed, but
     * based on the velocity of each of the wheels. This is significantly more
     * accurate than the alternatives.
     */
    public static StaticArray<SwerveModuleState> getSwerveModuleStates(
            StaticArray<SwerveModuleState> originalStates,
            StaticArray<SpeedConverter> speedConverters) {
        if (originalStates.size() != speedConverters.size()) {
            throw new IllegalArgumentException(
                    "State and speed converter size must be the same!"
            );
        }

        StaticArray<SwerveModuleState> newStates =
                new StaticArray<>(originalStates.size());

        originalStates.itr().forEach(state -> {
            int index = originalStates.itr().index();

            newStates.set(index, new SwerveModuleState(
                    speedConverters.get(index).inchesPerSecond(),
                    state.getTurnAngle()
            ));
        });

        return newStates;
    }
}
