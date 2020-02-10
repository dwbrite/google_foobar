package elevator_maintenance;

import java.util.*;

public class Answer {

    private static class VersionComparator implements Comparator<String>{
        @Override
        public int compare(String first, String second) {
            String[] firstVnums = first.split("\\.");
            String[] secondVnums = second.split("\\.");

            // There are 3 components to a version number: Major, Minor, and Revision
            int numParts = 3;

            // Convert string[] to new int[numParts]
            int[] firstInts = new int[numParts];
            int[] secondInts = new int[numParts];
            for (int i = 0; i < firstVnums.length; i++) { firstInts[i] = Integer.parseInt(firstVnums[i]); }
            for (int i = 0; i < secondVnums.length; i++) { secondInts[i] = Integer.parseInt(secondVnums[i]); }

            // Sort by major > minor > revision > length ascending
            for (int partIdx = 0; partIdx < numParts; partIdx++) {
                int cmp = Integer.compare(firstInts[partIdx], secondInts[partIdx]);
                if (cmp != 0) return cmp;
            }
            return first.compareTo(second);
        }
    }

    public static String[] answer(String[] versionList) {
        Arrays.sort(versionList, new VersionComparator());
        return versionList;
    }

    public static void main(String[] args) {
        String[][] lists = {
                {"1.1.2", "1.0", "1.3.3", "1.0.12", "1.0.2"},
                {"1.11", "2.0.0", "1.2", "2", "0.1", "1.2.1", "1.1.1", "2.0"}
        };

        for (String[] list : lists) {
            System.out.println(Arrays.asList(list) + " -> " + Arrays.asList(answer(list)));
        }
    }
}
