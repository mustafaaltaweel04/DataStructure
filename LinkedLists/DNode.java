public class DNode<T extends Comparable<T>> implements Comparable<DNode<T>> {
    T data;
    DNode<T> next;
    DNode<T> prev;
    
    DNode(T data) {
        this.data = data;
        next = prev = null;
    }

    public String toString() {
        return data + " -> " + next;
    }

    @Override
    public int compareTo(DNode<T> o) {
        return this.data.compareTo(o.data);
    }
}