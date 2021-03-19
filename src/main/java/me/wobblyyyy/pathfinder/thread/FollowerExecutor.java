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

package me.wobblyyyy.pathfinder.thread;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.followers.Follower;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.util.BcThread;

import java.util.ArrayList;

/**
 * Execute a given follower among several different threads.
 *
 * <p>
 * In order to compensate for the computationally-expensive nature of complex
 * pathfinding and trajectory generation, important following code is offloaded
 * to a secondary thread. This ensures that the main thread stays freed enough
 * to allow a user to still have control over the program.
 * </p>
 *
 * <p>
 * As with any threaded things, however, they suck to conceptualize. The aim
 * of this class is to make it easier to follow along with how the pathfinder
 * actually generates and follows paths and trajectories.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class FollowerExecutor {
    /**
     * Execution thread.
     */
    private final Runnable executor;

    /**
     * The currently-executed followers.
     */
    private final ArrayList<Follower> followers = new ArrayList<>();

    /**
     * A list of all of the followers that have run their calculation
     * method already.
     *
     * <p>
     * Depending on for how long the pathfinder is run for, this array list
     * may eventually overflow. In order to compensate for this potential
     * overflow, we should add some code that clears this list at some point.
     * </p>
     */
    private final DynamicArray<Follower> hasCalculated = new DynamicArray<>();

    /**
     * Actions to execute. MUST BE THREAD SAFE!
     *
     * <p>
     * Modification should only be handled through synchronized methods to
     * ensure no bad stuff happens.
     * </p>
     */
    private StaticArray<Runnable> actions = new StaticArray<>();

    /**
     * Should the thread continue its execution.
     */
    private boolean shouldRun = true;

    /**
     * The robot's drivetrain.
     *
     * <p>
     * This is used for enabling and disabling user control when the pathfinder
     * is enabled or disabled.
     * </p>
     */
    private final Drive drive;

    /**
     * Create a new follower executor and initialize the executor thread.
     *
     * <p>
     * PLEASE NOTE: Nothing's going to happen after you construct a new
     * instance of this executor. The thread must be enabled with the start
     * method prior to having any function. To disable the thread, you can
     * run the stop method.
     * </p>
     *
     * <p>
     * At this point, the thread hasn't been started yet. If you're
     * interfacing directly with this class rather than using a pathfinder
     * manager instance as a proxy, you'll need to remember to start the
     * threads before trying to follow paths.
     * </p>
     *
     * @param drive the robot's drivetrain.
     */
    public FollowerExecutor(Drive drive) {
        executor = () -> {
            /*
             * The thread should almost always be active - if it isn't,
             * the thread will stop, and, well, that wouldn't be cool.
             */
            while (shouldRun) {
                /*
                 * Tell the CPU that this is a busy-wait and it doesn't
                 * exactly matter how much CPU time the execution of
                 * this has, so long as it's still executed.
                 *
                 * This should improve performance by freeing up some
                 * processing time.
                 */
                BcThread.spin();

                /*
                 * Thread-safe way to add a list of actions.
                 */
                addActions();

                /*
                 * For each of the Runnable elements that need to be
                 * executed, we need to... well, actually run them.
                 *
                 * Note that any thrown exceptions here are ignored.
                 * If you need to debug this code, or code behind
                 * one of the generated Runnable elements, you need to
                 * make sure that you no longer ignore the exception.
                 */
                getActions().itr().forEach(runnable -> {
                    try {
                        runnable.run();
                    } catch (Exception ignored) {
                    }
                });

                /*
                 * Clear all of the actions - until next time!
                 */
                clearActions();
            }
        };

        this.drive = drive;
    }

    /**
     * Start the follower executor's thread.
     *
     * <p>
     * Threads should not be started after they're already running.
     * </p>
     */
    public void start() {
//        executor.start();
    }

    /**
     * Run the follower executor once.
     */
    public void tick() {
        executor.run();
    }

    /**
     * Stop the follower executor's thread.
     *
     * <p>
     * Thread.stop() has been a deprecated method for a while - is there an
     * effective alternative we can use?
     * </p>
     *
     * <p>
     * THIS DOES NOT ACTUALLY STOP THE THREAD! Rather, it updates the flag
     * indicating whether or not the follower should be run as intended.
     * </p>
     */
    public void stop() {
        shouldRun = false;
    }

    /**
     * Thread-safe way to get all of the actions.
     *
     * @return actions to be executed.
     */
    public synchronized StaticArray<Runnable> getActions() {
        return actions;
    }

    /**
     * Thread-safe method to generate actions.
     */
    public synchronized void addActions() {
        StaticArray<Runnable> actions = generateRunnables();
        this.actions = new StaticArray<>(actions);
    }

    /**
     * Thread-safe method to clear all of the actions.
     */
    public synchronized void clearActions() {

    }

    /**
     * Clear all of the followers and runnables, essentially resetting the
     * FollowerExecutor instance.
     */
    @SuppressWarnings("unused")
    public synchronized void clear() {
        followers.clear();
    }

    /**
     * Generate a list of Runnable elements to be executed by the execution
     * thread.
     *
     * <p>
     * If a follower has not finished its execution yet, we disable user
     * control for the robot's drivetrain as a precautionary measure. If the
     * follower HAS finished its execution, we can then enable user control
     * once again.
     * </p>
     *
     * @return to-be-executed Runnable elements.
     */
    public StaticArray<Runnable> generateRunnables() {
        DynamicArray<Runnable> runnables = new DynamicArray<>();

        try {
            Follower f = followers.get(0);

            if (f != null) {
                if (!f.isDone()) {
                    /*
                     * If the follower hasn't had its calculations executed yet,
                     * we need to make sure it does that.
                     *
                     * Add the follower to a list of followers that have already
                     * had their values calculated so we don't need to worry about
                     * it any longer.
                     */
                    if (!hasCalculated.contains(f)) {
                        runnables.add(
                                f::calculate
                        );
                        hasCalculated.add(f);
                    }

                    /*
                     * Make sure to disable the user's control over the robot so
                     * they don't cause any issues with the motors.
                     */
                    runnables.add(
                            drive::disableUserControl
                    );

                    /*
                     * Update the follower's power values.
                     */
                    runnables.add(
                            f::update
                    );

                    /*
                     * Make the follower drive around. Yay!
                     *
                     * If user control hasn't been disabled by this point, we may
                     * see some issues with the motors having seemingly random
                     * spasms.
                     */
                    runnables.add(
                            f::drive
                    );
                } else {
                    /*
                     * If the follower has finished its execution, we resume to
                     * normal.
                     *
                     * First and foremost, the user needs to re-gain control over
                     * their drivetrain.
                     *
                     * After giving the user their control back, we can focus on
                     * moving to the next follower.
                     *
                     * The user might only have control for a fraction of a second
                     * before any other queued followers are activated, but who
                     * cares - sucks to suck I guess.
                     */
                    runnables.add(
                            drive::enableUserControl
                    );

                    runnables.add(
                            moveUp(f)
                    );
                }
            }
        } catch (IndexOutOfBoundsException ignored) {

        }

        /*
         * Return a list of all of the Runnable items that need to be ran.
         */
        return new StaticArray<>(runnables);
    }

    /**
     * Generate a Runnable that can be executed to progress past the current
     * follower and onto the next one.
     *
     * @param f the current follower.
     * @return a Runnable to move upwards in the execution order.
     */
    private synchronized Runnable moveUp(Follower f) {
        /*
         * Return a new Runnable with the actions we need.
         */
        return () -> {
            try {
                /*
                 * Another nested try - I'm sorry, I'm sorry...
                 *
                 * This is done to avoid ArrayIndexOutOfBounds exceptions
                 * thrown by the dynamic array class.
                 */
                try {
                    /*
                     * Try removing the follower from the main follower list.
                     */
                    followers.remove(f);
                } catch (ArrayIndexOutOfBoundsException ignored) {
                    /*
                     * Ignore the exception, it doesn't actually do anything.
                     */
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

    /**
     * Queue a single follower element.
     *
     * <p>
     * Queuing followers isn't a complicated process. Note, however, that if
     * the thread that executes the followers IS NOT active, nothing will
     * actually happen to the followers.
     * </p>
     *
     * <p>
     * After a follower has been queued, it won't take effect immediately.
     * After the current bank of followers has been exhausted, it'll move on
     * to newly-queued followers, such as the follower that you may have just
     * provided. How cool!
     * </p>
     *
     * @param f the follower to queue.
     */
    public synchronized void queueFollower(Follower f) {
        /*
         * Run the follower's calculations before we need them - this way
         * the pathfinder can get right to pathfinding.
         *
         * Especially useful when calculations are performed on a secondary
         * thread, as they often are.
         */
//        f.calculate();

        /*
         * Add the follower to the regular followers list.
         */
        followers.add(f);
    }

    /**
     * Queue a collection of follower elements.
     *
     * <p>
     * Queuing followers isn't a complicated process. Note, however, that if
     * the thread that executes the followers IS NOT active, nothing will
     * actually happen to the followers.
     * </p>
     *
     * <p>
     * After a follower has been queued, it won't take effect immediately.
     * After the current bank of followers has been exhausted, it'll move on
     * to newly-queued followers, such as the follower that you may have just
     * provided. How cool!
     * </p>
     *
     * @param followers the follower elements to queue.
     */
    public synchronized void queueFollowers(DynamicArray<Follower> followers) {
        /*
         * For each follower, we go ahead and queue it. Crazy!
         */
        followers.itr().forEach(this::queueFollower);
    }

    /**
     * Check whether or not both of the follower array lists are empty,
     * indicating that there's nothing for the pathfinder to do.
     *
     * @return whether or not the pathfinder is idle.
     */
    public synchronized boolean isEmpty() {
        /*
         * Check whether both the follower bank and the followers list is empty.
         */
        return followers.isEmpty();
    }

    /**
     * Lock the current thread until the pathfinder has finished its
     * execution.
     *
     * <p>
     * Unfortunately, we can't use a Thread.join() like method because of the
     * way Pathfinder handles multithreading, especially path following. This
     * should be (roughly) the same.
     * </p>
     *
     * <p>
     * As a locking operation, the calling thread will not be able to progress
     * until the execution of this thread has finished. If you'd like to
     * queue another follower while the system is active, you can do so. Yay!
     * </p>
     */
    public void lock() {
        /*
         * While-true loop - should be repeated as long as the follower
         * list is NOT empty.
         */
        do {
            /*
             * Call the Thread.onSpinWait() method to ensure we don't waste
             * too many CPU cycles. This method marks the calling code as
             * unimportant in terms of the CPU - the CPU can prioritize other,
             * more important operation, over this right here.
             */
            BcThread.spin();
        } while (!isEmpty());
    }

    /**
     * Stop this {@code FollowerExecutor}'s execution, or at least attempt to
     * do so. This method won't actually stop the execution of the thread -
     * rather, it'll signal the thread's execution to come to a stop.
     *
     * @see #stop()
     */
    public void close() {
        stop();
    }
}
