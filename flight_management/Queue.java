public class Queue<T extends Comparable<T>>{
    
    private QNode<T> rear, front;
    private int size;

    Queue(){
        rear = front = null;
        size = 0;
    }

    void enQueue(T data){
        QNode<T> node = new QNode<T>(data);
        if(size == 0){
            front = rear = node;
            size++;
            return;
        }
        rear.next = node;
        rear = node;
        size++;
    }

    T deQueue(){
        if(size == 0){
            System.out.println("Empty Queue");
            return null;
        }
        T temp = front.data;
        front = front.next;
        size--;
        return temp;
    }

    T peek(){
        return front.data;
    }

    boolean isEmpty(){
        return size == 0;
    }

    void clear(){
        front = null;
    }

    int size(){
        return size;
    }

    String printList(){
        String s = "";
        for(int i = 0;i < size;i++){
            T temp = deQueue();
            s += temp.toString() + "\n";
            enQueue(temp);
        }
        return s;
    }

}
