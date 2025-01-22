import java.util.ArrayList;
import java.util.List;

public class AVLTree<T extends Comparable<T>> {
    int size = 0;
    private AVLNode<T> root;

    public void insert(T data) {
        if (root == null) {
            root = new AVLNode<T>(data);
        } else {
            AVLNode<T> rootTemp = root;
            insert(data, root);
            root = rebalance(rootTemp);
        }
        size++;
    }

    @SuppressWarnings("unchecked")
    public AVLNode<T> delete(String title) {
        AVLNode<Movie> curr = (AVLNode<Movie>)root;
        AVLNode<Movie> parent = (AVLNode<Movie>)root;
        boolean isLeftChild = false;

        if (root == null) {
            return null;
        }
        while (curr != null && !curr.data.getTitle().equals(title)) {
            parent = curr;
            if (title.compareTo(curr.data.getTitle()) < 0) {
                curr = curr.left;
                isLeftChild = true;
            } else {
                curr = curr.right;
                isLeftChild = false;
            }
        }
        if (curr == null) {
            return null;
        }
        if (curr.isLeaf()) {
            if (curr == root) {
                root = null;
            } else {
                if (isLeftChild) {
                    parent.left = null;
                } else {
                    parent.right = null;
                }
            }
        }

        else if (curr.hasLeft() && !curr.hasRight()) {
            if (curr == root) {
                root = (AVLNode<T>) curr.left;
            } else if (isLeftChild) {
                parent.left = curr.left;
            } else {
                parent.right = curr.left;
            }
        }

        else if (!curr.hasLeft() && curr.hasRight()) {
            if (curr == root) {
                root = (AVLNode<T>) curr.left;
            } else if (isLeftChild) {
                parent.left = curr.right;
            } else {
                parent.right = curr.right;
            }
        } else {
            AVLNode<T> succ = getSuccesor((AVLNode<T>) curr);
            if (curr == root) {
                root = succ;
            } else if (isLeftChild) {
                parent.left = (AVLNode<Movie>) succ;
            } else {
                parent.right = (AVLNode<Movie>) succ;
            }
            succ.left = (AVLNode<T>) curr.left;
        }
        rebalance((AVLNode<T>) parent);
        size--;
        return null;
    }

    public T search(String title){
        return search(root, title);
    }

    int getHeight() {
        return getHeight(root);
    }

    void printTree() {
        printTree(root);
    }

    public List<T> getAllValues() {
        List<T> movies = new ArrayList<>();
        collectValues(root, movies);
        return movies;
    }

    public String toString(){
        String s = "";
        List<T> movies = new ArrayList<>();
        collectValues(root, movies);
        for(T movie : movies){
            s += movie.toString() + "\n";
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    public Movie getHighestRate(){
        List<Movie> movies = (List<Movie>)getAllValues();
        double max = 0;
        Movie temp = movies.get(0);
        for(Movie movie : movies){
            if(movie.getRate() > max){
                max = movie.getRate();
                temp = movie;
            }
        }
        return temp;
    }

    @SuppressWarnings("unchecked")
    public Movie getLowestRate(){
        List<Movie> movies = (List<Movie>)getAllValues();
        double min = 10;
        Movie temp = movies.get(0);
        for(Movie movie : movies){
            if(movie.getRate() < min){
                min = movie.getRate();
                temp = movie;
            }
        }
        return temp;
    }

    // <-------- helper methods -------->

    private void insert(T data, AVLNode<T> node) {
        assert node != null;
        if (data.compareTo(node.data) < 0) {
            if (node.hasLeft()) {
                AVLNode<T> leftNode = node.left;
                insert(data, leftNode);
                node.left = rebalance(leftNode);
            } else {
                node.left = new AVLNode<T>(data);
            }
        } else {
            if (node.hasRight()) {
                AVLNode<T> rightNode = node.right;
                insert(data, rightNode);
                node.right = rebalance(rightNode);
            } else {
                node.right = new AVLNode<T>(data);
            }
        }
    }

    private T search(AVLNode<T> node, String title) {
        if (node == null) {
            return null;
        }
        if (((Movie)node.data).getTitle().equalsIgnoreCase(title)) {
            return node.data; 
        }
        T result = search(node.left, title); 
        if (result != null) {
            return result;
        }
        return search(node.right, title); 
    }

    private void printTree(AVLNode<T> node) {
        if (node != null) {
            printTree(node.left);
            System.out.print(node.data + " ");
            printTree(node.right);
        }
    }

    private int getHeight(AVLNode<T> node) {
        if (node == null) {
            return -1;
        }
        return Math.max(getHeight(node.left), getHeight(node.right)) + 1;
    }

    private int getHeightDifferance(AVLNode<T> node) {
        if (node == null) {
            return 0;
        }
        return getHeight(node.left) - getHeight(node.right);
    }

    private AVLNode<T> rotateLeft(AVLNode<T> N) {
        AVLNode<T> C = N.right;
        N.right = C.left;
        C.left = N;
        System.out.println(N.data + " Rotated Left");
        return C;
    }

    private AVLNode<T> rotateRight(AVLNode<T> N) {
        AVLNode<T> C = N.left;
        N.left = C.right;
        C.right = N;
        System.out.println(N.data + " Rotated Right");
        return C;
    }

    private AVLNode<T> rotateRightLeft(AVLNode<T> N) {
        AVLNode<T> C = N.right;
        N.right = rotateRight(C);
        return rotateLeft(N);
    }

    private AVLNode<T> rotateLeftRight(AVLNode<T> N) {
        AVLNode<T> C = N.left;
        N.left = rotateLeft(C);
        return rotateRight(N);
    }

    private AVLNode<T> rebalance(AVLNode<T> N) {
        int diff = getHeightDifferance(N);
        if (diff > 1) {
            if (getHeightDifferance(N.left) > 0) {
                N = rotateRight(N);
            } else {
                N = rotateLeftRight(N);
            }
        } else if (diff < -1) {
            if (getHeightDifferance(N.right) < 0) {
                N = rotateLeft(N);
            } else {
                N = rotateRightLeft(N);
            }
        }
        return N;
    }

    private AVLNode<T> getSuccesor(AVLNode<T> node) {
        AVLNode<T> parentOfSuccessor = node;
        AVLNode<T> successor = node;
        AVLNode<T> current = node.right;
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

    private void collectValues(AVLNode<T> root, List<T> movies) {
        if (root != null) {
            collectValues(root.left, movies);
            movies.add(root.data);
            collectValues(root.right, movies);
        }
    }
}
