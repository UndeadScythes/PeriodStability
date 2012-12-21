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
        for(int n = 18; n <= 31; n++) {
            final long order = (1 << n) - 1;
            final BufferedWriter out = new BufferedWriter(new FileWriter("Stab-" + n + ".csv"));
            final Polynomial primitive = Polynomial.getPrimitive(n, 0);
            final List<Long> factors = Factor.getPrimeFactors(order);
            System.out.println("n = " + n + "\to = " + order + "\tf = " + primitive.getLong() + "\tqs = " + factors.toString());
            for(long q : factors) {
                if(q > 17 && q < order) {
                    if((order / q) > Integer.MAX_VALUE) {           //
                        System.out.println("Integer overflow.");    // Not even sure if this is needed.
                    }                                               //
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
                    System.out.println("\tq = " + q + "\ts = " + sum);
                    out.write(q + "," + Long.toString(sum));
                    out.newLine();
                }
            }
            out.close();
        }
    }
}
