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

package me.wobblyyyy.pathfinder.runtime;

/**
 * Scan the classpath to look for classes that are annotated with the
 * {@link me.wobblyyyy.pathfinder.annotations.Paths} annotation. Any class
 * annotated with this annotation is then scanned for instances of either
 * {@link me.wobblyyyy.pathfinder.annotations.RelativePath} or
 * {@link me.wobblyyyy.pathfinder.annotations.AbsolutePath}. If instances of
 * either of those two annotations are found, these instances are added to
 * a runtime registry of possible dynamic paths. This way, users can access
 * a path they declared somewhere else. Additionally, this provides an easy
 * method for users to dynamically run a path.
 *
 * @author Colin Robertson
 * @since 0.3.0
 */
public class AnnotationProcessor {
}
