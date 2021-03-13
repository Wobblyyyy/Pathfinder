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

package me.wobblyyyy.pathfinder.util;

/**
 * Static time measurement utility designed to reduce the length of time
 * values by scaling them all down at start.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class Time {
    /**
     * The time at which this library's execution begins. This is used as an
     * offset.
     */
    private static final double startTime = System.currentTimeMillis();

    /**
     * Make it so Time can't be instantiated.
     */
    private Time() {

    }

    /**
     * Get the current relative time. This is, most simply, the current time,
     * minus whatever time this method was first called at.
     *
     * @return the current relative time.
     */
    public static double relativeTime() {
        return System.currentTimeMillis() - startTime;
    }
}
