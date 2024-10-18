public class DLinkedList<T extends Comparable<T>> {
    private int size;
    private DNode<T> head;
    private DNode<T> tail;

    DLinkedList() {
        size = 0;
        head = tail = null;
    }

    void insertFirst(T data) {
        DNode<T> node = new DNode<T>(data);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        size++;
    }

    void insertLast(T data) {
        DNode<T> node = new DNode<T>(data);
        if (head == null) {
            head = tail = node;
        } else {
            tail.next = node;
            node.prev = tail;
            tail = node;
        }
        size++;
    }

    void insert(T data, int index) {
        if (index == 0 || size == 0) {
            insertFirst(data);
        } else if (index == size) {
            insertLast(data);
        } else if (index <= size && index > 0) {
            DNode<T> node = new DNode<T>(data);
            DNode<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            node.next = curr.next;
            curr.next = node;
            node.prev = curr;
            size++;
        }

    }

    void insertSorted(T data) {
        DNode<T> node = new DNode<T>(data);
        if (head == null) {
            head = tail = node;
            node.prev = null;
            node.next = null;
        } else if (node.compareTo(head) <= 0) {
            node.next = head;
            node.prev = null;
            head.prev = node;
            head = node;// Insert First
        } else {
            DNode<T> curr = head;
            while (curr.next != null && node.compareTo(curr.next) > 0) {
                curr = curr.next;
            }

            node.next = curr.next;
            node.prev = curr;
            curr.next = node;

            if (node.next != null) {
                node.next.prev = node; // insert middle
            } else {
                tail = node; // insert last
            }
        }

        size++;
    }

    void deleteFirst() {
        head = head.next;
        size--;
    }

    void deleteLast() {
        tail = tail.prev;
        tail.next = null;
        size--;
    }

    void delete(int index){
        if(size == 1 || index == 0){
            deleteFirst();
        }else if(index == size-1){
            deleteLast();
        } else if(index < size && index > 0){
            DNode<T> curr = head;
            for(int i =0; i < index;i++){
                curr = curr.next;
            }
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
        }
    }

    void printList() {
        System.out.println(head.toString());
    }

    
}