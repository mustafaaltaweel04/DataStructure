import java.util.ArrayList;

@SuppressWarnings("unchecked")
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

    DNode<Major> getFirst() {
        return (DNode<Major>) head;
    }

    DNode<Major> getLast() {
        return (DNode<Major>) tail;
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

    int search(T data) {
        DNode<T> curr = head;
        int i = 0;
        for (i = 0; i < size; i++) {
            if (curr.data.equals(data)) {
                return i;
            }
            curr = curr.next;
        }
        return -1;
    }

    DNode<Major> searchByName(String name) { // search for a major by name and return it O(N)
        DNode<Major> curr = (DNode<Major>) head;
        for (int i = 0; i < size; i++) {
            if (curr.data.getName().equals(name)) {
                return curr;
            }
            curr = curr.next;
        }
        return null;
    }

    boolean exists(String name) { // checks if a Major exists in the system by giving name O(N)
        DNode<Major> curr = (DNode<Major>) head;
        for (int i = 0; i < size; i++) {
            if (curr.data.getName().equals(name)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    ArrayList<Student> traverseMajors() { // this method is used to make a single linked list have all students in the
                                          // system: O(M * N)
        DNode<Major> curr = (DNode<Major>) head;
        ArrayList<Student> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < curr.studentsList.size(); j++) {
                list.add(curr.studentsList.get(j));
            }
            curr = curr.next;
        }
        return list;
    }

    DLinkedList<Major> acceptedMajorsForStudent(Student student) { // this method is used to make a list for a student that contains all majors they can access
        DLinkedList<Major> newList = new DLinkedList<>();
        DNode<Major> curr = (DNode<Major>) head;
        while (curr != null) {
            Major major = curr.data;
            if (major != null && student.calculateAdmissionMark(major.getTawjihiWeight(), major.getTestWeight()) > major
                    .getAcceptGrade()) {
                newList.insertSorted(new DNode<>(curr.data, curr.studentsList));
            }
            curr = curr.next;
        }
        return newList;
    }

    int allAccepted(){ //O(N)
        int counter = 0;
        DNode<Major> curr = (DNode<Major>) head;
        for(int i = 0;i < size;i++){
            Major major = curr.data;
            counter += major.acceptNum;
            curr = curr.next;
        }
        return counter;
    }

    int allRejected(){ //O(N)
        int counter = 0;
        DNode<Major> curr = (DNode<Major>) head;
        for(int i = 0;i < size;i++){
            Major major = curr.data;
            counter += major.rejectNum;
            curr = curr.next;
        }
        return counter;
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
            System.out.print(curr.toString() + "\n");
            curr = curr.next;
        }
    }
}