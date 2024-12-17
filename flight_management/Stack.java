public class Stack<T extends Comparable<T>,J,K> {
    private SNode<T,J,K> top;
    private int size;

    SNode<T,J,K> peek() {
        return top;
    }

    SNode<T,J,K> pop() {
        if (isEmpty()) {
            System.out.println("Stack is Empty");
            return null;
        }
        SNode<T,J,K> data = top;
        top = top.next;
        size--;
        return data;
    }

    void push(T data,J op, K pass) {
        SNode<T,J,K> newNode = new SNode<>(data,op,pass);
        if (isEmpty()) {
            top = newNode;
        } else {
            newNode.next = top;
            top = newNode;
        }
        size++;
    }

    boolean isEmpty() {
        return top == null;
    }

    int size(){
        return size;
    }
}
