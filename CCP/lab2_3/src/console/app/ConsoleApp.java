package console.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ConsoleApp {

    private static Integer ARRAY_SIZE = 1000000;
    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;
    private static boolean sort = false;

    static final String FILE_BASE = "/home/anastasiya/university/CCP/lab2_3/resources/";
    private static String inputFileName = FILE_BASE + "numbers.txt";
    private static final String outputFileName = FILE_BASE + "sorted_numbers.txt";

    public static void main(String[] args) throws IOException {

        System.out.print("Enter file name : ");
        Scanner scanner = new Scanner(System.in);
        String filename = scanner.nextLine();

        doSort(filename, outputFileName);

    }

    public static void doSort(String filename, String outputFileName) throws IOException {

        if (filename == null || filename.isEmpty()) {
            List<Integer> generatedInts = generateInts();
            recordIntsInFile(generatedInts, inputFileName);
        } else {
            inputFileName = FILE_BASE + filename;
        }

        long from = System.nanoTime();
        List<Integer> ints = readIntsFromFile();

        List<Integer> sortedInts;
        if (sort) {
            sortedInts = sortUp(ints);
        } else {
            sortedInts = sortDown(ints);
        }

        recordIntsInFile(sortedInts, outputFileName);
        long to = System.nanoTime();
        System.out.println("Time - " + (to - from) / 1_000_000L + " ms");
    }

    private static List<Integer> sortUp(List<Integer> ints) {
        List<Integer> sortedInts = ints
                .stream()
                .sorted()
                .collect(Collectors.toList());

        return sortedInts;
    }

    private static List<Integer> sortDown(List<Integer> ints) {
        List<Integer> sortedInts = ints
                .stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        return sortedInts;
    }

    private static List<Integer> readIntsFromFile() throws FileNotFoundException {
        LinkedList<Integer> list = new LinkedList<>();
        Scanner scanner = new Scanner(new File(inputFileName));
        String[] arrSize = scanner.nextLine().split(" ");
        System.out.println("Array size: " + arrSize[0] + ", sort : " + arrSize[1]);
        ARRAY_SIZE = Integer.parseInt(arrSize[0]);
        sort = Boolean.parseBoolean(arrSize[1]);
        while (scanner.hasNextInt()) {
            list.addLast(scanner.nextInt());
        }
        return list;
    }

    private static List<Integer> generateInts() {

        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            int randomInt = (int) Math.floor(Math.random() * (MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE);
            ints.add(randomInt);
        }
        return ints;
    }

    private static void recordIntsInFile(List<Integer> arr, String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        int len = arr.size();
        writer.write(len + " " + sort + "\n");
        for (int i = 0; i < len; i++) {
            writer.write(arr.get(i) + "\n");
        }
        writer.close();
    }
}
