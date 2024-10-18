
public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {
    T data;
    Node<T> next;

    Node(T data) {
        this.data = data;
    }

    public String toString() {
        return data + " -> " + next;
    }

    @Override
    public int compareTo(Node<T> o) {
        return this.data.compareTo(o.data);
    }
}