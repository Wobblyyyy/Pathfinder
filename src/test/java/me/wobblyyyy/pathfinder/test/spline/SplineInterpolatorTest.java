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

import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.trajectory.SplineInterpolator;
import me.wobblyyyy.pathfinder.util.Distance;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class SplineInterpolatorTest {
    ArrayList<Double> xControl = new ArrayList<>() {{
        add(0.0);
        add(10.0);
        add(20.0);
    }};
    ArrayList<Double> yControl = new ArrayList<>() {{
        add(0.0);
        add(10.0);
        add(20.0);
    }};
    Point splineCenter = new Point(0, 20);
    Point logicalTangent = Distance.inDirection(
            splineCenter,
            -45,
            20
    );
    Point t = Distance.inDirection(
            splineCenter,
            -90,
            20
    );

    @Test
    public void testSplineInterpolation() {
        SplineInterpolator spline = SplineInterpolator.monotoneCubic(
                xControl,
                yControl
        );

        System.out.println(spline.toString());

        double yFromX = spline.interpolateFromX(logicalTangent.getX());
        double xFromY = spline.interpolateFromY(logicalTangent.getY());

        System.out.println("Y from X: " + yFromX);
        System.out.println("X from Y: " + xFromY);

        System.out.println("t: " + t.toString());
    }
}
