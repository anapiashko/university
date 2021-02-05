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

    public void insert(String str) {
        int h = hash(str);
        table[h].add(str);
    }

    public boolean search(String str) {

        int hash = hash(str);

        int index = table[hash].search(str);

        return index >= 0;
    }

    public void delete(String str) {

        int hash = hash(str);

        table[hash].delete(str);
    }

    public void showListByHash(String str){
        int hash = hash(str);

        table[hash].print();
    }

    // хэш функция
    int hash(String str) {
        str = str.toUpperCase();
        return (str.charAt(0) + str.charAt(1) + str.charAt(2)) % capacity;
    }
}
