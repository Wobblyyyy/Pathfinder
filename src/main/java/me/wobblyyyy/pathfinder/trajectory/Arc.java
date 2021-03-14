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

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.intra.ftc2.utils.math.Math;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Line;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.util.Distance;

/**
 * An {@code Arc} is an abstraction of the {@code Spline} class designed to
 * simplify the process of creating arcs that behave in a reliable way. The
 * {@code Arc} class allows you to create simple arcs with a single control
 * point between the start and stop points.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class Arc extends Spline {
    /**
     * Create a new {@code Spline} that will hit each of the required points.
     * Splines are created so that they hit each and every one of the target
     * points, meaning that if you tell the robot to pass through (10, 10),
     * the robot will pass through (10, 10) no matter what.
     *
     * @param points a container that contains each of the required points.
     *               Each and every one of these points will be passed through
     *               directly by the spline computational system, meaning that
     *               the robot will go exactly where you tell it to go.
     */
    private Arc(Arrayable<HeadingPoint> points) {
        super(points);
    }

    /**
     * Create an {@code Arc} instance by inserting a control point at the
     * midpoint of the line between the start and stop points and applying
     * a (X, Y) translational deviation to the newly-created point.
     *
     * @param start        the start point of the arc.
     * @param bendDistance how far from the center of the start and stop points
     *                     the arc will bend in a given direction.
     * @param stop         the stop point of the arc.
     * @return an arc with the requested bend.
     */
    public static Arc bend(HeadingPoint start,
                           double bendDistance,
                           HeadingPoint stop) {
        Line line = new Line(start, stop);
        double degreesBetween = HeadingPoint.angleOfDeg(start, stop);
        double perpendicularToLine = AngleUtils.fixDeg(degreesBetween + 90);
        Point midpoint = line.getMidpoint();
        HeadingPoint curvePoint = Distance.inDirection(
                midpoint,
                perpendicularToLine,
                bendDistance
        ).withHeading(Math.average(start.getHeading(), stop.getHeading()));
        return new Arc(new StaticArray<>(start, curvePoint, stop));
    }
}
