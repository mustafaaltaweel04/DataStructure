public class QNode<T extends Comparable<T>> implements Comparable<QNode<T>>{
    T data;
    QNode<T> next;
    QNode<T> prev;
    
    QNode(T data) {
        next = prev = null;
        this.data = data;
    }

    public String toString() {
        return data.toString();
    }

    @Override
    public int compareTo(QNode<T> o) {
        return this.data.compareTo(o.data);
    }
}