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

package me.wobblyyyy.pathfinder.robot;

import java.util.function.Supplier;

public class Velocity {
    private final Supplier<Double> velocitySupplier;
    private final Type velocityType;

    public Velocity(Supplier<Double> velocitySupplier,
                    Type velocityType) {
        this.velocitySupplier = velocitySupplier;
        this.velocityType = velocityType;
    }

    public static double perSecondToPerMinute(double perSecond) {
        return perSecond * 60;
    }

    public static double perMinuteToPerSecond(double perMinute) {
        return perMinute / 60;
    }

    private double raw() {
        return velocitySupplier.get();
    }

    public double get(Type type) {
        if (velocityType == type) {
            return raw();
        } else {
            if (velocityType == Type.ROTATIONS_PER_MINUTE) {
                return perMinuteToPerSecond(raw());
            } else {
                return perSecondToPerMinute(raw());
            }
        }
    }

    public double getRotationsPerSecond() {
        return get(Type.ROTATIONS_PER_SECOND);
    }

    public double getRotationsPerMinute() {
        return get(Type.ROTATIONS_PER_MINUTE);
    }

    public enum Type {
        ROTATIONS_PER_SECOND,
        ROTATIONS_PER_MINUTE
    }
}
