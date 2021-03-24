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

package me.wobblyyyy.pathfinder.kinematics;

import java.util.stream.DoubleStream;

public class MeccanumState {
    private ModuleState fl;
    private ModuleState fr;
    private ModuleState bl;
    private ModuleState br;

    public MeccanumState(ModuleState fl,
                         ModuleState fr,
                         ModuleState bl,
                         ModuleState br) {
        this.fl = fl;
        this.fr = fr;
        this.bl = bl;
        this.br = br;
    }

    public MeccanumState(double fl,
                         double fr,
                         double bl,
                         double br) {
        this(
                new ModuleState(fl),
                new ModuleState(fr),
                new ModuleState(bl),
                new ModuleState(br)
        );
    }

    public double flPower() {
        return fl.getPower();
    }

    public double frPower() {
        return fr.getPower();
    }

    public double blPower() {
        return bl.getPower();
    }

    public double brPower() {
        return br.getPower();
    }

    public ModuleState fl() {
        return fl;
    }

    public ModuleState fr() {
        return fr;
    }

    public ModuleState bl() {
        return bl;
    }

    public ModuleState br() {
        return br;
    }

    public double maxPower() {
        return DoubleStream.of(
                Math.abs(fl.getPower()),
                Math.abs(fr.getPower()),
                Math.abs(bl.getPower()),
                Math.abs(br.getPower())
        ).max().getAsDouble();
    }

    public void normalize(double max) {
        double realMax = maxPower();

        if (realMax > max) {
            fl = new ModuleState(flPower() / realMax * max);
            fr = new ModuleState(frPower() / realMax * max);
            bl = new ModuleState(blPower() / realMax * max);
            br = new ModuleState(brPower() / realMax * max);
        }
    }

    public void normalize() {
        normalize(1.0);
    }

    public void normalizeWithMax() {
        double realMax = maxPower();

        normalize(realMax);
    }

    public void normalizeFromMaxUnderOne() {
        double max = maxPower();

        normalize(max > 1 ? 1.0 : max);
    }
}