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

package me.wobblyyyy.pathfinder.math;

/**
 * A virtual representation of an input/output gear ratio. A specified amount
 * of input rotation corresponds to a specified amount of output rotation.
 * The intention of the gear ratio class is to make it easier to figure out
 * gear ratios - putting in a double can be somewhat hard to interpret at
 * times.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class GearRatio {
    /**
     * The input component of the ratio.
     */
    private final double inputRotations;

    /**
     * The output component of the ratio.
     */
    private final double outputRotations;

    /**
     * The ratio of inputs to outputs. 1 input = ? output.
     */
    private final double inputToOutput;

    /**
     * The ratio of outputs to inputs. 1 output = ? input.
     */
    private final double outputToInput;

    /**
     * Create a new {@code GearRatio} by providing both input and output
     * values. If, say, you have a 2:1 gear ratio, you can call this
     * constructor and pass those parameters in that order.
     *
     * @param inputRotations  the amount of input rotations that corresponds
     *                        to however many output rotations.
     * @param outputRotations the amount of output rotations that corresponds
     *                        to however many input rotations.
     * @see #oneInputEquals(double)
     * @see #oneOutputEquals(double)
     */
    public GearRatio(double inputRotations,
                     double outputRotations) {
        this.inputRotations = inputRotations;
        this.outputRotations = outputRotations;
        this.inputToOutput = outputRotations / inputRotations;
        this.outputToInput = inputRotations / outputRotations;
    }

    /**
     * Create a new {@code GearRatio} where one input rotation equals the
     * specified number of output rotations.
     *
     * @param oneOutput how much each input rotation should turn the output
     *                  rotation value by.
     * @return a shiny new {@code GearRatio}.
     */
    public static GearRatio oneInputEquals(double oneOutput) {
        return new GearRatio(1, oneOutput);
    }

    /**
     * Create a new {@code GearRatio} where one output rotation equals the
     * specified number of input rotations.
     *
     * @param oneInput how much each output rotation should turn the input
     *                 rotation value by.
     * @return a shiny new {@code GearRatio}.
     */
    public static GearRatio oneOutputEquals(double oneInput) {
        return new GearRatio(oneInput, 1);
    }

    /**
     * Get the input rotations component of the ratio. THIS IS NOT A RATIO!
     * This is just how many input rotations equal however many output
     * rotations - nothing more, nothing less.
     *
     * @return the input component of the gear ratio.
     */
    public double getInputRotations() {
        return inputRotations;
    }

    /**
     * Get the output rotations component of the ratio. THIS IS NOT A RATIO!
     * This is just how many output rotations equal however many input
     * rotations - nothing more, nothing less.
     *
     * @return the output component of the gear ratio.
     */
    public double getOutputRotations() {
        return outputRotations;
    }

    /**
     * How many output rotations is the specified input rotation worth? If you
     * have a gear ratio of 2:1, and you fed this method 1, this method would
     * return the following value: 0.5.
     *
     * @param input the amount of rotations to calculate for.
     * @return the calculated amount of output rotations.
     */
    public double howManyOut(double input) {
        return input * inputToOutput;
    }

    /**
     * How many input rotations is the specified output rotation worth? If you
     * have a gear ratio of 2:1, and you fed this method 1, this method would
     * return the following value: 2.
     *
     * @param output the amount of rotations to calculate for.
     * @return the calculated amount of output rotations.
     */
    public double howManyIn(double output) {
        return output * outputToInput;
    }
}
