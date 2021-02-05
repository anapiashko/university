import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main1 {

    static HashTable hashTable = new HashTable();

    public static void main(String[] args) {

        List<String> input = read();

        for (int i = 0; i < input.size(); i++) {
            hashTable.insert(input.get(i));
        }

        System.out.println(hashTable.search("string"));

        hashTable.delete("string");

        System.out.println(hashTable.search("string"));

        hashTable.showListByHash("string");
    }

    // построчное считывание файла
    public static List<String> read() {
        List<String> input = new ArrayList<>();
        try {
            File file = new File("input.txt");

            FileReader fr = new FileReader(file);

            BufferedReader reader = new BufferedReader(fr);

            String line = reader.readLine();
            while (line != null) {
                System.out.println(line);
                input.add(line);

                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
