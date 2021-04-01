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

package me.wobblyyyy.pathfinder.geometry;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.math.functional.PointRotation;

public class CircleInterpolator {
    private final Point center;
    private final double startDegrees;
    private final double endDegrees;
    private final double radius;
    private final double samples;

    public CircleInterpolator(Point center,
                              double startDegrees,
                              double endDegrees,
                              double radius,
                              double samples) {
        this.center = center;
        this.startDegrees = startDegrees;
        this.endDegrees = endDegrees;
        this.radius = radius;
        this.samples = samples;
    }

    public DynamicArray<Point> getPointsFromStart() {
        if (startDegrees < endDegrees) {
            return getPointsFromEnd();
        }

        DynamicArray<Point> points = new DynamicArray<>();

        for (double deg = startDegrees;
             deg >= endDegrees;
             deg += ((endDegrees - startDegrees) / samples)) {
            points.add(Distance.inDirection(
                    center,
                    deg,
                    radius
            ));
        }

        return points;
    }

    public DynamicArray<Point> getPointsFromEnd() {
        CircleInterpolator inverseInterpolator = new CircleInterpolator(
                center,
                endDegrees,
                startDegrees,
                radius,
                samples
        );

        DynamicArray<Point> inverted = inverseInterpolator.getPointsFromStart();
        DynamicArray<Point> normal = new DynamicArray<>() {{
            inverted.itr().forEach(this::add);
        }};
        normal = PointRotation.rotatePoints(normal, 270);

        return normal;
    }

    public static DynamicArray<Point> interpolate(Point center,
                                                  double startDegrees,
                                                  double endDegrees,
                                                  double radius,
                                                  double samples) {
        return (new CircleInterpolator(
                center,
                startDegrees,
                endDegrees,
                radius,
                samples
        )).getPointsFromStart();
    }
}
