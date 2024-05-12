public class BST {
    private BSTNode root;
    int size;

    public BST() {
        root = null;
    }

    /*---------------------------- main methods ------------------------------ */
    public void insert(Object data) { // inserts sorted
        root = insert_recursive(root, data);
        size++;
    }

    public boolean checkContains(Object data) {
        return contains(data, root);
    }

    public Object find(Object data) {
        BSTNode node = find(data, root);
        if (node != null) {
            return node.data;
        } else {
            // throw exception
            return null;
        }
    }

    public void remove(Object data) {
        size--;
        System.out.println("\n" + remove(data, root) + " is now the root");
    }

    public Object get(int index) {
        return get(index, root);
    }

    public Object min() {
        return min_value(root).data;
    }

    public Object max() {
        return max_value(root).data;
    }

    public Object most(BST tree) {
        return biggest_size(tree);
    }

    public void print_tree() {
        inorder_traversal(root);
    }

    /*--------------------- utility methods ---------------------- */
    private BSTNode insert_recursive(BSTNode root, Object data) {
        if (root == null) {
            root = new BSTNode(data);
            return root;
        }
        if (root.toString().compareToIgnoreCase(data.toString()) > 0) {
            root.left = insert_recursive(root.left, data);
        } else if (root.toString().compareToIgnoreCase(data.toString()) < 0) {
            root.right = insert_recursive(root.right, data);
        }
        return root;

    }

    private BSTNode min_value(BSTNode current) {
        if (current == null)
            return null;
        else if (current.left == null)
            return current; // found
        else
            return min_value(current.left); // keep going to the left
    }

    private BSTNode max_value(BSTNode current) {
        if (current == null)
            return null;
        else if (current.right == null)
            return current; // found
        else
            return max_value(current.right); // keep going to the right
    }

    private Object biggest_size(BST dates) { // this method is used to return the most size for a list
       int maxSize = 0;
       Dates current = ((Dates)dates.get(0));
       for(int i = 0;i < dates.size;i++){
            if(((Dates)dates.get(i)).martyrs.size() > maxSize){
                maxSize = ((Dates)dates.get(i)).martyrs.size();
                current = ((Dates)dates.get(i));
            }
       }
       return current;
    }

    private boolean contains(Object e, BSTNode current) {
        if (current == null)
            return false; // Not found, empty tree.
        if ((e.toString()).compareToIgnoreCase(current.toString()) < 0) {
            return contains(e, current.left); // Search left subtree
        } else if ((e.toString()).compareToIgnoreCase(current.toString()) > 0) {
            return contains(e, current.right); // Search right subtree
        }
        return true; // found .
    }

    private BSTNode find(Object e, BSTNode current) {
        if (current == null)
            return null; // Not found, empty tree.
        if ((e.toString()).compareToIgnoreCase(current.toString()) < 0) {
            return find(e, current.left); // Search left subtree
        } else if ((e.toString()).compareToIgnoreCase(current.toString()) > 0) {
            return find(e, current.right); // Search right subtree
        }
        return current; // found .
    }

    private BSTNode remove(Object e, BSTNode current) {
        if (current == null)
            return null; // if tree is null
        if ((e.toString()).compareToIgnoreCase(current.toString()) < 0)
            current.left = remove(e, current.left);
        else if ((e.toString()).compareToIgnoreCase(current.toString()) > 0)
            current.right = remove(e, current.right);
        else // found data to be deleted
        if (current.left != null && current.right != null)// two children
        {
            // replace with smallest in right subtree (successor)
            current.data = successor(current).data;
            current.right = remove(current.data, current.right);
        } else// one or zero child
            current = (current.left != null) ? current.left : current.right;
        return current;
    }

    private Object get(int index, BSTNode node) { // get by index
        if (node == null || index < 0 || index >= size) {
            return null;
        }
        BSTNode current = min_value(node);
        for (int i = 0; i < index; i++) {
            current = successor(current);
        }
        return current.data;
    }

    private void inorder_traversal(BSTNode node) { // prints all data sorted from lowest to highest(fro max left to max
                                                   // right)
        if (node != null) {
            inorder_traversal(node.left);
            System.out.print(node.data + " ");
            inorder_traversal(node.right);
        }
    }

    private BSTNode successor(BSTNode node) { // this method finds the succesor for any tree/subtree
        if (node.right != null) {
            return min_value(node.right);
        } else {
            BSTNode successor = null;
            BSTNode current = root;
            while (current != null) {
                if (node.toString().compareToIgnoreCase(current.toString()) < 0) {
                    successor = current;
                    current = current.left;
                } else if (node.toString().compareToIgnoreCase(current.toString()) > 0) {
                    current = current.right;
                } else {
                    break;
                }
            }
            return successor;
        }
    }

}
