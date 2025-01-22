public class BST<T extends Comparable<T>> {
    private TNode<T> root;

    public TNode<T> find(T data) {
        return find(data, root);
    }

    public void insert(T data) {
        if (root == null) {
            root = new TNode<T>(data);
        } else {
            insert(data, root);
        }

    }

    public TNode<T> delete(T data){
        TNode<T> curr = root;
        TNode<T> parent = root;
        boolean isLeftChild = false;

        if(root == null){
            return null;
        }
        while(curr != null && !curr.data.equals(data)){
            parent = curr;
            if(data.compareTo(curr.data) < 0){
                curr = curr.left;
                isLeftChild = true;
            }
            else{
                curr = curr.right;
                isLeftChild = false;
            }
        }
        if(curr == null){
            return null;
        }
        if(curr.isLeaf()){
            if (curr == root) {
                root = null;
            }else{
                if (isLeftChild) {
                    parent.left = null;
                } else{
                    parent.right = null;
                }
            }
        }

        else if(curr.hasLeft() && !curr.hasRight()){
            if(curr == root){
                root = curr.left;
            } else if(isLeftChild){
                parent.left = curr.left;
            } else{
                parent.right = curr.left;
            }
        }

        else if(!curr.hasLeft() && curr.hasRight()){
            if(curr == root){
                root = curr.left;
            } else if(isLeftChild){
                parent.left = curr.right;
            } else{
                parent.right = curr.right;
            }
        }
        else{
            TNode<T> succ = getSuccesor(curr);
            if(curr == root){
                root = succ;
            } else if(isLeftChild){
                parent.left = succ;
            } else{
                parent.right = succ;
            }
            succ.left = curr.left;
        }
        return null;
    }

    public TNode<T> max() {
        return max(root);
    }

    public TNode<T> min() {
        return min(root);
    }

    public void inOrder() {
        inorder(root);
    }

    public void preOrder() {
        preorder(root);
    }

    public void postOrder() {
        postorder(root);
    }

    /* ---- HELPER METHODS ---- */

    private TNode<T> find(T data, TNode<T> node) {
        if (node != null) {
            int comp = node.data.compareTo(data);
            if (comp == 0) {
                return node;
            } else if (comp > 0 && node.hasLeft()) {
                return find(data, node.left);
            } else if (comp < 0 && node.hasRight()) {
                return find(data, node.right);
            }
        }
        return null;
    }

    private void insert(T data, TNode<T> node) {
        if (data.compareTo(node.data) >= 0) {
            if (!node.hasRight()) {
                node.right = new TNode<T>(data);
            } else {
                insert(data, node.right);
            }
        } else {
            if (!node.hasLeft()) {
                node.left = new TNode<T>(data);
            } else {
                insert(data, node.left);
            }
        }
    }

    private TNode<T> max(TNode<T> node) {
        if (node != null) {
            if (!node.hasRight()) {
                return node;
            } else {
                return max(node.right);
            }
        }
        return null;
    }

    private TNode<T> min(TNode<T> node) {
        if (node != null) {
            if (!node.hasLeft()) {
                return node;
            } else {
                return min(node.left);
            }
        }
        return null;
    }

    private void inorder(TNode<T> node) {
        if (node != null) {
            inorder(node.left);
            System.out.print(node.data + " ");
            inorder(node.right);
        }
    }

    private void preorder(TNode<T> node) {
        if (node != null) {
            System.out.print(node.data + " ");
            preorder(node.left);
            preorder(node.right);
        }
    }

    private void postorder(TNode<T> node) {
        if (node != null) {
            postorder(node.left);
            postorder(node.right);
            System.out.print(node.data + " ");
        }
    }

    private TNode<T> getSuccesor(TNode<T> node) {
        TNode<T> parentOfSuccessor = node;
        TNode<T> successor = node;
        TNode<T> current = node.right;
        while (current != null) {
            parentOfSuccessor = successor;
            successor = current;
            current = current.left;
        }
        if (successor != node.right) { 
            parentOfSuccessor.left = successor.right;
            successor.right = node.right;
        }
        return successor;
    }

}
