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

package me.wobblyyyy.pathfinder.zones;

import me.wobblyyyy.pathfinder.geometry.Shape;
import me.wobblyyyy.pathfinder.geometry.Zone;
import me.wobblyyyy.pathfinder.shapes.FieldShapeFRC;

/**
 * A generic zone representing an FRC field.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class FieldZoneFRC implements Zone {
    @Override
    public String getName() {
        return "Field";
    }

    @Override
    public Shape getParentShape() {
        return FieldShapeFRC.EMPTY;
    }

    @Override
    public int getZonePriority() {
        return 0;
    }

    @Override
    public double getDriveSpeedMultiplier() {
        return 0;
    }

    @Override
    public int getComponents() {
        return 0;
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}
