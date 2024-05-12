public class BSTNode {
    BSTNode left;
    BSTNode right;
    Object data;

    public BSTNode(Object data) {
        this.data = data;
        left = right = null; 
    }

    @Override
    public String toString() {
        return data.toString();
    }


    

}
