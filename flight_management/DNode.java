public class DNode<T extends Comparable<T>> implements Comparable<DNode<T>>{
    Flight flight;
    Queue<Passenger> regQueue; //CHECK_IN
    Queue<Passenger> vipQueue; //CHECK_IN
    Stack<T,Character, Passenger> undoStack; //UNDO
    Stack<T,Character, Passenger> redoStack; //REDO
    LinkedList<Passenger> boardedPassengers; //BOARD
    LinkedList<Passenger> canceledPassengers; //CANCEL
    DNode<T> next;
    DNode<T> prev;

    
    DNode(T data) {
        next = prev = null;
        vipQueue = new Queue<>();
        regQueue = new Queue<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        boardedPassengers = new LinkedList<>();
        canceledPassengers = new LinkedList<>();
        this.flight = (Flight)data;
    }

    public String toString() {
        return flight.toString() + "\nVIP Passengers:\n" + vipQueue.printList() + "\nRegular Passengers:\n" + regQueue.printList();
    }

    @Override
    public int compareTo(DNode<T> o) {
        return this.flight.compareTo(o.flight);
    }
}