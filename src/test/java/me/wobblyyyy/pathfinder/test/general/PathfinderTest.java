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

package me.wobblyyyy.pathfinder.test.general;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import org.junit.jupiter.api.Test;

public class PathfinderTest {
    Pathfinder pathfinder;

    @Test
    public void testPathfinder() throws InterruptedException {
        DynamicArray<HeadingPoint> points = new DynamicArray<>() {{
            add(new HeadingPoint(
                    0,
                    0,
                    0
            ));
            add(new HeadingPoint(
                    20,
                    30,
                    19
            ));
            add(new HeadingPoint(
                    78,
                    103,
                    24
            ));
            add(new HeadingPoint(
                    201,
                    201,
                    201
            ));
        }};

        long execPID = 0;
        long execTRA = 0;
        pathfinder = new Pathfinder(new CoolConfig());

        execTRA = System.currentTimeMillis();
        pathfinder.followPath(points);
//        pathfinder.lock();
        execTRA = System.currentTimeMillis() - execTRA;

        DynamicArray<Point> path = pathfinder.getManager().getWaypointPath(points);

        path.itr().forEach(point -> System.out.println(point.toString()));

        System.out.println(execTRA);
    }
}