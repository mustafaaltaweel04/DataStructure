public class LinkedList<T extends Comparable<T>> {
    private int size;
    Node<T> head;

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
            throw new IllegalArgumentException("Node to insert cannot be null");
        }

        Node<T> node = new Node<>(data);
        
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

        prev.next = node;
        node.next = curr;
        size++;
    }

    T deleteFirst() {
        if (size == 0) {
            throw new IllegalStateException("List is empty");
        }

        T data = head.data;
        if (size == 1) {
            head = null;
        } else {
            Node<T> curr = head;
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

        Node<T> curr = head;
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
            Node<T> curr = head;
            for (int i = 0; i < index - 1; i++) {
                curr = curr.next;
            }
            curr.next = curr.next.next;
            size--;
        }
    }

    Node<T> lastNodeStanding(int index) {
        index = index % this.size;
        while (this.size > 1) {
            this.delete(index);
            index = ++index % this.size;

        }
        return this.head;
    }

    /*
     * Node<T> search(T data){ // search
     *
     * }
     */
    T get(int index) {
        if (index == 0) {
            return head.data;
        } else if (index == size - 1) {
            Node<T> curr = head;
            for (int i = 0; i < size - 1; i++) {
                curr = curr.next;
            }
            return curr.data;
        } else {
            Node<T> curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr.data;
        }
    }

    int size() {
        return size;
    }

    LinkedList<T> merge(LinkedList<T> list1) {
        Node<T> curr1 = this.head;
        Node<T> curr2 = list1.head;
        LinkedList<T> newList = new LinkedList<T>();

        while (curr1 != null || curr2 != null) {
            if (curr1 != null && (curr2 == null || curr1.compareTo(curr2) <= 0)) {
                newList.insertLast(curr1.data);
                curr1 = curr1.next;
            }
            if (curr2 != null && (curr1 == null || curr2.compareTo(curr1) < 0)) {
                newList.insertLast(curr2.data);
                curr2 = curr2.next;
            }
        }

        return newList;
    }

    @SuppressWarnings("unchecked")
    public int[] radixSort(int[] list1) {
        int[] newList = list1.clone();

        LinkedList<Integer>[] buckets = new LinkedList[10];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new LinkedList<>();
        }

        int d = maxDigits(newList);
        for (int i = 0; i < d; i++) {
            for (int num : newList) {
                buckets[getDigit(num, i)].insertLast(num);
            }
            int index = 0;
            for (LinkedList<Integer> bucket : buckets) {
                while (!bucket.isEmpty()) {
                    newList[index++] = bucket.deleteFirst();
                }
            }
        }
        return newList;
    }

    private int getDigit(int number, int position) {
        return (number / (int) Math.pow(10, position)) % 10;
    }

    private int maxDigits(int[] list) {
        int max = list[0];
        for (int i = 0; i < list.length; i++) {
            if (max < list[i]) {
                max = list[i];
            }
        }
        int d = 0;
        while (max > 0) {
            max /= 10;
            d++;
        }
        return d;
    }

    void printList() {
        System.out.println(head.toString());
    }

    boolean isEmpty() {
        return head == null;
    }

    public int compareTo(LinkedList<T> data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
}
