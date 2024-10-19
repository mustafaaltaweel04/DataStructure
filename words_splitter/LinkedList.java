public class LinkedList {
    private int size;
    private Node head;

    LinkedList() {
        head = null;
        size = 0;
    }

    void insertFirst(String data) {
        Node node = new Node(data);
        if (size == 0) {
            head = node;
            node.next = null;
        } else {
            node.next = head;
            head = node;
        }
        size++;
    }

    void insertLast(String data) {
        Node node = new Node(data);
        if (size == 0) {
            insertFirst(data);
            return;
        }
        node.next = head;
        Node curr = head;
        while (curr.next != head) {
            curr = curr.next;
        }
        curr.next = node;
        size++;
    }

    void insert(String data, int index) {
        Node node = new Node(data);
        if (index == 0) {
            insertFirst(data);
            return;
        } else if (index >= size) {
            insertLast(data);
            return;
        }

        Node curr = head;
        for (int i = 0; i < index - 1; i++) {
            curr = curr.next;
        }
        node.next = curr.next;
        curr.next = node;
        size++;
    }

    void insertSorted(String data) {
        if (data == null) {
            throw new IllegalArgumentException("Node to insert cannot be null");
        }

        Node node = new Node(data);
        
        if (head == null || node.compareTo(head) < 0) {
            node.next = head;
            head = node;
            size++;
            return;
        }

        Node curr = head;
        Node prev = null;

        while (curr != null && node.compareTo(curr) > 0) {
            prev = curr;
            curr = curr.next;
        }

        prev.next = node;
        node.next = curr;
        size++;
    }

    String deleteFirst() {
        if (size == 0) {
            throw new IllegalStateException("List is empty");
        }

        String data = head.data;
        if (size == 1) {
            head = null;
        } else {
            Node curr = head;
            while (curr.next != head) {
                curr = curr.next;
            }
            head = head.next;
            curr.next = head;
        }
        size--;
        return data;
    }

    void deleteLast() {
        if (size == 0) {
            throw new IllegalStateException("List is empty");
        } else if (size == 1) {
            deleteFirst();
            return;
        }

        Node curr = head;
        while (curr.next.next != head) {
            curr = curr.next;
        }
        curr.next = head;
        size--;
    }

    void delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        if (index == 0) {
            deleteFirst();
        } else if (index == size - 1) {
            deleteLast();
        } else {
            Node curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            curr.next = curr.next.next;
            size--;
        }
    }

    Node lastNodeStanding(int index) {
        index = index % this.size;
        while (this.size > 1) {
            this.delete(index);
            index = ++index % this.size;

        }
        return this.head;
    }

    /*
     * Node search(String data){ // search
     *
     * }
     */
    String get(int index) {
        if (index == 0) {
            return head.data;
        } else if (index == size - 1) {
            Node curr = head;
            for (int i = 0; i < size - 1; i++) {
                curr = curr.next;
            }
            return curr.data;
        } else {
            Node curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr.data;
        }
    }

    int size() {
        return size;
    }

    void printList() {
        Node curr = head;
        while(curr != null){
            System.out.println(curr.toString() + "\n");
            curr = curr.next;
        }
    }

    @Override
    public String toString() {
        Node curr = head;
        String str = "";
        while(curr != null){
            str += curr.toString() + " ";
            curr = curr.next;
        }
        return str;
    }
}
