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

package me.wobblyyyy.pathfinder.core;

import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.ArrayList;

/**
 * Interface used for connecting different types of path generation.
 *
 * <p>
 * Generators are essentially little tiny itsy bitsy pathfinders. In order to
 * give the end user more control over the operation of this library, there
 * are several different generators that can be used.
 * </p>
 *
 * <p>
 * Generators shouldn't be toggled on/off during runtime - the pathfinder
 * configuration should determine which generators are to be used during the
 * pathfinder's operation.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Generator {
    /**
     * Get a path, from a start coordinate to an end coordinate, that's
     * directly readable by our implementation of the pathfinding system.
     *
     * @param start the start coordinate.
     * @param end   the end coordinate.
     * @return a group, composed of individual Point items.
     */
    ArrayList<Point> getCoordinatePath(Point start, Point end);
}
