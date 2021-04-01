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

import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.math.functional.Average;

public class TankOdometry {
    private HeadingPoint position;
    private final Angle offsetAngle;
    private Angle previousAngle;
    private double previousL;
    private double previousR;

    public TankOdometry(Angle gyroscopeAngle,
                        HeadingPoint startingPosition) {
        offsetAngle = gyroscopeAngle;
        position = startingPosition;
    }

    public HeadingPoint update(double distanceL,
                               double distanceR,
                               Angle gyroscopeAngle) {
        double deltaL = distanceL - previousL;
        double deltaR = distanceR - previousR;
        double delta = Average.of(deltaL, deltaR);

        Angle angle = gyroscopeAngle.plus(offsetAngle);

        position = position.transform(
                0.0,
                delta,
                angle.minus(previousAngle)
        );

        previousL = distanceL;
        previousR = distanceR;
        previousAngle = angle;

        return position;
    }
}
