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

package me.wobblyyyy.pathfinder.test.general;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.tracking.Tracker;

public class TestOdometry implements Tracker {
    private double size;
    private HeadingPoint pos = new HeadingPoint(0, 0, 0);

    public void setPos(HeadingPoint pos) {
        this.pos = pos;
    }

    public void setSize(double size) {
        this.size = size;
    }

    /**
     * Get the robot's position and heading.
     *
     * @return the robot's position and heading.
     */
    @Override
    public HeadingPoint getPos() {
        return pos;
    }

    /**
     * Update the odometry system.
     */
    @Override
    public void update() {

    }

    @Override
    public HeadingPoint getFrPos() {
        return HeadingPoint.add(
                new HeadingPoint(
                        size / 2,
                        size / 2,
                        0
                ),
                getPos()
        );
    }

    @Override
    public HeadingPoint getFlPos() {
        return HeadingPoint.add(
                new HeadingPoint(
                        -size / 2,
                        size / 2,
                        0
                ),
                getPos()
        );
    }

    @Override
    public HeadingPoint getBrPos() {
        return HeadingPoint.add(
                new HeadingPoint(
                        size / 2,
                        -size / 2,
                        0
                ),
                getPos()
        );
    }

    @Override
    public HeadingPoint getBlPos() {
        return HeadingPoint.add(
                new HeadingPoint(
                        -size / 2,
                        -size / 2,
                        0
                ),
                getPos()
        );
    }
}
