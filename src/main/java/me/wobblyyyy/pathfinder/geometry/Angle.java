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
 * A representation of an angle. Angles are defined as... well, angles. I'd
 * hope you know what an angle is before trying to program a robot capable of
 * dynamic and reactive autonomous navigation. Anyways, angles can be expressed
 * in either radians or degrees. The purpose of this class is to make sure that
 * angles are expressed in the correct form - either radians or degrees -
 * when they should be in that form.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class Angle {
    /**
     * The angle, notated in degrees.
     */
    private final double angleDegrees;

    /**
     * The angle, notated in radians.
     */
    private final double angleRadians;

    /**
     * Create a new angle based off a measurement in degrees.
     *
     * @param angleDegrees the angle, represented in degrees.
     * @see #fromDegrees(double)
     * @see #fromRadians(double)
     */
    public Angle(double angleDegrees) {
        this.angleDegrees = angleDegrees;
        this.angleRadians = Math.toRadians(angleDegrees);
    }

    /**
     * Create a new angle from a measurement in degrees.
     *
     * @param degrees the degree measurement of the angle.
     * @return a new angle based on the desired measurement.
     * @see #Angle(double)
     * @see #fromRadians(double)
     */
    public static Angle fromDegrees(double degrees) {
        return new Angle(degrees);
    }

    /**
     * Create a new angle from a measurement in radians.
     *
     * @param radians the degree measurement of the angle.
     * @return a new angle based on the desired measurement.
     * @see #Angle(double)
     * @see #fromDegrees(double)
     */
    public static Angle fromRadians(double radians) {
        return new Angle(Math.toDegrees(radians));
    }

    /**
     * Get the angle in degrees.
     *
     * @return the angle in degrees.
     */
    public double getDegrees() {
        return angleDegrees;
    }

    /**
     * Get the angle in radians.
     *
     * @return the angle in radians.
     */
    public double getRadians() {
        return angleRadians;
    }

    /**
     * Get the angle's cosine.
     *
     * @return the angle's cosine.
     */
    public double cos() {
        return Math.cos(angleRadians);
    }

    /**
     * Get the angle's sine.
     *
     * @return the angle's sine.
     */
    public double sin() {
        return Math.sin(angleRadians);
    }

    /**
     * Get the angle's tangent.
     *
     * @return the angle's tangent.
     */
    public double tan() {
        return Math.tan(angleRadians);
    }

    /**
     * Get the angle's secant.
     *
     * @return the angle's secant.
     */
    public double sec() {
        return 1 / cos();
    }

    /**
     * Get the angle's cosecant.
     *
     * @return the angle's cosecant.
     */
    public double csc() {
        return 1 / sin();
    }

    /**
     * Get the angle's cotangent.
     *
     * @return the angle's cotangent.
     */
    public double cot() {
        return 1 / tan();
    }

    /**
     * Add the input angle to the current angle.
     *
     * @param angle the angle to add.
     * @return a new angle based on the current and the input angle.
     */
    public Angle plus(Angle angle) {
        return plus(this, angle);
    }

    /**
     * Subtract the input angle from the current angle.
     *
     * @param angle the angle to subtract.
     * @return a new angle based on the current and the input angle.
     */
    public Angle minus(Angle angle) {
        return minus(this, angle);
    }

    /**
     * Multiply the current angle by the input angle.
     *
     * @param angle the angle to multiply by.
     * @return a new angle based on the current and the input angle.
     */
    public Angle times(Angle angle) {
        return times(this, angle);
    }

    /**
     * Divide the current angle by the input angle.
     *
     * @param angle the angle to divide by.
     * @return a new angle based on the current and the input angle.
     */
    public Angle divide(Angle angle) {
        return divide(this, angle);
    }

    /**
     * Add two angles together.
     *
     * @param angle1 one of the two angles to add.
     * @param angle2 one of the two angles to add.
     * @return the sum of the two angles.
     */
    public static Angle plus(Angle angle1,
                             Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() + angle2.getDegrees());
    }

    /**
     * Subtract angle2 from angle1.
     *
     * @param angle1 one of the two angles to subtract.
     * @param angle2 one of the two angles to subtract.
     * @return the difference of the two angles.
     */
    public static Angle minus(Angle angle1,
                              Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() - angle2.getDegrees());
    }

    /**
     * Multiply angle1 and angle2 together.
     *
     * @param angle1 one of the two angles to multiply.
     * @param angle2 one of the two angles to multiply.
     * @return the angles multiplied together.
     */
    public static Angle times(Angle angle1,
                              Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() * angle2.getDegrees());
    }

    /**
     * Divide angle1 by angle2.
     *
     * @param angle1 one of the two angles to divide.
     * @param angle2 one of the two angles to divide.
     * @return the quotient of the two angles.
     */
    public static Angle divide(Angle angle1,
                               Angle angle2) {
        return Angle.fromDegrees(angle1.getDegrees() / angle2.getDegrees());
    }
}
