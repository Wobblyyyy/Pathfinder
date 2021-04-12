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
 * Analogous to the {@link DefaultTurnController}, but it's inverted.
 * Fancy, I know!
 *
 * @author Colin Robertson
 * @since 0.7.0
 * @see DefaultTurnController
 */
public class ReversedTurnController extends ProportionalController {
    /**
     * Create a new {@code ReversedTurnController}.
     *
     * @see DefaultTurnController#DefaultTurnController()
     */
    public ReversedTurnController() {
        super(-Reciprocal.of(90));

        setTarget(0);
        setMin(-1.0);
        setMax(+1.0);
    }
}
