public class OrderedList {

    private Node first, last;
    private int size = 0;

    public OrderedList() {

    }

    public void add(String data) {

        if (first == null) {  // if empty list
            Node newNode = new Node(data);
            first = last = newNode;
            size = size + 1;
            return;
        }

        Node node = first;

        // цикл вставки элемента в упорядоченный список
        for (int i = 0; i < size; i++) {

            if (data.charAt(0) <= node.data.charAt(0)) {

                addFirst(node.previous, node, data);

                break;
            }

            node = node.next;
        }

        size = size + 1;
    }

    // возвращает индекс элемента в списке
    public int search(String data) {
        Node node = first;
        for (int i = 0; i < size; i++) {
            if (data.equals(node.data)) {
                return i;
            }
            node = node.next;
        }
        return -1;
    }

    public void delete(String str) {
        int index = search(str);

        if(index < 0){
            return;
        }

        Node node = first;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }

        // если один элемент в списке
        if(node.previous == null && node.next == null){
            first = last = null;
        }
        // если это первый элемент
        else if(node.previous == null && node.next != null){
            first = node.next;
            node.next.previous = null;
        }
        // если это последний элемент
        else if(node.previous != null && node.next == null){
            node.previous.next = null;
        }
        // в остальных случаях
        else {
           Node temp = node.previous;
           temp.next = node.next;
           node.next = temp;
        }
        size = size - 1;
    }

    public void addFirst(Node prev, Node next, String data) {

        final Node newNode = new Node(prev, next, data);

        next.previous = newNode;

        if (prev != null) {
            prev.next = newNode;
        } else {
            first = newNode;
        }
    }

    public void print() {
        System.out.println("---OrderedList print list---");
        Node node = first;
        for (int i = 0; i < size; i++) {
            System.out.println(node.data);
            node = node.next;
        }
    }

    private class Node {
        String data;
        Node next;
        Node previous;

        public Node() {

        }

        public Node(String data) {
            this.data = data;
            next = null;
            previous = null;
        }

        public Node(Node aPrev, Node aNext, String data) {
            this.data = data;
            next = aNext;
            previous = aPrev;
        }
    }
}


