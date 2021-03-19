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
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.util.Extra;
import org.junit.jupiter.api.Test;

public class RepeatedPoints {
    @Test
    public void testRepeatedPoints() {
        HeadingPoint a = new HeadingPoint(10, 10, 0);
        HeadingPoint b = new HeadingPoint(15, 15, 0);
        HeadingPoint c = new HeadingPoint(20, 15, 0);
        HeadingPoint d = new HeadingPoint(20, 20, 0);
        HeadingPoint e = new HeadingPoint(15, 15, 0);
        HeadingPoint f = new HeadingPoint(15, 10, 0);

        DynamicArray<Point> removed = Extra.removeAdjacentDuplicates(
                new DynamicArray<>(a, b, c, d, e, f)
        );

        removed.itr().forEach(p -> System.out.println(p.toString()));
    }
}
