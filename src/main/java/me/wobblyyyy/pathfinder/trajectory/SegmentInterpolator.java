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

package me.wobblyyyy.pathfinder.trajectory;

import me.wobblyyyy.intra.ftc2.utils.math.Range;
import me.wobblyyyy.pathfinder.geometry.Point;

public class SegmentInterpolator {
    private final Segment segment;

    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    private final double sizeX;
    private final double sizeY;

    private final Range xRange;
    private final Range yRange;

    public SegmentInterpolator(Segment segment) {
        this.segment = segment;

        minX = segment.minimum().getX();
        minY = segment.minimum().getY();
        maxX = segment.maximum().getX();
        maxY = segment.maximum().getY();

        sizeX = maxX - minX;
        sizeY = maxY - minY;

        xRange = new Range(minX, maxX);
        yRange = new Range(minY, maxY);
    }

    public boolean validX(double x) {
        return xRange.inRange(x);
    }

    public boolean validY(double y) {
        return yRange.inRange(y);
    }

    public boolean valid(Point point) {
        return validX(point.getX()) && validY(point.getY());
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public double percentX(double x) {
        if (!validX(x)) return -1;

        double withoutMin = x - minX;
        double percentOf = withoutMin / sizeX;

        return percentOf;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public double percentY(double y) {
        if (!validY(y)) return -1;

        double withoutMin = y - minY;
        double percentOf = withoutMin / sizeY;

        return percentOf;
    }

    public Point atPercentX(double percentX) {
        double realX = (percentX * sizeX) + minX;

        return segment.interpolateFromX(realX);
    }

    public Point atPercentY(double percentY) {
        double realY = (percentY * sizeY) + minY;

        return segment.interpolateFromY(realY);
    }

    public Point atPercent(double percent) {
        Point basedOnX = atPercentX(percent);
        Point basedOnY = atPercentY(percent);

        return new Point(
                ((basedOnX.getX() + basedOnY.getX()) / 2),
                ((basedOnX.getY() + basedOnY.getY()) / 2)
        );
    }
}
