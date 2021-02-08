import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    // хэш-таблица
    static HashTable hashTable = new HashTable();

    public static void main(String[] args) {

        // чтение строк из файла
        List<String> input = read();

        for (int i = 0; i < input.size(); i++) {

            // функция вставки в таблицу элемента
            hashTable.insert(input.get(i));
        }

        // функция вывода элементов таблицы с одинаковым хэшем, таким как у передаваемого элемента
        hashTable.showListByHash("string");
        System.out.println();

        // функция search Для поиска элемента в таблице
        // возвращает boolean, который означает присутсвие элемента в таблице
        boolean exists = hashTable.search("string");
        System.out.println("Search \"string\" = " + exists);
        if (exists){

            System.out.println("Deleting...");

            // функция удаления элемента из таблицы
            hashTable.delete("string");
        }

        // опять ищем тот же элемент, только теперь ожидаем результат false
        System.out.println("Search \"string\" = " + hashTable.search("string"));
        System.out.println();

        // функция вывода элементов таблицы с одинаковым хэшем, таким как у передаваемого элемента
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
                // System.out.println(line);
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
