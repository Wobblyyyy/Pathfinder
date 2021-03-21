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

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.intra.ftc2.utils.math.Range;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Convert a trajectory into a path.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class PathGenerator {
    public static DynamicArray<HeadingPoint> toPath(Trajectory trajectory) {
        DynamicArray<Segment> segments = trajectory.getSegments();
        DynamicArray<HeadingPoint> points = new DynamicArray<>(segments.size() * 100);

        segments.itr().forEach(segment -> {
            Range xRange = new Range(
                    Math.min(segment.start().getX(), segment.end().getX()),
                    Math.max(segment.start().getX(), segment.end().getX())
            );

            DynamicArray<HeadingPoint> localPoints = new DynamicArray<>();

            for (double i = xRange.getMin();
                 i < xRange.getMax();
                 i += ((xRange.getMax() - xRange.getMin()) / 50)) {

                if (segment.start().getX() < segment.end().getX()) {
                    points.add(Point.withHeading(
                            segment.interpolateFromX(i),
                            segment.angleAt(segment
                                    .interpolateFromX(i))
                                    .getDegrees()
                    ));
                } else {
                    localPoints.add(0, Point.withHeading(
                            segment.interpolateFromX(i),
                            segment.angleAt(segment
                                    .interpolateFromX(i))
                                    .getDegrees()
                    ));
                }
            }

            final int maxIndex = points.size() - 1;
            localPoints.itr().forEach(point -> points.add(maxIndex, point));
        });

        return points;
    }
}
