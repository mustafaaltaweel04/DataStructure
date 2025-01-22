public class Driver {
    public static void main(String[] args) {
        BST<Integer> tree = new BST<>();

        tree.insert(10);
        tree.insert(5);
        tree.insert(7);
        tree.insert(3);
        tree.insert(12);
        tree.insert(8);

        tree.delete(8);

        tree.inOrder();
        System.out.println();
        tree.postOrder();
        System.out.println();
        tree.preOrder();
    }
}
