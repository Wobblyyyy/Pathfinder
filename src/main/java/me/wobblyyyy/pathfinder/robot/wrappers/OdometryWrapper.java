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

package me.wobblyyyy.pathfinder.robot.wrappers;

import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Odometry;

/**
 * Wrapper that surrounds user-provided odometry systems in case the user wants
 * to swap their X and Y values or invert either their X or Y values. This does
 * not provide any additional functionality aside from swapping and inverting
 * X and Y values for odometry systems that may be calibrated correctly but
 * aren't reporting the proper values.
 */
public class OdometryWrapper implements Odometry {
    private final Odometry odometry;
    private final boolean odometrySwapXY;
    private final boolean odometryInvertX;
    private final boolean odometryInvertY;

    /**
     * Create a new OdometryWrapper.
     *
     * @param odometry        the odometry that will be wrapped by the wrapper.
     * @param odometrySwapXY  should the odometry's X and Y values be swapped?
     * @param odometryInvertX should the odometry's post-swap X be inverted?
     * @param odometryInvertY should the odometry's post-swap Y be inverted?
     */
    public OdometryWrapper(Odometry odometry,
                           boolean odometrySwapXY,
                           boolean odometryInvertX,
                           boolean odometryInvertY) {
        this.odometry = odometry;
        this.odometrySwapXY = odometrySwapXY;
        this.odometryInvertX = odometryInvertX;
        this.odometryInvertY = odometryInvertY;
    }

    /**
     * Get the robot's position and heading.
     *
     * @return the robot's position and heading.
     */
    @Override
    public HeadingPoint getPos() {
        HeadingPoint raw = odometry.getPos();

        if (odometrySwapXY) {
            raw = new HeadingPoint(raw.getY(), raw.getX(), raw.getHeading());
        }

        raw.scale(
                odometryInvertX ? -1 : 1,
                odometryInvertY ? -1 : 1
        );

        return raw;
    }

    /**
     * Update the odometry system.
     */
    @Override
    public void update() {
        odometry.update();
    }
}
