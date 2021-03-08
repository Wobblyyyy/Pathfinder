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

package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.robot.Motor;

public class TestSwerveMotor implements Motor {
    double lastPower = 0;

    /**
     * Allow the motor to be controlled by a user.
     *
     * <p>
     * Implementing this can be a little bit annoying. For that reason,
     * I'd suggest that you use whatever Pathfinder distro is available for
     * the robotics competition you're engaged in.
     * </p>
     *
     * <p>
     * While user control is disabled, the user SHOULD NOT be able to control
     * the robot via a joystick. In fact, nothing should be able to control
     * the robot's motors UNLESS it's specifically marked as a non-user,
     * which can be done however you'd like.
     * </p>
     */
    @Override
    public void enableUserControl() {
//        System.out.println("User control enabled!");
    }

    /**
     * Stop allowing the motor to be controlled by a user.
     *
     * <p>
     * Implementing this can be a little bit annoying. For that reason,
     * I'd suggest that you use whatever Pathfinder distro is available for
     * the robotics competition you're engaged in.
     * </p>
     *
     * <p>
     * While user control is disabled, the user SHOULD NOT be able to control
     * the robot via a joystick. In fact, nothing should be able to control
     * the robot's motors UNLESS it's specifically marked as a non-user,
     * which can be done however you'd like.
     * </p>
     */
    @Override
    public void disableUserControl() {
//        System.out.println("User control disabled!");
    }

    /**
     * Set power to the motor.
     *
     * <p>
     * Power set to motors is always within the range of (-1) to (+1).
     * <ul>
     *     <li>
     *         Positive 1 represents the motor's maximum speed in
     *         the POSITIVE/FORWARDS direction.
     *     </li>
     *     <li>
     *         Negative 1 represents the motor's maximum speed in
     *         the NEGATIVE/BACKWARDS direction.
     *     </li>
     * </ul>
     * </p>
     *
     * @param power the power to set to the motor.
     */
    @Override
    public void setPower(double power) {
        if (power != lastPower) {
            lastPower = power;
            System.out.println("Turn power set to " + power);
        }
    }

    /**
     * Set power to the motor.
     *
     * <p>
     * Power set to motors is always within the range of (-1) to (+1).
     * <ul>
     *     <li>
     *         Positive 1 represents the motor's maximum speed in
     *         the POSITIVE/FORWARDS direction.
     *     </li>
     *     <li>
     *         Negative 1 represents the motor's maximum speed in
     *         the NEGATIVE/BACKWARDS direction.
     *     </li>
     * </ul>
     * </p>
     *  @param power the power to set to the motor.
     *
     * @param user whether or not this power change is the result of a user
     *             or non-user. true means that a user made the change, while
     */
    @Override
    public void setPower(double power, boolean user) {
        if (power != lastPower) {
            lastPower = power;
            System.out.println("Power set to " + power);
        }
    }

    /**
     * Get power from the motor.
     *
     * @return the motor's power value.
     */
    @Override
    public double getPower() {
        return 0.0;
    }
}