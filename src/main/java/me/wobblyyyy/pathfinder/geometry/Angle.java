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

import me.wobblyyyy.intra.ftc2.utils.math.Math;

public class Angle {
    private final double angleDegrees;
    private final double angleRadians;

    public Angle(double angleDegrees) {
        this.angleDegrees = angleDegrees;
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    public static Angle fromDegrees(double degrees) {
        return new Angle(degrees);
    }

    public static Angle fromRadians(double radians) {
        return new Angle(Math.toDegrees(radians));
    }

    public double getDegrees() {
        return angleDegrees;
    }

    public double getRadians() {
        return angleRadians;
    }

    public double getCos() {
        return Math.cos(angleRadians);
    }

    public double getSin() {
        return Math.sin(angleRadians);
    }

    public double getTan() {
        return Math.tan(angleRadians);
    }

    public double getSec() {
        return 1 / getCos();
    }

    public double getCsc() {
        return 1 / getSin();
    }

    public double getCot() {
        return 1 / getTan();
    }

    public Angle plus(Angle angle) {
        return plus(this, angle);
    }

    public Angle minus(Angle angle) {
        return minus(this, angle);
    }

    public Angle times(Angle angle) {
        return times(this, angle);
    }

    public Angle divide(Angle angle) {
        return divide(this, angle);
    }

    public static Angle plus(Angle angle1,
                             Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() + angle2.getDegrees());
    }

    public static Angle minus(Angle angle1,
                              Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() - angle2.getDegrees());
    }

    public static Angle times(Angle angle1,
                              Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() * angle2.getDegrees());
    }

    public static Angle divide(Angle angle1,
                               Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() / angle2.getDegrees());
    }
}
