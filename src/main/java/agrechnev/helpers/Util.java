package agrechnev.helpers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Oleksiy Grechnyev on 10/29/2016.
 * Misc small utils
 */
public class Util {
    // No instantiation, please
    private Util() {
    }

    /**
     * Check if a String is null or empty
     * Whitespace-only strings, e.g. "    " are considered empty
     * @param s
     * @return
     */
    public static boolean isEmptyString(String s) {
        if (s == null || s.trim().equals("")) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Create a separated list (e.g. comma-separated) out of a collection
     * and return it as a String
     *
     * @param collection The input collection
     * @param separator  The separator, e.g. ", "
     * @param <E>
     * @return The list as a string
     */
    public static <E> String separatedList(Collection<E> collection, String separator) {
        StringBuilder result = new StringBuilder();

        boolean first = true;

        for (E element : collection) {
            if (!first) {
                result.append(separator); // Add separator before if not first iteration
            } else {
                first = false;
            }

            result.append(element.toString()); // Add next element
        }

        return result.toString();
    }

    /**
     * Convert a list of arbitrary objects into a list of String
     * Using Arrays.toString for arrays
     *
     * @param in The input list
     * @return The list as strings
     */
    public static List<String> listToStringList(List<?> in) {
        return in.stream().map(
                o -> o.getClass().isArray() ? Arrays.toString((Object[]) o) : o.toString()
        ).collect(Collectors.toList());
    }

}
