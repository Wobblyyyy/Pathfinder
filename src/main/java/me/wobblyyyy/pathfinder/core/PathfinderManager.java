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

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.annotations.Async;
import me.wobblyyyy.pathfinder.annotations.Sync;
import me.wobblyyyy.pathfinder.annotations.Wait;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.error.InvalidPathException;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.map.Map;
import me.wobblyyyy.pathfinder.thread.FollowerExecutor;
import me.wobblyyyy.pathfinder.thread.PathfinderThreadManager;
import me.wobblyyyy.pathfinder.util.Extra;

/**
 * A manager, designed for interacting with the whole of Pathfinder.
 *
 * <p>
 * Unlike the more refined Pathfinder, this class is filled with all sorts
 * of different methods for all sorts of different purposes. Unless you have
 * a pretty strong reason to be working directly with the manager, it's most
 * certainly a better idea to stick to the most abstract form of Pathfinder
 * that you can possibly get to.
 * </p>
 *
 * <p>
 * With that being said, however, you can still feel free to do whatever you'd
 * like here - I'm not gonna stop you. All of the public-facing methods in this
 * class are fairly well-documented, so I'd suggest you read that documentation
 * before trying something pointless.
 * </p>
 *
 * <p>
 * Best of luck in your pathfinding adventures!
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class PathfinderManager {
    /**
     * The pathfinder's configuration.
     */
    private final PathfinderConfig config;

    /**
     * The robot's width.
     *
     * @see PathfinderConfig#getRobotX()
     */
    private final int width;

    /**
     * The robot's height.
     *
     * @see PathfinderConfig#getRobotY()
     */
    private final int height;

    /**
     * The specificity of the field.
     */
    private final int specificity;

    /**
     * The field's map.
     */
    private final Map map;

    /**
     * A reference to the pathfinding pathfinder.
     */
    private GeneratorManager finder;

    /**
     * The follower execution system.
     */
    private FollowerExecutor exec;

    /**
     * Manager used for updating loaded odometry systems.
     */
    private PathfinderThreadManager thread;

    /**
     * Create a new PathfinderManager.
     *
     * <p>
     * All of the Core subcomponents are initialized here as well. Although
     * they should work perfectly fine, if you're getting initialization errors,
     * you're probably doing something wrong with your pathfinder configuration.
     * </p>
     *
     * @param config the pathfinder's configuration class.
     */
    @Sync
    public PathfinderManager(PathfinderConfig config) {
        this.config = config;

        this.width = config.getFieldWidth();
        this.height = config.getFieldHeight();
        this.specificity = config.getSpecificity();
        this.map = config.getMap();
    }

    /**
     * Open the {@code PathfinderManager}'s threads and make it start doing
     * its thing. The {@code PathfinderManager} or {@code Pathfinder} MUST be
     * opened before it can be used - not opening the {@code PathfinderManager}
     * will result in {@link NullPointerException}s being thrown. And we all
     * know those aren't very fun.
     */
    @Sync
    public void open() {
        finder = new GeneratorManager(config);
        exec = new FollowerExecutor(config.getDrive());
        thread = new PathfinderThreadManager(config.getOdometry());

        exec.start();
        thread.start();
    }

    /**
     * Merge several paths into a single path by adding the DynamicArray of points
     * of each of the paths together and then removing any duplicates.
     *
     * <p>
     * This method assumes that any end user already has all of the paths they
     * plan on following in order. If the paths are not in order, the final
     * merged path won't be either.
     * </p>
     *
     * @param paths several pre-sorted array lists of target points.
     * @return a merged array list of several different paths.
     * @see Extra#removeDuplicatePoints(DynamicArray)
     */
    @Sync
    public final DynamicArray<Point> merge(DynamicArray<DynamicArray<Point>> paths) {
        DynamicArray<Point> merged = new DynamicArray<>();

        DynamicArray<Point> finalMerged = merged;
        paths.itr().forEach(path -> path.itr().forEach(finalMerged::add));

        merged = Extra.removeDuplicatePoints(finalMerged);

        return merged;
    }

    /**
     * Get a path of points between two points.
     *
     * <p>
     * Path generation is expensive in terms of CPU time - don't generate
     * any paths that you don't absolutely need (or want) to.
     * </p>
     *
     * <p>
     * Generated paths are always notated in Point form. Most controllers
     * and followers would prefer to use HeadingPoint - actually, they
     * require it. In order to convert Point to HeadingPoint, you can use
     * the withHeading() method in conjunction with two HeadingPoint(s) to
     * generate a much more usable list.
     * </p>
     *
     * @param start starting position.
     * @param end   ending position.
     * @return a path between two given points.
     * @see GeneratorManager#getCoordinatePath(Point, Point)
     */
    @Sync
    public DynamicArray<Point> getPath(HeadingPoint start,
                                       HeadingPoint end) {
        if (HeadingPoint.isSame(start, end)) {
            try {
                throw new InvalidPathException("Points can not be identical!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return finder.getCoordinatePath(start, end);
    }

    /**
     * Get an extended path between two or more points.
     *
     * <p>
     * This method works by finding individual paths for each waypoint segment
     * and then merging all of the paths together to form a single large path.
     * This is by far the most useful when combined with trajectory following,
     * such as by using the SwerveFollower class.
     * </p>
     *
     * <p>
     * If the length of the array list of points that have been provided is
     * less than 2, meaning there's only a single point or so, tell the user
     * that they're stupid and COOL - COOL, of course, meaning...
     * <ul>
     *     <li>Constipated</li>
     *     <li>Over-rated</li>
     *     <li>Out-of-style</li>
     *     <li>Loser!</li>
     * </ul>
     * </p>
     *
     * @param points a list of target points that must be met.
     * @return an extended path based off of waypoints.
     * @see PathfinderManager#getPath(HeadingPoint, HeadingPoint)
     */
    @Sync
    public DynamicArray<Point> getWaypointPath(DynamicArray<HeadingPoint> points) {
        if (points.size() < 2) {
            try {
                throw new InvalidPathException("Too few target points!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        DynamicArray<DynamicArray<Point>> paths = new DynamicArray<>();

        points.itr().forEach(point -> {
            try {
                HeadingPoint q = points.itr().next();
                DynamicArray<Point> pqPath = getPath(point, q);
                paths.add(pqPath);
            } catch (Exception ignored) {
            }
        });

        return merge(paths);
    }

    /**
     * Generate a list of followers to complete a given path.
     *
     * <p>
     * Depending on the selected follower type, the followers that are
     * generated may be slightly different. However, all followers should
     * (hopefully) accomplish the same goal of moving the robot from point
     * A to point B in a fairly reasonable manner.
     * </p>
     *
     * @param path a list of points, comprising a path.
     * @return a list of followers to follow that path.
     * @see PathfinderConfig#getFollowerType()
     */
    @Sync
    public DynamicArray<Follower> generateFollowers(
            DynamicArray<HeadingPoint> path) {
        DynamicArray<Follower> followers = new DynamicArray<>();

        Followers followerType = config.getFollowerType();

        if (followerType != Followers.LINEAR &&
                followerType != Followers.DUAL_PID &&
                followerType != Followers.TRI_PID) {
            throw new UnsupportedOperationException(
                    "Follower type " + followerType.toString() + " " +
                            "is not yet supported!"
            );
        }

        path.add(0, config.getOdometry().getPos());

        path.itr().forEach(point -> {
            try {
                HeadingPoint nextPoint = path.itr().next();

                if (nextPoint != null) {
                    DynamicArray<HeadingPoint> points = new DynamicArray<>(
                            point, nextPoint
                    );

                    switch (config.getFollowerType()) {
                        case LINEAR:
                            followers.add(
                                    FollowerFactory.linear(
                                            config, points
                                    )
                            );
                            break;
                        case DUAL_PID:
                            followers.add(
                                    FollowerFactory.dualPid(
                                            config, points
                                    )
                            );
                            break;
                        case TRI_PID:
                            followers.add(
                                    FollowerFactory.triPid(
                                            config, points
                                    )
                            );
                            break;
                        default:
                    }
                }
            } catch (Exception ignored) {

            }
        });

        return followers;
    }

    /**
     * Find a path, generate followers, and queue those followers.
     *
     * <p>
     * The path that's generated is later converted to a heading path.
     * If the path is only a single step, which it shouldn't ever be, there
     * may be some issues with the robot's heading.
     * </p>
     *
     * @param start the starting position.
     * @param end   the ending position.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#getPath(HeadingPoint, HeadingPoint)
     * @see PathfinderManager#generateFollowers(DynamicArray)
     * @see FollowerExecutor#queueFollower(Follower)
     */
    @Sync
    public PromisedFinder generateAndQueueFollowers(HeadingPoint start,
                                                    HeadingPoint end) {
        DynamicArray<Point> path = getPath(start, end);

        DynamicArray<Follower> followers = generateFollowers(
                withHeading(
                        path,
                        start,
                        end
                )
        );

        exec.queueFollowers(followers);

        return new PromisedFinder(
                path.size() > 0,
                path
        );
    }

    /**
     * Don't find any path - instead, simply queue the inputted followers.
     *
     * <p>
     * If you'd like to get a little bit more control over the way followers
     * are handled, you can feel free to manually queue them like this. It's
     * worth noting, however, that this probably isn't the most incredibly
     * fantastical idea, as it can be difficult to ensure everything is
     * happening at the right time, especially in a multi-threaded environment.
     * </p>
     *
     * @param followers followers to be queued.
     * @see FollowerExecutor#queueFollower(Follower)
     */
    @Sync
    public void queueFollowers(DynamicArray<Follower> followers) {
        exec.queueFollowers(followers);
    }

    /**
     * Convert an DynamicArray of Point instances to an DynamicArray of
     * HeadingPoint instances.
     *
     * <p>
     * If the size of the input list is only 1, it'll have the ending heading.
     * </p>
     *
     * <p>
     * If the size of the list is greater than 1, the first of the points
     * in the list will have the START heading, and every other point will
     * have the same ending as the END HeadingPoint.
     * </p>
     *
     * @param points the list of points.
     * @param start  the start point.
     * @param end    the end point.
     * @return a new array list.
     */
    @Sync
    public DynamicArray<HeadingPoint> withHeading(DynamicArray<Point> points,
                                                  HeadingPoint start,
                                                  HeadingPoint end) {
        DynamicArray<HeadingPoint> withHeading = new DynamicArray<>();

        if (points.size() == 1) {
            withHeading.add(
                    new HeadingPoint(
                            points.get(0).getX(),
                            points.get(0).getY(),
                            end.getHeading()
                    )
            );
        } else {
            points.itr().forEach(point -> {
                if (points.itr().index() == 0) {
                    withHeading.add(new HeadingPoint(
                            point.getX(),
                            point.getY(),
                            start.getHeading()
                    ));
                } else {
                    withHeading.add(new HeadingPoint(
                            point.getX(),
                            point.getY(),
                            end.getHeading()
                    ));
                }
            });
        }

        return withHeading;
    }

    /**
     * Go to a position from the robot's current position.
     *
     * <p>
     * This method finds the robot's current position by getting the
     * odometry system and querying it, and then plans and executes paths
     * and followers to follow along that path.
     * </p>
     *
     * @param end the target position.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#generateAndQueueFollowers(HeadingPoint, HeadingPoint)
     */
    @Async
    public PromisedFinder goToPosition(HeadingPoint end) {
        /*
         * A bit confusing here - but what happens...
         *
         * generateAndQueueFollowers returns a PromisedFinder, meaning we can
         * get the path after the path has been generated.
         *
         * That's not all, however.
         *
         * When that method is invoked, followers are generated and queued.
         *
         * Thus, we're knocking two birds out with one stone by calling the
         * generateAndQueueFollowers() method and getting the path from there.
         *
         * This is bad programming practice and should be refactored for the
         * sake of my sanity, as well as code cleanliness, but for now, it
         * should get the job done.
         */
        DynamicArray<Point> path = generateAndQueueFollowers(
                config.getOdometry().getPos(),
                end
        ).getPath();

        /*
         * Create a new PromisedFinder based on the path that's been generated.
         */
        return new PromisedFinder(
                path.size() > 0,
                path
        );
    }

    /**
     * Generate a path based on a set of target points, rather than start and
     * end positions.
     *
     * <p>
     * In addition to generating a path along a set of waypoints, this method
     * will begin following the path by adding any generated followers to the
     * follower execution thread's queue.
     * </p>
     *
     * <p>
     * This method is especially useful when you're making use of trajectories.
     * Generating an extended path to follow and creating a follower designed
     * to run at its maximum possible speed as much as possible ensures that
     * the robot moves incredibly quickly.
     * </p>
     *
     * @param points the waypoints that should be used in path generation.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#followPath(DynamicArray)
     */
    @Async
    public PromisedFinder followPath(HeadingPoint... points) {
        DynamicArray<HeadingPoint> list = new DynamicArray<>(points);

        return followPath(list);
    }

    /**
     * Generate a path based on a set of target points, rather than start and
     * end positions.
     *
     * <p>
     * In addition to generating a path along a set of waypoints, this method
     * will begin following the path by adding any generated followers to the
     * follower execution thread's queue.
     * </p>
     *
     * <p>
     * This method is especially useful when you're making use of trajectories.
     * Generating an extended path to follow and creating a follower designed
     * to run at its maximum possible speed as much as possible ensures that
     * the robot moves incredibly quickly.
     * </p>
     *
     * @param points the waypoints that should be used in path generation.
     * @return a chainable PromisedFinder object.
     * @see PathfinderManager#getWaypointPath(DynamicArray)
     * @see PathfinderManager#generateFollowers(DynamicArray)
     * @see PathfinderManager#queueFollowers(DynamicArray)
     */
    @Async
    public PromisedFinder followPath(DynamicArray<HeadingPoint> points) {
        DynamicArray<Point> path = getWaypointPath(points);
        DynamicArray<Follower> followers = generateFollowers(
                withHeading(
                        path,
                        points.get(0),
                        points.get(points.size() - 1)
                )
        );

        queueFollowers(followers);

        return new PromisedFinder(
                path.size() > 0,
                path
        );
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
     * @see FollowerExecutor#lock()
     */
    @Sync
    @Wait
    public void lock() {
        exec.lock();
    }

    /**
     * Stop the robot's drivetrain. This is useful when you're trying to
     * make sure the robot stops at a target point - ie, if you don't stop
     * the robot at that target point, it'll miss the point entirely.
     */
    @Sync
    public void stopRobot() {
        config.getDrive().drive(0, 0);
    }

    /**
     * Pause the odometry updater, temporarily stopping it from updating
     * the values of the odometry system based one encoder values.
     *
     * <p>
     * By default, odometry updating is enabled. In order to disable it, you
     * need to pause it. And in order to re-enable it from there, you need to
     * unpause it. Lovely, right?
     * </p>
     */
    @Sync
    public void pauseOdometry() {
        thread.stop();
    }

    /**
     * Unpause the odometry system, allowing it to function normally once
     * again.
     *
     * <p>
     * By default, odometry updating is enabled. In order to disable it, you
     * need to pause it. And in order to re-enable it from there, you need to
     * unpause it. Lovely, right?
     * </p>
     */
    @Async
    public void unpauseOdometry() {
        thread.start();
    }

    /**
     * Get the width of the robot.
     *
     * @return the robot's width.
     */
    @Sync
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the robot.
     *
     * @return the robot's height.
     */
    @Sync
    public int getHeight() {
        return height;
    }

    /**
     * Get the specificity of the field.
     *
     * @return the field's specificity.
     */
    @Sync
    public int getSpecificity() {
        return config.getSpecificity();
    }

    /**
     * Get the pathfinder's configuration.
     *
     * @return the pathfinder's configuration.
     */
    @Sync
    public PathfinderConfig getConfig() {
        return config;
    }

    /**
     * Finish this instance of the {@code PathfinderManager}'s execution. This
     * will stop any of the threads that are still active, or at least try to
     * do so. Threads spawned by followers can't be managed through here, but
     * follower execution and general execution/odometry updating can be.
     *
     * <p>
     * This method will close both the execution and general pathfinder threads.
     * Before finishing using a pathfinder, remember to call the close methods.
     * Otherwise, you might have dangling threads that can eat up a lot of CPU.
     * </p>
     */
    @Sync
    public void close() {
        exec.close();
        thread.close();
    }
}
