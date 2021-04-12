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

package me.wobblyyyy.pathfinder.control;

import me.wobblyyyy.pathfinder.math.functional.Reciprocal;

/**
 * A "default" turn controller. This is the controller that's used when no
 * other controller is configured for turning.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class DefaultTurnController extends ProportionalController {
    /**
     * Default turn controller.
     *
     * <p>
     * This turn controller is configured to have a coefficient of the
     * reciprocal of 90, which is equal to about 0.01111111111. Such, the
     * controller will turn as fast as possible whenever the distance from
     * the target angle is greater than 90.
     * </p>
     */
    public DefaultTurnController() {
        super(Reciprocal.of(90)); // 1/90, 0.01111111111

        /*
         * Turn controllers should almost always have a target of zero.
         * Remember, controllers operate on delta, not absolute.
         */
        setTarget(0); // attempt to minimize the angle delta.

        setMin(-1.0); // shouldn't ever turn faster than 1x negative.
        setMin(+1.0); // shouldn't ever turn faster than 1x positive.
    }
}
