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

package me.wobblyyyy.pathfinder.test.trajectory;

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.trajectory.SegmentInterpolator;
import org.junit.jupiter.api.Test;

public class SegmentInterpolatorTest extends SplineTest {
    SegmentInterpolator segment1 = new SegmentInterpolator(spline1);
    SegmentInterpolator segment2 = new SegmentInterpolator(spline2);
    SegmentInterpolator segment3 = new SegmentInterpolator(spline3);
    SegmentInterpolator segment4 = new SegmentInterpolator(spline4);

    StaticArray<Double> targetPercents = new StaticArray<>(
            0.000 / 100,
            25.00 / 100,
            50.00 / 100,
            75.00 / 100,
            100.0 / 100
    );

    @Test
    public void testSegmentInterpolation() {
        targetPercents.itr().forEach(percent -> {
            System.out.println("Segment 1 at " +
                    percent + " x: " +
                    segment1.atPercentX(percent).toString());
            System.out.println("Segment 1 at " +
                    percent + " y: " +
                    segment1.atPercentY(percent).toString());
            System.out.println("Segment 1 at " +
                    percent + ": " +
                    segment1.atPercent(percent).toString());
        });
    }
}
