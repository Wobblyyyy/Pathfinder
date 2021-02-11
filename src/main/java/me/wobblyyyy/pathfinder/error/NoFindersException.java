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
 * An exception to be used in cases where the user hasn't enabled any
 * of the finders available.
 *
 * <p>
 * If you're confused about what the hell is going wrong, you should make sure
 * to set at least one of the finders (usesLightning, usesFast, usesTheta, etc)
 * to "TRUE" in the pathfinder config.
 * </p>
 *
 * @author Colin Robertson
 */
public class NoFindersException extends Exception {
    /**
     * Throw a new InvalidPathException.
     *
     * @param exception the exception message.
     */
    public NoFindersException(String exception) {
        super(exception);
    }
}
