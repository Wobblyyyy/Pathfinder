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

package me.wobblyyyy.pathfinder.tracking;

import me.wobblyyyy.pathfinder.robot.Encoder;

/**
 * Track an angle based on an encoder.
 *
 * <p>
 * Angle tracking is actually incredibly simple - use the encoder's CPR and the
 * encoder's count to determine how many ticks equals how many degrees.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @since 0.1.0
 */
public class AngleTracker {
    /**
     * A reference to the encoder that's used in tracking.
     *
     * <p>
     * AngleTracker is a self-sufficient class, meaning that it doesn't need
     * any other parameters fed into it for it to work - it just does its
     * thing. To support this FABULOUS lifestyle, we need to locally store
     * an encoder and get the value from it, rather than outsourcing encoder
     * value reading.
     * </p>
     */
    private final Encoder encoder;

    /**
     * The encoder's CPR.
     *
     * <p>
     * CPR, in the event you were unaware, is counts per rotation. In other
     * words, how many times the encoder has to say "hey!" for the code
     * to register that the encoder has rotated fully once.
     * </p>
     */
    private final double cpr;

    /**
     * TPD = Ticks per Degrees.
     */
    private final double tpd;

    /**
     * The current angle, measured in degrees.
     *
     * <p>
     * This value should always be within the defined range of (0) to (360).
     * It isn't required for some mathematical complexity or anything - it's
     * just a matter of personal preference.
     * </p>
     */
    private double angle;

    /**
     * Create a new angle tracker.
     *
     * @param encoder the encoder to be used.
     */
    public AngleTracker(Encoder encoder) {
        /*
         * Create a new angle tracker and set the constructor values.
         *
         * CPR can be determined by asking the encoder "hey, what's your
         * CPR?" TPD can be determined by dividing that number by 360.
         *
         * TPD stands for "ticks per degree." Dividing the CPR by 360 gives us
         * the amount of ticks that make up a single degree of rotation.
         */

        this.encoder = encoder;
        this.cpr = encoder.getCpr();
        this.tpd = this.cpr / 360;
    }

    /**
     * Update the tracker based on the encoder's count.
     */
    public void update() {
        /*
         * Get the angle that we need to perform calculations with.
         *
         * The encoder's count is multiplied by the ticks per degree value,
         * giving us a measurement, in degrees, of the encoder's current
         * position / heading.
         */
        angle = encoder.getCount() * tpd;

        /*
         * While the angle is greater than 360. AND
         * While the angle is less than 0.
         *
         * Either add or subtract 360 from the angle until the angle's measure
         * fits within the defined range of (0) to (360).
         */
        while (angle > 360) angle -= 360;
        while (angle <  0 ) angle += 360;
    }

    /**
     * Get angle.
     *
     * <p>
     * Hit da Nae-Nae? Hell to the yeah.
     * </p>
     *
     * @return angle.
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Get the encoder used in the tracker.
     *
     * @return the tracker's encoder.
     */
    public Encoder getEncoder() {
        return encoder;
    }
}
