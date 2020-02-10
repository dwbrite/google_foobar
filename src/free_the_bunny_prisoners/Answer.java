package free_the_bunny_prisoners;

import java.util.ArrayList;
import java.util.Arrays;

public class Answer {

    public static int[][] answer(int numBunnies, int numConsoles) {
        // No consoles? No keys. Go directly to return. Do not pass Go.
        if (numConsoles == 0) {
            return new int[numBunnies][0];
        }

        // The rest of this could technically be done better, but I have other things to work on.
        // For example, they result ArrayList could be put in the first loop,
        //   the while could be a for loop going up to nCr keys,
        //   and probably a few other things.
        // Regardless, I'm done with this. I'm submitting it. It needs to be out of my head.
        // Maybe I'll improve it post-submission.

        // This was an assumption based on the example results.
        int numKeyCopies = numBunnies - numConsoles + 1;

        // This List represents distinct keys
        // It is mutable because we don't know how many distinct keys we'll need from the start. (afaik)
        // the boolean[] represents if each bunny has a given distinct key or not.
        ArrayList<boolean[]> distinctKeyDistributions = new ArrayList<>();

        // The first n bunnies (n = numKeyCopies) will always have the first key
        // So let's create the boolean[] for the first distinct key, fill it, and add it to the list of distinct keys.
        boolean[] first = new boolean[numBunnies];
        for(int bunny = 0; bunny < numKeyCopies; bunny++) {
            first[bunny] = true;
        }
        distinctKeyDistributions.add(first);

        // Here's a visualization of it for the inputs (5,3)
        // there are 5 columns that each represent a bunny
        // each row represents a distinct key, and will have 3 (that's 5-3+1) copies of each key
        //
        // for each new row, we move the rightmost key (true) over to the next bunny until we hit the last bunny
        // at that point, we move the next key (true) with an empty space to the right of it
        // and "reset" all values to the right of that.
        //
        //      b0  b1  b2  b3  b4
        // k0:   1   1   1   0   0
        // k1:   1   1   0   1   0
        // k2:   1   1   0   0   1
        // k3:   1   0   1   1   0
        // k4:   1   0   1   0   1
        // k5:   1   0   0   1   1
        // k6:   0   1   1   1   0
        // k7:   0   1   1   0   1
        // k8:   0   1   0   1   1
        // k9:   0   0   1   1   1
        //
        // if we replace 1s with the row number and 0s with blank space,
        // then flip the distinctKeyDistributions along the x axis and turn it 90 deg clockwise,
        // then we get our final result:
        //
        // {0, 1, 2, 3, 4, 5            },
        // {0, 1, 2,          6, 7, 8   },
        // {0,       3, 4,    6, 7,    9},
        // {   1,    3,    5, 6,    8, 9},
        // {      2,    4, 5,    7, 8, 9}

        boolean done = false;

        while(!done) {
            // initialize the next key distribution as a clone of the last key distribution.
            boolean[] bunnyHasKey = distinctKeyDistributions.get(distinctKeyDistributions.size()-1).clone();

            // number of keys that can't be passed to the next bunny
            int found = 0;

            // grab the rightmost bunny that has a copy of the current distinct key
            for(int currentBunny = numBunnies - 1; currentBunny >= 0; currentBunny--) {
                if (bunnyHasKey[currentBunny]) {
                    if(currentBunny < numBunnies-1 && !bunnyHasKey[currentBunny+1]) {
                        // If the _next_ bunny doesn't have a key
                        // give this bunny's key to the next bunny.
                        bunnyHasKey[currentBunny] = false;
                        bunnyHasKey[currentBunny+1] = true;

                        if(found > 0) {
                            // If there are bunnies with keys to the right of this bunny
                            // "reset" the keys of all bunnies this bunny..
                            for (int bunny = currentBunny+1; bunny < numBunnies; bunny++) {
                                bunnyHasKey[bunny] = bunny <= currentBunny + 1 + found;
                            }
                        }

                        // Add this distribution to the list and start the next distinct key distribution
                        distinctKeyDistributions.add(bunnyHasKey);
                        break;
                    }

                    found++;
                    // If last few bunnies have _all_ of the copies of this distinct key, we're done!
                    if (found == numKeyCopies) {
                        done = true;
                        break;
                    }
                }
            }
        }

        // Replace 1s with the row number and 0s with blank space, throw all of that into a 2d ArrayList
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        for(int bunny = 0; bunny < numBunnies; bunny++) {
            result.add(new ArrayList<>());
            for(int distinctKey = 0; distinctKey < distinctKeyDistributions.size(); distinctKey++) {
                if(distinctKeyDistributions.get(distinctKey)[bunny]) {
                    result.get(bunny).add(distinctKey);
                }
            }
        }

        return resultToArray(result);
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
        }
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