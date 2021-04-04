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

import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.pathfinder.control.Controller;
import me.wobblyyyy.pathfinder.control.ProportionalController;
import me.wobblyyyy.pathfinder.geometry.Angle;
import me.wobblyyyy.pathfinder.geometry.AngleUtils;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.kinematics.*;
import me.wobblyyyy.pathfinder.math.functional.Reciprocal;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.robot.Motor;
import me.wobblyyyy.pathfinder.robot.Odometry;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SwerveDrive implements Drive, Odometry {
    private Supplier<Angle> getGyroAngle;

    private Motor motor_fr_t;
    private Motor motor_fr_d;
    private Motor motor_fl_t;
    private Motor motor_fl_d;
    private Motor motor_br_t;
    private Motor motor_br_d;
    private Motor motor_bl_t;
    private Motor motor_bl_d;

    private Supplier<Double> fr_pos_deg;
    private Supplier<Double> fl_pos_deg;
    private Supplier<Double> br_pos_deg;
    private Supplier<Double> bl_pos_deg;

    private Controller fr_controller;
    private Controller fl_controller;
    private Controller br_controller;
    private Controller bl_controller;

    private SwerveModule fr;
    private SwerveModule fl;
    private SwerveModule br;
    private SwerveModule bl;

    private SpeedConverter frConverter;
    private SpeedConverter flConverter;
    private SpeedConverter brConverter;
    private SpeedConverter blConverter;

    private Arrayable<SpeedConverter> speedConverters;

    private Arrayable<Point> modulePositions;

    private SwerveKinematics kinematics;
    private SwerveOdometry odometry;

    private Arrayable<SwerveState> states;

    public SwerveDrive() {

    }

    private void initialize() {
        initializeKinematics();
        initializeOdometry();
        initializeControllers();
        initializeSwerveModules();
    }

    private void initializeKinematics() {
        kinematics = new SwerveKinematics(modulePositions);
    }

    private void initializeOdometry() {
        odometry = new SwerveOdometry(kinematics);
    }

    private void initializeControllers() {
        fr_controller = fr_controller == null ?
                new TurnController() :
                fr_controller;
        fl_controller = fl_controller == null ?
                new TurnController() :
                fl_controller;
        br_controller = br_controller == null ?
                new TurnController() :
                br_controller;
        bl_controller = bl_controller == null ?
                new TurnController() :
                bl_controller;
    }

    private void initializeSwerveModules() {
        SwerveModule fr = new SwerveModule(
                motor_fr_t::setPower,
                motor_fr_d::setPower,
                this::distanceFr,
                fr_controller
        );
        SwerveModule fl = new SwerveModule(
                motor_fl_t::setPower,
                motor_fr_d::setPower,
                this::distanceFl,
                fl_controller
        );
        SwerveModule br = new SwerveModule(
                motor_br_t::setPower,
                motor_br_d::setPower,
                this::distanceBr,
                br_controller
        );
        SwerveModule bl = new SwerveModule(
                motor_bl_t::setPower,
                motor_br_d::setPower,
                this::distanceBl,
                bl_controller
        );

        setModule(this::setFr, fr);
        setModule(this::setFl, fl);
        setModule(this::setBr, br);
        setModule(this::setBl, bl);
    }

    private void setModule(Consumer<SwerveModule> setModule,
                           SwerveModule module) {
        setModule.accept(module);
    }

    private void setFr(SwerveModule fr) {
        this.fr = fr;
    }

    private void setFl(SwerveModule fl) {
        this.fl = fl;
    }

    private void setBr(SwerveModule br) {
        this.br = br;
    }

    private void setBl(SwerveModule bl) {
        this.bl = bl;
    }

    private Arrayable<SwerveState> getStates(RTransform transform) {
        return kinematics.getStates(transform);
    }

    private void decodeModules(Arrayable<SwerveState> modules) {
        states = modules;

        SwerveState frState = modules.get(0);
        SwerveState flState = modules.get(1);
        SwerveState brState = modules.get(2);
        SwerveState blState = modules.get(3);

        decodeModule(frState, this::setFr);
        decodeModule(flState, this::setFl);
        decodeModule(brState, this::setBr);
        decodeModule(brState, this::setBl);
    }

    private void decodeModule(SwerveState state,
                              BiConsumer<Double, Double> acceptor) {
        acceptor.accept(
                state.getDegrees(),
                state.getPower()
        );
    }

    private void setFr(double turn,
                       double power) {
        fr.update(turn, power);
    }

    private void setFl(double turn,
                       double power) {
        fl.update(turn, power);
    }

    private void setBr(double turn,
                       double power) {
        br.update(turn, power);
    }

    private void setBl(double turn,
                       double power) {
        bl.update(turn, power);
    }

    private double getLowestDistance(double t1,
                                     double t2) {
        double absT1 = Math.abs(t1);
        double absT2 = Math.abs(t2);

        return absT1 < absT2 ? t1 : t2;
    }

    private double distance(double current,
                            double target) {
        double pos = AngleUtils.fixDeg(current);

        double distanceT1 = AngleUtils.fixDeg(target - pos);
        double distanceT2 = AngleUtils.fixDeg(pos - target);

        return getLowestDistance(distanceT1, distanceT2);
    }

    private double distanceFr(double target) {
        return distance(fr_pos_deg.get(), target);
    }

    private double distanceFl(double target) {
        return distance(fl_pos_deg.get(), target);
    }

    private double distanceBr(double target) {
        return distance(br_pos_deg.get(), target);
    }

    private double distanceBl(double target) {
        return distance(bl_pos_deg.get(), target);
    }

    private Arrayable<SwerveState> getStates() {
        return states;
    }

    @Override
    public void drive(RTransform transform) {
        decodeModules(
                getStates(
                        transform
                )
        );
    }

    @Override
    public void enableUserControl() {

    }

    @Override
    public void disableUserControl() {

    }

    @Override
    public HeadingPoint getPos() {
        return odometry.getPosition();
    }

    @Override
    public void update() {
        odometry.update(
                getGyroAngle.get(),
                SpeedConverter.getSwerveModuleStates(
                        states,
                        speedConverters
                )
        );
    }

    private static class TurnController extends ProportionalController {
        public TurnController() {
            super(Reciprocal.of(90));
        }
    }
}
