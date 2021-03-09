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

package me.wobblyyyy.pathfinder.config;

import me.wobblyyyy.pathfinder.core.Followers;
import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.map.Map;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.RobotProfile;

/**
 * Abstraction of the {@link PathfinderConfig} class designed to make life a
 * little bit easier and those parameters a little bit less grouped together.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
@SuppressWarnings("unused")
public class PathfinderOptions {
    private final PathfinderConfig config = new PathfinderConfig();

    /**
     * Create a new PathfinderOptions instance.
     *
     * <p>
     * All of the options are configured using the provided methods in this
     * class, NOT right here. You can put these methods in an anonymous class
     * initializer - very epic!
     * </p>
     */
    public PathfinderOptions() {

    }

    public void setRobotX(double x) {
        config.setRobotX(x);
    }

    public void setRobotY(double y) {
        config.setRobotY(y);
    }

    public void setFieldX(double x) {
        config.setFieldWidth((int) x);
    }

    public void setFieldY(double y) {
        config.setFieldHeight((int) y);
    }

    public void setMap(Map map) {
        config.setMap(map);
    }

    public void setRobotProfile(RobotProfile profile) {
        config.setProfile(profile);
    }

    public void setOdometry(Odometry odometry) {
        config.setOdometry(odometry);
    }

    public void setDrive(Drive drive) {
        config.setDrive(drive);
    }

    public void setFollowerType(Followers follower) {
        config.setFollower(follower);
    }

    public void setSpeed(double speed) {
        config.setSpeed(speed);
    }

    public void setGapX(double x) {
        config.setGapX(x);
    }

    public void setGapY(double y) {
        config.setGapY(y);
    }

    public PathfinderConfig build() {
        return config;
    }
}
