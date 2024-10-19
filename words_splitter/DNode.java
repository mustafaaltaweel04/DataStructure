public class DNode implements Comparable<DNode>{
    LinkedList data;
    int numOfDigits;
    DNode next;
    DNode prev;
    
    DNode(LinkedList data) {
        this.data = data;
        next = prev = null;
    }

    public String toString() {
        return data.toString();
    }

    @Override
    public int compareTo(DNode o) {
        return this.numOfDigits - o.numOfDigits;
    }
}