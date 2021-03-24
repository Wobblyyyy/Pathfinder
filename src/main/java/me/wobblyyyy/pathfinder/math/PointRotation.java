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

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.util.Distance;

public class PointRotation {
    private static final Point ZERO = new Point(0, 0);

    private PointRotation() {

    }

    public static Point rotatePoint(Point toRotate,
                                    double deltaDegrees) {
        double currentAngle = toRotate.getTheta();
        double nextAngle = currentAngle + deltaDegrees;
        return Distance.inDirection(
                ZERO,
                nextAngle,
                Distance.getDistance(ZERO, toRotate)
        );
    }

    public static DynamicArray<Point> rotatePoints(
            final DynamicArray<Point> toRotate,
            final double deltaDegrees) {
        DynamicArray<Point> rotatedPoints = new DynamicArray<>(toRotate.size());

        toRotate.itr().forEach(point -> {
            rotatedPoints.add(rotatePoint(point, deltaDegrees));
        });

        return rotatedPoints;
    }

    /**
     * Rotate a set of points by a specified amount of degrees.
     *
     * @param points          the points to rotate.
     * @param degreesToRotate the degrees to rotate the points by.
     * @return the rotated points.
     */
    public static DynamicArray<HeadingPoint> rotatePointsWithHeading(
            DynamicArray<HeadingPoint> points,
            double degreesToRotate) {
        DynamicArray<Point> asRegularPoints = new DynamicArray<>() {{
            points.itr().forEach(headingPoint -> add(headingPoint.getPoint()));
        }};
        DynamicArray<Point> rotated = PointRotation.rotatePoints(
                asRegularPoints,
                degreesToRotate
        );
        return new DynamicArray<>() {{
            rotated.itr().forEach(withoutHeading -> {
                add(withoutHeading.withHeading(
                        points.get(rotated.itr().index()).getHeading()
                ));
            });
        }};
    }
}
