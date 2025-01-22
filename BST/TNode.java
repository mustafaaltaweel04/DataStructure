class TNode<T extends Comparable<T>> implements Comparable<TNode<T>>{
    T data;
    TNode<T> right;
    TNode<T> left;

    TNode(T data) {
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
    public int compareTo(TNode<T> o) {
        return data.compareTo(o.data);
    }

}