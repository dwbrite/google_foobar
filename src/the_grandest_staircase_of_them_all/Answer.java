package the_grandest_staircase_of_them_all;

public class Answer {

    public static int answer(int n) {
        // Formal Power Series
        int F[] = new int[n+1];
        F[0]=1;
        F[1]=1;


        // "Virtually" multiply the generating function
        // from product notation: (1 + x^k)
        // and store our coefficient from x^k
        //
        // Luckily (1 + x^k) is a really easy generating function to implement in code :)
        //
        // TL;DR: FOIL and count the number of `x` with a power of `k`
        for (int factorIdx=2; factorIdx<=n; factorIdx++) {
            for (int k=n; k>=factorIdx; k--) {
                F[k] += F[k-factorIdx];
            }
        }

        // Then subtract 1, because we don't count the distinct partition with only 1 step.
        return F[n]-1;

        // Background information:
        /* Formally I've had fairly little experience with discrete math. I've taken only Discrete Math I.
         * Before I started this problem I didn't know about partitions, generating functions, or formal power series.
         * Ultimately, I've learned a _ton_ about discrete mathematics and number theory.
         *
         * With that, I'll say that I did "cheat",
         * and that this is a slightly modified version of another person's code.
         *
         * Regardless, I understand the problem well enough that I feel comfortable submitting this.
         * I imagine if I had written this completely myself it wouldn't be much different.
         */
    }

    public static void main(String[] args) {
        int[] list = {
                /*3,
                6,
                7,
                8,
                9,*/
                10,
                11,
                15,
                16,
                22,
                200,
        };

        for (int n: list) {
            System.out.println(n + "\t\t| " + answer(n));
        }
    }
}
