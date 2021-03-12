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
import me.wobblyyyy.pathfinder.geometry.Point;
import me.wobblyyyy.pathfinder.util.Distance;

public class RTransform {
    private final Point start;
    private final Point stop;
    private final Angle turn;

    private final double x;
    private final double y;

    public RTransform(Point start,
                      Point stop,
                      Angle turn) {
        this.start = start;
        this.stop = stop;
        this.turn = turn;

        this.x = Distance.distanceX(start, stop);
        this.y = Distance.distanceY(start, stop);
    }

    public RTransform(double x,
                      double y,
                      Angle turn) {
        this(
                new Point(0, 0),
                new Point(x, y),
                turn
        );
    }

    public Point getStart() {
        return start;
    }

    public Point getStop() {
        return stop;
    }

    public Angle getTurn() {
        return turn;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
