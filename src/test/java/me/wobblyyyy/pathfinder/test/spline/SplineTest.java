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

package me.wobblyyyy.pathfinder.test.spline;

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.trajectory.Spline;
import me.wobblyyyy.pathfinder.util.Distance;
import org.junit.jupiter.api.Test;

public class SplineTest {
    /*
     * Four points here:
     * TOP
     * RIGHT
     * BOTTOM
     * LEFT
     */

    final double radius = 10;

    Point center = new Point(0, 0);
    Point top = new Point(0, radius);
    Point right = new Point(radius, 0);
    Point bottom = new Point(0, -radius);
    Point left = new Point(-radius, 0);

    Point topToRight = Distance.inDirection(center, 45, radius);
    Point rightToBottom = Distance.inDirection(center, -45, radius);
    Point bottomToLeft = Distance.inDirection(center, -135, radius);
    Point leftToTop = Distance.inDirection(center, 135, radius);

    StaticArray<HeadingPoint> arrayQuad1 = new StaticArray<>(
            top.withHeading(0),
            topToRight.withHeading(0),
            right.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad4 = new StaticArray<>(
            right.withHeading(0),
            rightToBottom.withHeading(0),
            bottom.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad3 = new StaticArray<>(
            bottom.withHeading(0),
            bottomToLeft.withHeading(0),
            left.withHeading(0)
    );
    StaticArray<HeadingPoint> arrayQuad2 = new StaticArray<>(
            left.withHeading(0),
            leftToTop.withHeading(0),
            top.withHeading(0)
    );

    Spline spline1 = new Spline(arrayQuad1);
    Spline spline2 = new Spline(arrayQuad2);
    Spline spline3 = new Spline(arrayQuad3);
    Spline spline4 = new Spline(arrayQuad4);

    @Test
    public void printSplines() {
        System.out.println("Spline 1: " + spline1.toString());
        System.out.println("Spline 2: " + spline2.toString());
        System.out.println("Spline 3: " + spline3.toString());
        System.out.println("Spline 4: " + spline4.toString());
    }
}
