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

package me.wobblyyyy.pathfinder.map;

import me.wobblyyyy.pathfinder.geometry.Shape;
import me.wobblyyyy.pathfinder.geometry.Zone;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A representation of all of the physical elements on a playing field.
 *
 * <p>
 * Maps are simply a list of a bunch of different zones. In order to create a
 * new map, you can create a class that extends this one, and, in the
 * constructor of your extension, put a call to super with a new ArrayList
 * of zones representing the field's physical layout.
 * </p>
 *
 * <p>
 * Maps themselves aren't all that difficult to figure out how to use - the
 * real issue is zones and shapes. For your convenience, both fo those are
 * linked right here.
 * <ul>
 *     <li>{@link Shape}</li>
 *     <li>{@link Zone}</li>
 * </ul>
 * </p>
 *
 * <p>
 * Zone/map/field wrapping is not yet done - we need to work on that before we
 * can hope to do anything important with this library.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Map {
    /**
     * A list of all of the possible names for field zones.
     *
     * <p>
     * Please, for the sake of everyone's sanity - name every single zone that's
     * a field "field". Every potential problem is thus averted - wonderful,
     * right?!
     * </p>
     */
    public static final ArrayList<String> FIELD_NAMES =
            new ArrayList<String>() {{
        add("field");
        add("main");
        add("field2d");
        add("frame");
        add("box");
        add("reference");
        add("bounds");
        add("limits");
    }};

    /**
     * All of the zones contained within the map.
     */
    public final ArrayList<Zone> zones;

    /**
     * Create a new map without any elements.
     *
     * <p>
     * You should not EVER use this constructor in a non-testing environment!
     * Because zones is final, you'll be entirely screwed over if you construct
     * a new map like this.
     * </p>
     */
    public Map() {
        this(new ArrayList<>());
    }

    /**
     * Create a new map with a single zone.
     *
     * @param zone the zone to add.
     */
    public Map(Zone zone) {
        zones = new ArrayList<>();
        zones.add(zone);
        wrap(zones);
    }

    /**
     * Create a new map with several zones.
     *
     * @param zone the zones to add to the map.
     */
    public Map(Zone... zone) {
        this(new ArrayList<>(Arrays.asList(zone)));
    }

    /**
     * Create a new map with a list of zones.
     *
     * @param zones the zones to add to the map.
     */
    public Map(ArrayList<Zone> zones) {
        this.zones = zones;
        wrap(zones);
    }

    /**
     * Check whether or not a given zone is technically field.
     *
     * @param zone the zone to check.
     * @return whether or not that zone is classified as a field zone.
     */
    private static boolean isField(Zone zone) {
        for (String s : FIELD_NAMES) {
            if (zone.getName().equalsIgnoreCase(s)) return true;
        }

        return false;
    }

    /**
     * Wrap an entire field with four zones, to prevent the pathfinder from
     * finding paths that are out-of-bounds.
     *
     * @param zones the original zones.
     * @return a list of wrapped zones.
     */
    private ArrayList<Zone> wrap(ArrayList<Zone> zones) {
//        Zone field = null;
//
//        for (Zone zone : zones) {
//            if (isField(zone)) field = zone;
//        }
//
//        if (field == null) try {
//            throw new NoFieldException("You didn't pass any " +
//                    "zones named `field` to a map constructor!");
//        } catch (NoFieldException e) {
//            e.printStackTrace();
//        }
//
//        assert field != null;
//        if (!(field.getParentShape() instanceof Rectangle)) try {
//            throw new NonRectangularFieldException("The field zone you " +
//                    "passed is not rectangular!");
//        } catch (NonRectangularFieldException e) {
//            e.printStackTrace();
//        }

        return new ArrayList<>();
    }
}
