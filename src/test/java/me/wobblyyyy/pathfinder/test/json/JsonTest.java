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

package me.wobblyyyy.pathfinder.test.json;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.json.JsonIO;
import me.wobblyyyy.pathfinder.trajectory.Spline;
import me.wobblyyyy.pathfinder.trajectory.Trajectory;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;
import java.util.ArrayList;

public class JsonTest {
    @Test
    public void testSaveAndLoadJson() {
        ArrayList<HeadingPoint> points = new ArrayList<>() {{
            add(new HeadingPoint(0, 0, 0));
            add(new HeadingPoint(10, 10, 0));
        }};

        String path = FileSystems.getDefault().getPath("Foo.txt").toString();

        try {
            JsonIO.savePoints(path, points);
            ArrayList<HeadingPoint> loadedPoints = JsonIO.loadPoints(path);

            for (HeadingPoint p : loadedPoints) {
                System.out.println(p.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSegments() {
        Trajectory trajectory = new Trajectory(new DynamicArray<>(
                new Spline(new DynamicArray<>(
                        new HeadingPoint(0, 0, 0),
                        new HeadingPoint(10, 10, 0)
                )),
                new Spline(new DynamicArray<>(
                        new HeadingPoint(10, 10, 0),
                        new HeadingPoint(20, 20, 0)
                ))
        ));

        String json = JsonIO.trajectoryToJson(trajectory);
        System.out.println(json);
        Trajectory newTrajectory = JsonIO.trajectoryFromJson(json);

        newTrajectory.getSegments().itr().forEach(segment -> {
            System.out.println(segment.toString());
        });
    }
}
