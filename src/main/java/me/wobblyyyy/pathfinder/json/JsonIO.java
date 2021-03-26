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

package me.wobblyyyy.pathfinder.json;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.wobblyyyy.edt.Arrayable;
import me.wobblyyyy.edt.DynamicArray;
import me.wobblyyyy.pathfinder.geometry.HeadingPoint;
import me.wobblyyyy.pathfinder.geometry.Spline;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Input/output class for use in loading and saving JSON information to the
 * host device's local file system. In cases where there's a very large path
 * or very dense trajectory that would take up a lot of time to generate on the
 * fly, saving and loading that path or trajectory to the host's filesystem
 * can decrease the amount of time it takes to deal with that path.
 *
 * @author Colin Robertson
 * @since 0.5.0
 */
public class JsonIO {
    /**
     * GSON instance used for doing lovely GSON things.
     */
    private static final Gson gson = new Gson();

    /**
     * Type used for point lists.
     */
    private static final Type POINT_LIST_TYPE =
            new TypeToken<ArrayList<HeadingPoint>>() {
            }.getType();

    /**
     * Type used for segment lists.
     */
    private static final Type SEGMENT_LIST_TYPE =
            new TypeToken<ArrayList<Spline>>() {
            }.getType();

    /**
     * Empty private constructor to ensure that utility class cannot be
     * instantiated by a user.
     */
    private JsonIO() {

    }

    /**
     * Ensure that a file exists, and if it doesn't, create it.
     *
     * @param pathToFile the path of the file that will be assured.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void assureFileExists(String pathToFile) {
        File file = new File(pathToFile) {{
            try {
                createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }};
    }

    /**
     * Get a file writer for the specified path.
     *
     * @param path the path that the writer will write to.
     * @return a file writer from the path.
     */
    private static FileWriter getFileWriter(String path) {
        assureFileExists(path);

        try {
            return new FileWriter(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get a file reader for the specified path.
     *
     * @param path the path that the reader will read from.
     * @return a file reader from the path.
     */
    private static FileReader getFileReader(String path) {
        assureFileExists(path);

        try {
            return new FileReader(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Convert an {@code ArrayList} of points to a JSON string.
     *
     * @param points the points to parse.
     * @return a JSON string, representing all of the points.
     */
    public static String pointsToJson(ArrayList<HeadingPoint> points) {
        return gson.toJson(points, POINT_LIST_TYPE);
    }

    /**
     * Get an {@code ArrayList} of points from a JSON string.
     *
     * @param json the string to parse.
     * @return the points from the string.
     */
    public static ArrayList<HeadingPoint> pointsFromJson(String json) {
        return gson.fromJson(json, POINT_LIST_TYPE);
    }

    /**
     * Save an {@code ArrayList} of {@code HeadingPoint}s to a JSON file.
     *
     * @param path      the path of the JSON file to save.
     * @param pointList the {@code ArrayList} of points to save.
     */
    public static void savePoints(String path,
                                  ArrayList<HeadingPoint> pointList) {
        FileWriter writer = getFileWriter(path);
        assert writer != null;

        try {
            writer.write(pointsToJson(pointList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read an {@code ArrayList} of {@code HeadingPoint}s from a JSON file.
     *
     * @param path the path to the JSON file that will be read from.
     * @return the generated {@code ArrayList}.
     */
    public static ArrayList<HeadingPoint> loadPoints(String path) {
        FileReader reader = getFileReader(path);
        assert reader != null;

        return gson.fromJson(reader, POINT_LIST_TYPE);
    }

    /**
     * Convert an {@code Arrayable} to an ArrayList of heading points.
     *
     * @param points the points to convert to an {@code ArrayList}.
     * @return the converted {@code ArrayList}.
     */
    public static ArrayList<HeadingPoint> toArrayList(
            Arrayable<HeadingPoint> points) {
        ArrayList<HeadingPoint> list = new ArrayList<>(points.size());
        points.itr().forEach(list::add);
        return list;
    }

    /**
     * Convert an {@code ArrayList} into a {@code DynamicArray}.
     *
     * @param points the points to convert.
     * @return the converted points.
     */
    public static DynamicArray<HeadingPoint> toDynamicArray(
            ArrayList<HeadingPoint> points) {
        return new DynamicArray<>(points);
    }
}
