package please_pass_the_coded_messages;

import java.util.*;

public class Answer {
    public static int answer(int[] l) {
        Arrays.sort(l);

        // Sum digits and create mutable list
        ArrayList<Integer> list = new ArrayList<>();
        int sum = 0;
        for (int val : l) {
            list.add(val);
            sum += val;
        }

        // If the number is evenly divisible by 3, return
        int remainder = sum % 3;
        if (remainder != 0) {
            removeProblemChildren(list, remainder);
        }
        return reversedListToInt(list);
    }

    private static void removeProblemChildren(ArrayList<Integer> list, int remainder) {
        // Removes the smallest element that makes the solution divisible by 3
        for (Integer item : list) {
            if (item % 3 == remainder) {
                list.remove(item);
                return;
            }
        }

        /* > Given 1-9 decimal digits, we can remove up to two digits
         * to determine whether or not the resulting integer is the highest possible value divisible by 3.
         *
         * Given any decimal digit x, there are _effectively_ only 3 relevant values.
         *          x%3 == 0  |  (0, 3, 6, 9)
         *          x%3 == 1  |  (1, 4, 7)
         *          x%3 == 2  |  (2, 5, 8)
         * 0, 1, and 2.
         *
         * Consider the following in the context of a given list:
         *
         * 0.) Two effective zeroes will sum to 0.
         *          0 + 0 == 0; 0%3 == 0
         * 1.) An effective 1 and 2 will sum to 0.
         *          1 + 2 == 3; 3%3 == 0
         * 2.) Two of the same effective values will sum to the other non-zero value.
         *          1 + 1 == 2; 2%3 == 2
         *          2 + 2 == 4; 4%3 == 1
         *
         * Table 1.1
         * id | original list   | effective list    | simplified    | best result   | n removed
         * -------------------------------------------------------------------------------------
         * l0 | {1, 4}          | {1, 1}            | 2             | 0             | 2
         * l1 | {5, 8}          | {2, 2}            | 1             | 0             | 2
         * l2 | {3, 5, 8}       | {0, 2, 2}         | 1             | 3             | 2
         * l3 | {0, 4, 6}       | {0, 1, 0}         | 1             | 60            | 1
         * l4 | {3, 4, 5}       | {0, 1, 2}         | 0             | 543           | 0
         * l5 | {6, 4, 5, 7}    | {0, 1, 2, 1}      | 1             | 765           | 1
         * l6 | {9, 4, 1, 1}    | {0, 1, 1, 1}      | 0             | 9411          | 0
         * l7 | {3, 4, 1, 5, 1} | {0, 1, 1, 2, 1}   | 2             | 4311          | 1
         * l8 | {6, 5, 2, 8, 1} | {0, 2, 2, 2, 1}   | 1             | 8652          | 1
         * -------------------------------------------------------------------------------------
         *
         * With these methods, we can infer that where the simplified result is not 0,
         * we can remove one or two items from the effective list (and their corresponding digit in the original list)
         * to achieve an outcome where the simplified result is 0.
         *
         * qed? I'll be honest, I've completely forgotten how to write a proof, but this is close enough, right?
         */

        // Removes two elements, assuming that one was not found earlier and that element removal is necessary.
        // This part's a little bumpy, hold on tight!
        for (Integer item : list) {
            int partial_diff = item%3;
            for (Integer next: list) {
                if (list.indexOf(item) != list.indexOf(next)) {
                    if ((partial_diff + next%3)%3 == remainder) {
                        list.remove(item);
                        list.remove(next);
                        return;
                    }
                }
            }
        }
    }

    private static int reversedListToInt(ArrayList<Integer> list) {
        Collections.reverse(list);
        String s = "";
        for (int num : list) {
            s += num;
        }

        if (list.size() == 0) { return 0; }
        else { return Integer.valueOf(s); }
    }
}