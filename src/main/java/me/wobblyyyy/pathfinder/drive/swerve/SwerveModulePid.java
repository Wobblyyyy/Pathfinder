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

package me.wobblyyyy.pathfinder.drive.swerve;

import me.wobblyyyy.intra.ftc2.utils.math.PidController;

/**
 * PID controller wrapper class used in determining the suggested power for
 * swerve modules based on a target and a current measurement. This class makes
 * uses of the functional interface pattern included in Java - the
 * {@link #calculate(double, double)} has the same signature as the signature
 * required by the {@link SwerveTurn} functional interface.
 *
 * @author Colin Robertson
 */
public class SwerveModulePid {
    /**
     * Internal PID controller. These measurements were based off tests I
     * had run at my own high school's robotics team.
     */
    private static final PidController controller = new PidController(
            0.009599311,
            0.000000000,
            0.005000000
    );

    /**
     * Implement the {@link SwerveTurn} functional interface - provide a
     * default way to determine power from angle.
     *
     * @param current the current angle of the swerve module.
     * @param target the target angle of the swerve module.
     * @return a calculated power value based on the current and target values.
     * This power value is calculated by the {@link #controller} PID controller.
     */
    public static double calculate(double current, double target) {
        return controller.calculate(current, target);
    }
}
