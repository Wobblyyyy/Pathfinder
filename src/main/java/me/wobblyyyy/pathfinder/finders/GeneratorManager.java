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

package me.wobblyyyy.pathfinder.finders;

import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.config.PathfinderConfig;
import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Manager class used for controlling and manipulating different generators.
 *
 * <p>
 * Generators are path finding algorithms, wrapped up like a Christmas present
 * for our lovely code to use. This class is designed to control and interface
 * with any generators that have been enabled by users.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class GeneratorManager {
    /**
     * A list of all of the currently enabled generators.
     */
    private final DynamicArray<Generator> gens = new DynamicArray<>();

    /**
     * Create a new generator manager and instantiate the generators that the
     * user has chosen to use.
     *
     * @param config the Pathfinder configuration class.
     */
    public GeneratorManager(PathfinderConfig config) {
        Xygum.Finders f = config.doesUseThetaStar() ?
                Xygum.Finders.T :
                Xygum.Finders.A;
        if (config.doesUseLightning()) gens.add(new LightningFinder(config));
        if (config.doesUseFast()) gens.add(new SpeedFinder(config));
        if (config.doesUseThetaStar()) gens.add(new Xygum(config, f));
    }

    /**
     * Add a generator to the execution cycle.
     *
     * @param g     the generator to add.
     * @param index the index to add it at. The closer this number is to zero,
     *              the higher priority the generator has over other generators.
     */
    @SuppressWarnings("unused")
    public void addGenerator(Generator g,
                             int index) {
        gens.add(index, g);
    }

    /**
     * Add a generator to the execution cycle.
     *
     * @param g the generator to add.
     */
    @SuppressWarnings("unused")
    public void addGenerator(Generator g) {
        gens.add(g);
    }

    /**
     * Get all of the current generators.
     *
     * @return the current in-use generators.
     */
    public DynamicArray<Generator> getGenerators() {
        return gens;
    }

    /**
     * Get a coordinate path based on the array list of path generators
     * that's provided earlier in this class.
     *
     * <p>
     * Different generators handle generation in different ways. However, all
     * of them DO handle one thing the same way - if the array list that's
     * returned from the path's generation method has a size of zero, we move
     * on, moving to the next pathfinder.
     * </p>
     *
     * <p>
     * If all of the path generators are exhausted - meaning that they fail to
     * find a path to the given target - we return an array list of size zero,
     * indicating to the pathfinder that no paths were found.
     * </p>
     *
     * @param start the start position.
     * @param end   the end position.
     * @return a path to the point.
     */
    public DynamicArray<Point> getCoordinatePath(Point start,
                                                 Point end) {
        AtomicReference<DynamicArray<Point>> toReturn =
                new AtomicReference<>(new DynamicArray<>());
        AtomicBoolean shouldGo = new AtomicBoolean(true);

        /*
         * For each generator:
         * - Generate a path between the two points.
         * - If the path's size is non-zero, we return the generated path.
         *
         * If no path has been found by the time all of the generators have
         * been exhausted, we simply return an empty array list.
         */
        gens.itr().forEach(generator -> {
            if (shouldGo.get()) {
                DynamicArray<Point> generated = generator.getCoordinatePath(
                        start,
                        end
                );

                if (generated.size() > 0) {
                    toReturn.set(new DynamicArray<>(generated));
                    shouldGo.set(false);
                }
            }
        });

        return toReturn.get();
    }
}
