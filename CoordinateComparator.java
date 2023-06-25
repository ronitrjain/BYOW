package byow.Core;

import java.util.Comparator;
public class CoordinateComparator implements Comparator<int[]> {
    @Override
    public int compare(int[] c1, int[] c2) {
        // Compare the y-coordinates first
        int result = Integer.compare(c1[1], c2[1]);
        if (result == 0) {
            // If the y-coordinates are equal, compare the x-coordinates
            result = Integer.compare(c1[0], c2[0]);
        }
        return result;
    }
}

