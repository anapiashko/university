import java.math.BigInteger;

public class MainClass {

    public static void main(String[] args) {
        String message = "Hello world";
        String test = "abc";

        // ----First step-----

        BigInteger h0 = new BigInteger("67452301",16),
                h1 = new BigInteger("EFCDAB89", 16),
                h2 = new BigInteger("98BADCFE", 16),
                h3 = new BigInteger("10325476", 16);

        BigInteger b = new BigInteger("5619165283");

        System.out.println(rotateLeft(new BigInteger("5619165283")));
        System.out.println(cyclicLeftShift(new BigInteger("5619165283"),b.toString(2).length(),11));

        String str = b.toString(2);
        System.out.println(str.length());

       Algorithm_RIPEMD128 algorithm_ripemd128 = new Algorithm_RIPEMD128(test);
       algorithm_ripemd128.start();

//        RIPEMD_128 algh = new RIPEMD_128();
//        algh.Run(test);

    }

    private static BigInteger rotateLeft(BigInteger bi) {
        BigInteger ret = bi.shiftLeft(11);
        if (ret.testBit(32)) {
            ret = ret.clearBit(32).setBit(0);
        }
        return ret;
    }

    static BigInteger allOnes(int L) {
        return BigInteger.ZERO
                .setBit(L)
                .subtract(BigInteger.ONE);
    }

    static BigInteger cyclicLeftShift(BigInteger n, int L, int k) {
        return n.shiftLeft(k)
                .or(n.shiftRight(L - k))
                .and(allOnes(L));
    }
}
