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

import me.wobblyyyy.pathfinder.kinematics.MeccanumState;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import me.wobblyyyy.pathfinder.kinematics.RelativeMeccanumKinematics;
import org.junit.jupiter.api.Test;

public class TestRelativeMeccanumKinematics {
    private final RelativeMeccanumKinematics kinematics = new RelativeMeccanumKinematics();

    public void printState(RTransform transform) {
        System.out.println(transform.toString());

        MeccanumState state = kinematics.toMeccanumState(transform);
        System.out.println("FL: " + state.flPower());
        System.out.println("FR: " + state.frPower());
        System.out.println("BL: " + state.blPower());
        System.out.println("BR: " + state.brPower());

        System.out.println();
    }

    @Test
    public void testKinematics() {
        printState(new RTransform(1, 1, 0));
        printState(new RTransform(1, 0, 1));
        printState(new RTransform(0, 1, 1));
    }
}
