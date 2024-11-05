public class LinkedList<T extends Comparable<T>> {
    private int size;
    private Node<T> head;

    LinkedList() {
        head = null;
        size = 0;
    }

    void insertFirst(T data) {
        Node<T> node = new Node<T>(data);
        if (size == 0) {
            head = node;
            node.next = null;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    void insertLast(T data) {
        Node<T> node = new Node<T>(data);
        if (size == 0) {
            insertFirst(data);
            return;
        }
        node.next = head;
        Node<T> curr = head;
        while (curr.next != head) {
            curr = curr.next;
        }
        curr.next = node;
        size++;
    }

    void insert(T data, int index) {
        Node<T> node = new Node<T>(data);
        if (index == 0) {
            insertFirst(data);
            return;
        } else if (index >= size) {
            insertLast(data);
            return;
        }

        Node<T> curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }
        node.next = curr.next;
        curr.next = node;
        size++;
    }

    void insertSorted(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Node<T> to insert cannot be null");
        }
        Node<T> node = new Node<T>(data);    
        if (head == null || node.compareTo(head) < 0) {
            node.next = head;
            head = node;
            size++;
            return;
        }
    
        Node<T> curr = head;
        Node<T> prev = null;
        while (curr != null && node.compareTo(curr) > 0) {
            prev = curr;
            curr = curr.next;
        }
        if (prev != null) {
            prev.next = node;
        }
        node.next = curr;
        size++;
    }
    

    void deleteFirst() {
        head = head.next;
        size--;
    }

    public void deleteLast() {
        if (head == null) {
            System.out.println("list is empty.");
            return;
        }
        if (head.next == null) {
            head = null;
            return;
        }
        Node<T> curr = head;
        while (curr.next.next != null) {
            curr = curr.next;
        }
        curr.next = null; 
    }

    void delete(int index) {
        if (index < 0 || index >= size) {
            System.out.println("out of bounds");
            return;
        }
        if (index == 0) {
            deleteFirst();
        } else if (index == size - 1) {
            deleteLast();
        } else {
            Node<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            curr.next = curr.next.next;
            size--;
        }
    }

    public int search(T data) {
        Node<T> curr = head;
        int i = 0;
        for (i = 0;i < size; i++) {
            if (curr.data.equals(data)) {
                return i; 
            }
            curr = curr.next;
        }
        return -1; 
    }

    
    public T get(int index) {
        Node<T> curr = head;
        for(int i = 0; i < index;i++) {
            curr = curr.next;
        }
        return curr == null ? null : curr.data; 
    }
    
    int size() {
        return size;
    }

    void printList() {
        Node<T> curr = head;
        while (curr != null) {
            System.out.println(curr.toString());
            curr = curr.next;
        }
    }

    @Override
    public String toString() {
        Node<T> curr = head;
        String str = "";
        while (curr != null) {
            str += curr.toString();
            curr = curr.next;
        }
        return str;
    }
}
