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

package me.wobblyyyy.pathfinder.core;

import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.followers.LinearFollower;
import me.wobblyyyy.pathfinder.followers.PIDFollower;
import me.wobblyyyy.pathfinder.followers.SwerveFollower;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;

import java.util.ArrayList;

/**
 * Factory used for generating followers.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Factory {
    /**
     * The configuration that's in use.
     */
    public static PathfinderConfig config;

    /**
     * PID follower factory.
     */
    public static PID pid;

    /**
     * Linear follower factory.
     */
    public static Linear linear;

    /**
     * Swerve follower factory.
     */
    public static Swerve swerve;

    /**
     * Initialize the factory with the pathfinder's configuration.
     *
     * @param config the pathfinder's configuration.
     */
    public static void init(PathfinderConfig config) {
        Factory.config = config;

        pid = new PID();
        linear = new Linear();
        swerve = new Swerve();
    }

    /**
     * Core used between different follower factories.
     */
    public interface FollowerFactory {
        Follower build(ArrayList<HeadingPoint> points);
    }

    /**
     * Used for generating new PID followers.
     */
    public static class PID implements FollowerFactory {
        @Override
        public Follower build(ArrayList<HeadingPoint> points) {
            return new PIDFollower(
                    config.getDrive(),
                    config.getProfile(),
                    points.get(0),
                    points.get(1)
            );
        }
    }

    /**
     * Used for generating new Linear followers.
     */
    public static class Linear implements FollowerFactory {
        @Override
        public Follower build(ArrayList<HeadingPoint> points) {
            return new LinearFollower(
                    config.getDrive(),
                    config.getOdometry(),
                    points.get(0),
                    points.get(1),
                    0.5
            );
        }
    }

    /**
     * Used for generating new Swerve followers.
     */
    public static class Swerve implements FollowerFactory {
        @Override
        public Follower build(ArrayList<HeadingPoint> points) {
            return new SwerveFollower(
                    points,
                    config.getOdometry(),
                    config.getDrive(),
                    config.getGapX(),
                    config.getGapY(),
                    config.getProfile()
            );
        }
    }
}
