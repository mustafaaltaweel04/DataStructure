class HNode implements Comparable<Movie> {
    AVLTree<Movie> tree;
    char status;
    int key;

    HNode() {
        status = 'E';

        key = 0;
    }

    HNode(AVLTree<Movie> tree) {
        this.tree = tree;
        status = 'E';
    }

    @Override
    public String toString() {
        return key + " \t|\t" + tree.toString() + "\t |\t" + status;
    }

    @Override
    public int compareTo(Movie o) {
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }

}