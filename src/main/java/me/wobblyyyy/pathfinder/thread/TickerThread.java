/*
 * ======================================================================
 * || Copyright (c) 2020 Tejas Mehta (tmthecoder@gmail.com)            ||
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

package me.wobblyyyy.pathfinder.thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Execute the pathfinder ticks asynchronously with the following thread
 * implementation.
 *
 * Utilizes a Boolean supplier to determine whether the thread should
 * continue execution and a Runnable that points to method that performs
 * the tick
 *
 * The point of this class is to prevent ticking from blocking other execution
 * flows with Pathfinder implementations
 *
 * @author Tejas Mehta
 * @since 0.6.1
 */
public class TickerThread extends Thread {

    /**
     * The supplier to determine whether the thread should keep running
     */
    private final Supplier<Boolean> shouldRun;

    /**
     * The runnable to actually perform the tick
     */
    private final Runnable tick;

    /**
     * The executor to manage running the tick on specified short interval.
     */
    private ScheduledExecutorService executor =
            Executors.newSingleThreadScheduledExecutor();

    /**
     * Create a new TickerThread object to perform Pathfinder ticks
     * asynchronously
     *
     * @param shouldRun A Boolean Supplier to dynamically determine whether
     *                 the thread should continue execution.
     * @param tick      A Runnable that actually performs the Pathfinder tick
     */
    public TickerThread(Supplier<Boolean> shouldRun, Runnable tick) {
        this.shouldRun = shouldRun;
        this.tick = tick;
    }

    /**
     * Initialize the executor to run at a 10 millisecond interval and
     * perform a tick.
     *
     * If shouldRun returns false, stop the executor, closing out the thread
     */
    @Override
    public void run() {
        executor.scheduleAtFixedRate(() -> {
            if (!shouldRun.get()) {
                executor.shutdown();
            }
            tick.run();
        }, 0, 10, TimeUnit.MILLISECONDS);
    }

    /**
     * Stop the ticker thready by shutting down the executor, finishing the
     * thread's execution sequence
     */
    public void stopThread() {
        executor.shutdown();
    }
}
