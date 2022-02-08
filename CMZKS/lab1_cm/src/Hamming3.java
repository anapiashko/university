import java.util.Arrays;
import java.util.stream.Collectors;

public class Hamming3 {

    static int[][] matrix = {
            {0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1},
            {0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1},
            {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1},
            {1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1},
    };

    static int[][] matrix1 = {
            {0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0},
            {0, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 0, 0},
            {1, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0},
            {1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 0, 0, 1},
    };

    public static void main(String[] args) {
        doMain(new int[][]{
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0},
                {1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0}
        });
    }

    public static Integer[][] doMain(int[][] words) {

        int[] codeWord = new int[15];
        int errorInBit = (int) (Math.random() * 15);
        Integer[][] recoveredWords = new Integer[words.length][];
        for (int i = 0; i < words.length; i++) {
            int[] word = words[i];

            codeWord[0] = word[0];
            codeWord[1] = word[1];
            codeWord[2] = word[2];
            codeWord[3] = word[3];
            codeWord[4] = word[4];
            codeWord[5] = word[5];
            codeWord[6] = word[6];
            codeWord[7] = word[7];
            codeWord[8] = word[8];
            codeWord[9] = word[9];
            codeWord[10] = word[10];

            codeWord[11] = (codeWord[4] + codeWord[5] + codeWord[6] + codeWord[7] + codeWord[8] + codeWord[9] + codeWord[10]) % 2;
            codeWord[12] = (codeWord[1] + codeWord[2] + codeWord[3] + codeWord[7] + codeWord[8] + codeWord[9] + codeWord[10]) % 2;
            codeWord[13] = (codeWord[0] + codeWord[2] + codeWord[3] + codeWord[5] + codeWord[6] + codeWord[9] + codeWord[10]) % 2;
            codeWord[14] = (codeWord[0] + codeWord[1] + codeWord[3] + codeWord[4] + codeWord[6] + codeWord[8] + codeWord[10]) % 2;

//            codeWord[2] = word[0];
//            codeWord[4] = word[1];
//            codeWord[5] = word[2];
//            codeWord[6] = word[3];
//            codeWord[8] = word[4];
//            codeWord[9] = word[5];
//            codeWord[10] = word[6];
//            codeWord[11] = word[7];
//            codeWord[12] = word[8];
//            codeWord[13] = word[9];
//            codeWord[14] = word[10];
//
//            //
//            //r1=u3+u5+u7
//            //r2=u3+u6+u7
//            //r3=u5+u6+u7
//            codeWord[0] = (codeWord[4] + codeWord[5] + codeWord[6] + codeWord[7] + codeWord[8] + codeWord[9] + codeWord[10]) % 2;
//            codeWord[1] = (codeWord[1] + codeWord[2] + codeWord[3] + codeWord[7] + codeWord[8] + codeWord[9] + codeWord[10]) % 2;
//            codeWord[3] = (codeWord[0] + codeWord[2] + codeWord[3] + codeWord[5] + codeWord[6] + codeWord[9] + codeWord[10]) % 2;
//            codeWord[7] = (codeWord[0] + codeWord[1] + codeWord[3] + codeWord[4] + codeWord[6] + codeWord[8] + codeWord[10]) % 2;

            System.out.println("Отправляемое сообщение: " + Arrays.toString(codeWord));

            //добавляем ошибку
            int errorIndex = (int) (Math.random() * 15);
            System.out.println("Bit with mistake : " + errorIndex);
            if (errorIndex != 0) {
                codeWord[errorIndex] ^= 1;
            }
            System.out.println("Message with mistake: " + Arrays.toString(codeWord));

            int[] syndromeMatrix = createSyndromeMatrix(codeWord, matrix1);

            int h;
            int count = 0;
            for (h = 0; h < 15; h++) {
                count = 0;
                for (int j = 0; j < syndromeMatrix.length; j++) {
                    if( matrix1[j][h] == syndromeMatrix[j]){
                        count++;
                    }
                }
                if (count == 4) {
                    break;
                }
            }

            if (h == 15) {
                errorInBit = 0;
            } else {
                errorInBit = h;
            }

            if (errorInBit == 0) {
                System.out.println("No error found");
            } else {
                System.out.println("Error in bit = " + errorInBit);
                codeWord[errorInBit] = codeWord[errorInBit] ^ 1;
            }

            System.out.println("Исходное сообщение:" + codeWord[0] + "" + codeWord[1] + "" + codeWord[2] + "" + codeWord[3] +
                    "" + codeWord[4] + "" + "" + codeWord[5] + "" + "" + codeWord[6] + "" + "" + codeWord[7] + "" +
                    "" + codeWord[8] + "" + "" + codeWord[9] + ""+ "" + codeWord[10]);
            System.out.println("------------------------------------------");

            recoveredWords[i] = Arrays.stream(codeWord)
                    .boxed()
                    .collect(Collectors.toList())
                    .subList(0, 11)
                    .toArray(new Integer[0]);
        }
        return recoveredWords;
    }

    static int[] createSyndromeMatrix(int[] generatedCode, int[][] checkMatrix) {
        int[] syndromeMatrix = new int[4];

        for (int i = 0; i < 4; i++) {
            int count = 0;
            for (int j = 0; j < generatedCode.length; j++) {
                if (generatedCode[j] == 1 && checkMatrix[i][j] == 1) {
                    count++;
                }
            }
            syndromeMatrix[i] = count % 2;
        }

        return syndromeMatrix;
    }
}
