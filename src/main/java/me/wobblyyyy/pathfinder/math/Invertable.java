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

/**
 * Represent an invertable of some sort. The best way to demonstrate this
 * concept is with an example.
 *
 * <p>
 * Let's say you have a velocity output from a motor, but this motor might
 * be inverted. You could check to see if the motor was inverted, if it is,
 * multiply the value by -1. If it isn't, don't multiply it at all. Now,
 * yes, this would work. Or you could use this lovely class. How kind
 * and generous of this class to offer that lovely functionality!
 * </p>
 */
public class Invertable {
    /**
     * Apply a potential inversion to a target number.
     *
     * @param target     the number that may or may not be inverted.
     * @param isInverted is the number inverted?
     * @return the number multiplied by -1 if it's inverted, the number (not
     * multiplied at all) if it isn't inverted.
     */
    public static double apply(double target,
                               boolean isInverted) {
        return isInverted ? target * -1 : target;
    }
}
