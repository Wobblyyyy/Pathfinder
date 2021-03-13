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

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.*;
import org.junit.jupiter.api.Test;

public class TestSwerveFBKinematics {
    SwerveKinematics kinematics = new SwerveKinematics(new StaticArray<>(
            new Point(+10, +10),
            new Point(-10, +10),
            new Point(-10, -10),
            new Point(+10, -10)
    ));
    SwerveOdometry odometry = new SwerveOdometry(kinematics);

    SwerveState POWER_100 = new SwerveState(1.00, new Angle(0));
    SwerveState POWER_050 = new SwerveState(0.50, new Angle(0));
    SwerveState POWER_025 = new SwerveState(0.25, new Angle(0));

    Angle ANGLE_0 = new Angle(0);

    @Test
    public void testKinematics() {
        RTransform transform = new RTransform(1, 1, Angle.fromDegrees(0));

        StaticArray<SwerveState> states = kinematics.getStates(transform);

        states.itr().forEach(state -> {
            System.out.println("Power: " + state.getPower());
            System.out.println("Angle: " + state.getDegrees());
            System.out.println("");
        });
    }

    @Test
    public void testOdometry() {
        double seconds1 = 0;
        double seconds2 = 1;
        double seconds3 = 1;
        double seconds4 = 1.5;

        StaticArray<SwerveState> states = new StaticArray<>(
                POWER_100,
                POWER_100,
                POWER_100,
                POWER_100
        );

        odometry.update(seconds1, ANGLE_0, states);
        odometry.update(seconds2, ANGLE_0, states);
        odometry.update(seconds3, ANGLE_0, states);
        odometry.update(seconds4, ANGLE_0, states);

        System.out.println(odometry.getPosition().toString());
    }

    @Test
    public void testOdometryAndKinematics() {
        double s0 = 0;
        double s1 = 1;
        double s2 = 2;
        double s3 = 3;
        double s4 = 4;
        double s5 = 5;

        RTransform t1 = new RTransform(1, 0, new Angle(0));
        RTransform t2 = new RTransform(0, 1, new Angle(0));
        RTransform t3 = new RTransform(1, 1, new Angle(0));
        RTransform t4 = new RTransform(0, 0, new Angle(0));
        RTransform t5 = new RTransform(2, 2, new Angle(0));

        StaticArray<SwerveState> ss1 = kinematics.getStates(t1);
        StaticArray<SwerveState> ss2 = kinematics.getStates(t2);
        StaticArray<SwerveState> ss3 = kinematics.getStates(t3);
        StaticArray<SwerveState> ss4 = kinematics.getStates(t4);
        StaticArray<SwerveState> ss5 = kinematics.getStates(t5);

        odometry.update(s0, ANGLE_0, ss1);
        odometry.update(s1, ANGLE_0, ss1);
        System.out.println(odometry.getPosition().toString());

        odometry.update(s2, ANGLE_0, ss2);
        System.out.println(odometry.getPosition().toString());

        odometry.update(s3, ANGLE_0, ss3);
        System.out.println(odometry.getPosition().toString());

        odometry.update(s4, ANGLE_0, ss4);
        System.out.println(odometry.getPosition().toString());

        odometry.update(s5, ANGLE_0, ss5);
        System.out.println(odometry.getPosition().toString());
    }
}
