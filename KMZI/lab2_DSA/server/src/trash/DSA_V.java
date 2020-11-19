package trash;

import java.math.BigInteger;

public class DSA_V {
    public static void main(String[] args) {
        BigInteger G = BigInteger.valueOf(4);
        BigInteger P = BigInteger.valueOf(23);
        BigInteger q = BigInteger.valueOf(11);

        BigInteger x = BigInteger.valueOf(7);

        BigInteger Y = (G.pow(x.intValue())).mod(P);

        BigInteger m = BigInteger.valueOf(9);
        BigInteger k = BigInteger.valueOf(3);
        BigInteger r = ((G.pow(k.intValue())).mod(P)).mod(q);
        BigInteger s = (k.modInverse(q).multiply((x.multiply(r)).add(m))).mod(q);


        System.out.println("r = " + r);
        System.out.println("s = " + s);

        BigInteger w = s.modInverse(q);

        BigInteger u1 = (m.multiply(w)).mod(q);
        BigInteger u2 = (r.multiply(w)).mod(q);

        BigInteger V = (G.pow(u1.intValue())
                .multiply(Y.pow(u2.intValue()))
                .mod(P))
                .mod(q);

        System.out.println("v = " + V);
    }
}
