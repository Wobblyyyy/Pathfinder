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

/**
 * Represent the state of a single drive motor module. The power values
 * stored in this module state correspond to 1 being full speed forwards,
 * negative 1 being full speed backwards, 0 being no speed at all, and
 * anything in between representing a combination of two of those values.
 * 
 * <p>
 * Please note that these module states ARE NOT adjusted for motors on one
 * side of the robot spinning in the opposite direction as the other side of
 * the robot. You have to handle this manually, preferably through whatever
 * motor implementation you've chosen to make.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class ModuleState {
    private final double power;

    public ModuleState() {
        this(0.0);
    }

    public ModuleState(double power) {
        this.power = power;
    }

    /**
     * Get the module's suggested power.
     *
     * @return the motor's power value. This value is within the range of
     * -1 to 1, with -1 being full speed BACKWARDS and +1 being full speed
     * FORWARDS. A power value of 0 represents that the motor shouldn't be
     * spinning at all. These power values are relative to the center of
     * the robot. If every wheel is set to 1.0 power, the robot should move
     * forwards in a straight line.
     */
    public double getPower() {
        return power;
    }

    @Override
    public String toString() {
        return "(Power: " + getPower() + ")";
    }
}
