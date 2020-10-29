import java.math.BigInteger;

public class Algorithm_RIPEMD128 {

    private String message;
    private final long message_length;
    private byte[] byteMessage;

    private final BigInteger[] K1 = { new BigInteger("00000000",16),
            new BigInteger("5A827999", 16),
            new BigInteger("6ED9EBA1", 16),
            new BigInteger("8F1BBCDC", 16),
            new BigInteger("A953FD4E", 16) };

    private final BigInteger[] K2 = { new BigInteger("50A28BE6", 16),
            new BigInteger("5C4DD124", 16),
            new BigInteger("6D703EF3", 16),
            new BigInteger("7A6D76E9", 16),
            new BigInteger("00000000", 16) };

    private BigInteger h0 = new BigInteger("67452301",16),
            h1 = new BigInteger("EFCDAB89", 16),
            h2 = new BigInteger("98BADCFE", 16),
            h3 = new BigInteger("10325476", 16);

    public Algorithm_RIPEMD128 (String message) {
        this.message = message;
        this.message_length = message.length();
    }

    public void start() {
        step_1_init();
        step_2();
        step_4();
    }

    // Добавление дополнительных битов.
    public void step_1_init() {

        int numberBlocks = (message.length() /64) + 1;

        byteMessage = new byte[numberBlocks * 64];

        byte[] temp = message.getBytes();
        for (int i = 0; i < message_length; i++) {
            byteMessage[i] = temp[i];
        }

        byteMessage[temp.length] = -128; // 128 = 10000000, так мы добавили 1, как один бит

        // отнимаем 8 т.к. далее туда допишем длину сообщения
        for (int i = temp.length + 1; i < byteMessage.length - 8; i++) {
            byteMessage[i] = 0;
        }

        for (int i = 0; i < byteMessage.length; i++) {
          //  System.out.println(i + ". " + byteMessage[i]);
        }
        //System.out.println("BYTE MESSAGE LENGTH = " + byteMessage.length);
    }

    // Добавление длины сообщения в конец сообщения
    void step_2() {

        String binaryStringMessageLength = Long.toBinaryString(message_length);

        // Добавляем нулями до 64 бит
        StringBuffer messageBuffer = new StringBuffer(binaryStringMessageLength);
        while (messageBuffer.length() != 64) {
            messageBuffer.insert(0, "0");
        }
        binaryStringMessageLength = messageBuffer.toString();

        // берем младшие 4 байта и записываем на место 56, ... , 59 байтов сообщения
        for (int i = 32, j = 56; i < 64; i += 8, j++) {
            int t = Integer.parseInt(binaryStringMessageLength.substring(i, i + 8), 2);
            byteMessage[j] = (byte) t;
        }

        // берем старшие 4 байта и записываем на место 60, ... , 63 байта сообщения
        for (int i = 0, j = 60; i < 32; i += 8, j++) {
            int t = Integer.parseInt(binaryStringMessageLength.substring(i, i + 8), 2);
            byteMessage[j] = (byte) t;
        }

        System.out.println("Message length = " + message.length());
        for (int i = 0; i < byteMessage.length; i++) {
       //     System.out.println(i + ". " + byteMessage[i]);
        }
    }

    // Определение констант и используемых функций.
    void step_3() {

    }

    // Основной цикл.
    void step_4() {
        for (int i = 0; i < byteMessage.length / 64; i++) {
            BigInteger A1 = h0, B1 = h1, C1 = h2, D1 = h3;
            BigInteger A2 = h0, B2 = h1, C2 = h2, D2 = h3;

            BigInteger T;
            for (int j = 0; j < 64; j++) {

                T = ((A1
                        .add(function_f(j,B1,C1,D1))
                                .add(utfToBin(subArray(i,R1(j))))
                                .add(getValueFromK(j, K1)))
                        .mod(BigInteger.valueOf(2).pow(32)));
                T = cyclicLeftShift(T, T.toString(2).length(), S1(j));

                A1 = D1;
                D1 = C1;
                C1 = B1;
                B1 = T;

                T = ((A2
                        .add(function_f(63 - j, B2, C2, D2))
                        .add(utfToBin(subArray(i,R2(j))))
                        .add(getValueFromK(j, K2)))
                        .mod(BigInteger.valueOf(2).pow(32)));

                T = cyclicLeftShift(T, T.toString(2).length(), S2(j));

                A2 = D2;
                D2 = C2;
                C2 = B2;
                B2 = T;

            }

            T = (h1.add(C1).add(D2)).mod(BigInteger.valueOf(2).pow(32));
            h1 = (h2.add(D1).add(A2)).mod(BigInteger.valueOf(2).pow(32));
            h2 = (h3.add(A1).add(B2)).mod(BigInteger.valueOf(2).pow(32));
            h3 = (h0.add(B1).add(C2)).mod(BigInteger.valueOf(2).pow(32));
            h0 = T;
        }

        System.out.println("------------16-----------");
        System.out.println("h0 = " + h0.toString(16));
        System.out.println("h1 = " + h1.toString(16));
        System.out.println("h2 = " + h2.toString(16));
        System.out.println("h3 = " + h3.toString(16));
    }

    // i - номер блока, j - номер слова
    byte[] subArray(int i, int j){
        byte[] arr = new byte[4];
        arr[3] = byteMessage[i*64 + j*4];
        arr[2] = byteMessage[i*64 + j*4 + 1];
        arr[1] = byteMessage[i*64 + j*4 + 2];
        arr[0] = byteMessage[i*64 + j*4 + 3];
        return arr;
    }

    BigInteger utfToBin(byte[] utf) {

        // Convert to binary
        byte[] bytes = utf;

        String bin = "";
        for (int i = 0; i < bytes.length; i++) {
            int value = bytes[i];
            for (int j = 0; j < 8; j++) {
                bin += ((value & 128) == 0 ? 0 : 1);
                value <<= 1;
            }
        }
        return getBin(bin);
    }

    BigInteger getBin(String bin){
        BigInteger bInt = new BigInteger(bin, 2);
        return bInt;
    }

    private BigInteger allOnes(int L) {
        return BigInteger.ZERO
                .setBit(L)
                .subtract(BigInteger.ONE);
    }

    private BigInteger cyclicLeftShift(BigInteger n, int L, int k) {
        return n.shiftLeft(k)
                .or(n.shiftRight(L - k))
                .and(allOnes(L));
    }

    private BigInteger function_f(int j, BigInteger x, BigInteger y, BigInteger z) {
        if (j >= 0 && j <= 15) {
            return x.xor(y).xor(z);
        } else if (j >= 16 && j <= 31) {
            return (x.and(y)).or(x.not().and(z));
        } else if (j >= 32 && j <= 47) {
            return (x.or(y.not())).xor(z);
        } else if (j >= 48 && j <= 63) {
            return (x.and(z)).or(y.and(z.not()));
        } else if (j >= 64 && j <= 79) {
            return x.xor(y.or(z.not()));
        }else {
            return BigInteger.valueOf(-1);
        }
    }

    private BigInteger getValueFromK(int j, BigInteger[] array) {
        if (j >= 0 && j <= 15) {
            return array[0];
        } else if (j >= 16 && j <= 31) {
            return array[1];
        } else if (j >= 32 && j <= 47) {
            return array[2];
        } else if (j >= 48 && j <= 63) {
            return array[3];
        } else if (j >= 64 && j <= 79) {
            return array[4];
        } else {
            return BigInteger.valueOf(-1);
        }
    }

    private int R1(int j) {
        int[][] R = {
                {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
                {7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8},
                {3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12},
                {1, 9, 11, 10, 0, 8, 12, 4, 13, 3, 7, 15, 14, 5, 6, 2},
                {4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13}};
        return R[j / 16][j % 16];
    }

    private int R2(int j) {
        int[][] R = {{5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12},
                {6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2},
                {15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13},
                {8, 6, 4, 1, 3, 11, 15, 0, 5, 12, 2, 13, 9, 7, 10, 14},
                {12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11}};
        return R[j / 16][j % 16];
    }

    private int S1(int j) {
        int[][] S = {{11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8},
                {7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12},
                {11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5},
                {11, 12, 14, 15, 14, 15, 9, 8, 9, 14, 5, 6, 8, 6, 5, 12},
                {9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6}};
        return S[j / 16][j % 16];

    }

    private int S2(int j) {
        int[][] S = {{8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6},
                {9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11},
                {9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5},
                {15, 5, 8, 11, 14, 14, 6, 14, 6, 9, 12, 9, 12, 5, 15, 8},
                {8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11}};
        return S[j / 16][j % 16];
    }
}
