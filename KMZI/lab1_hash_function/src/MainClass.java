import java.math.BigInteger;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args) {
        String m = "Hello world";
        String test = "abc";

        System.out.print("Input string : ");
        Scanner scanner = new Scanner(System.in);
        String message = scanner.nextLine();

        Algorithm_RIPEMD128 algorithm_ripemd128 = new Algorithm_RIPEMD128(message);
        algorithm_ripemd128.start();
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
