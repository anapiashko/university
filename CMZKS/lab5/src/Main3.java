import java.math.BigInteger;

public class Main3 implements MainInterface {

    public static final BigInteger N = new BigInteger("385751370271");
    public static int e1 = 365797;
    public static int e2 = 1109663;
    public static final BigInteger C1 = new BigInteger("58541562205" +
            "167003685579" +
            "381877628242" +
            "256218527098" +
            "164244249864" +
            "6588741823" +
            "180308234660" +
            "174572441677" +
            "259951955034" +
            "378589342820" +
            "319378579620" +
            "21405495597" +
            "226860843155");
    public static final BigInteger C2 = new BigInteger("78032032470" +
            "13064174635" +
            "326727914830" +
            "364066420370" +
            "177576861402" +
            "65863828523" +
            "111437045566" +
            "124743274954" +
            "119577259869" +
            "85769669875" +
            "4688914942" +
            "261002397567" +
            "341722428571");

    private static BigInteger r;
    private static BigInteger s;
    private static BigInteger C1r;
    private static BigInteger C2minuss;
    private static BigInteger m;
    private static BigInteger mModN;

    public static void main(String[] args) {

        Main3 main = new Main3();

        TriplBig triplBig1 = main.gcdWide(BigInteger.valueOf(e1), BigInteger.valueOf(e2));
        r = triplBig1.x;
        System.out.println("r = " + r); // r
        s = triplBig1.y;
        System.out.println("s = " + s); // s
        System.out.println(triplBig1.d); // НОД

        C1r = C1.modPow(triplBig1.x, N);

        C2minuss = C2.modPow(triplBig1.y, N);

        System.out.println("C1r = " + C1r);
        System.out.println("C2minuss = " + C2minuss);

        m = C1r.multiply(C2minuss);

        System.out.println("m = " + m);

//        BigInteger mI = m.modInverse(N);
//        System.out.println("mI = " + mI);

        mModN = m.mod(N);

        System.out.println("mN = " + mModN);

        BigInteger euler = eulerf(N);
        System.out.println("euler(" + N + ") = " + euler);

        BigInteger d = new BigInteger("7").modInverse(euler);
        System.out.println("d = " + d);

        BigInteger cipher1 = new BigInteger("13");

        BigInteger decipher1= cipher1.modPow(d, N);
        System.out.println("decipher1 = " + decipher1);

//        BigInteger cipher2 = new BigInteger("118");
//
//        BigInteger decipher2= cipher2.modPow(d, N);
//        System.out.println("decipher2 = " + decipher2);
//
//        BigInteger cipher3 = new BigInteger("787");
//
//        BigInteger decipher3= cipher3.modPow(d, N);
//        System.out.println("decipher3 = " + decipher3);
//
//        BigInteger cipher4 = new BigInteger("3");
//
//        BigInteger decipher4= cipher4.modPow(d, N);
//        System.out.println("decipher4 = " + decipher4);
    }

    public BigInteger getR() {
        return r;
    }

    public BigInteger getS() {
        return s;
    }

    public BigInteger getC1r() {
        return C1r;
    }

    public BigInteger getC2s() {
        return C2minuss;
    }

    public BigInteger getM() {
        return m;
    }

    public BigInteger getMModN() {
        return mModN;
    }

    public static BigInteger eulerf(BigInteger n) {
        BigInteger result = n;
        for (BigInteger i = new BigInteger("2"); i.multiply(i).compareTo(n) <= 0; i = i.add(BigInteger.ONE)) {
            if (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                while (n.mod(i).compareTo(BigInteger.ZERO) == 0) {
                    n = n.divide(i);
                }
                result = result.subtract(result.divide(i));
            }
        }
        if (n.compareTo(BigInteger.ONE) > 0) {
            result = result.subtract(result.divide(n));
        }
        return result;
    }

    TriplBig gcdWide(BigInteger a, BigInteger b) {
        TriplBig temphere = new TriplBig(a, BigInteger.ONE, BigInteger.ZERO);
        TriplBig temphere2 = new TriplBig();

        if (b == BigInteger.ZERO) {
            return temphere;
        }

        temphere2 = gcdWide(b, a.mod(b));
        temphere = new TriplBig();

        temphere.d = temphere2.d;
        temphere.x = temphere2.y;
        temphere.y = temphere2.x.subtract(a.divide(b).multiply(temphere2.y));

        return temphere;
    }

    public void start(String e1Field, String e2Field) {

        if (e1Field.isEmpty()) {
            e1Field = String.valueOf(e1);
        } else {
            e1 = Integer.parseInt(e1Field);
        }
        if (e2Field.isEmpty()) {
            e2Field = String.valueOf(e2);
        } else {
            e2 = Integer.parseInt(e2Field);
        }

        TriplBig triplBig1 = gcdWide(new BigInteger(e1Field), new BigInteger(e2Field));
        r = triplBig1.x;
        System.out.println("r = " + r); // r
        s = triplBig1.y;
        System.out.println("s = " + s); // s
        System.out.println(triplBig1.d); // НОД

        C1r = C1.modPow(triplBig1.x, N);

        C2minuss = C2.modPow(triplBig1.y, N);

        System.out.println("C1r = " + C1r);
        System.out.println("C2minuss = " + C2minuss);

        m = C1r.multiply(C2minuss);

        System.out.println("m = " + m);

        mModN = m.mod(N);

        System.out.println("mN = " + mModN);
    }

    @Override
    public BigInteger getN() {
        return N;
    }

    @Override
    public int getE1() {
        return e1;
    }

    @Override
    public int getE2() {
        return e2;
    }

    @Override
    public BigInteger getC1() {
        return C1;
    }

    @Override
    public BigInteger getC2() {
        return C2;
    }

}
