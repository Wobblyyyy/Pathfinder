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

package me.wobblyyyy.pathfinder.config;

import me.wobblyyyy.pathfinder.followers.Followers;
import me.wobblyyyy.pathfinder.robot.Drive;
import me.wobblyyyy.pathfinder.error.NoFindersException;
import me.wobblyyyy.pathfinder.map.Map;
import me.wobblyyyy.pathfinder.robot.Odometry;
import me.wobblyyyy.pathfinder.robot.wrappers.DriveWrapper;
import me.wobblyyyy.pathfinder.robot.wrappers.OdometryWrapper;
import me.wobblyyyy.pathfinder.util.RobotProfile;

/**
 * The lowest-level and least-abstract configuration available for the
 * Pathfinder library.
 *
 * <p>
 * All of these settings are incredibly important to how the pathfinder
 * actually functions. If you're confused about why something isn't working,
 * you should read through this file and make sure that you're aware of
 * everything you're doing.
 * </p>
 *
 * <p>
 * Hey! You! Yes, you. If you're reading this, trying to figure out what the
 * hell is going wrong, you can feel free to shoot me an email on what's
 * missing documentation. This project is designed to help people, not
 * fry their brain.
 * </p>
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
@SuppressWarnings("UnusedReturnValue")
public class PathfinderConfig {
    /**
     * The rawOdometry subsystem that the robot uses.
     *
     * <p>
     * Obviously, this is pretty necessary for a pathfinder. If you don't
     * have any rawOdometry, you can't keep track of where the robot is. And if
     * you can't keep track of where the robot is, then how are you supposed
     * to actually navigate anywhere?
     * </p>
     */
    private Odometry rawOdometry;

    /**
     * A wrapper that surrounds the inputted rawOdometry system. This field
     * is automatically created on initialization of a configuration class
     * and you don't have to worry about it.
     */
    private OdometryWrapper wrappedOdometry;

    /**
     * The height of the field.
     *
     * <p>
     * Ultimately, you're free to do whatever the hell you'd like - I could
     * care less. However, for the sake of consistency, I'd suggest that all
     * units of measurement be in inches unless otherwise specified.
     * </p>
     *
     * <p>
     * <ul>
     *     <li>
     *         FRC Field Width: 323.25 inches
     *     </li>
     *     <li>
     *         FRC Field Height: 629.25 inches
     *     </li>
     *     <li>
     *         FTC Field Width: 144 inches
     *     </li>
     *     <li>
     *         FTC Field Height: 144 inches
     *     </li>
     * </ul>
     * </p>
     */
    private int fieldHeight;

    /**
     * The height of the field.
     *
     * <p>
     * Ultimately, you're free to do whatever the hell you'd like - I could
     * care less. However, for the sake of consistency, I'd suggest that all
     * units of measurement be in inches unless otherwise specified.
     * </p>
     *
     * <p>
     * <ul>
     *     <li>
     *         FRC Field Width: 323.25 inches
     *     </li>
     *     <li>
     *         FRC Field Height: 629.25 inches
     *     </li>
     *     <li>
     *         FTC Field Width: 144 inches
     *     </li>
     *     <li>
     *         FTC Field Height: 144 inches
     *     </li>
     * </ul>
     * </p>
     */
    private int fieldWidth;

    /**
     * The field's "specificity" factor, which determines how double to
     * integer conversions are handled.
     *
     * <p>
     * There aren't many rules for what this number can be - however, it has
     * to be non-zero and greater than 0.
     * </p>
     *
     * <p>
     * Higher specificity values provide more accuracy at the price of more
     * expensive calculation operations. Lower specificity values provide
     * less accuracy at the price of much faster calculations. Although I've
     * yet to do any performance profiling on it...
     * </p>
     *
     * <p>
     * <ul>
     *     <li>
     *         Low width, low height, low specificity.
     *         <ul>
     *             <li>Width: 128</li>
     *             <li>Height: 128</li>
     *             <li>Execution time: 113ms</li>
     *         </ul>
     *     </li>
     *     <li>
     *         High width, high height, low specificity.
     *         <ul>
     *             <li>Width: 1024</li>
     *             <li>Height: 1024</li>
     *             <li>Execution time: 622ms</li>
     *         </ul>
     *     </li>
     *     <li>
     *         High width, high height, high specificity.
     *         <ul>
     *             <li>Width: 2048</li>
     *             <li>Width: 2048</li>
     *             <li>Execution time: OutOfMemory exception...</li>
     *         </ul>
     *     </li>
     * </ul>
     * </p>
     *
     * <p>
     * My point is, choose a specificity value that doesn't hinder your
     * performance all that much. For FTC, I use a specificity of 6. For FRC,
     * I use a specificity of 2, because the field is so fucking big.
     * </p>
     */
    private int specificity;

    /**
     * The robot's X dimension.
     *
     * <p>
     * robotX and robotY are interchangeable - ultimately, the radius of the
     * robot's "hit circle" is dependent on the hypotenuse of the X and Y
     * values, so it doesn't really matter which is which.
     * </p>
     *
     * <p>
     * Ultimately, you're free to do whatever the hell you'd like - I could
     * care less. However, for the sake of consistency, I'd suggest that all
     * units of measurement be in inches unless otherwise specified.
     * </p>
     */
    private double robotX;

    /**
     * The robot's Y dimension.
     *
     * <p>
     * robotX and robotY are interchangeable - ultimately, the radius of the
     * robot's "hit circle" is dependent on the hypotenuse of the X and Y
     * values, so it doesn't really matter which is which.
     * </p>
     *
     * <p>
     * Ultimately, you're free to do whatever the hell you'd like - I could
     * care less. However, for the sake of consistency, I'd suggest that all
     * units of measurement be in inches unless otherwise specified.
     * </p>
     */
    private double robotY;

    /**
     * The X distance, measured in inches, between the FR-FL pair of swerve
     * modules. This distance should be measured from the very center of the
     * swerve module's rotational radius.
     */
    private double gapX;

    /**
     * The X distance, measured in inches, between the FR-BR pair of swerve
     * modules. This distance should be measured from the very center of the
     * swerve module's rotational radius.
     */
    private double gapY;

    /**
     * The robot's motion file, used in motion profiling and planning.
     *
     * <p>
     * Out-of-the-box, Pathfinder is tuned to use a proportional integral
     * derivative controller follower to follow a predefined path. In order
     * to effectively optimize this follower, there are some coefficients that
     * need to be known.
     * </p>
     *
     * <p>
     * For more information regarding robot profiling, go click on the
     * RobotProfile class. Here. A link, because I'm just so kind, right?
     * {@link RobotProfile}
     * </p>
     */
    private RobotProfile profile;

    /**
     * The robot's drivetrain.
     *
     * <p>
     * Now, obviously, we both know that a wheeled robot needs to have wheels
     * to move, right? In order to make the pathfinder able to control the
     * robot and its movement, we have to give the pathfinder some interface
     * to control the motors. This is that interface.
     * </p>
     */
    private Drive rawDrive;

    /**
     * Wrapper surrounding the robot's drive interface.
     */
    private Drive wrappedDrive;

    /**
     * The field's map.
     *
     * <p>
     * Go read these JavaDocs if you're confused about what a map is.
     * {@link Map}
     * </p>
     */
    private Map map;

    /**
     * What type of follower does the pathfinder use?
     *
     * <p>
     * All of the types of followers and information about them can be found
     * right here. Or here. Or, actually, here. Just kidding, it's right here:
     * {@link Followers}
     * </p>
     */
    private Followers follower;

    /**
     * Should the pathfinder use the lightning finder, or skip it entirely?
     *
     * <p>
     * Generally, using the lighting and fast followers is optimal, as the
     * amount of time required to generate a path is reduced by quite a fair
     * bit. However, if you'd prefer to not use the lightning generator, fear
     * not - you don't have to.
     * </p>
     */
    private boolean usesLightning;

    /**
     * Should the pathfinder use the fast finder, or skip it entirely?
     *
     * <p>
     * Generally, using the lighting and fast followers is optimal, as the
     * amount of time required to generate a path is reduced by quite a fair
     * bit. However, if you'd prefer to not use the lightning generator, fear
     * not - you don't have to.
     * </p>
     */
    private boolean usesFast;

    /**
     * Should the pathfinder use a theta star pathfinding algorithm?
     *
     * <p>
     * If lighting and fast followers don't generate a path, this one is the
     * fall-back. This pathfinder is slower, but will actually find a path,
     * rather than just running quick potential collision detection.
     * </p>
     */
    private boolean usesThetaStar;

    /**
     * The speed that certain types of linear followers should run at.
     */
    private double speed = 0.5;

    /**
     * Should the pathfinder swap X and Y pairs?
     */
    private boolean odometrySwapXY = false;

    /**
     * Should X values be inverted?
     */
    private boolean odometryInvertX = false;

    /**
     * Should Y values be inverted?
     */
    private boolean odometryInvertY = false;

    /**
     * Should the pathfinder swap X and Y pairs?
     */
    private boolean driveSwapXY = false;

    /**
     * Should the X values be inverted?
     */
    private boolean driveInvertX = false;

    /**
     * Should the Y values be inverted?
     */
    private boolean driveInvertY = false;

    /**
     * Create a new {@code PathfinderConfig} without any configuration elements
     * set.
     */
    public PathfinderConfig() {

    }

    /**
     * Create a new PathfinderConfig to be fed to a Pathfinder.
     *
     * <p>
     * For your own sake - please go read the field descriptions in this Java
     * file if you're confused about what any of these means. I've written out
     * nice and long descriptions for everything, so there shouldn't be all that
     * much confusion about what's going on.
     * </p>
     *
     * @param rawOdometry      the rawOdometry subsystem that's used by the pathfinder
     *                      in determining the robot's position. This rawOdometry
     *                      system should be as accurate as possible and maintain
     *                      contact with the ground at all times.
     * @param fieldWidth    the fieldWidth of the field, in whatever units you'd like.
     *                      Although the units don't matter much, you need to be
     *                      sure to keep the units consistent.
     * @param fieldHeight   the fieldHeight of the field, in whatever units you'd like.
     *                      Although the units don't matter much, you need to be
     *                      sure to keep the units consistent.
     * @param specificity   the field's specificity factor. Low specificity means
     *                      lower precision. High specificity means higher
     *                      precision. The higher the specificity value is, the
     *                      longer paths will take to find and the more resources
     *                      are needed on the host computer.
     * @param robotX        the robot's X dimension. X/Y are the same thing here.
     *                      That's not to say that you shouldn't measure X and Y
     *                      values - accuracy is still just as important as ever.
     * @param robotY        the robot's Y dimension. X/Y are the same thing here.
     *                      That's not to say that you shouldn't measure X and Y
     *                      values - accuracy is still just as important as ever.
     * @param gapX          the distance (in inches) between the pair of front
     *                      right and front left wheel centers.
     * @param gapY          the distance (in inches) between the pair of front
     *                      right and back right wheel centers.
     * @param profile       the robot's motion profiling profile. This profile
     *                      should provide (at least somewhat accurate) info
     *                      on the robot's motion in real life.
     * @param rawDrive         the robot's drivetrain. The drivetrain is (rather
     *                      obviously) used to actually rawDrive the robot.
     * @param map           a virtual map of something. In most cases, this is
     *                      a game field with all your different obstacles and
     *                      what not.
     * @param follower      what type of follower the pathfinder uses.
     * @param speed         the speed that linear followers should run at.
     * @param usesLightning see: {@link PathfinderConfig#usesLightning}
     * @param usesFast      see: {@link PathfinderConfig#usesFast}
     * @param usesThetaStar see: {@link PathfinderConfig#usesThetaStar}
     */
    public PathfinderConfig(Odometry rawOdometry,
                            int fieldWidth,
                            int fieldHeight,
                            int specificity,
                            double robotX,
                            double robotY,
                            double gapX,
                            double gapY,
                            RobotProfile profile,
                            Drive rawDrive,
                            Map map,
                            Followers follower,
                            double speed,
                            boolean usesLightning,
                            boolean usesFast,
                            boolean usesThetaStar) {
        this.rawOdometry = rawOdometry;
        this.wrappedOdometry = new OdometryWrapper(rawOdometry, false, false, false);
        this.fieldHeight = fieldHeight;
        this.fieldWidth = fieldWidth;
        this.specificity = specificity;
        this.robotX = robotX;
        this.robotY = robotY;
        this.gapX = gapX;
        this.gapY = gapY;
        this.profile = profile;
        this.rawDrive = rawDrive;
        this.wrappedDrive = new DriveWrapper(rawDrive, false, false, false);
        this.map = map;
        this.follower = follower;
        this.speed = speed;
        this.usesLightning = usesLightning;
        this.usesFast = usesFast;
        this.usesThetaStar = usesThetaStar;

        /*
         * Go read the JavaDoc for NoFindersException if you're confused
         * about what this means.
         *
         * me.wobblyyyy.pathfinder.error.NoFindersException
         */
        try {
            if (usesLightning && usesFast && usesThetaStar)
                throw new NoFindersException(
                        "You didn't enable any of the available finders, so " +
                                "Pathfinder wil be unable to find any paths. " +
                                "Please make sure at least one of the finders " +
                                "is enabled in your PathfinderConfig."
                );
        } catch (Exception ignored) {
        }
    }

    public PathfinderConfig setFollower(Followers follower) {
        this.follower = follower;
        return this;
    }

    public PathfinderConfig setUsesLightning(boolean usesLightning) {
        this.usesLightning = usesLightning;
        return this;
    }

    public PathfinderConfig setUsesFast(boolean usesFast) {
        this.usesFast = usesFast;
        return this;
    }

    public PathfinderConfig setUsesThetaStar(boolean usesThetaStar) {
        this.usesThetaStar = usesThetaStar;
        return this;
    }

    /**
     * Get the fieldHeight of the field.
     *
     * @return the field's fieldHeight.
     */
    public int getFieldHeight() {
        return fieldHeight;
    }

    public PathfinderConfig setFieldHeight(int fieldHeight) {
        this.fieldHeight = fieldHeight;
        return this;
    }

    /**
     * Get the fieldWidth of the field.
     *
     * @return the field's fieldWidth.
     */
    public int getFieldWidth() {
        return fieldWidth;
    }

    public PathfinderConfig setFieldWidth(int fieldWidth) {
        this.fieldWidth = fieldWidth;
        return this;
    }

    /**
     * Get the field's specificity.
     *
     * @return the field's specificity.
     */
    public int getSpecificity() {
        return specificity;
    }

    public PathfinderConfig setSpecificity(int specificity) {
        this.specificity = specificity;
        return this;
    }

    /**
     * Get the robot's X value.
     *
     * @return the robot's X value.
     */
    public double getRobotX() {
        return robotX;
    }

    public PathfinderConfig setRobotX(double robotX) {
        this.robotX = robotX;
        return this;
    }

    /**
     * Get the robot's Y value.
     *
     * @return the robot's Y value.
     */
    public double getRobotY() {
        return robotY;
    }

    public PathfinderConfig setRobotY(double robotY) {
        this.robotY = robotY;
        return this;
    }

    /**
     * The robot's X gap.
     *
     * @return the robot's X gap.
     */
    public double getGapX() {
        return gapX;
    }

    public PathfinderConfig setGapX(double gapX) {
        this.gapX = gapX;
        return this;
    }

    /**
     * The robot's Y gap.
     *
     * @return the robot's Y gap.
     */
    public double getGapY() {
        return gapY;
    }

    public PathfinderConfig setGapY(double gapY) {
        this.gapY = gapY;
        return this;
    }

    public Odometry getRawOdometry() {
        return rawOdometry;
    }

    /**
     * Get the rawOdometry system.
     *
     * @return the rawOdometry system.
     */
    public Odometry getOdometry() {
        return wrappedOdometry;
    }

    public PathfinderConfig setRawOdometry(Odometry rawOdometry) {
        this.wrappedOdometry =
                new OdometryWrapper(rawOdometry, odometrySwapXY, odometryInvertX, odometryInvertY);
        this.rawOdometry = rawOdometry;
        return this;
    }

    /**
     * Get the robot's motion profile.
     *
     * @return the robot's motion profile.
     */
    public RobotProfile getProfile() {
        return profile;
    }

    public PathfinderConfig setProfile(RobotProfile profile) {
        this.profile = profile;
        return this;
    }

    /**
     * Get the robot's raw drivetrain.
     *
     * @return the robot's raw drivetrain.
     */
    public Drive getRawDrive() {
        return rawDrive;
    }

    /**
     * Get the robot's drivetrain.
     *
     * @return the robot's drivetrain.
     */
    public Drive getDrive() {
        return wrappedDrive;
    }

    public void setDrive(Drive drive) {
        this.rawDrive = drive;
        wrappedDrive = new DriveWrapper(
                drive,
                driveSwapXY,
                driveInvertX,
                driveInvertY
        );
    }

    /**
     * Get the field's map.
     *
     * @return the field's map.
     */
    public Map getMap() {
        return map;
    }

    public PathfinderConfig setMap(Map map) {
        this.map = map;
        return this;
    }

    /**
     * Get what type of follower the pathfinder uses.
     *
     * @return the pathfinder's follower type.
     */
    public Followers getFollowerType() {
        return follower;
    }

    /**
     * Set the speed that linear followers will be executed at.
     *
     * @param speed the speed that linear followers will be executed at.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Get the speed that linear followers should run at.
     *
     * @return the speed linear followers should run at.
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Does the pathfinder utilize lightning finding?
     *
     * @return whether or not the pathfinder utilizes x type of finding.
     */
    public boolean doesUseLightning() {
        return usesLightning;
    }

    /**
     * Does the pathfinder utilize fast finding?
     *
     * @return whether or not the pathfinder utilizes x type of finding.
     */
    public boolean doesUseFast() {
        return usesFast;
    }

    /**
     * Does the pathfinder utilize ThetaStar finding?
     *
     * @return whether or not the pathfinder utilizes x type of finding.
     */
    public boolean doesUseThetaStar() {
        return usesThetaStar;
    }

    /**
     * Swap the X and Y coordinate inputs.
     */
    public void odometrySwapXY(boolean xy) {
        this.wrappedOdometry =
                new OdometryWrapper(
                        rawOdometry,
                        odometrySwapXY,
                        odometryInvertX,
                        odometryInvertY
                );
        this.odometrySwapXY = xy;
    }

    /**
     * Set the X coordinates to be inverted or not.
     * True = inverted, false = normal.
     */
    public void odometryInvertX(boolean invert) {
        this.wrappedOdometry =
                new OdometryWrapper(
                        rawOdometry,
                        odometrySwapXY,
                        odometryInvertX,
                        odometryInvertY
                );
        this.odometryInvertX = invert;
    }

    /**
     * Set the Y coordinates to be inverted or not.
     * True = inverted, false = normal.
     */
    public void odometryInvertY(boolean invert) {
        this.wrappedOdometry =
                new OdometryWrapper(
                        rawOdometry,
                        odometrySwapXY,
                        odometryInvertX,
                        odometryInvertY
                );
        this.odometryInvertY = invert;
    }

    /**
     * Are the X and Y coordinates swapped?
     */
    public boolean odometrySwapXY() {
        return odometrySwapXY;
    }

    /**
     * Is the X coordinate inverted?
     */
    public boolean odometryInvertX() {
        return odometryInvertX;
    }

    /**
     * Is the Y coordinate inverted?
     */
    public boolean odometryInvertY() {
        return odometryInvertY;
    }

    /**
     * Swap the X and Y coordinate inputs.
     */
    public void driveSwapXY(boolean xy) {
        this.wrappedDrive =
                new DriveWrapper(
                        rawDrive,
                        driveSwapXY,
                        driveInvertX,
                        driveInvertY
                );
        this.driveSwapXY = xy;
    }

    /**
     * Set the X coordinates to be inverted or not.
     * True = inverted, false = normal.
     */
    public void driveInvertX(boolean invert) {
        this.wrappedDrive =
                new DriveWrapper(
                        rawDrive,
                        driveSwapXY,
                        driveInvertX,
                        driveInvertY
                );
        this.driveInvertX = invert;
    }

    /**
     * Set the Y coordinates to be inverted or not.
     * True = inverted, false = normal.
     */
    public void driveInvertY(boolean invert) {
        this.wrappedDrive =
                new DriveWrapper(
                        rawDrive,
                        driveSwapXY,
                        driveInvertX,
                        driveInvertY
                );
        this.driveInvertY = invert;
    }

    /**
     * Are the X and Y coordinates swapped?
     */
    public boolean driveSwapXY() {
        return driveSwapXY;
    }

    /**
     * Is the X coordinate inverted?
     */
    public boolean driveInvertX() {
        return driveInvertX;
    }

    /**
     * Is the Y coordinate inverted?
     */
    public boolean driveInvertY() {
        return driveInvertY;
    }
}
