import java.math.BigInteger;

public class DSA {

    public static boolean checkSignature(String message, BigInteger P, BigInteger q, BigInteger G, BigInteger Y, BigInteger[] rs) {

        BigInteger r = rs[0];
        BigInteger s = rs[1];

        RIPEMD128 ripemd128 = new RIPEMD128();
        BigInteger m = ripemd128.hash(message);

        BigInteger w = s.modInverse(q);

        BigInteger u1 = (m.multiply(w)).mod(q);
        BigInteger u2 = (r.multiply(w)).mod(q);

        BigInteger V = (G.modPow(u1,P))
                .multiply(Y.modPow(u2, P))
                .mod(P)
                .mod(q);

        System.out.println("r = " + r);
        System.out.println("v = " + V);

        return r.compareTo(V) == 0;
    }
}
