package console.app;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleApp {

    private static final Integer ARRAY_SIZE = 100;
    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 1000;

    public static void main(String[] args) throws IOException {

        List<Integer> ints = generateInts();
        recordIntsInFile(ints);
    }

    private static List<Integer> generateInts() {

        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            int randomInt = (int) Math.floor(Math.random() * (MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE);
            ints.add(randomInt);
        }
        return ints;
    }

    private static void recordIntsInFile(List<Integer> arr) throws IOException {
        FileWriter writer = new FileWriter("/home/anastasiya/university/CCP/lab2_3/resources/numbers.txt");
        int len = arr.size();
        writer.write(len + "\n");
        for (int i = 0; i < len; i++) {
            writer.write(arr.get(i) + "\n");
        }
        writer.close();
    }
}
