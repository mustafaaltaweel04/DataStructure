public class Test {
    public static void main(String[] args) {
        Heap node = new HeapMin(5);
        node.push(5);
        node.push(4);
        node.push(18);
        node.push(7);
        node.push(20);
        node.push(11);
        node.print();
    }
}
