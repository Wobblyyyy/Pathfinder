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

package me.wobblyyyy.pathfinder.test.kinematics;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.MeccanumKinematics;
import me.wobblyyyy.pathfinder.kinematics.MeccanumState;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.util.Distance;
import org.junit.jupiter.api.Test;

public class TestMeccanumKinematics {
    private final Point fr = new Point(+10, +10);
    private final Point fl = new Point(-10, +10);
    private final Point br = new Point(+10, -10);
    private final Point bl = new Point(-10, -10);

    private final MeccanumKinematics kinematics = new MeccanumKinematics(
            fl,
            fr,
            bl,
            br
    );

    public MeccanumState getState(double degrees) {
        Point target = Distance.inDirection(
                new Point(0, 0),
                AngleUtils.fixDeg(degrees),
                20
        );

        double x = target.getX();
        double y = target.getY();

        return kinematics.toMeccanumState(new RTransform(x, y, new Angle(0)));
    }

    @Test
    public void testMeccanumKinematics() {
        DynamicArray<Double> tests = new DynamicArray<>(
                0.0,
                90.0,
                180.0,
                270.0,
                360.0,
                45.0,
                135.0
        );

        tests.itr().forEach(degrees -> {
            MeccanumState state = getState(degrees);

            state.normalizeFromMaxUnderOne();

            System.out.println("States for " + degrees + ":");
            System.out.println("FR: " + state.fr().toString());
            System.out.println("FL: " + state.fl().toString());
            System.out.println("BR: " + state.br().toString());
            System.out.println("BL: " + state.bl().toString());
            System.out.println();
        });
    }
}
