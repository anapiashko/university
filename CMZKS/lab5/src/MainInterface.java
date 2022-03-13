import java.math.BigInteger;

public interface MainInterface {

    void start(String e1Field, String e2Field);

    BigInteger getN();

    int getE1();

    int getE2();

    BigInteger getC1();

    BigInteger getC2();

    BigInteger getR();

    BigInteger getS();

    BigInteger getC1r();

    BigInteger getC2s();

    BigInteger getM();

    BigInteger getMModN();
}
