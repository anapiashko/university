public class HashTable {

    private final int capacity = 50;

    // массив упорядоченых списков
    private final OrderedList[] table = new OrderedList[capacity];

    public HashTable() {
        // начальная инициализация пустыми списками
        for (int i = 0; i < capacity; i++) {
            table[i] = new OrderedList();
        }
    }

    // вставка элемента в таблицу
    // метод add реализует класс OrderedList
    public void insert(String str) {
        int h = hash(str);
        table[h].add(str);
    }

    // поик элемента в таблице
    // возвращает boolean, который означает присутсвие/отсутствие элемента в таблице
    // метод search реализует класс OrderedList
    public boolean search(String str) {

        int hash = hash(str);

        int index = table[hash].search(str);

        return index >= 0;
    }

    // удаление элемента
    public void delete(String str) {

        int hash = hash(str);

        table[hash].delete(str);
    }

    // вывод элементов таблицы с одинаковым хэшем, таким как у элемента str
    public void showListByHash(String str){
        int hash = hash(str);

        table[hash].print();
    }

    // хэш-функция
    int hash(String str) {
        str = str.toUpperCase();
        return (str.charAt(0) + str.charAt(1) + str.charAt(2)) % capacity;
    }
}
