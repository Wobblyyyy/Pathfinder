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
 * Exception to be thrown when a portion of code isn't fully implemented
 * yet and, as a result, isn't yet ready for use.
 *
 * <p>
 * If you're seeing this error, I hate to break it to you, but you're flat
 * out of luck. You'll have to either make your own method or... suffer.
 * </p>
 *
 * @author Colin Robertson
 */
public class UnimplementedException extends Exception {
    /**
     * Throw a new UnimplementedException.
     *
     * <p>
     * If you're seeing this error, I hate to break it to you, but you're flat
     * out of luck. You'll have to either make your own method or... suffer.
     * </p>
     *
     * @param exception the exception message.
     */
    public UnimplementedException(String exception) {
        super(exception);
    }
}
