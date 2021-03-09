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

package me.wobblyyyy.pathfinder.geometry;

/**
 * Utility class for dealing with angles in both radians and degrees.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class Angle {
    public static final double ZERO = 0;
    public static final double PI = Math.PI;
    public static final double PI_TIMES_2 = PI * 2;
    public static final double PI_OVER_2 = PI / 2;
    public static final double PI_OVER_3 = PI / 3;
    public static final double PI_OVER_4 = PI / 4;
    public static final double PI_OVER_5 = PI / 5;
    public static final double PI_OVER_6 = PI / 6;
    public static final double PI_OVER_7 = PI / 7;
    public static final double PI_OVER_8 = PI / 8;

    private Angle() {

    }

    public static double toPiRads(double radsWithoutPi) {
        return radsWithoutPi * PI;
    }

    public static double toPiRads2(double radsWithoutPi) {
        return toPiRads(radsWithoutPi) * 2;
    }

    public static double fixRad(double rad) {
        while (rad < toPiRads(0)) rad += toPiRads(2);
        while (rad > toPiRads(2)) rad -= toPiRads(2);
        return rad;
    }

    public static double fixDeg(double deg) {
        while (deg < 0) deg += 360;
        while (deg > 360) deg -= 360;
        return deg;
    }
}
