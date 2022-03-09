import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    // rows
    static final int k1 = 2;

    // columns
    static final int k2 = 5;

    // z
    static final int z = 2;

    int[][][] matrix = new int[k1][k2][z];

    int[][] paritetZ = new int[k1][k2];
//    //
//    int[] Xhv = new int[k1 + k2 + 1];

    int[][][] matrixWithMistake = new int[k1][k2][z];

    public static void main(String[] args) {

        // length 20
        String message = "01011011110000110110";

        Main main = new Main();
        main.fillMatrixByMessage(message);
        // size k2
        List<Integer[]> Xh = main.calculateXh();
        // size k1
        List<Integer[]> Xv = main.calculateXv();
        main.calculateZ();

        Integer[] indexesWithMistake = {1, 6, 12, 19};

        main.fillMatrixByMessageWithMistake(message, indexesWithMistake);

    }

    private void calculateZ() {
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < k2; j++) {
                int t = 0;
                for (int m = 0; m < z; m++) {
                    t ^= matrix[i][j][m];
                }
                paritetZ[i][j] = t;
            }
        }

        System.out.println("\nparitetZ :");
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < k2; j++) {
                System.out.print(paritetZ[i][j]);
            }
            System.out.println();
        }
    }

    private List<Integer[]> calculateXh() {

        List<Integer[]> Xh = new ArrayList<>();

        for (int m = 0; m < z; m++) {
            Integer[] XhSingle = new Integer[k1];
            for (int i = 0; i < k1; i++) {
                int t = matrix[i][0][m];
                for (int j = 1; j < k2; j++) {
                    t ^= matrix[i][j][m];
                }
                XhSingle[i] = t;
            }
            Xh.add(XhSingle);
        }
        System.out.println("Xh.get(0) = " + Arrays.toString(Xh.get(0)));
        System.out.println("Xh.get(1) = " + Arrays.toString(Xh.get(1)));

//        int checksumXh = 0;
//        for (int i = 0; i < k1; i++) {
//            Xhv[i] = Xh[i];
//            checksumXh ^= Xh[i];
//        }
//
//        int checksumXv = 0;
//        for (int i = k1, j = 0; i < k1 + k2; i++, j++) {
//            Xhv[i] = Xv[j];
//            checksumXv ^= Xv[j];
//        }
//
//        Xhv[k1 + k2] = checksumXh == checksumXv ? checksumXh : -1;
//        System.out.println("Xhv[k1 + k2] = " + Xhv[k1 + k2]);
        return Xh;
    }

    private List<Integer[]> calculateXv() {

        List<Integer[]> Xv = new ArrayList<>();

        for (int m = 0; m < z; m++) {
            Integer[] XvSingle = new Integer[k2];
            for (int i = 0; i < k2; i++) {
                int t = matrix[0][i][m];
                for (int j = 1; j < k1; j++) {
                    t ^= matrix[j][i][m];
                }
                XvSingle[i] = t;
            }
            Xv.add(XvSingle);
        }
        System.out.println("Xv.get(0) = " + Arrays.toString(Xv.get(0)));
        System.out.println("Xv.get(1) = " + Arrays.toString(Xv.get(1)));
        return Xv;
    }

    private void fillMatrixByMessageWithMistake(String message, Integer[] indexesWithMistake) {

        for (int m = 0, c = 0; m < z; m++) {
            for (int i = 0; i < k1; i++) {
                for (int j = 0; j < k2; j++, c++) {
                    System.out.print(message.charAt(c));

                    List<Integer> indexesWithMistakeList = Arrays.asList(indexesWithMistake);

                    int valueByIndex = Integer.parseInt(String.valueOf(message.charAt(c)));

                    if (indexesWithMistakeList.contains(c)) {
                        matrixWithMistake[i][j][m] = valueByIndex == 0 ? 1 : 0;
                    } else {
                        matrixWithMistake[i][j][m] = valueByIndex;
                    }

                }
            }
        }

        printMatrix("\nMatrix With Mistake :", matrixWithMistake);
    }

    private void fillMatrixByMessage(String message) {

        for (int m = 0, c = 0; m < z; m++) {
            for (int i = 0; i < k1; i++) {
                for (int j = 0; j < k2; j++, c++) {
                    System.out.print(message.charAt(c));
                    matrix[i][j][m] = Integer.parseInt(String.valueOf(message.charAt(c)));

                }
            }
        }

        printMatrix("\nMatrix :", matrix);
    }

    private void printMatrix(String messageToAnnounce, int[][][] matrix) {

        System.out.println(messageToAnnounce);
        for (int m = 0; m < z; m++) {
            for (int i = 0; i < k1; i++) {
                for (int j = 0; j < k2; j++) {
                    System.out.print(matrix[i][j][m]);
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
