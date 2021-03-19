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

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.DynamicArray;

/**
 * Resource class for storing trajectory segments in a single compact form.
 * A trajectory is composed of an {@code Arrayable} of trajectory segments.
 * Interpolation and following of trajectories and trajectory segments is
 * to be handled elsewhere.
 *
 * @author Colin Robertson
 * @since 0.4.0
 */
public class Trajectory {
    /**
     * The internal container for each of the trajectory's segments.
     */
    private final DynamicArray<Segment> segments;

    /**
     * The segment that's currently being worked with.
     */
    private int currentSegment = 0;

    /**
     * Create a new {@code Trajectory} based on an {@code Arrayable} container
     * of individual trajectory segments.
     *
     * @param segments an {@code Arrayable} containing each of the individual
     *                 component trajectory segments.
     */
    public Trajectory(Arrayable<Segment> segments) {
        this.segments = new DynamicArray<>(segments);
    }

    /**
     * Get a {@code DynamicArray} of each of the trajectory's segments.
     *
     * @return all of the trajectory's segments.
     */
    public DynamicArray<Segment> getSegments() {
        return segments;
    }

    /**
     * Get the currently-active segment.
     *
     * @return the currently-active segment.
     */
    public Segment getCurrentSegment() {
        return segments.get(currentSegment);
    }

    /**
     * Get the next segment. If there is no next segment, return null instead.
     * In addition to getting the next segment, this method will increment the
     * current pointer.
     *
     * @return the next segment (if it exists) or null (if it doesn't).
     */
    public Segment getNextSegment() {
        if (currentSegment <= segments.size() - 1) {
            return segments.get(currentSegment + 1);
        }

        return null;
    }

    /**
     * "Complete" the current segment by incrementing the current segment
     * counter by one. Throw an ArrayIndexOutOfBoundsException if the requested
     * index increment is out of the trajectory's array bounds.
     */
    public void completeSegment() {
        if (currentSegment <= segments.size() - 1) {
            currentSegment++;
        } else {
            throw new ArrayIndexOutOfBoundsException(
                    "No next segment at index " + currentSegment + " + 1!"
            );
        }
    }
}
