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

package me.wobblyyyy.pathfinder.geometry;

/**
 * Shapes - the very first thing you learn in pre-school, and virtualized
 * field mapping.
 *
 * <p>
 * Shapes aren't the big boys here - shapes are a lower-level form of zones,
 * which are the components that actually go into maps.
 * </p>
 *
 * <p>
 * If you'd like to create a new zone, or if you'd like to create a new map,
 * or if you'd like to modify an existing map, you should go check out the
 * zones class instead of this one.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Shape {
    /**
     * Get how many components make up the shape.
     *
     * <p>
     * Components are loosely defined as anything smaller than a shape - in
     * the case of a rectangle, for example, there would be the following
     * components:
     * <ul>
     *     <li>Line</li>
     *     <ul>
     *         <li>Point</li>
     *         <li>Point</li>
     *     </ul>
     *     <li>Line</li>
     *     <ul>
     *         <li>Point</li>
     *         <li>Point</li>
     *     </ul>
     *     <li>Line</li>
     *     <ul>
     *         <li>Point</li>
     *         <li>Point</li>
     *     </ul>
     *     <li>Line</li>
     *     <ul>
     *         <li>Point</li>
     *         <li>Point</li>
     *     </ul>
     * </ul>
     * In total, that's 12. So many, so crazy!
     * </p>
     *
     * @return how many components make up the shape.
     */
    int getComponents();

    /**
     * Check whether or not a given point is inside the shape.
     *
     * <p>
     * Different shapes have different methods of checking whether or not
     * there's a point inside of the shape. For example, determining if a
     * point is contained in a rectangle is significantly more expensive than
     * determining if a point is contained inside of a circle.
     * </p>
     *
     * <p>
     * For that reason, repeated calls to check for points being in a shape
     * are not suggested. Unless you really need to, checking for shape
     * to shape collisions might be a better idea.
     * </p>
     *
     * @param point the point to check.
     * @return whether or not the point is in the shape.
     */
    boolean isPointInShape(Point point);

    /**
     * Check whether or not a given line is inside the shape.
     *
     * <p>
     * Line-checks are the most expensive operation for a shape to perform,
     * aside, of course, from initialization. Unless you have a very strong
     * reason to use this method, the {@link Shape#isPointInShape(Point)}
     * method is significantly more effective.
     * </p>
     *
     * @param line the line to check.
     * @return whether or not the line intersects with the shape.
     */
    boolean isLineInShape(Line line);
}
