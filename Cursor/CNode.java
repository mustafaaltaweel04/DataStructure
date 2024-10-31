class CNode<T extends Comparable<T>>{
    T data;
    int next;

    CNode(T data, int next){
        this.data = data;
        this.next = next;
    }

    @Override
    public String toString() {
        return data + "";
    }

    
    
}