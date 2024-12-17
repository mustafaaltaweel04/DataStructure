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

    void insertSorted(DNode<T> node) {
        if (head == null) {
            head = tail = node;
            node.prev = null;
            node.next = null;
        } else if (node.compareTo(head) <= 0) {
            node.next = head;
            node.prev = null;
            head.prev = node;
            head = node;
        } else {
            DNode<T> curr = head;

            while (curr.next != null && node.compareTo(curr.next) > 0) {
                curr = curr.next;
            }

            node.next = curr.next;
            node.prev = curr;
            curr.next = node;

            if (node.next != null) {
                node.next.prev = node;
            } else {
                tail = node;
            }
        }

        size++;
    }

    void deleteFirst() {
        if (head == null)
            return;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
    }

    void deleteLast() {
        if (tail == null)
            return;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
    }

    void delete(int index) {
        if (index < 0 || index >= size)
            return;
        if (index == 0) {
            deleteFirst();
        } else if (index == size - 1) {
            deleteLast();
        } else {
            DNode<T> curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;
            size--;
        }
    }

    DNode<T> getFirst() {
        return head;
    }

    DNode<T> getLast() {
        return tail;
    }

    public DNode<T> get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        DNode<T> curr = head;
        for (int i = 0; i < index; i++) {
            if (curr != null) {
                curr = curr.next;
            } else {
                return null;
            }
        }
        return curr;
    }

    int searchIndex(T data) {
        DNode<T> curr = head;
        int i = 0;
        for (i = 0; i < size; i++) {
            if (curr.flight.equals(data)) {
                return i;
            }
            curr = curr.next;
        }
        return -1;
    }

    DNode<T> searchByID(int fId){
        DNode<T> curr = head;
        for(int i = 0;i < size;i++){
            if(curr.flight.getfID() == fId){
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }

    int numOfVipCheckIn(DNode<Flight> node){
        return node.vipQueue.size();
    }

    int numOfRegCheckIn(DNode<Flight> node){
        return node.regQueue.size();
    }

    int numOfVipBoarded(DNode<Flight> node){
        int c = 0;
        for(int i = 0;i < node.boardedPassengers.size();i++){
            if(node.boardedPassengers.get(i).isVip())
            c++;
        }
        return c;
    }

    int numOfRegBoarded(DNode<Flight> node){
        int c = 0;
        for(int i = 0;i < node.boardedPassengers.size();i++){
            if(!node.boardedPassengers.get(i).isVip())
            c++;
        }
        return c;
    }

    int numOfVipCanceled(DNode<Flight> node){
        int c = 0;
        for(int i = 0;i < node.canceledPassengers.size();i++){
            if(node.canceledPassengers.get(i).isVip())
            c++;
        }
        return c;
    }

    int numOfRegCanceled(DNode<Flight> node){
        int c = 0;
        for(int i = 0;i < node.canceledPassengers.size();i++){
            if(!node.canceledPassengers.get(i).isVip())
            c++;
        }
        return c;
    }
    
    int size() {
        return size;
    }

    boolean isEmpty() {
        return head == null;
    }

    void printList() {
        DNode<T> curr = head;
        while (curr != null) {
            System.out.print(curr.toString() + "------------------------------------------------------------\n");
            curr = curr.next;
        }
    }
}