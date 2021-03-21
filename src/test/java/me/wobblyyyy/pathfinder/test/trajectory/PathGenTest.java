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

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.json.JsonIO;
import me.wobblyyyy.pathfinder.trajectory.Linear;
import me.wobblyyyy.pathfinder.trajectory.PathGenerator;
import me.wobblyyyy.pathfinder.trajectory.Spline;
import me.wobblyyyy.pathfinder.trajectory.Trajectory;
import org.junit.jupiter.api.Test;

public class PathGenTest {
    @Test
    public void testGeneratePath() {
        Trajectory trajectory = new Trajectory(new DynamicArray<>(
                new Linear(
                        new HeadingPoint(0, 0, 0),
                        new HeadingPoint(10, 10, 0)
                ),
                new Spline(new DynamicArray<>(
                        new HeadingPoint(10, 10, 0),
                        new HeadingPoint(14, 13, 0),
                        new HeadingPoint(20, 20, 0),
                        new HeadingPoint(25, 25, 0)
                ))
        ));

        DynamicArray<HeadingPoint> points = PathGenerator.toPath(trajectory);

        System.out.println(JsonIO.pointsToJson(JsonIO.toArrayList(points)));
    }
}
