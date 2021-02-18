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

package me.wobblyyyy.pathfinder.api;

import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.core.PathfinderManager;
import me.wobblyyyy.pathfinder.core.PromisedFinder;
import me.wobblyyyy.pathfinder.followers.PIDFollower;
import me.wobblyyyy.pathfinder.followers.SwerveFollower;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.thread.FollowerExecutor;

import java.util.ArrayList;

/**
 * The highest-level Pathfinder available.
 *
 * <p>
 * As an abstraction, this class provides everything you'd possibly need to
 * get started with finding paths and conquering noobs. Operation is fairly
 * simple - if you're not following a tutorial on what's going on right now,
 * you might want to go check one out.
 * </p>
 *
 * <p>
 * Documentation is always available online - check out Pathfinder's GitHub
 * repository if you're confused about where to find it. From there, you can
 * learn about all kinds of very wonderful and very cool topics.
 * </p>
 *
 * <p>
 * Although this class is designed to be as incredibly simple as possible,
 * there's a ton of customizable features behind Pathfinder. In order to
 * customize these features, and, actually, in order to instantiate a new
 * Pathfinder, you'll need to read up on all of the different options available
 * in the {@link PathfinderConfig} class.
 * </p>
 *
 * <p>
 * This class, as a result of its high level of abstraction, is little more
 * than a wrapper class with more user-friendly documentation. Anything that
 * you'd like to accomplish but can't with this class can likely be
 * accomplished by using the {@link PathfinderManager} class, which contains
 * many more methods and bits of functionality that you can make use of.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.1
 * @see PathfinderManager
 * @since 0.1.0
 */
public class Pathfinder {
    /**
     * The pathfinder's configuration.
     */
    private final PathfinderConfig config;

    /**
     * The code that interfaces with the pathfinding math.
     */
    private final PathfinderManager pathfinderManager;

    /**
     * Create a new Pathfinder instance.
     *
     * <p>
     * Although you CAN have multiple pathfinders running at the same time,
     * I don't think you'd really want to - a single pathfinder should do the
     * trick for you.
     * </p>
     *
     * <p>
     * Once a Pathfinder instance is created, the thread manager will begin
     * running its thread. Although technically a busy-wait, onSpinLoop()
     * should help to reduce any performance overhead caused by running a
     * thread in the background. If you'd like to manually control the thread
     * and thread manager, you can access the threadManager and run the stop
     * or start methods.
     * </p>
     *
     * @param config the pathfinder's configuration. You should go check
     *               out the field descriptions for the {@link PathfinderConfig}
     *               class if you're at all confused about what each of these
     *               configuration options do.
     */
    public Pathfinder(PathfinderConfig config) {
        /*
         * Set variables that are initialized in the constructor.
         *
         * The variables we're talking about here, are, of course:
         * - The pathfinder's configuration.
         * - The pathfinder's manager class.
         */

        this.config = config;
        pathfinderManager = new PathfinderManager(config);
    }

    /**
     * Get the position, including heading, of the robot.
     *
     * <p>
     * This position is based exclusively on the provided odometry system's
     * reported position - if this isn't accurate, its very likely an issue
     * you're having with your odometry system.
     * </p>
     *
     * @return the robot's position.
     */
    public HeadingPoint getPosition() {
        /*
         * Get the odometry subsystem from the Pathfinder configuration, and
         * determine the robot's position by asking the odometry system "hey,
         * where am I?"
         */
        return config.getOdometry().getPos();
    }

    /**
     * Find a path to a position and go to it!
     *
     * <p>
     * Generating a path, especially on a larger field, is rather expensive.
     * Unless you need to, you should try to avoid generating paths. The larger
     * your field is, and the higher your specificity value is, the more
     * expensive a pathfinding operation is.
     * </p>
     *
     * <p>
     * If the pathfinder fails to find/generate a path, nothing will happen.
     * No path will be followed. The robot won't move. The robot won't do
     * anything at all, actually. So if you're wondering why a path isn't
     * generating, well... now you know.
     * </p>
     *
     * <p>
     * Post-2.0, a huge optimization area would be converting off of a grid
     * based pathfinder and onto a node based one. Node pathfinders don't
     * require as large of a memory commitment from the host.
     * </p>
     *
     * <p>
     * As Pathfinder is still a developing library (and I'm still a developing
     * developer) (and I'm really awful at math), this might not always be
     * entirely the most efficient way possible. Optimizations will certainly
     * be needed.
     * </p>
     *
     * <p>
     * If your motors have the ability to enable and disable user control,
     * which they should if they're implementing the Motor interface, and
     * automatic thread-based updating is enabled, this method will handle
     * absolutely everything for you.
     * </p>
     *
     * <p>
     * After a path is found, the robot will attempt to navigate along the
     * path to the target position. If no path is found, nothing will happen.
     * If a path is found and execution begins, only for an issue to take place,
     * such as an odometry failure, a motor failure, or pathfinding timeout,
     * the pathfinder will stop and this method will finish. This method
     * is non-blocking, meaning you can execute other code after calling this.
     * </p>
     *
     * @param target the robot's target position and orientation.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#goToPosition(HeadingPoint)
     */
    public PromisedFinder goToPosition(HeadingPoint target) {
        /*
         * As a wrapper class, Pathfinder doesn't do very much.
         *
         * We simply pass along the request to the manager, which will
         * accomplish our task for us.
         */
        return getManager().goToPosition(target);
    }

    /**
     * Follow a given path.
     *
     * <p>
     * Unlike the {@link Pathfinder#goToPosition(HeadingPoint)} method, this
     * does not attempt to optimize a path to as few targets as possible.
     * Rather, this method generates paths to each of the waypoints and merges
     * them all together - thus ensuring that these waypoints are still reached
     * and not entirely ignored.
     * </p>
     *
     * <p>
     * If the pathfinder fails to find/generate a path, nothing will happen.
     * No path will be followed. The robot won't move. The robot won't do
     * anything at all, actually. So if you're wondering why a path isn't
     * generating, well... now you know.
     * </p>
     *
     * <p>
     * Path and trajectory generations can be very expensive. The longer the
     * path you'd like to follow, the more paths, and thus the more
     * trajectories, that need to be generated.
     * </p>
     *
     * <p>
     * This method is used most effectively in conjunction with any type of
     * trajectory-based follower, such as the {@link SwerveFollower}. Although
     * it does work with any other type of follower, you don't get any of the
     * benefits of waypoint-based path generation by using a simpler follower
     * type, such as the {@link PIDFollower}.
     * </p>
     *
     * @param points the points to be used as waypoints in path generation.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#followPath(HeadingPoint...)
     */
    public PromisedFinder followPath(ArrayList<HeadingPoint> points) {
        /*
         * As a wrapper class, Pathfinder provides very little functionality.
         *
         * The PathfinderManager class, then, does all of the heavy lifting.
         * The methods presented in the Pathfinder class are designed to
         * simplify the implementation of Pathfinder, not add to it.
         */
        return getManager().followPath(points);
    }

    /**
     * Lock the current thread and prevent it from progressing until the
     * pathfinder's execution thread is idle.
     *
     * <p>
     * This is used in situations such as an autonomous program where you
     * don't know how long the execution of a path will take. You can tell
     * the pathfinder to follow a path or go to a point, then call this
     * method, which will prevent the method from progressing until the
     * pathfinder indicates that it has finished moving.
     * </p>
     *
     * <p>
     * This can also be used during tele-operated parts of matches - say you'd
     * like to drive the robot to a certain point using the most mathematically
     * optimized route. You can tell the pathfinder to go to your target point
     * and then lock the tele-operated thread until the pathfinder has reached
     * its destination.
     * </p>
     *
     * <p>
     * This type of code is known as blocking code, meaning that nothing on
     * the current thread can happen until a certain thing has happened.
     * </p>
     *
     * @see PathfinderManager#lock()
     * @see FollowerExecutor#lock()
     */
    public void lock() {
        /*
         * Lock the current thread until the pathfinder's execution has
         * finished.
         *
         * Can we add a lockFor(Double) method that locks the current thread
         * for a given amount of time? That might make it a bit easier to
         * use Pathfinder in the future.
         */
        getManager().lock();
    }

    /**
     * Get the PathfinderManager that Pathfinder uses.
     *
     * <p>
     * Unless you want to do some really advanced and cool stuff with the
     * pathfinder, you don't need to get the manager to do anything.
     * </p>
     *
     * @return this instance of Pathfinder's manager class.
     */
    public PathfinderManager getManager() {
        return pathfinderManager;
    }
}
