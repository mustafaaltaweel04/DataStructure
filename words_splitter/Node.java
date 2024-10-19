public class Node implements Comparable<Node>{
    String data;
    Node next;

    Node(String data) {
        this.data = data;
    }

    public String toString() {
        return data;
    }

    @Override
    public int compareTo(Node o) {
        return this.data.compareTo(o.data);
    }
}