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

package me.wobblyyyy.pathfinder.control;

/**
 * A coefficient container for a PID controller. These coefficients can be
 * dynamically set during operation of the PID controller.
 *
 * @author Colin Robertson
 * @since 0.6.1
 */
public class PidCoefficients {
    private Coefficient kP;
    private Coefficient kI;
    private Coefficient kD;

    public PidCoefficients(double proportional,
                           double integral,
                           double derivative) {
        this(
                new Coefficient(proportional),
                new Coefficient(integral),
                new Coefficient(derivative)
        );
    }

    public PidCoefficients(Coefficient proportional,
                           Coefficient integral,
                           Coefficient derivative) {
        kP = proportional;
        kI = integral;
        kD = derivative;
    }

    public void setP(double coefficient) {
        setP(new Coefficient(coefficient));
    }

    public void setP(Coefficient coefficient) {
        kP = coefficient;
    }

    public void setI(double coefficient) {
        setI(new Coefficient(coefficient));
    }

    public void setI(Coefficient coefficient) {
        kI = coefficient;
    }

    public void setD(double coefficient) {
        setD(new Coefficient(coefficient));
    }

    public void setD(Coefficient coefficient) {
        kD = coefficient;
    }

    public Coefficient getP() {
        return kP;
    }

    public Coefficient getI() {
        return kI;
    }

    public Coefficient getD() {
        return kD;
    }
}
