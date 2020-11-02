import java.math.BigInteger;
import java.util.Random;

public class Generator {

    private static BigInteger p;
    private static BigInteger q;

    public static BigInteger getP() {
        return p;
    }

    public static BigInteger getQ() {
        return q;
    }

    private static RIPEMD128 ripemd128 = new RIPEMD128();

    private final static int b = 127;
    private final static int n = 3;

    static BigInteger[] generate(){
        boolean toOne = true;
        while(true) {
            q = BigInteger.valueOf(1);
            BigInteger seed = BigInteger.probablePrime(130, new Random());
            int seedlen = seed.toString(2).length();
            while (!q.isProbablePrime(1)) {
                seed = BigInteger.probablePrime(130, new Random());
                seedlen = seed.toString(2).length();
                BigInteger U = ripemd128.hash(seed.toString(16))
                        .xor(ripemd128.hash(seed.add(BigInteger.ONE)
                                .mod(BigInteger.valueOf(2).pow(seedlen)).toString(16)));

                q = U.or(BigInteger.ONE).setBit(U.toString(2).length()-1);
            }

            int lenQ = q.toString(2).length();

            BigInteger count = BigInteger.ZERO;
            BigInteger offset = BigInteger.valueOf(2);
            toOne = false;
            while (!toOne) {
                BigInteger[] V = new BigInteger[4];
                for (int k = 0; k <= n; k++) {
                    V[k] = ripemd128.hash(seed.add(offset).add(BigInteger.valueOf(k))
                            .mod(BigInteger.valueOf(2).pow(seedlen))
                            .toString(16));
                }
                BigInteger W = BigInteger.ZERO;
                for (int i = 0; i <= n; i++) {
                    W = W.add(V[i].multiply(BigInteger.valueOf(2).pow(i*lenQ))).mod(BigInteger.valueOf(2).pow(b));
                }
                W = W.mod(BigInteger.valueOf(2).pow(b));
                BigInteger X = W.add(BigInteger.valueOf(2).pow(511));
                BigInteger c = X.mod(q.multiply(BigInteger.valueOf(2)));
                p = X.subtract(c.subtract(BigInteger.ONE));
                if (p.compareTo(BigInteger.valueOf(2).pow(511)) >= 0) {
                    System.out.println("p = " + p);
                    if (p.isProbablePrime(1)) {
                        BigInteger[] b = new BigInteger[2];
                        b[0] = q;
                        b[1] = p;
                        return b;
                    }else{
                        count = count.add(BigInteger.ONE);
                        offset = offset.add(BigInteger.valueOf(n)).add(BigInteger.ONE);
                        if (count.compareTo(BigInteger.valueOf(2).pow(12)) == 0) {
                            toOne = true;
                        }else{
                            toOne = false;
                        }
                    }
                }else{
                    count = count.add(BigInteger.ONE);
                    offset = offset.add(BigInteger.valueOf(n)).add(BigInteger.ONE);
                    if (count.compareTo(BigInteger.valueOf(2).pow(12)) == 0) {
                        toOne = true;
                    }else{
                        toOne = false;
                    }
                }
            }
        }
    }
}
