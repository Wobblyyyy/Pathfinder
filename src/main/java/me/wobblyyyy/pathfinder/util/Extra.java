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

package me.wobblyyyy.pathfinder.util;

import me.wobblyyyy.pathfinder.geometry.Point;

import java.util.ArrayList;

/**
 * Random utilities that don't have a specific classification.
 *
 * @author Colin Robertson
 * @version 1.0.0
 * @since 0.1.0
 */
public class Extra {
    /**
     * Remove any duplicates from an array list.
     *
     * @param list the list.
     * @param <T>  the list.
     * @return the list, without duplicates.
     */
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list) {
        ArrayList<T> newList = new ArrayList<T>();
        for (T element : list) {
            if (!newList.contains(element)) {
                newList.add(element);
            }
        }
        return newList;
    }

    public static ArrayList<Point> removeDuplicatePoints(
            ArrayList<Point> points) {
        ArrayList<Point> cleaned = new ArrayList<>();

        for (Point p : points) {
            boolean canAdd = true;
            for (Point c : cleaned) {
                if (Point.isSame(p, c)) canAdd = false;
            }
            if (canAdd) cleaned.add(p);
        }

        return cleaned;
    }
}
