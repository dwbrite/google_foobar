package the_cake_is_a_lie;

import java.util.ArrayList;

public class Answer {
    public static int answer(String s) {
        // Find factors of the length
        ArrayList<Integer> factors = getFactors(s.length());
        // `abcabc` has factors 1 2 3 and 6
        // These will be the number of M&Ms in the pattern.

        for (Integer factor : factors) {
            // Get the M&M pattern with length of the smallest factor
            String test = s.substring(0, factor);

            // Test if the pattern follows through to the end
            if (s.matches("^(" + test + ")*$")) {
                return s.length() / factor;
            }
        }

        return 0;

        // Notes:
        // Technically this can be brute forced without calculating factors,
        // but that's inefficient, right?

        // I forgot how hard arrays/primitives/lists are to work with in Java
    }

    private static ArrayList<Integer> getFactors(int length) {
        ArrayList<Integer> factors = new ArrayList<Integer>();
        for (int i=1; i <= length; i++) {
            if (length%i==0) {
                factors.add(i);
            }
        }

        return factors;
    }

    public static void main(String[] args) {
        String[] strings = {
                "abcabc",
                "abc",
                "a",
                "abccbaabccba",
                "abccba",
                "abcabcabc"
        };

        for (String s : strings) {
            System.out.println(s + ": " + answer(s));
        }
    }
}