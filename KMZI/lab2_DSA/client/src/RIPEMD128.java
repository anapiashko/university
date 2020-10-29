import java.math.BigInteger;

public class RIPEMD128 {

    public BigInteger hash(String message){

        BigInteger h0 = new BigInteger("67452301",16);
        BigInteger h1 = new BigInteger("EFCDAB89", 16);
        BigInteger h2 = new BigInteger("98BADCFE", 16);
        BigInteger h3 = new BigInteger("10325476", 16);

        byte[] bytes = parseString(message);
        BigInteger[][] M = getWords(bytes);

        for (int i = 0; i < M.length; i++) {
            BigInteger T;
            BigInteger A1 = h0; BigInteger B1 = h1; BigInteger C1 = h2; BigInteger D1 = h3;
            BigInteger A2 = h0; BigInteger B2 = h1; BigInteger C2 = h2; BigInteger D2 = h3;
            for (int j = 0; j < 64; j++) {
                T = BigInteger.valueOf(
                        Integer
                                .rotateLeft((A1.add(f(j, B1, C1, D1)).add(M[i][R1(j)]).add(K1(j)))
                                        .mod(BigInteger.valueOf((long)Math.pow(2,32)))
                                        .intValue(), S1(j)));
                A1 = D1; D1 = C1; C1 = B1; B1 = T;
                T = BigInteger.valueOf(
                        Integer
                                .rotateLeft ((A2.add(f(63-j, B2, C2, D2)).add(M[i][R2(j)]).add(K2(j)))
                                                .mod(BigInteger.valueOf((long)Math.pow(2,32)))
                                                .intValue(), S2(j)));
                A2 = D2; D2 = C2; C2 = B2; B2 = T;
            }
            T = h1.add(C1).add(D2)
                    .mod(BigInteger.valueOf((long)Math.pow(2,32)));
            h1 = h2.add(D1).add(A2)
                    .mod(BigInteger.valueOf((long)Math.pow(2,32)));
            h2 = h3.add(A1).add(B2)
                    .mod(BigInteger.valueOf((long)Math.pow(2,32)));
            h3 = h0.add(B1).add(C2)
                    .mod(BigInteger.valueOf((long)Math.pow(2,32)));
            h0 = T;
        }

        String[] strs = {h0.toString(2),h1.toString(2),h2.toString(2),h3.toString(2)};

        return getHash(strs);

    }

    public BigInteger getHash(String[] strings){
        String res ="";
        for (int i = 0; i < strings.length; i++) {
            while(strings[i].length() < 32){
                strings[i] = '0'+strings[i];
            }
            res+=strings[i];
        }
        return new BigInteger(res, 2);
    }

    private byte[] parseString(String data) {
        int byteCount = data.getBytes().length;
        long bitCount = byteCount * 8;
        byte[] bytes = data.getBytes();
        int size;

        size = bytes.length + (64 - bytes.length % 64);

        byte[] byteArray = new byte[size];
        for (int i = 0; i < byteArray.length; i++) {
            if (i < byteCount) {
                byteArray[i] = bytes[i];
            } else if (i == byteCount) {
                byteArray[i] = (byte) 0x80;
            } else {
                byteArray[i] = 0x00;
            }
        }
        byteArray[byteArray.length - 8] = (byte) (bitCount);
        byteArray[byteArray.length - 7] = (byte) (bitCount >> 8);
        byteArray[byteArray.length - 6] = (byte) (bitCount >> 16);
        byteArray[byteArray.length - 5] = (byte) (bitCount >> 24);
        byteArray[byteArray.length - 4] = (byte) (bitCount >> 32);
        byteArray[byteArray.length - 3] = (byte) (bitCount >> 40);
        byteArray[byteArray.length - 2] = (byte) (bitCount >> 48);
        byteArray[byteArray.length - 1] = (byte) (bitCount >> 56);
        return byteArray;
    }

    private BigInteger[][] getWords(byte[] bytes) {
        int[][] words = new int[bytes.length / 64][16];
        BigInteger[][] result = new BigInteger[bytes.length/64][16];
        int id = 0;
        for (int i = 0; i < bytes.length / 64; i++) {
            for (int j = 0; j < 16; j++) {
                words[i][j] = ((int) bytes[id]) & 0xff
                        | (((int) bytes[id + 1]) & 0xff) << 8
                        | (((int) bytes[id + 2]) & 0xff) << 16
                        | (((int) bytes[id + 3]) & 0xff) << 24;
                id += 4;
                result[i][j] = BigInteger.valueOf(words[i][j]);

            }
        }
        return result;
    }

    private BigInteger K1(int j){
        if(0 <= j && j <= 15){
            return new BigInteger("00000000",16);
        }
        if(16 <= j && j <= 31){
            return new BigInteger("5A827999",16);
        }
        if(32 <= j && j <= 47){
            return new BigInteger("6ED9EBA1",16);
        }
        if(48 <= j && j <= 63){
            return new BigInteger("8F1BBCDC",16);
        }
        if(64 <= j && j <= 79){
            return new BigInteger("A953FD4E",16);
        }
        return new BigInteger("-1");
    }
    private BigInteger K2(int j){
        if(0 <= j && j <= 15){
            return new BigInteger("50A28BE6",16);
        }
        if(16 <= j && j <= 31){
            return new BigInteger("5C4DD124",16);
        }
        if(32 <= j && j <= 47){
            return new BigInteger("6D703EF3",16);
        }
        if(48 <= j && j <= 63){
            return new BigInteger("7A6D76E9",16);
        }
        if(64 <= j && j <= 79){
            return new BigInteger("00000000",16);
        }
        return new BigInteger("-1");
    }

    private BigInteger f(int j, BigInteger x, BigInteger y, BigInteger z){
        if(0<=j && j<=15){
            return x.xor(y).xor(z);
        }
        if(16<=j && j<=31){
            return (x.and(y))
                    .or(x.not().and(z));
        }
        if(32<=j && j<=47){
            return (x.or(y.not())).xor(z);
        }
        if(48<=j && j<=63){
            return (x.and(z))
                    .or(y.and(z.not()));
        }
        if(64<=j && j<=79){
            return x.xor((y.or(z.not())));
        }
        return x;
    }

    private int R1(int j){
        int[][] R = {{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
                {7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8},
                {3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12},
                {1, 9, 11, 10, 0, 8, 12, 4, 13, 3, 7, 15, 14, 5, 6, 2},
                {4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13}};
        return R[j/16][j%16];
    }
    private int R2(int j){
        int[][] R = {{ 5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12},
                {6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2},
                {15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13},
                {8, 6, 4, 1, 3, 11, 15, 0, 5, 12, 2, 13, 9, 7, 10, 14},
                {12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11}};
        return R[j/16][j%16];
    }

    private int S1(int j){
        int[][] S = {{ 11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8},
                {7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12},
                {11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5},
                {11, 12, 14, 15, 14, 15, 9, 8, 9, 14, 5, 6, 8, 6, 5, 12},
                {9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6}};
        return S[j/16][j%16];
    }
    private int S2(int j){
        int[][] S = {{ 8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6},
                {9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11},
                {9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5},
                {15, 5, 8, 11, 14, 14, 6, 14, 6, 9, 12, 9, 12, 5, 15, 8},
                {8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11}};
        return S[j/16][j%16];
    }

}
