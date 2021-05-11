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

package me.wobblyyyy.pathfinder.test.drive;

import me.wobblyyyy.pathfinder.drive.MeccanumDrive;
import me.wobblyyyy.pathfinder.kinematics.RTransform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class TestMeccanumDrive {
    private DummyMotor fr;
    private DummyMotor fl;
    private DummyMotor br;
    private DummyMotor bl;
    private MeccanumDrive drive;

    private List<RTransform> transforms = new ArrayList<>() {{
        add(new RTransform(0, 0, 0));
        add(new RTransform(1, 0, 0));
        add(new RTransform(0, 1, 0));
        add(new RTransform(0, 0, 1));
        add(new RTransform(1, 0, 1));
        add(new RTransform(0, 1, 1));
        add(new RTransform(1, 1, 0));
        add(new RTransform(1, 1, 1));
        add(new RTransform(0.2, 0.2, 0.2));
        add(new RTransform(0.2, 0, 0.5));
        add(new RTransform(0, 0.2, 0.5));
        add(new RTransform(0, 0, 0.5));
        add(new RTransform(0.75, 0.75, 0));
        add(new RTransform(0, 0, 0.5));
        add(new RTransform(0, 0, 1));
        add(new RTransform(0, 0, 1.5));
        add(new RTransform(0, 0, 2));
        add(new RTransform(1, 0, 0.5));
        add(new RTransform(0, 1, 0.5));
        add(new RTransform(0.1, 0.1, 0.5));
        add(new RTransform(-0.5, -0.5, 1));
        add(new RTransform(-0.5, 0, 0));
        add(new RTransform(-0.5, -0.5, 0));
        add(new RTransform(-0.5, 0, -0.5));
        add(new RTransform(0, -0.5, -0.5));
        add(new RTransform(-0.5, -0.5, 0.5));
    }};

    @BeforeEach
    public void createDrive() {
        this.fr = new DummyMotor();
        this.fl = new DummyMotor();
        this.br = new DummyMotor();
        this.bl = new DummyMotor();
        this.drive = new MeccanumDrive(
                fl, fr, bl, br,
                336 / 100D, 403.5 / 100D
        );
    }

    private void printPower() {
        System.out.println("FR: " + fr.getPower());
        System.out.println("FL: " + fl.getPower());
        System.out.println("BR: " + br.getPower());
        System.out.println("BL: " + bl.getPower());
    }

    @Test
    public void testMeccanumTurning() {
        for (RTransform t : transforms) {
            System.out.println("===== Transforms =====");
            System.out.println("X: " + t.getX() + " inches");
            System.out.println("Y: " + t.getY() + " inches");
            System.out.println("Turn: " + t.getTurn() + " rad/s");
            System.out.println("=====   States   =====");
            drive.drive(t);
            printPower();
            System.out.println();
        }
    }
}
