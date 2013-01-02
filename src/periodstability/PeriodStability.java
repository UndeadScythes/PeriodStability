package periodstability;

import algebra.*;
import java.io.*;
import java.util.*;
import prng.*;

/**
 *
 * @author UndeadScythes
 */
public class PeriodStability {
    /**
     * @param args The command line arguments
     */
    public static void main(final String[] args) throws IOException {
        for(int n = 2; n <= 30; n++) {
            final BufferedWriter out = new BufferedWriter(new FileWriter("PeriodStability-" + n + ".csv"));
            final Polynomial primitive = Polynomial.getPrimitive(n, 0);
            final int order = (1 << n) - 1;
            final List<Integer> factors = Factor.getPrimeFactors(order);
            System.out.println("n = " + n + "\to = " + order + "\tf = " + primitive.toInt() + "\tqs = " + factors.toString());
            for(int q : factors) {
                if(q > 17 && q < order) {
                    final int mid = q / 2;
                    final int r = order / q;
                    int[] weights = new int[r];
                    int i = 0;
                    int sum = 0;
                    final GaloisLFSR lfsr = new GaloisLFSR(n, primitive, 1);
                    do {
                        weights[i] += lfsr.getBit(0);
                        i = (i + 1) % r;
                        lfsr.clock();
                    } while(lfsr.getState() != 1);
                    for(i = 0; i < r; i++) {
                        sum += (weights[i] > mid) ? q - weights[i] : weights[i];
                    }
                    System.out.println("\tq = " + q + "\tr = " + r + "\ts = " + sum);
                    out.write(n + "," + q + "," + Long.toString(sum));
                    out.newLine();
                }
            }
            out.close();
        }
    }
}
