public class SNode<F, O, P> {
    F flight;
    O operation;
    P passenger;
    SNode<F, O, P> next;

    public SNode(F flight, O operation, P passenger) {
        this.flight = flight;
        this.operation = operation;
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return flight.toString() + " - " + operation.toString();
    }
}
