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

import me.wobblyyyy.pathfinder.geometry.Point;

import me.wobblyyyy.edt.DynamicArray;

/**
 * A partially uncompleted (or partially completed) pathfinder state.
 *
 * <p>
 * PromisedFinder as a class doesn't actually do very much. However, the
 * purpose of PromisedFinder is to allow you to easily write what are in
 * essence "callbacks" upon the path generation's termination.
 * </p>
 *
 * <p>
 * For example, if you'd like to run a piece of code after the pathfinder
 * has tried (and failed) to generate a path, you could make use of the
 * {@link PromisedFinder#fail(Runnable)} method.
 * </p>
 *
 * <p>
 * As a more contextualized example, take:
 * <pre>
 * <code>
 * Pathfinder pathfinder;
 *
 * pathfinder.followPath([path]).after(() -> {
 *    // Code to be executed after the pathfinder finishes
 *    // path generation and finding.
 * }).pass(() -> {
 *     // Code to be executed after the pathfinder successfully
 *     // generates a path.
 * }).fail(() -> {
 *     // Code to be executed after the pathfinder doesn't manage
 *     // to generate a path.
 * });
 * </code>
 * </pre>
 * </p>
 *
 * <p>
 * {@link PromisedFinder} objects are also capable of providing information
 * about the status of path generation. If, for example, you'd like to execute
 * some code after the pathfinder's execution, but you need to get the path
 * that was just generated, you can use the method
 * {@link PromisedFinder#getPath()} to do exactly that.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.2.0
 */
public class PromisedFinder {
    /**
     * A default Runnable element.
     *
     * <p>
     * This element is subbed in for Runnable code if that code is null.
     * </p>
     */
    private static final Runnable DEFAULT = new Runnable() {
        @Override
        public void run() {
            /*
             * Obviously, nothing happens here.
             *
             * You're still reading this, even though you know nothing is
             * going to happen here.
             *
             * And you're still reading this. I have to ask - why? Are you
             * that bored?
             *
             * Come on, you expert software engineer! Go write some code and
             * slay some monsters!
             *
             * Drinking gasoline is pretty cool too.
             */
        }
    };

    /**
     * Did the pathfinder's path generation succeed or not?
     */
    private final boolean passed;

    /**
     * The pathfinder's path generation's outputted path.
     */
    private final DynamicArray<Point> path;

    /**
     * Create a new PromisedFinder object.
     *
     * @param pass whether or not the path generation succeeded.
     * @param path the path generation's outputted path.
     */
    public PromisedFinder(boolean pass,
                          DynamicArray<Point> path) {
        /*
         * Set constructor variables.
         *
         * In the future, it might be a better idea to only require a single
         * parameter to be passed. We can validate whether or not the path
         * is empty (has failed) here, instead of having to do that externally.
         */

        this.passed = pass;
        this.path = path;
    }

    /**
     * Get the generated path.
     *
     * <p>
     * This is the path that's been generated by the path generation code.
     * It isn't modified or in any way mutilated before it's available here.
     * </p>
     *
     * @return the generated path.
     */
    public DynamicArray<Point> getPath() {
        return path;
    }

    /**
     * Code that should be executed when the pathfinder finishes finding a
     * path.
     *
     * <p>
     * After code is run once, as soon as the pathfinder finishes its path
     * generation calculations. It doesn't matter if the pathfinder managed
     * to find a path or not - this is run anyways.
     * </p>
     *
     * @param after the code that should be executed on the pathfinder's
     *              path generation's completion.
     * @return this - a chainable PromisedFinder object.
     */
    public PromisedFinder after(Runnable after) {
        /*
         * All after code should be run always, no matter what.
         *
         * Run the after Runnable. It's crazy, I know. The amount of complexity
         * in that statement right there is truly off-the-charts. But trust me,
         * it'll be okay.
         */
        after.run();

        /*
         * Return this, so we can daisy-chain other PromisedFinder code.
         */
        return this;
    }

    /**
     * Code that should be executed when the pathfinder finishes finding a
     * path, if that path generation succeeds.
     *
     * <p>
     * Any code you put here will be run after the pathfinder has finished
     * calculating a path, but it won't always be executed. Code that goes
     * here will only be executed if the pathfinder DID find a path.
     * </p>
     *
     * <p>
     * The following ruleset is applied to pathfinder passing and failing.
     * <ul>
     *     <li>The path has PASSED if it's non-zero in length.</li>
     *     <li>The path has FAILED if it's zero in length.</li>
     * </ul>
     * </p>
     *
     * @param pass the code that should be executed on the pathfinder's
     *             path generation's success.
     * @return this - a chainable PromisedFinder object.
     */
    public PromisedFinder pass(Runnable pass) {
        /*
         * Check to see if the path generation passed.
         *
         * If the path generation DID pass, we execute the pass code.
         */
        if (passed) pass.run();

        /*
         * Return this, so we can daisy-chain other PromisedFinder code.
         */
        return this;
    }

    /**
     * Code that should be executed when the pathfinder finishes finding a
     * path, if that path generation fails.
     *
     * <p>
     * Any code you put here will be run after the pathfinder has finished
     * calculating a path, but it won't always be executed. Code that goes
     * here will only be executed if the pathfinder DID NOT find a path.
     * </p>
     *
     * <p>
     * The following ruleset is applied to pathfinder passing and failing.
     * <ul>
     *     <li>The path has PASSED if it's non-zero in length.</li>
     *     <li>The path has FAILED if it's zero in length.</li>
     * </ul>
     * </p>
     *
     * @param fail the code that should be executed on the pathfinder's
     *             path generation's failure.
     * @return this - a chainable PromisedFinder object.
     */
    public PromisedFinder fail(Runnable fail) {
        /*
         * Check to see if the path generation failed.
         *
         * If the path generation DID fail, we execute the fail code.
         */
        if (!passed) fail.run();

        /*
         * Return this, so we can daisy-chain other PromisedFinder code.
         */
        return this;
    }
}
