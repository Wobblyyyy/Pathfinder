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

package me.wobblyyyy.pathfinder.math;

import java.util.function.Supplier;

/**
 * A representation of a rotational velocity force. This force is measured in
 * either rotations per second or rotations per minute. Either way works just
 * fine. This class was created mostly to simplify the process of writing code
 * that modifies swerve module values for swerve module odometry.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class RotationalVelocity {
    /**
     * An internal access point for the wheel's rotational velocity.
     */
    private final Supplier<Double> velocitySupplier;

    /**
     * An internal reference to what type of velocity this instance of the
     * {@code RotationalVelocity} is - RPS or RPM.
     */
    private final Type velocityType;

    /**
     * Create a new {@code RotationalVelocity} instance.
     *
     * @param velocitySupplier a method that is capable of supplying the
     *                         velocity of the given wheel. This method should
     *                         call some sort of a "getVelocity()" method that
     *                         will determine the wheel's velocity based on
     *                         an encoder.
     * @param velocityType     what type of velocity is being used. In general,
     *                         this is most often RPM, not RPS. Unless you know
     *                         for certain that this is measured in RPS and not
     *                         RPM, you should just use RPM.
     */
    public RotationalVelocity(Supplier<Double> velocitySupplier,
                              Type velocityType) {
        this.velocitySupplier = velocitySupplier;
        this.velocityType = velocityType;
    }

    /**
     * Convert rotations per second to rotations per minute.
     *
     * @param perSecond the value to convert.
     * @return the converted value.
     */
    public static double perSecondToPerMinute(double perSecond) {
        return perSecond * 60;
    }

    /**
     * Convert rotations per minute to rotations per second.
     *
     * @param perMinute the value to convert.
     * @return the converted value.
     */
    public static double perMinuteToPerSecond(double perMinute) {
        return perMinute / 60;
    }

    /**
     * Get the raw velocity of the wheel.
     *
     * @return the wheel's raw velocity.
     */
    private double raw() {
        return velocitySupplier.get();
    }

    /**
     * Get the wheel's rotational velocity in whatever type is specified.
     *
     * @param type the type of velocity to get.
     * @return the wheel's velocity, in whatever type you specified.
     */
    public double get(Type type) {
        if (velocityType == type) {
            return raw();
        } else {
            if (velocityType == Type.ROTATIONS_PER_MINUTE) {
                return perMinuteToPerSecond(raw());
            } else {
                return perSecondToPerMinute(raw());
            }
        }
    }

    /**
     * Get the velocity, notated in rotations per second (RPS)
     *
     * @return the velocity of the target, notated in RPS
     */
    public double getRotationsPerSecond() {
        return get(Type.ROTATIONS_PER_SECOND);
    }

    /**
     * Get the velocity, notated in rotations per minute (RPM)
     *
     * @return the velocity of the target, notated in RPM
     */
    public double getRotationsPerMinute() {
        return get(Type.ROTATIONS_PER_MINUTE);
    }

    /**
     * Different types of rotational velocity measurement.
     */
    public enum Type {
        /**
         * ROTATIONS PER SECOND - RPS.
         *
         * <p>
         * Less common than RPM.
         * </p>
         */
        ROTATIONS_PER_SECOND,

        /**
         * ROTATIONS PER MINUTE - RPM.
         *
         * <p>
         * More common than RPS.
         * </p>
         */
        ROTATIONS_PER_MINUTE
    }
}
