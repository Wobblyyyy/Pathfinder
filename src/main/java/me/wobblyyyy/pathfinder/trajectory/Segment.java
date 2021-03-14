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

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.Point;

/**
 * Trajectory segments are the core components behind trajectories, which are
 * best represented as a combination of segments. Segments can take on any form
 * you'd desire - splines, arcs, curves, regular old lines, etc - just about
 * whatever you want. The {@code Segment} interface is designed to provide an
 * easy way to meld together different representations of trajectory segments,
 * allowing for simplified complex path management and following.
 *
 * <p>
 * Trajectory segments don't actually do very much on their own. For example,
 * robot jerk is not taken into account when generating and/or following any
 * given trajectory. Thus, trajectory segments must be constructed in a way
 * such that the robot CAN accurately follow the trajectory. Whether this means
 * slowing the robot down, making the segment "larger" (for curves, really)
 * or whatever other modifications need to be made, these modifications are to
 * be handled by the user, not us.
 * </p>
 *
 * <p>
 * All trajectory segments should provide methods for interpolating a point on
 * the segment based on either an X or a Y value.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public interface Segment {
    /**
     * Get a point from the segment based on an X value. This point should fit
     * within the bounds of the segment and should be represented partially by
     * the provided value, which provides a basis for interpolation from there.
     *
     * @param xValue the value that the segment should interpolate from. This
     *               means, in essence, that the segment should determine the
     *               nearest possible point to the requested X value.
     * @return an interpolated point based on an inputted value. This point has
     * no regard for heading - rather, it's a simple and plain point that can
     * be used for following the trajectory or just figuring out where a
     * certain value on the trajectory lies.
     */
    Point interpolateFromX(double xValue);

    /**
     * Get a point from the segment based on an Y value. This point should fit
     * within the bounds of the segment and should be represented partially by
     * the provided value, which provides a basis for interpolation from there.
     *
     * @param yValue the value that the segment should interpolate from. This
     *               means, in essence, that the segment should determine the
     *               nearest possible point to the requested Y value.
     * @return an interpolated point based on an inputted value. This point has
     * no regard for heading - rather, it's a simple and plain point that can
     * be used for following the trajectory or just figuring out where a
     * certain value on the trajectory lies.
     */
    Point interpolateFromY(double yValue);

    /**
     * Get the angle which the robot should be facing at the given trajectory.
     * This angle is handled separately from the interpolation methods to
     * increase the degree of flexibility the {@code Segment} interface will
     * provide both internally and externally.
     *
     * @param point the point that will serve as a basis for fetching the
     *              desired robot heading.
     * @return the most closely interpolated heading/angle at a given point.
     * This angle typically corresponds to how far along the segment the robot
     * has travelled, but it's up to the implementations of the segment class
     * to decide how exactly this is handled. Yay interfaces!
     */
    Angle angleAt(Point point);

    /**
     * Get a combination of the minimum X and Y values for the segment. This
     * point is used to reduce primitive clutter - instead of returning an
     * individual X and Y double, we can return a single point that represents
     * our requested information.
     *
     * @return a combination of the requested X and Y values. This is used
     * mostly in segment interpolation, as individual segments are predicated
     * on raw X and Y coordinates, not interpolatable coordinates.
     */
    Point minimum();

    /**
     * Get a combination of the maximum X and Y values for the segment. This
     * point is used to reduce primitive clutter - instead of returning an
     * individual X and Y double, we can return a single point that represents
     * our requested information.
     *
     * @return a combination of the requested X and Y values. This is used
     * mostly in segment interpolation, as individual segments are predicated
     * on raw X and Y coordinates, not interpolatable coordinates.
     */
    Point maximum();

    /**
     * Get the point where the segment starts. This is defined as the position
     * that represents 0 percent of the segment's completion.
     *
     * @return the point at which the segment begins.
     */
    Point start();

    /**
     * Get the point where the segment ends. This is defined as the position
     * that represents 100 percent of the segment's completion.
     *
     * @return the point at which the segment ends.
     */
    Point end();
}
