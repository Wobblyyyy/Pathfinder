/*
 *  ======================================================================
 *  || Copyright (c) 2020 Colin Robertson (wobblyyyy@gmail.com)         ||
 *  ||                                                                  ||
 *  || This file is part of the "Pathfinder" project, which is licensed ||
 *  || and distributed under the GPU General Public License V3.         ||
 *  ||                                                                  ||
 *  || Pathfinder is available on GitHub:                               ||
 *  || https://github.com/Wobblyyyy/Pathfinder                          ||
 *  ||                                                                  ||
 *  || Pathfinder's license is available:                               ||
 *  || https://www.gnu.org/licenses/gpl-3.0.en.html                     ||
 *  ||                                                                  ||
 *  || Re-distribution of this, or any other files, is allowed so long  ||
 *  || as this same copyright notice is included and made evident.      ||
 *  ||                                                                  ||
 *  || Unless required by applicable law or agreed to in writing, any   ||
 *  || software distributed under the license is distributed on an "AS  ||
 *  || IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either  ||
 *  || express or implied. See the license for specific language        ||
 *  || governing permissions and limitations under the license.         ||
 *  ||                                                                  ||
 *  || Along with this file, you should have received a license file,   ||
 *  || containing a copy of the GNU General Public License V3. If you   ||
 *  || did not receive a copy of the license, you may find it online.   ||
 *  ======================================================================
 *
 */

package me.wobblyyyy.pathfinder.drive.meccanum;

import me.wobblyyyy.pathfinder.drive.Drive;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.robot.Motor;

/**
 * Meccanum drivetrains are surprisingly annoying to work with, let me
 * just say that now.
 *
 * <p>
 * As usual, all of the math behind this is available online. In this case,
 * a lot of the meccanum control algorithm comes from the following site.
 * <a href="https://forums.parallax.com/uploads/attachments/53043/79261.pdf">
 * (here)
 * </a>
 * </p>
 *
 * <p>
 * <ul>
 *     <li>Wheel 1: front-left</li>
 *     <li>Wheel 2: front-right</li>
 *     <li>Wheel 3: back-left</li>
 *     <li>Wheel 4: back-right</li>
 * </ul>
 * <pre>
 * V_1 = V_d * sin(theta_d + PI/4) + V_theta (FL)
 * V_2 = V_d * cos(theta_d + PI/4) - V_theta (FR)
 * V_3 = V_d * cos(theta_d + PI/4) + V_theta (BL)
 * V_4 = V_d * sin(theta_d + PI/4) - V_theta (BR)
 * </pre>
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Meccanum implements Drive {
    private final Motor fr;
    private final Motor fl;
    private final Motor br;
    private final Motor bl;

    /**
     * Create a new instance of the meccanum drivetrain.
     *
     * @param fr front-right motor.
     * @param fl front-left motor.
     * @param br back-right motor.
     * @param bl back-left motor.
     */
    public Meccanum(Motor fr,
                    Motor fl,
                    Motor br,
                    Motor bl) {
        this.fr = fr;
        this.fl = fl;
        this.br = br;
        this.bl = bl;
    }

    /**
     * Drive the robot between two points.
     *
     * <p>
     * This uses tangent functionality to determine angle and drive the robot
     * based on that determined angle.
     * </p>
     *
     * @param start the start point.
     * @param end the end point.
     * @param power the percent (0 to 1) of power the drivetrain should
     */
    @Override
    public void drive(HeadingPoint start, HeadingPoint end, double power) {
        HeadingPoint difference = new HeadingPoint(
                end.getX() - start.getX(),
                end.getY() - start.getY(),
                end.getHeading() - start.getHeading()
        );

        double angle = difference.getTheta();

        drive(power, angle);
    }

    /**
     * Drive the robot - woot woot!
     *
     * <p>
     * Unlike the other drive method, this method just uses an inputted angle
     * rather than having to generate an angle.
     * </p>
     *
     * @param power the power at which the motors should operate.
     * @param angle the angle at which the motors should point.
     */
    @Override
    public void drive(double power, double angle) {
        final double cos = Math.cos(
                Math.toRadians(angle) +
                        (Math.PI / 4)
        );
        final double sin = Math.sin(
                Math.toRadians(angle) +

                        (Math.PI / 4)
        );
        double fr = power * cos - 0.0;
        double fl = power * sin + 0.0;
        double br = power * sin - 0.0;
        double bl = power * cos + 0.0;

        this.fr.setPower(fr);
        this.fl.setPower(fl);
        this.br.setPower(br);
        this.bl.setPower(bl);
    }

    /**
     * Enable the user being able to control the robot.
     */
    @Override
    public void enableUserControl() {
        fr.enableUserControl();
        fl.enableUserControl();
        br.enableUserControl();
        bl.enableUserControl();
    }

    /**
     * Disable the user being able to control the robot.
     */
    @Override
    public void disableUserControl() {
        fr.disableUserControl();
        fl.disableUserControl();
        br.disableUserControl();
        bl.disableUserControl();
    }
}
