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

package me.wobblyyyy.pathfinder.error;

/**
 * A type of exception used to signify when a path that was provided by a user
 * is in some way invalid.
 *
 * <p>
 * Path generation is stopped whenever an invalid path is presented. If you're
 * wondering why exactly a path is invalid, the likely reasons are:
 * <ul>
 *     <li>You didn't pass enough parameters.</li>
 *     <li>You tried to find a path to a target outside of the map.</li>
 * </ul>
 * ... etc. Your error message will probably tell you more specifics.
 * </p>
 *
 * @author Colin Robertson
 */
public class InvalidPathException extends Exception {
    /**
     * Throw a new InvalidPathException.
     *
     * @param exception the exception message.
     */
    public InvalidPathException(String exception) {
        super(exception);
    }
}
