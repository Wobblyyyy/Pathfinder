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
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.time.Time;
import me.wobblyyyy.pathfinder.time.TimeUnit;

/**
 * Helper class for determining the position of a robot that uses a swerve
 * drive. This class uses linear algebra and inverse kinematics to determine
 * a theoretical position from real or unreal velocity vectors representative
 * of the robot's current translation. This class is designed to mesh with the
 * {@link SwerveKinematics} class - in fact, you need to use an instance of the
 * swerve kinematics class in order to make this class do anything at all.
 * Unfortunately, this class doesn't handle EVERYTHING for you. You'll need to
 * convert between swerve module states that indicate percent output power and
 * swerve module states that indicate actual velocity vectors.
 *
 * <p>
 * It's strongly suggested that you make use of the {@code SpeedConverter}
 * class to handle conversion between motor percent output and actual velocity.
 * This conversion system utilizes motor encoders and velocities to determine
 * the actual velocity of the robot.
 * </p>
 *
 * <p>
 * This class is pretty confusing, I can't lie. Don't be discouraged if you
 * don't understand this at all - none of us do, let's be honest. However, if
 * you're confused and want to not be confused, you can check out the online
 * documentation for Pathfinder, shoot me an email, or post something on a
 * forum maybe? Up to you. Good luck!
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
@SuppressWarnings("unused")
public class SwerveOdometry {
    private final SwerveKinematics kinematics;
    private HeadingPoint position;
    private double previousSeconds = -1;

    private final Angle gyroOffset;

    /**
     * Create a new {@code SwerveOdometry} tracker based on a kinematic
     * representation of the swerve chassis, an angle offset, and the current
     * or initial position of the robot.
     *
     * @param kinematics      a kinematic representation of the robot's swerve drive
     *                        chassis. See the SwerveKinematics class to learn more.
     * @param gyroOffset      the offset that should be applied to the angle.
     * @param initialPosition the starting position of the robot.
     */
    public SwerveOdometry(SwerveKinematics kinematics,
                          Angle gyroOffset,
                          HeadingPoint initialPosition) {
        this.kinematics = kinematics;
        this.position = initialPosition;
        this.gyroOffset = initialPosition.getAngle().minus(gyroOffset);
    }

    /**
     * Create a new {@code SwerveOdometry} instance without using an angle
     * offset or a positional offset. This means that whatever angle the gyro
     * reports at the time of initialization is the "base" angle, and the
     * robot's starting position is set to (0, 0).
     *
     * @param kinematics a kinematic representation of the robot's swerve drive
     *                   chassis. See the SwerveKinematics to learn more.
     */
    public SwerveOdometry(SwerveKinematics kinematics) {
        this(kinematics, new Angle(0), new HeadingPoint(0, 0, 0));
    }

    /**
     * Get the current reported position of the tracker. This position
     * is represented in whatever units you're using natively - meters,
     * inches, feet, miles, whatever you want, really?
     *
     * @return the robot's reported position. This position is based on
     * a series of inverse kinematic operations that are performed on a
     * constantly-transformed internal position. Please note that this
     * position value will be very inaccurate if your timing isn't at
     * least relatively consistent and small.
     */
    public HeadingPoint getPosition() {
        return position;
    }

    private double getGapAndUpdateTime(double currentTime) {
        double gap = previousSeconds >= 0 ?
                currentTime - previousSeconds :
                0.0;
        previousSeconds = currentTime;
        return gap;
    }

    private Angle applyAngleOffset(Angle gyroAngle) {
        return Angle.fromDegrees(
                gyroAngle.getDegrees() +
                        gyroOffset.getDegrees()
        );
    }

    private RTransform getChassisStateFromStates(
            StaticArray<SwerveState> states) {
        return kinematics.getTransform(states);
    }

    private double deltaX(RTransform transformation, double time) {
        return (transformation.getX() * time);
    }

    private double deltaY(RTransform transformation, double time) {
        return (transformation.getY() * time);
    }

    private double deltaTheta(RTransform transformation) {
        return transformation.getTurn().getDegrees();
    }

    private HeadingPoint transformCurrentPosition(double time,
                                                  RTransform transformation) {
        double dX = deltaX(transformation, time);
        double dY = deltaY(transformation, time);
        double dH = deltaTheta(transformation);

        return position.transform(dX, dY, Angle.fromDegrees(dH));
    }

    /**
     * Update the positional tracker's internal position. This method should
     * be called as frequently to ensure the highest degree of accuracy when
     * it comes to positional tracking. The angle should be reported directly
     * from a gyroscope. In most cases, these states should be passed through
     * a speed conversion system prior to this method's invocation. Regular
     * swerve module states use a (-1, 1) range for power - NOT actual movement
     * velocity, meaning that positional tracking is rendered inaccurate.
     *
     * <p>
     * On these states: in most cases, the swerve module states that are set to your
     * robot are within the range of (-1, 1) for power. These values are set to the
     * robot's motor controllers or motors, and thus, the motors are powered accordingly.
     * This works fine, but there's one major issue. If you attempt to use these values
     * to determine the position of the robot, the robot's assumed position will be
     * terribly inaccurate. This is because these states aren't representative of the
     * robot's actual movement, but rather, the robot's desired movement. There are
     * already provided methods that will allow you to get the robot's actual
     * position instead of the robot's theoretical position - check out the online
     * documentation or shoot me an email if you're confused or need any help.
     * </p>
     *
     * @param currentSeconds the current time, in seconds. This time can be fetched
     *                       using the Time.relativeTime() method. Using this method
     *                       will ensure the best degree of accuracy.
     * @param gyroAngle      the angle of the gyroscope. This angle should come right
     *                       from the gyroscope without any intervention.
     * @param states         swerve module states that represent the robot's current
     *                       movement vector.
     */
    public HeadingPoint update(double currentSeconds,
                               Angle gyroAngle,
                               StaticArray<SwerveState> states) {
        double timeSinceLastUpdate = getGapAndUpdateTime(currentSeconds);
        Angle angle = applyAngleOffset(gyroAngle);
        RTransform transformation = getChassisStateFromStates(states);
        this.position = transformCurrentPosition(
                timeSinceLastUpdate,
                transformation
        );
        return position;
    }

    /**
     * Update the positional tracker's internal position. This method should
     * be called as frequently to ensure the highest degree of accuracy when
     * it comes to positional tracking. The angle should be reported directly
     * from a gyroscope. In most cases, these states should be passed through
     * a speed conversion system prior to this method's invocation. Regular
     * swerve module states use a (-1, 1) range for power - NOT actual movement
     * velocity, meaning that positional tracking is rendered inaccurate.
     *
     * <p>
     * On these states: in most cases, the swerve module states that are set to your
     * robot are within the range of (-1, 1) for power. These values are set to the
     * robot's motor controllers or motors, and thus, the motors are powered accordingly.
     * This works fine, but there's one major issue. If you attempt to use these values
     * to determine the position of the robot, the robot's assumed position will be
     * terribly inaccurate. This is because these states aren't representative of the
     * robot's actual movement, but rather, the robot's desired movement. There are
     * already provided methods that will allow you to get the robot's actual
     * position instead of the robot's theoretical position - check out the online
     * documentation or shoot me an email if you're confused or need any help.
     * </p>
     *
     * <p>
     * This method works by multiplying the amount of time since the last update by
     * the robot's current velocity vector, denoted as d(vX), d(vY), d(vT), where
     * T is theta, or the robot's current gyroscope angle. If the amount of time
     * is wildly inconsistent, such as a whole second or so, this method will then be
     * very inaccurate, and the robot's position will seem to teleport a significant
     * amount. Such, it's very important to call this method as frequently as possible,
     * thus ensuring the odometry's accuracy.
     * </p>
     *
     * <p>
     * Just as a side note: this method is an overloaded method for the other update
     * method. In almost all cases, you should use THIS method, and not the other
     * method. This ensures standardization of time - calling the other method can
     * cause confusing results, as time measurements might not be as accurate as
     * this method ensures.
     * </p>
     *
     * @param gyroAngle      the angle of the gyroscope. This angle should come right
     *                       from the gyroscope without any intervention.
     * @param states         swerve module states that represent the robot's current
     *                       movement vector.
     */
    public HeadingPoint update(Angle gyroAngle,
                               StaticArray<SwerveState> states) {
        return update(
                Time.relativeTime(TimeUnit.SECOND),
                gyroAngle,
                states
        );
    }
}
