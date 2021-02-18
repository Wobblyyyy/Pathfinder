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

package me.wobblyyyy.pathfinder.geometry;

/**
 * A zone is any two-dimensional area in which another two-dimensional entity
 * can be within.
 *
 * <p>
 * Zones are the very core of maps and field maps - each zone represents a
 * different portion of the field. It's worth noting and observing: zones
 * are VERY expensive to check. By this, I mean collision checking and
 * avoidance. When the pathfinding core is initially spun up, it checks every
 * possible point against every single zone as a method of ensuring that no
 * potential collisions occur. Although a more efficient way of doing this
 * is ABSOLUTELY needed in the future, for now, you'll just have to live with
 * it. If you do end up creating a zone for some reason, but you don't want
 * it to slow down collision checking and the robot's initialization, you
 * can make sure that the isSolid flag is set to FALSE.
 * </p>
 *
 * <p>
 * As with the Rectangles code, this shit is seriously outdated. If you have
 * the time, a documentation refresh or reformat would be much appreciated.
 * </p>
 *
 * <p>
 * Zones are immobile, meaning once they are defined, they can not be
 * re-defined without entirely deleting the zone and creating a new one.
 * Because zones are generally just different areas of the field, they
 * shouldn't need to be redefined anyways. But, if, for whatever reason,
 * you have a very strong desire to have them redefined, you can just go
 * ahead and make another one.
 * </p>
 *
 * <p>
 * In the context of field mapping, zones allow us to do some
 * pretty cool things, such as...
 * <ul>
 *     <li>Dynamically adjust speed multipliers based on location.</li>
 *     <li>Automatically perform certain operations when near targets.</li>
 *     <li>Show the driver, via telemetry, where they are.</li>
 *     <li>Automatically line the robot up to perform certain tasks.</li>
 * </ul>
 * ... and much more. I'm not here to tell you what to do with zones - rather,
 * I'm here to give you some god-awful documentation as to how they function,
 * so you can use them to the best of your ability. You poor soul.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public interface Zone {
    /**
     * Get the name of the specific zone.
     *
     * <p>
     * Zone names don't matter... at all, really. They mattered in the first
     * release of Pathfinder, as zone collisions made use of String keys for
     * a reason I couldn't possibly explain. But they don't anymore. You can
     * really just make the name whatever you'd like, but for the sake of
     * consistency and clean code, I name all of my zones after the class name.
     * </p>
     *
     * @return the name of the zone.
     */
    String getName();

    /**
     * Get the "parent shape" of the zone.
     *
     * <p>
     * A "parent shape" is quite literally just a regular shape. Zones are
     * based on shapes, and parent shapes are those shapes. You could, for
     * example, have a rectangular zone name "Zone_A." Zone A's parent shape
     * would be considered the rectangle which Zone A uses to define it's
     * boundaries.
     * </p>
     *
     * @return a parent shape.
     */
    Shape getParentShape();

    /**
     * The priority of the zone - read the rest of this JavaDoc to understand.
     *
     * <p>
     * Each and every zone has a priority. A priority is just how important that
     * zone is compared to other zones. Higher priorities are more important than
     * lower ones - for example, if the robot is in two zones at once (the main
     * field zone, and, say, an objective zone) and the main zone has a priority of 0
     * compared to the objective zone's priority of 1, the objective zone's priority
     * will override that of the main zone. If the objective zone has a speed multiplier
     * which is higher or lower than the main zone's multiplier, that new multiplier
     * will override the current one.
     * </p>
     *
     * @return the zone's priority.
     */
    int getZonePriority();

    /**
     * Check whether or not a given point is in a zone.
     *
     * <p>
     * As a default boolean, this basically CCs the check whether or not the
     * point is in the zone to the parent shape, which will actually determine
     * whether or not the point is contained in the shape/zone.
     * </p>
     *
     * @param point the point to check
     * @return whether ot the point is in the zone or not
     */
    default boolean isPointInZone(Point point) {
        return getParentShape().isPointInShape(point);
    }

    /**
     * Just in case you wanted to have certain zones automatically
     * change the speed at which the robot drives at.
     *
     * @return a drive speed multiplier
     */
    double getDriveSpeedMultiplier();

    /**
     * Get the total count of components.
     *
     * <p>
     * Countable components include things like lines and arcs. Note
     * that component counts have all but no utility, save debugging
     * (or flexing) purposes. You know how showing off how hard you can
     * push the engine of your shitty little car you got from your
     * grandparents for $3,000 is some kind of flex? In the same way,
     * showing off how many virtually-rendered components your FTC
     * code can have is a flex as well.
     * </p>
     *
     * @return how many components there are, in total
     */
    int getComponents();

    /**
     * Is the zone a solid?
     *
     * <p>
     * Non-solid zones are sorted out during the pathfinding core's
     * initialization stage in order to reduce the price of checking for
     * potential collisions.
     * </p>
     *
     * <p>
     * Solid objects can't be collided with, meaning that the pathfinder
     * will automatically navigate around them. Additionally, solid objects
     * won't be collision-checked against each other, as any collisions between
     * them are intentional and can't be changed with the robot.
     * </p>
     *
     * @return whether or not the zone is solid.
     */
    boolean isSolid();
}
