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

package me.wobblyyyy.pathfinder.followers;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.followers.DualPidFollower;
import me.wobblyyyy.pathfinder.followers.LinearFollower;
import me.wobblyyyy.pathfinder.followers.PidFollower;
import me.wobblyyyy.pathfinder.followers.TriPidFollower;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

public class FollowerFactory {
    public static PidFollower pid(PathfinderConfig config,
                                  DynamicArray<HeadingPoint> points) {
        return new PidFollower(
                config.getDrive(),
                config.getOdometry(),
                config.getOdometry().getPos(),
                points.get(1),
                config.getSpeed()
        );
    }

    public static DualPidFollower dualPid(PathfinderConfig config,
                                          DynamicArray<HeadingPoint> points) {
        return new DualPidFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(1),
                config.getSpeed()
        );
    }

    public static TriPidFollower triPid(PathfinderConfig config,
                                        DynamicArray<HeadingPoint> points) {
        return new TriPidFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(1),
                config.getSpeed()
        );
    }

    public static LinearFollower linear(PathfinderConfig config,
                                        DynamicArray<HeadingPoint> points) {
        return new LinearFollower(
                config.getDrive(),
                config.getOdometry(),
                points.get(0),
                points.get(1),
                config.getSpeed()
        );
    }
}
