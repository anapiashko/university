import java.util.ArrayList;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        EllipticCurve ec = new EllipticCurve(-1, 1, 751);
        EPoint Pb = new EPoint(ec, 179, 275);

        System.out.println("Task 1:");
        EllipticCipher ellipticCipher = new EllipticCipher(Pb);
        ellipticCipher.setG(new EPoint(ec, 0, 1));
        ellipticCipher.setK(new int[]{11, 17, 18, 19, 16, 6, 12, 8, 2});
        ellipticCipher.setP(Arrays.asList(
                new EPoint(ec, 243, 664),
                new EPoint(ec, 236, 39),
                new EPoint(ec, 238, 175),
                new EPoint(ec, 238, 175),
                new EPoint(ec, 234, 587),
                new EPoint(ec, 247, 266),
                new EPoint(ec, 243, 87),
                new EPoint(ec, 236, 39),
                new EPoint(ec, 257, 458)));
        ArrayList<EPoint> cipher_encode = ellipticCipher.encode();
        for (int i = 0; i < cipher_encode.size(); i += 2)
            System.out.println("[" + cipher_encode.get(i) + ";" + cipher_encode.get(i+1) + "]");

        System.out.println("Task 2:");
        ellipticCipher.setG(new EPoint(ec, -1, 1));
        ellipticCipher.setNb(34);
        ArrayList<EPoint> text = ellipticCipher.decode(Arrays.asList(
                new EPoint(ec,  618,  206),  new EPoint(ec,  426,  662),
                new EPoint(ec,  72,  254),  new EPoint(ec,  67,  667),
                new EPoint(ec,  286,  136),  new EPoint(ec,  739,  574),
                new EPoint(ec,  16,  416),  new EPoint(ec,  143,  602),
                new EPoint(ec,  618,  206),  new EPoint(ec,  313,  203),
                new EPoint(ec,  618,  206),  new EPoint(ec,  114,  607),
                new EPoint(ec,  618,  206),  new EPoint(ec,  438,  711),
                new EPoint(ec,  188,  93),  new EPoint(ec,  573,  168)
        ));
        for (EPoint ePoint : text)
            System.out.println(ePoint);

        System.out.println("Task 3:");
        EPoint P = new EPoint(ec, 56, 332);
        EPoint Q = new EPoint(ec, 69, 241);
        EPoint R = new EPoint(ec, 83, 373);
        EPoint res = P.multiply(2).add(Q.multiply(3)).subtract(R);
        System.out.println("2P + 3Q - R = " + res);

        System.out.println("Task 4:");
        EPoint P2 = new EPoint(ec, 43, 527);
        res = P2.multiply(107);
        System.out.println("107 * P = " + res);

        System.out.println("Task 5:");
        EllipticSignature sign = new EllipticSignature(new EPoint(ec, 416, 55), 13, 3, 4, 7);
        int[] signature = sign.sign();
        System.out.println("Signature: {" + signature[0] + "," + signature[1] +"}");

        System.out.println("Task 6:");
        int[] sign2 = { 5, 7 };
        sign.setG(new EPoint(ec, 562, 89));
        if (sign.check(sign2, 6, new EPoint(ec, 562, 662))){
            System.out.println("Signature accepted");
        }
        else {
            System.out.println("Signature denied");
        }
    }
}
