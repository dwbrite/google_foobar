package free_the_bunny_prisoners;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class Answer2 {

    public static int[][] answer(int numBunnies, int numConsoles) {
        // No consoles? No keys. Go directly to return. Do not pass Go.
        if (numConsoles == 0) {
            return new int[numBunnies][0];
        }

        // This was an assumption based on the example results.
        int numKeyCopies = numBunnies - numConsoles + 1;

        // Sprinkle in a little bit of Pascal's triangle...
        //int[][] result = new int[numBunnies][nCr(numBunnies-1, numConsoles-1)];

        ArrayList<ArrayList<Integer>> distribution = new ArrayList<>();
        for (int i = 0; i < numBunnies; i++) {
            distribution.add(new ArrayList<>());
        }

        // The first n bunnies (n = numKeyCopies) will always have the first key
        // So let's create the boolean[] for the first distinct key, fill it, and add it to the list of distinct keys.
        boolean[] first = new boolean[numBunnies];
        for(int bunny = 0; bunny < numKeyCopies; bunny++) {
            first[bunny] = true;
        }

        boolean[] hasLastKey = first;
        for(int distinctKey = 0; distinctKey < nCr(numBunnies, numConsoles); distinctKey++) {
            boolean[] hasThisKey = hasLastKey.clone();

            int found = 0;

            for(int bunny = numBunnies-1; bunny >= 0; bunny--) {
                if(hasThisKey[bunny]) {
                    if(bunny < numBunnies-1 && !hasThisKey[bunny+1]) {
                        hasThisKey[bunny] = false;
                        hasThisKey[bunny + 1] = true;

                        if (found > 0) {
                            for (int bb = bunny + 1; bb < numBunnies; bb++) {
                                hasThisKey[bunny] = bb <= bunny + found;
                            }
                        }

                        for(int i = 0; i < numBunnies; i++) {
                            if(hasThisKey[i]) {
                                distribution.get(i).add(distinctKey);
                            }
                        }
                        hasLastKey = hasThisKey;
                        break;
                    }
                    found++;
                }
            }
        }

        return resultToArray(distribution);
    }

    private static int[][] resultToArray(ArrayList<ArrayList<Integer>> distribution) {
        int numBunnies = distribution.size();
        int numKeysPer = distribution.get(0).size();

        int[][] result = new int[numBunnies][numKeysPer];
        for(int bunny = 0; bunny < numBunnies; bunny++) {
            for(int key = 0; key < numKeysPer; key++) {
                result[bunny][key] = distribution.get(bunny).get(key);
            }
        }

        return result;
    }

    private static int fac(int n) {
        int result = n;
        for(int i = 1; i < n; i++) {
            result *= n-i;
        }
        return result;
    }

    private static int nCr(int n, int r) {
        int numerator = fac(n);
        int denominator = fac(r) * fac(n-r);
        return denominator>0?numerator/denominator:1;
    }

    public static void main(String[] args) {
        int[][] inputs = new int[][] {
                {2, 0},
                {2, 1},
                {2, 2},
                {3, 2},
                {4, 2},
                {4, 4},
                {5, 2},
                {5, 3},
                {6, 3},
        };

        int[][][] outputs = new int[][][] {
                {
                        {},
                        {}
                },
                {
                        { 0 },
                        { 0 }
                },
                {
                        { 0    },
                        {    1 }
                },
                {
                        { 0, 1    },
                        { 0,    2 },
                        {    1, 2 }
                },
                {
                        { 0, 1, 2    },
                        { 0, 1,    3 },
                        { 0,    2, 3 },
                        {    1, 2, 3 }
                },
                {
                        { 0          },
                        {    1       },
                        {       2    },
                        {          3 }
                },
                {
                        { 0, 1, 2, 3    },
                        { 0, 1, 2,    4 },
                        { 0, 1,    3, 4 },
                        { 0,    2, 3, 4 },
                        {    1, 2, 3, 4 }
                },
                {
                        { 0, 1, 2, 3, 4, 5             },
                        { 0, 1, 2,          6, 7, 8    },
                        { 0,       3, 4,    6, 7,    9 },
                        {    1,    3,    5, 6,    8, 9 },
                        {       2,    4, 5,    7, 8, 9 }
                },
                {
                        {  0,  1,  2,  3,  4,  5,  6,  7,  8,  9                     },
                        {  0,  1,  2,  3,  4,  5,                 10, 11, 12, 13     },
                        {  0,  1,  2,              6,  7,  8,     10, 11, 12,     14 },
                        {  0,          3,  4,      6,  7,      9, 10, 11,     13, 14 },
                        {      1,      3,      5,  6,      8,  9, 10,     12, 13, 14 },
                        {          2,      4,  5,      7,  8,  9,     11, 12, 13, 14 }
                },
                {
                        {  0,  1,  2,  3,  4,  5,  6,  7,  8,  9                                         },
                        {  0,  1,  2,  3,                         10, 11, 12, 13, 14, 15                 },
                        {  0,              4,  5,  6,             10, 11, 12,             16, 17, 18     },
                        {      1,          4,          7,  8,     10,         13, 14,     16, 17,     19 },
                        {          2,          5,      7,      9,     11,     13, 15,     16,     18, 19 },
                        {              3,          6,      8,  9,         12, 14, 15,         17, 18, 19 },
                },
        };

        for(int i = 0; i < inputs.length; i++) {
            System.out.format("(%d, %d)? %b\n", inputs[i][0], inputs[i][1], Arrays.deepEquals(outputs[i], (answer(inputs[i][0], inputs[i][1]))));
        }

        System.out.println("--------------------");

        for(int x = 1; x < 10; x++) {
            for (int y = 1; y <= x; y++) {
                System.out.format("(%d,%d) = %d\n", x, y, answer(x, y)[0].length);
            }
            System.out.println();
        }

        System.out.println("--------------------");

        for(int x = 1; x < 10; x++) {
            for(int y = 1; y <= x; y++) {
                int[][] a = answer(x,y);
                System.out.format("(%d,%d) = %d\n", x, y, a[a.length-1][a[0].length-1]);
            }
            System.out.println();
        }/*

        for(int x = 1; x < 10; x++) {
            for(int y = 1; y <= x; y++) {
                printResult(answer(x, y));
            }
            System.out.println();
        }*/
    }

    public static void printResult(int[][] result) {
        int max = result[result.length-1][result[0].length-1];
        System.out.print("{\n");
        for(int[] x : result) {
            System.out.print("\t\t{ ");
            for(int y = 0; y < x.length; y++) {
                System.out.format("%" + ((x[y] - ((x[y]>0&&y!=0)? x[y-1]+1 : 0)) * 5 + 3) + "d", x[y]);
                if (y < x.length-1) {
                    System.out.print(",");
                }
                System.out.format(" ");
            }
            System.out.format("%" + ((max-x[x.length-1])*5+2) + "s}\n", " ");
        }
        System.out.print("},\n");
    }
}