package periodstability;

import java.util.List;

/**
 *
 * @author UndeadScythes
 */
public class PeriodStability {

    /**
     * @param args The command line arguments
     */
    public static void main(String[] args) {
        for(int n = 19; n < 32; n++) {
            long order = (1 << n) - 1;
            Polynomial primitive = Polynomial.getPrimitive(n, 0);
            List<Long> factors = Algebra.getPrimeFactors(order);
            for(long q : factors) {
                if(q > 17) {
                    if((order / q) > Integer.MAX_VALUE) {           //
                        System.out.println("Integer overflow.");    // Not even sure if this is needed.
                    }                                               //
                    int r = (int)(order / q);
                    int i = 0;
                    long mid = q / 2;
                    long sum = 0;
                    long[] weights = new long[r];
                    GaloisLFSR lfsr = new GaloisLFSR(n, primitive, 1);
                    lfsr.clock();
                    while(lfsr.state() != 1) {
                        if(lfsr.bit(0) == 1) {
                            weights[i]++;
                        }
                        i = (i + 1) % r;
                        lfsr.clock();
                    }
                    for(i = 0; i < r; i++) {
                        if(weights[i] > mid) {
                            weights[i] = q - weights[i];
                        }
                        sum += weights[i];
                    }
                    System.out.println("n = " + n + "\tq = " + q + "\ts = " + sum);
                }
            }
        }
    }
}
