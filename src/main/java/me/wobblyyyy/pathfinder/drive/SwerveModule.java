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

package me.wobblyyyy.pathfinder.drive;

import me.wobblyyyy.pathfinder.control.Controller;

import java.util.function.Consumer;
import java.util.function.Function;

public class SwerveModule {
    private final Consumer<Double> setTurn;
    private final Consumer<Double> setDrive;
    private final Function<Double, Double> getTargetDistance;
    private final Controller controller;

    public SwerveModule(Consumer<Double> setTurn,
                        Consumer<Double> setDrive,
                        Function<Double, Double> getTargetDistance,
                        Controller controller) {
        this.setTurn = setTurn;
        this.setDrive = setDrive;
        this.getTargetDistance = getTargetDistance;
        this.controller = controller;

        controller.setTarget(0);
    }

    public void update(double turnTarget,
                       double drivePower) {
        double turnPower = controller.calculate(
                getTargetDistance.apply(turnTarget)
        );

        setTurn.accept(turnPower);
        setDrive.accept(drivePower);
    }
}
