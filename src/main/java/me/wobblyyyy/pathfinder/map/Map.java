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
 *  ======================================================================
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
 * @author Colin Robertson
 */
public class Map {
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
    }
}
