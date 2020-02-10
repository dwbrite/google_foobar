package fuel_injection_perfection;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class Answer {
    public static int answer(String m) {
        /*
        // easy
        // wait no it's not

        // there's a naive solution of:
        int numOps = 0;
        BigInteger bigN = new BigInteger(m);
        BigInteger FOUR = new BigInteger("4");
        BigInteger TWO = new BigInteger("2");
        BigInteger ONE = BigInteger.ONE;
        BigInteger ZERO = BigInteger.ZERO;

        // while n > 1
        while (bigN.compareTo(ONE) > 0) {
            if (bigN.mod(TWO).equals(ZERO)) {
                bigN = bigN.divide(TWO);
                // if number is a power of two,
            } else if (bigN.add(ONE).mod(FOUR).equals(ZERO)) {
                bigN = bigN.add(ONE);
            } else {
                bigN = bigN.subtract(ONE);
            }
            numOps++;
        }

        //return numOps;
        */

        int n = 15;
        int numOps = 0;

        while (n > 1) {
            if (n%2==0) {
                //bit twiddling
                if ((n & (n - 1)) == 0) {

                }

                n /= 2;
            } else if ((n+1)%4==0) {
                n += 1;
            } else {
                n -= 1;
            }
            numOps++;
        }

        return numOps;
    }

    public static void main(String[] args) {
        HashMap<String, Integer> answers = new HashMap<>();
        answers.put("15", 5);
        answers.put("4", 2);

        for (Map.Entry<String, Integer> entry : answers.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue() + " -> " + answer(entry.getKey()));
        }
    }
}
