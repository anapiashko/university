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
//    //
//    int[] Xhv = new int[k1 + k2 + 1];

    int[][][] matrixWithMistake = new int[k1][k2][z];

    public static void main(String[] args) {

        // length 20
        String message = "01011011110000110110";

        Main main = new Main();

        main.fillMatrixByMessage(message);
        // size k2
        List<Integer[]> Xh = main.calculateXh(main.matrix);
        // size k1
        List<Integer[]> Xv = main.calculateXv(main.matrix);
        // size k1xk2
        int[][] paritetZ = main.calculateZ(main.matrix);

        Integer[] indexesWithMistake = {6, 12};

        main.fillMatrixByMessageWithMistake(message, indexesWithMistake);

        List<Integer[]> XhWithMistake = main.calculateXh(main.matrixWithMistake);
        List<Integer[]> XvWithMistake = main.calculateXv(main.matrixWithMistake);
        int[][] paritetZWithMistake = main.calculateZ(main.matrixWithMistake);

        List<Boolean[]> placeOfMistakeXh = main.compareParitetsXh(Xh, XhWithMistake);
        List<Boolean[]> placeOfMistakeXv = main.compareParitetsXv(Xv, XvWithMistake);
        boolean[][] placeOfMistakeParitetsZ = main.compareParitetsZ(paritetZ, paritetZWithMistake);

        main.findAndCorrectMistake(placeOfMistakeXh, placeOfMistakeXv, placeOfMistakeParitetsZ);

    }

    private void findAndCorrectMistake(List<Boolean[]> placeOfMistakeXh, List<Boolean[]> placeOfMistakeXv,
                                       boolean[][] placeOfMistakeParitetsZ) {


        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < k2; j++) {
                if (placeOfMistakeParitetsZ[i][j]) {
                    for (int m = 0; m < placeOfMistakeXh.size(); m++) {
                        if (placeOfMistakeXh.get(m)[i] && placeOfMistakeXv.get(m)[j]) {
                            matrixWithMistake[i][j][m] ^= 1;
                        }
                    }
                }
            }
        }

        printMatrix("\nCorrected matrix with mistakes : ", matrixWithMistake);
    }

    private boolean[][] compareParitetsZ(int[][] paritetZ, int[][] paritetZWithMistake) {
        boolean[][] placeOfMistake = new boolean[k1][k2];
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < k2; j++) {
                placeOfMistake[i][j] = paritetZ[i][j] != paritetZWithMistake[i][j];
            }
        }
        System.out.println("\nplaceOfMistake paritetZ :");
        for (int i = 0; i < k1; i++) {
            for (int j = 0; j < k2; j++) {
                System.out.print(placeOfMistake[i][j] + " ");
            }
            System.out.println();
        }
        return placeOfMistake;
    }

    private List<Boolean[]> compareParitetsXh(List<Integer[]> Xh, List<Integer[]> XhWithMistake) {

        // size k1
        List<Boolean[]> placeOfMistake = new ArrayList<>();
        for (int i = 0; i < Xh.size(); i++) {
            Boolean[] placeOfMistakeSingle = new Boolean[k1];
            for (int j = 0; j < k1; j++) {
                placeOfMistakeSingle[j] = !Xh.get(i)[j].equals(XhWithMistake.get(i)[j]);
            }
            placeOfMistake.add(placeOfMistakeSingle);
        }
        System.out.println("placeOfMistake.get(0) = " + Arrays.toString(placeOfMistake.get(0)));
        System.out.println("placeOfMistake.get(1) = " + Arrays.toString(placeOfMistake.get(1)));
        return placeOfMistake;
    }

    private List<Boolean[]> compareParitetsXv(List<Integer[]> Xv, List<Integer[]> XvWithMistake) {

        // size k1
        List<Boolean[]> placeOfMistake = new ArrayList<>();
        for (int i = 0; i < Xv.size(); i++) {
            Boolean[] placeOfMistakeSingle = new Boolean[k2];
            for (int j = 0; j < k2; j++) {
                placeOfMistakeSingle[j] = !Xv.get(i)[j].equals(XvWithMistake.get(i)[j]);
            }
            placeOfMistake.add(placeOfMistakeSingle);
        }
        System.out.println("placeOfMistake.get(0) = " + Arrays.toString(placeOfMistake.get(0)));
        System.out.println("placeOfMistake.get(1) = " + Arrays.toString(placeOfMistake.get(1)));
        return placeOfMistake;
    }

    private int[][] calculateZ(int[][][] matrix) {

        int[][] paritetZ = new int[k1][k2];

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
        return paritetZ;
    }

    private List<Integer[]> calculateXh(int[][][] matrix) {

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

        return Xh;
    }

    private List<Integer[]> calculateXv(int[][][] matrix) {

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
