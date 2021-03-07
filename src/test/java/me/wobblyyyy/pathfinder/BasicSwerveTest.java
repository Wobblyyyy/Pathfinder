/*
 *  ======================================================================
 *  || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 *  ||                                                                  ||
 *  || This file is part of the "Pathfinder" project, which is licensed ||
 *  || and distributed under the GPU General Public License V3.         ||
 *  ||                                                                  ||
 *  || Pathfinder is available on GitHub:                               ||
 *  || https://github.com/Wobblyyyy/Pathfinder                          ||
 *  ||                                                                  ||
 *  || Pathfinder's license is available:                               ||
 *  || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 *  ||                                                                  ||
 *  || Re-distribution of this, or any other files, is allowed so long  ||
 *  || as this same copyright notice is included and made evident.      ||
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
 */

package me.wobblyyyy.pathfinder;

import me.wobblyyyy.pathfinder.api.Pathfinder;
import me.wobblyyyy.pathfinder.config.SimpleConfig;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.map.Map;
import org.junit.jupiter.api.Test;

public class BasicSwerveTest {
    private Drive drive = new TestSwerveDrive();
    private Map map = new CoolMap();
    private TestOdometry odometry = new TestOdometry();

    @Test
    public void testSwerveDriving() {
        SimpleConfig config = new SimpleConfig() {{
            setDrive(drive);
            setMap(map);
            setOdometry(odometry);
            setRobotX(10);
            setRobotY(10);
        }};

        Pathfinder pathfinder = new Pathfinder(config);

        new Thread(() -> {
            double start = 0;
            double startTime = System.currentTimeMillis();
            double target = 5000;
            double targetTime = System.currentTimeMillis() + target;

            while (System.currentTimeMillis() < targetTime) {
                double current = System.currentTimeMillis() - startTime;
                double percent = current / target * 10;
                odometry.setPos(new HeadingPoint(
                        percent,
                        percent,
                        percent
                ));
            }
        }).start();

        pathfinder.goToPosition(new HeadingPoint(10, 10, 10));
        pathfinder.lock();
    }
}
