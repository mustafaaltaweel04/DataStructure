import java.io.*;
import java.util.Scanner;

class Test {
    static DLinkedList<Flight> flights = new DLinkedList<>();

    public static void main(String[] args) throws Exception {
        openFlightFile(new File("flights.txt"));
        openPassengerFile(new File("passengers.txt"));
        flights.printList();
    }

    static void openPassengerFile(File f) throws Exception {
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String parts[] = scanner.nextLine().split(",");
            int pID = Integer.parseInt(parts[0]);
            String pName = parts[1];
            int fID = Integer.parseInt(parts[2]);
            char status = parts[3].charAt(0);

            Passenger passenger = new Passenger(pID, pName, fID, status);
            DNode<Flight> node = flights.searchByID(fID);
            if (passenger.isVip()) {
                node.vipQueue.enQueue(passenger);
            } else {
                node.regQueue.enQueue(passenger);
            }
        }
        scanner.close();
    }

    static void openFlightFile(File f) throws Exception {
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            int fId = Integer.parseInt(parts[0]);
            String dest = parts[1];
            char status = parts[2].charAt(0);

            Flight flight = new Flight(fId, dest, status);
            flights.insertSorted(new DNode<Flight>(flight));
        }
        scanner.close();
    }

    int generateID() {
        int max = 0;
        for (int i = 0; i < flights.size(); i++) {
            LinkedList<Passenger> allPassengers = flights.get(i).boardedPassengers
                    .merge(flights.get(i).canceledPassengers);
            for (int j = 0; j < allPassengers.size(); i++) {
                if (max < allPassengers.get(j).getpID()) {
                    max = allPassengers.get(j).getpID();
                }
            }
        }
        return ++max;
    }

}
