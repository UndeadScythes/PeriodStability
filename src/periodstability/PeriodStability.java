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
        final BufferedWriter out = new BufferedWriter(new FileWriter("PeriodStability.csv"));
        for(int n = 3; n <= 29; n++) {
            final long order = (1 << n) - 1;
            final Polynomial primitive = Polynomial.getPrimitive(n, 0);
            final List<Long> factors = Factor.getPrimeFactors(order);
            System.out.println("n = " + n + "\to = " + order + "\tf = " + primitive.getLong() + "\tqs = " + factors.toString());
            for(long q : factors) {
                if(q > 17 && q < order) {
                    int i = 0;
                    long sum = 0;
                    final int r = (int)(order / q);
                    final long mid = q / 2;
                    long[] weights = new long[r];
                    final GaloisLFSR lfsr = new GaloisLFSR(n, primitive, 1);
                    do {
                        if(lfsr.getBit(0) == 1) {
                            weights[i]++;
                        }
                        i = (i + 1) % r;
                        lfsr.clock();
                    } while(lfsr.getState() != 1);
                    for(i = 0; i < r; i++) {
                        if(weights[i] > mid) {
                            weights[i] = q - weights[i];
                        }
                        sum += weights[i];
                    }
                    System.out.println("\tq = " + q + "\tr = " + r + "\ts = " + sum);
                    out.write(n + "," + q + "," + Long.toString(sum));
                    out.newLine();
                }
            }
        }
        out.close();
    }
}
