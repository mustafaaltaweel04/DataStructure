public class DNode<T extends Comparable<T>> implements Comparable<DNode<T>>{
    LinkedList<Student> studentsList;
    T data;
    DNode<T> next;
    DNode<T> prev;
    
    DNode(T data) {
        next = prev = null;
        this.data = data;
        studentsList = new LinkedList<>();
    }

    DNode(T data, LinkedList<Student> list) {
        next = prev = null;
        this.data = data;
        studentsList = list;
    }

    public String toString() {
        return data.toString() + studentsList.toString();
    }

    @Override
    public int compareTo(DNode<T> o) {
        return this.data.compareTo(o.data);
    }
}