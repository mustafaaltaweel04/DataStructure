public class Queue {
    private int front, rear;
    private Object[] queueArray;
    private int maxSize;

    Queue(int size) {
        maxSize = size;
        queueArray = new Object[maxSize];
        front = rear = -1;
    }

    public void enQueue(Object element) {
        if (isFull())
            System.out.println("Queue is full");
        else if (isEmpty()) {
            front++;
            rear++;
            queueArray[rear] = element;
        } else {
            rear = (rear + 1) % maxSize;
            queueArray[rear] = element;
        }
    }

    public Object deQueue() {
        Object element = null;
        if (isEmpty())
            System.out.println("Queue is empty");
        else if (front == rear) {
            element = queueArray[front];
            front = rear = -1;
        } else {
            element = queueArray[front];
            front = (front + 1) % maxSize;
        }
        return element;
    }

    public Object front() {
        if (isEmpty()) {
            System.out.println("Error: cannot return front from empty queue");
            return null;
        }
        return queueArray[front];
    }

    public boolean isEmpty() { // return true if the queue is empty
        return front == -1 && rear == -1;
    }

    public boolean isFull() { // return true if the queue is full
        return ((rear + 1) % maxSize == front);
    }
}
