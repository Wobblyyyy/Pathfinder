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

package me.wobblyyyy.pathfinder.test.controller;

import me.wobblyyyy.pathfinder.control.Controller;
import me.wobblyyyy.pathfinder.control.DefaultTurnController;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import org.junit.jupiter.api.Test;

public class TestTurnController {
    private double[] deltas;

    @Test
    public void testMinimumAngleDelta() {
        double[] a = {0, 45, 90, 135, 180, 225, 270, 315, 360};
        double[] b = new double[a.length];
        deltas = new double[a.length];

        for (int i = 0; i < a.length; i++) {
            b[i] = AngleUtils.fixDeg(a[i] + 90 * i);
        }

        for (int i = 0; i < a.length; i++) {
            double delta = AngleUtils.minimumAngleDelta(a[i], b[i]);
            deltas[i] = delta;

            System.out.println("A: " + a[i]);
            System.out.println("B: " + b[i]);
            System.out.println("Delta: " + delta);
            System.out.println();
        }
    }

    @Test
    public void testTurnController() {
        testMinimumAngleDelta();

        Controller controller = new DefaultTurnController() {{
            setTarget(0);
        }};

        for (double delta : deltas) {
            System.out.println("Delta: " + delta);
            System.out.println("Power: " + controller.calculate(delta));
            System.out.println();
        }
    }
}
