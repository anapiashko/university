import java.math.BigInteger;

public class DSA {

    public static BigInteger Y;
    public static BigInteger G;
    public static BigInteger P;
    public static BigInteger q;

    public static BigInteger[] subscribe(String message) {

        P = new BigInteger("6703903964971298549787012499102923063739682910296196688861780721860882015036773488400937149083451713845015929093243056271952419563142882186777485787833071");
        q = new BigInteger("2643573169557117016987442101865800057");

//        BigInteger[] generated = Generator.generate();
//
//        P = generated[1];
//        q = generated[0];
//
//        System.out.println("p = " + P);
//        System.out.println("q = " + q);

        BigInteger h = BigInteger.valueOf(2);
        G = h.modPow(P.subtract(BigInteger.ONE).divide(q), P);

        BigInteger x = BigInteger.valueOf(7);

        Y = (G.pow(x.intValue())).mod(P);

        RIPEMD128 ripemd128 = new RIPEMD128();
        BigInteger m = ripemd128.hash(message);
        BigInteger k = BigInteger.valueOf(3);
        BigInteger r = ((G.pow(k.intValue())).mod(P)).mod(q);
        BigInteger s = (k.modInverse(q).multiply((x.multiply(r)).add(m))).mod(q);

        System.out.println("r = " + r);
        System.out.println("s = " + s);

        return new BigInteger[]{r, s};
    }
}
