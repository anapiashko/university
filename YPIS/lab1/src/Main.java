import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    static List<String> table = new ArrayList<>(270);
    final static int n = 10;

    public static void main(String[] args) {

        List<String> input = read();

        for (int i = 0; i < input.size(); i++) {
            int index = hash(input.get(i));
            table.add(index, input.get(i));
        }

        for (String str : table) {
            System.out.println(str);
        }

    }

    static void delete(String str) throws Exception {
        int index = search(str);
        table.remove(index);
    }

    static int search(String str) throws Exception {
        int i = 0;
        int hash = hash(str);

        while (i != n) {
            int h = hash + i;
            if (table.get(h) == null) {
                throw new Exception("Element not found");
            } else {
                if(table.get(hash).equals(str)){
                    return h;
                }
                i++;
            }
        }

        return -1;
    }

    static void insertId(String str) throws Exception {
        int i = 0;
        int hash = hash(str);
        while (i != n) {
            int h = hash + i;
            if (table.get(h) != null) {
                table.add(h, str);
                break;
            } else {
                i++;
            }
        }

        if(i == n){
            throw new Exception("Failed to add item");
        }
    }

    static int hash(String str) {
        str = str.toUpperCase();
        return str.charAt(0) + str.charAt(1) + str.charAt(2);
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
