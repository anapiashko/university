package console.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConsoleAppTest {

    private static final String outputFileName = ConsoleApp.FILE_BASE + "actual.txt";

    public static void main(String[] args) throws IOException {

        String filename = "test1.txt";

        ConsoleApp.doSort(filename, outputFileName);

        Path path1 = Paths.get(ConsoleApp.FILE_BASE + "expected.txt");
        Path path2 = Paths.get(outputFileName);
        long result = filesCompareByLine(path1, path2);

        if (result == 0) {
            System.out.println("Test passed!");
        } else {
            System.err.println("Test failed!\n Expected and Actual files are NOT identical! Line : " + result);
        }
    }

    public static long filesCompareByLine(Path path1, Path path2) throws IOException {
        try (BufferedReader bf1 = Files.newBufferedReader(path1);
             BufferedReader bf2 = Files.newBufferedReader(path2)) {

            long lineNumber = 1;
            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return lineNumber;
                }
                lineNumber++;
            }
            if (bf2.readLine() == null) {
                return 0;
            } else {
                return lineNumber;
            }
        }
    }
}
