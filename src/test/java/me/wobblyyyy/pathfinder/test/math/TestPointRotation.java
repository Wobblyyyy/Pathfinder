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

package me.wobblyyyy.pathfinder.test.math;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.math.PointRotation;
import me.wobblyyyy.pathfinder.trajectory.Arcs;
import org.junit.jupiter.api.Test;

public class TestPointRotation {
    @Test
    public void rotatePointTest() {
        Point point = new Point(0, 10);

        Point rotated0 = PointRotation.rotatePoint(point, 0);
        Point rotated1 = PointRotation.rotatePoint(rotated0, 90);
        Point rotated2 = PointRotation.rotatePoint(rotated1, 90);
        Point rotated3 = PointRotation.rotatePoint(rotated2, 90);
        Point rotated4 = PointRotation.rotatePoint(rotated3, 90);

        System.out.println("0: " + rotated0.toString());
        System.out.println("1: " + rotated1.toString());
        System.out.println("2: " + rotated2.toString());
        System.out.println("3: " + rotated3.toString());
        System.out.println("4: " + rotated4.toString());
    }

    @Test
    public void rotateManyPoints() {
        DynamicArray<HeadingPoint> headingPoints = Arcs.interpolateQuad1(10);
        DynamicArray<Point> points = new DynamicArray<>(headingPoints.size());
        headingPoints.itr().forEach(points::add);
        DynamicArray<Double> rotationAmounts = new DynamicArray<>(
                0D,
                45D,
                90D,
                135D,
                180D,
                360D,
                720D
        );
        rotationAmounts.itr().forEach(degreesToRotate -> {
            DynamicArray<Point> rotated = PointRotation.rotatePoints(
                    points,
                    degreesToRotate
            );
            System.out.println(
                    "Points rotated by " + degreesToRotate + " degrees"
            );
            rotated.itr().forEach(point -> {
                System.out.println(point.toString());
            });
            System.out.println();
        });
    }
}
