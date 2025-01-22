class AVLNode<T extends Comparable<T>> implements Comparable<AVLNode<T>>{
    T data;
    AVLNode<T> right;
    AVLNode<T> left;

    AVLNode(T data) {
        this.data = data;
    }

    boolean hasRight(){
        return right != null;
    }
    boolean hasLeft(){
        return left != null;
    }
    boolean isLeaf(){
        return !hasRight() && !hasLeft();
    }

    public String toString() {
        return data.toString();
    }

    @Override
    public int compareTo(AVLNode<T> o) {
        return data.compareTo(o.data);
    }

}