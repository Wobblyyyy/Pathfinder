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

import me.wobblyyyy.edt.StaticArray;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.util.Time;

@SuppressWarnings("unused")
public class SwerveOdometry {
    private final SwerveKinematics kinematics;
    private HeadingPoint position;
    private double previousSeconds = -1;

    private final Angle gyroOffset;

    public SwerveOdometry(SwerveKinematics kinematics,
                          Angle gyroOffset,
                          HeadingPoint initialPosition) {
        this.kinematics = kinematics;
        this.position = initialPosition;
        this.gyroOffset = initialPosition.getAngle().minus(gyroOffset);
    }

    public HeadingPoint getPosition() {
        return position;
    }

    private double getGapAndUpdateTime(double currentTime) {
        double gap = previousSeconds >= 0 ?
                currentTime - previousSeconds :
                0.0;
        previousSeconds = currentTime;
        return gap;
    }

    private Angle applyAngleOffset(Angle gyroAngle) {
        return Angle.fromDegrees(
                gyroAngle.getDegrees() +
                        gyroOffset.getDegrees()
        );
    }

    private RTransform getChassisStateFromStates(
            StaticArray<SwerveModuleState> states) {
        return kinematics.getTransform(states);
    }

    private double deltaX(RTransform transformation, double time) {
        return transformation.getY() * time - position.getX();
    }

    private double deltaY(RTransform transformation, double time) {
        return transformation.getY() * time - position.getY();
    }

    private double deltaTheta(RTransform transformation) {
        return transformation.getTurn().getDegrees() - position.getHeading();
    }

    private HeadingPoint transformCurrentPosition(double time,
                                                  RTransform transformation) {
        double dX = deltaX(transformation, time);
        double dY = deltaY(transformation, time);
        double dH = deltaTheta(transformation);

        return position.transform(dX, dY, Angle.fromDegrees(dH));
    }

    public HeadingPoint update(double currentSeconds,
                               Angle gyroAngle,
                               StaticArray<SwerveModuleState> states) {
        double timeSinceLastUpdate = getGapAndUpdateTime(currentSeconds);
        Angle angle = applyAngleOffset(gyroAngle);
        RTransform transformation = getChassisStateFromStates(states);
        this.position = transformCurrentPosition(
                timeSinceLastUpdate,
                transformation
        );
        return position;
    }

    public HeadingPoint update(Angle gyroAngle,
                               StaticArray<SwerveModuleState> states) {
        return update(
                Time.relativeTime(),
                gyroAngle,
                states
        );
    }
}
