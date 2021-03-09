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

package me.wobblyyyy.pathfinder.annotations;

/**
 * Marker interface used for annotating methods that operate asynchronously.
 *
 * <p>
 * Async operations are operations that DO NOT have a result realized after the
 * completion of the execution of the annotated method. The result will be
 * realized at a later point in time, likely by another thread.
 * </p>
 *
 * <p>
 * This means that the program will:
 * <ul>
 *     <li>Get to the code annotated with the {@code Async} interface.</li>
 *     <li>Continue executing as intended.</li>
 *     <li>Without stopping, run the code contained in the method's body.</li>
 *     <li>Continue executing as intended.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Marker interfaces have no purpose other than to indicate to anyone who sees
 * the annotation that the annotated field or class or method has a certain
 * property.
 * </p>
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public @interface Async {
}
