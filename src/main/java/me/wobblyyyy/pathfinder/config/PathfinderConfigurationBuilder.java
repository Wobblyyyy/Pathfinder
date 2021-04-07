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

package me.wobblyyyy.pathfinder.config;

import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.map.Map;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.util.RobotProfile;

import java.util.function.Supplier;

/**
 * Simple interface for creating Pathfinder configurations. Please note
 * that there's no documentation in this class. If you're confused about
 * the meaning of any of these fields, you can check out the (rather
 * bulky) {@link PathfinderConfig} class, which explains everything
 * rather in-depth.
 *
 * <p>
 * You should follow this order while building a pathfinder config.
 * {@link #newConfiguration}, then any of the following:
 * {@link #followerType(Followers)},
 * {@link #drive(Drive)},
 * {@link #odometry(Odometry)},
 * {@link #map(Map)},
 * {@link #drivetrainSwapXY(boolean)},
 * {@link #drivetrainInvertX(boolean)},
 * {@link #drivetrainInvertY(boolean)},
 * {@link #odometrySwapXY(boolean)},
 * {@link #odometryInvertX(boolean)},
 * {@link #odometryInvertY(boolean)},
 * {@link #speed(double)}, and then finally (this one has to be last)
 * you should call {@link #build()}.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class PathfinderConfigurationBuilder {
    private Followers followerType = Followers.LINEAR;
    private Drive drive;
    private Odometry odometry;
    private Map map = new Map();
    private double speed = 0.225;
    private boolean drivetrainSwapXY = false;
    private boolean drivetrainInvertX = false;
    private boolean drivetrainInvertY = false;
    private boolean odometrySwapXY = false;
    private boolean odometryInvertX = false;
    private boolean odometryInvertY = false;
    private Supplier<Boolean> shouldRun = () -> true;

    private PathfinderConfigurationBuilder() {
    }

    public static PathfinderConfigurationBuilder newConfiguration() {
        return new PathfinderConfigurationBuilder();
    }

    public PathfinderConfigurationBuilder followerType(Followers _type) {
        followerType = _type;
        return this;
    }

    public PathfinderConfigurationBuilder drive(Drive _drive) {
        drive = _drive;
        return this;
    }

    public PathfinderConfigurationBuilder odometry(Odometry _odometry) {
        odometry = _odometry;
        return this;
    }

    public PathfinderConfigurationBuilder map(Map _map) {
        map = _map;
        return this;
    }

    public PathfinderConfigurationBuilder speed(double _speed) {
        speed = _speed;
        return this;
    }

    public PathfinderConfigurationBuilder drivetrainSwapXY(boolean should) {
        drivetrainSwapXY = should;
        return this;
    }

    public PathfinderConfigurationBuilder drivetrainInvertX(boolean should) {
        drivetrainInvertX = should;
        return this;
    }

    public PathfinderConfigurationBuilder drivetrainInvertY(boolean should) {
        drivetrainInvertY = should;
        return this;
    }

    public PathfinderConfigurationBuilder odometrySwapXY(boolean should) {
        odometrySwapXY = should;
        return this;
    }

    public PathfinderConfigurationBuilder odometryInvertX(boolean should) {
        odometryInvertX = should;
        return this;
    }

    public PathfinderConfigurationBuilder odometryInvertY(boolean should) {
        odometryInvertY = should;
        return this;
    }

    public PathfinderConfigurationBuilder tickerThreadOnStop(Supplier<Boolean> shouldRun) {
        this.shouldRun = shouldRun;
        return this;
    }

    public PathfinderConfig build() {
        if (drive == null)
            throw new IllegalArgumentException(
                    "Please use the setDrive() method before building!");
        if (odometry == null)
            throw new IllegalArgumentException(
                    "Please use the setOdometry() method before building!");

        return new PathfinderConfig(
                odometry,
                0, 0, 3, 0, 0, 0, 0,
                new RobotProfile(0, 0, 0, 0, 0, 0),
                drive,
                map,
                followerType,
                speed,
                true,
                true,
                true,
                shouldRun
        ) {{
            driveSwapXY(drivetrainSwapXY);
            driveInvertX(drivetrainInvertX);
            driveInvertY(drivetrainInvertY);
            odometrySwapXY(odometrySwapXY);
            odometryInvertX(odometryInvertX);
            odometryInvertY(odometryInvertY);
        }};
    }
}
