import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
    Label label;
    int maxPassID, maxFlightID;
    DLinkedList<Flight> flights = new DLinkedList<>();
    static File logFile = new File("log.txt");
    static File passFile = new File("passengers.txt");
    static File flightsFile = new File("flights.txt");

    @Override
    public void start(Stage primaryStage) {
        maxPassID = maxFlightID = 0;

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(600, 400);

        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        MenuItem flightOpenItem = new MenuItem("Flights");
        MenuItem passengerOpenItem = new MenuItem("Passengers");
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().addAll(flightOpenItem, passengerOpenItem, saveItem);

        Menu flightsMenu = new Menu("Flights");
        MenuItem addFlightItem = new MenuItem("Add");
        MenuItem deleteFlightItem = new MenuItem("Delete");
        MenuItem searchFlightItem = new MenuItem("Search");
        MenuItem updateFlightItem = new MenuItem("Update");
        MenuItem printAllFlightsItem = new MenuItem("Print All Flights");
        MenuItem printActiveFlightsItem = new MenuItem("Print All Active Flights");
        MenuItem printInactiveFlightsItem = new MenuItem("Print All Inactive Flights");

        flightsMenu.getItems().addAll(
                addFlightItem,
                deleteFlightItem,
                searchFlightItem,
                updateFlightItem,
                printAllFlightsItem,
                printActiveFlightsItem,
                printInactiveFlightsItem);

        Menu passengersMenu = new Menu("Passengers");
        MenuItem addPassenger = new MenuItem("Add");
        MenuItem deletePassenger = new MenuItem("Delete");
        MenuItem searchPassenger = new MenuItem("Search");
        MenuItem updatePassenger = new MenuItem("Update");
        MenuItem printPassenger = new MenuItem("Print All");
        passengersMenu.getItems().addAll(addPassenger, deletePassenger, searchPassenger, updatePassenger,
                printPassenger);

        Menu operationsMenu = new Menu("Operations");
        MenuItem checkInOperation = new MenuItem("Check-In");
        MenuItem boardOperation = new MenuItem("Board");
        MenuItem cancelOperation = new MenuItem("Cancel");
        MenuItem undoOperation = new MenuItem("Undo");
        MenuItem redoOperation = new MenuItem("Redo");

        operationsMenu.getItems().addAll(
                checkInOperation,
                boardOperation,
                cancelOperation,
                undoOperation,
                redoOperation);

        Menu statisticsMenu = new Menu("Statistics");
        statisticsMenu.getItems().add(new MenuItem("Show All"));

        menuBar.getMenus().addAll(fileMenu, flightsMenu, passengersMenu, operationsMenu, statisticsMenu);

        borderPane.setTop(menuBar);

        VBox vbox = new VBox();
        vbox.setAlignment(javafx.geometry.Pos.CENTER);
        vbox.setPrefSize(100, 200);

        label = new Label("Label");

        vbox.getChildren().add(label);

        borderPane.setCenter(vbox);

        flightOpenItem.setOnAction(e -> {
            try {
                openFlightFile(primaryStage);
                label.setText("Flights file opened succesfully");
            } catch (Exception e1) {
            }
        });

        passengerOpenItem.setOnAction(e -> {
            try {
                openPassengerFile(primaryStage);
                label.setText("Passengers file opened succesfully");
            } catch (Exception e1) {

            }
        });

        addPassenger.setOnAction(e -> {
            addPassengerLayout();
        });

        deletePassenger.setOnAction(e -> {
            deletePassengerLayout();
        });

        searchPassenger.setOnAction(e -> {
            searchPassengerLayout();
        });

        updatePassenger.setOnAction(e -> {
            updatePassengerLayout();
        });

        printPassenger.setOnAction(e -> {
            printPassengerLayout();
        });

        addFlightItem.setOnAction(e -> {
            addFlightLayout();
        });

        deleteFlightItem.setOnAction(e -> {
            deleteFlightLayout();
        });

        updateFlightItem.setOnAction(e -> {
            updateFlightLayout();
        });

        searchFlightItem.setOnAction(e -> {
            searchFlightLayout();
        });

        printAllFlightsItem.setOnAction(e -> {
            printFlightLayout('D');
        });

        printActiveFlightsItem.setOnAction(e -> {
            printFlightLayout('A');
        });

        printInactiveFlightsItem.setOnAction(e -> {
            printFlightLayout('I');
        });

        checkInOperation.setOnAction(e -> {
            checkInPassengerLayout();
        });

        boardOperation.setOnAction(e -> {
            boardPassengerLayout();
        });

        cancelOperation.setOnAction(e -> {
            cancelPassengerLayout();
        });

        undoOperation.setOnAction(e -> {
            undoLayout();
        });

        statisticsMenu.setOnAction(e -> {
            statisticsLayout();
        });

        Scene scene = new Scene(borderPane, 600, 400);
        primaryStage.setTitle("JavaFX Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    void openPassengerFile(Stage primaryStage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(primaryStage);
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String parts[] = scanner.nextLine().split(",");
            int pID = Integer.parseInt(parts[0]);
            String pName = parts[1];
            int fID = Integer.parseInt(parts[2]);
            char status = parts[3].charAt(0);

            if (pID > maxPassID) { // always get the max id when open passengers file
                maxPassID = pID;
            }
            DNode<Flight> node = flights.searchByID(fID);
            Passenger passenger = new Passenger(pID, pName, fID, status);
            if (node != null) {
                if (passenger.isVip()) {
                    node.vipQueue.enQueue(passenger);
                } else {
                    node.regQueue.enQueue(passenger);
                }
            }
        }
        scanner.close();
    }

    void openFlightFile(Stage primaryStage) throws Exception {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(primaryStage);
        flights = new DLinkedList<>();
        Scanner scanner = new Scanner(f);
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(",");
            int fId = Integer.parseInt(parts[0]);
            String dest = parts[1];
            char status = parts[2].charAt(0);
            if (fId > maxFlightID) { // always get the max id when open flights file
                maxFlightID = fId;
            }
            Flight flight = new Flight(fId, dest, status);
            flights.insertSorted(new DNode<Flight>(flight));
        }
        scanner.close();
    }

    public void addPassengerLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);

        HBox nameBox = new HBox(5);
        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameBox.getChildren().addAll(nameLabel, nameField);

        HBox flightBox = new HBox(5);
        flightBox.setAlignment(Pos.CENTER);
        Label flightLabel = new Label("Flight");
        ComboBox<Flight> flightComboBox = new ComboBox<>();
        for (int i = 0; i < flights.size(); i++) {
            flightComboBox.getItems().add(flights.get(i).flight);
        }
        flightComboBox.setPrefWidth(150);
        flightBox.getChildren().addAll(flightLabel, flightComboBox);

        HBox radioBox = new HBox(5);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton regularButton = new RadioButton("Regular");
        RadioButton vipButton = new RadioButton("VIP");
        ToggleGroup group = new ToggleGroup();
        regularButton.setToggleGroup(group);
        vipButton.setToggleGroup(group);
        radioBox.getChildren().addAll(regularButton, vipButton);

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Check In");
        buttonBox.getChildren().add(submitButton);

        root.getChildren().addAll(nameBox, flightBox, radioBox, buttonBox);

        submitButton.setOnAction(e -> {
            Flight flight = flightComboBox.getValue();
            int flightId = flight.getfID();
            int passId = nextPassId(); // handle passenger id
            String passName = nameField.getText();
            char isVip = ((RadioButton) group.getSelectedToggle()).getText().equals("VIP") ? 'V' : 'R';

            Passenger passenger = new Passenger(passId, passName, flightId, isVip);
            DNode<Flight> flightNode = flights.searchByID(flightId);
            if (isVip == 'V') {
                flightNode.vipQueue.enQueue(passenger);
                label.setText("{" + passId + ", " + passName + "} has been added to VIP passengers on plane "
                        + flight.getfID());
                passFileWrite(passenger, 'A');
                logFileWrite(passenger, "Check-IN");

            } else {
                flightNode.regQueue.enQueue(passenger);
                label.setText("{" + passId + ", " + passName + "} has been added to Regular passengers on plane "
                        + flight.getfID());
                passFileWrite(passenger, 'A');
                logFileWrite(passenger, "Check-IN");
            }
        });

        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Add Passenger");
        stage.show();
    }

    public void deletePassengerLayout() {
        ComboBox<Flight> flightComboBox = new ComboBox<>();
        for (int i = 0; i < flights.size(); i++) {
            flightComboBox.getItems().addAll(flights.get(i).flight);
        }

        ComboBox<Passenger> passengerComboBox = new ComboBox<>();
        flightComboBox.setOnAction(e -> {
            passengerComboBox.getItems().clear();
            DNode<Flight> flightNode = flights.searchByID(flightComboBox.getValue().getfID());
            Queue<Passenger> passRegQueue = flightNode.regQueue;
            Queue<Passenger> passVipQueue = flightNode.vipQueue;

            for (int i = 0; i < passRegQueue.size(); i++) {
                Passenger passenger = passRegQueue.deQueue();
                passengerComboBox.getItems().add(passenger);
                passRegQueue.enQueue(passenger);
            }

            for (int i = 0; i < passVipQueue.size(); i++) {
                Passenger passenger = passVipQueue.deQueue();
                passengerComboBox.getItems().add(passenger);
                passVipQueue.enQueue(passenger);
            }
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            Passenger selectedPassenger = passengerComboBox.getSelectionModel().getSelectedItem();
            if (selectedPassenger != null) {
                passengerComboBox.getItems().remove(selectedPassenger);
                if (selectedPassenger.getStatus() == 'V') {
                    DNode<Flight> flightNode = flights.searchByID(flightComboBox.getValue().getfID());
                    Queue<Passenger> passVipQueue = flightNode.vipQueue;
                    passVipQueue = removePassQueue(passVipQueue, selectedPassenger);
                    passFileWrite(selectedPassenger, 'D');
                } else if (selectedPassenger.getStatus() == 'R') {
                    DNode<Flight> flightNode = flights.searchByID(flightComboBox.getValue().getfID());
                    Queue<Passenger> passRegQueue = flightNode.regQueue;
                    passRegQueue = removePassQueue(passRegQueue, selectedPassenger);
                    passFileWrite(selectedPassenger, 'D');
                }
                System.out.println("Deleted: " + selectedPassenger);
            } else {
                System.out.println("No passenger selected.");
            }
        });

        VBox layout = new VBox(10, flightComboBox, passengerComboBox, deleteButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Remove Passenger");
        stage.show();
    }

    public void searchPassengerLayout() {
        TextField textField = new TextField();

        Button button = new Button("Submit");
        button.setOnAction(e -> {
            int passId = Integer.parseInt(textField.getText());
            System.out.println("You entered: " + passId);
            Passenger passenger = searchPassengerByID(passId);
            if (passenger != null)
                System.out.println("Passenger Found: " + passenger.toString());
        });

        VBox layout = new VBox(10, textField, button);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Search Passenger");
        stage.show();
    }

    public void updatePassengerLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);

        ComboBox<Flight> flightComboBox = new ComboBox<>();
        for (int i = 0; i < flights.size(); i++) {
            flightComboBox.getItems().add(flights.get(i).flight);
        }

        ComboBox<Passenger> passengerComboBox = new ComboBox<>();
        flightComboBox.setOnAction(e -> {
            passengerComboBox.getItems().clear();
            DNode<Flight> flightNode = flights.searchByID(flightComboBox.getValue().getfID());
            Queue<Passenger> passRegQueue = flightNode.regQueue;
            Queue<Passenger> passVipQueue = flightNode.vipQueue;

            for (int i = 0; i < passRegQueue.size(); i++) {
                Passenger passenger = passRegQueue.deQueue();
                passengerComboBox.getItems().add(passenger);
                passRegQueue.enQueue(passenger);
            }

            for (int i = 0; i < passVipQueue.size(); i++) {
                Passenger passenger = passVipQueue.deQueue();
                passengerComboBox.getItems().add(passenger);
                passVipQueue.enQueue(passenger);
            }
        });
        HBox nameBox = new HBox(5);
        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameBox.getChildren().addAll(nameLabel, nameField);

        HBox flightBox = new HBox(5);
        flightBox.setAlignment(Pos.CENTER);
        Label flightLabel = new Label("Flight");

        ComboBox<Flight> newFlightComboBox = new ComboBox<>();
        for (int i = 0; i < flights.size(); i++) {
            newFlightComboBox.getItems().add(flights.get(i).flight);
        }

        flightComboBox.setPrefWidth(150);
        flightBox.getChildren().addAll(flightLabel, newFlightComboBox);

        HBox radioBox = new HBox(5);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton regularButton = new RadioButton("Regular");
        RadioButton vipButton = new RadioButton("VIP");
        ToggleGroup group = new ToggleGroup();
        regularButton.setToggleGroup(group);
        vipButton.setToggleGroup(group);
        radioBox.getChildren().addAll(regularButton, vipButton);

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Update");
        buttonBox.getChildren().add(submitButton);

        passengerComboBox.setOnAction(e -> {
            Passenger currPassenger = passengerComboBox.getValue();
            nameField.setText(currPassenger.getpName());
            newFlightComboBox.setValue(flightComboBox.getValue());
            if (currPassenger.getStatus() == 'V') {
                vipButton.setSelected(true);
            } else {
                regularButton.setSelected(true);
            }
        });

        submitButton.setOnAction(e -> {
            try {
                Passenger oldPassenger = passengerComboBox.getValue();
                char oldStatus = oldPassenger.getStatus();
                char status = vipButton.isSelected() ? 'V' : 'R';
                Passenger newPassenger = new Passenger(oldPassenger.getpID(), nameField.getText(),
                        newFlightComboBox.getValue().getfID(), status);
                passFileWriteUpdate(newPassenger, oldPassenger);
                oldPassenger.setStatus(newPassenger.getStatus());
                oldPassenger.setpName(newPassenger.getpName());
                oldPassenger.setfID(newPassenger.getfID());

                DNode<Flight> oldFlight = flights.searchByID(flightComboBox.getValue().getfID());
                Queue<Passenger> oldQueue;
                if (oldStatus == 'V') {
                    oldQueue = oldFlight.vipQueue;
                } else {
                    oldQueue = oldFlight.regQueue;
                }
                removePassQueue(oldQueue, oldPassenger);
                DNode<Flight> newFlight = flights.searchByID(newFlightComboBox.getValue().getfID());
                Queue<Passenger> newQueue;
                if (oldPassenger.isVip()) {
                    newQueue = newFlight.vipQueue;
                    newQueue.enQueue(oldPassenger);
                } else {
                    newQueue = newFlight.regQueue;
                    newQueue.enQueue(oldPassenger);
                }
                openFlightFile(stage);
                openPassengerFile(stage);
            } catch (Exception er) {

            }
        });

        root.getChildren().addAll(flightComboBox, passengerComboBox, nameBox, flightBox, radioBox, buttonBox);
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Update Passenger");
        stage.show();
    }

    @SuppressWarnings("unchecked")
    public void printPassengerLayout() {
        Stage stage = new Stage();
        ObservableList<Passenger> data = readFileToObservableList(passFile);

        TableView<Passenger> tableView = new TableView<>();
        TableColumn<Passenger, Integer> pidColumn = new TableColumn<>("PID");
        pidColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getpID()).asObject());

        TableColumn<Passenger, String> pnameColumn = new TableColumn<>("Name");
        pnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getpName()));

        TableColumn<Passenger, Integer> fidColumn = new TableColumn<>("FID");
        fidColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getfID()).asObject());

        TableColumn<Passenger, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStatus())));

        tableView.setItems(data);

        tableView.getColumns().addAll(pidColumn, pnameColumn, fidColumn, statusColumn);

        Scene scene = new Scene(tableView, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Print All Passengers");
        stage.show();
    }

    public void addFlightLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);

        HBox destBox = new HBox(5);
        destBox.setAlignment(Pos.CENTER);
        Label destLabel = new Label("Destination");
        TextField destField = new TextField();
        destBox.getChildren().addAll(destLabel, destField);

        HBox radioBox = new HBox(5);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton activeButton = new RadioButton("Active");
        RadioButton inActiveButton = new RadioButton("Inactive");
        ToggleGroup group = new ToggleGroup();
        activeButton.setToggleGroup(group);
        inActiveButton.setToggleGroup(group);
        radioBox.getChildren().addAll(activeButton, inActiveButton);

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Submit");
        buttonBox.getChildren().add(submitButton);

        root.getChildren().addAll(destBox, radioBox, buttonBox);

        submitButton.setOnAction(e -> {
            boolean isActive = activeButton.isSelected();
            String dest = destField.getText();
            char status;
            if (isActive) {
                status = 'A';
            } else {
                status = 'I';
            }
            Flight newFlight = new Flight(nextFlightId(), dest, status);
            flights.insertSorted(new DNode<Flight>(newFlight));
            flightFileWrite(newFlight, 'A');
        });
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Add Passenger");
        stage.show();
    }

    public void deleteFlightLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);

        ComboBox<Flight> flightList = new ComboBox<Flight>();
        for (int i = 0; i < flights.size(); i++) {
            flightList.getItems().add(flights.get(i).flight);
        }

        Button submitButton = new Button("Delete");
        submitButton.setOnAction(e -> {
            Flight flight = flightList.getValue();
            int index = flights.searchIndex(flight);
            flights.delete(index);
            flightFileWrite(flight, 'D');
            deleteAllpassengersByFID(flight.getfID());
            try {
                openPassengerFile(stage);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            System.out.println("Flight " + flight.getfID() + " deleted");
        });

        root.getChildren().addAll(flightList, submitButton);

        stage.setScene(new Scene(root, 600, 400));
        stage.show();

    }

    public void updateFlightLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);
        ComboBox<Flight> flightList = new ComboBox<Flight>();
        for (int i = 0; i < flights.size(); i++) {
            flightList.getItems().add(flights.get(i).flight);
        }
        HBox destBox = new HBox(5);
        destBox.setAlignment(Pos.CENTER);
        Label destLabel = new Label("Destination");
        TextField destField = new TextField();
        destBox.getChildren().addAll(destLabel, destField);

        HBox radioBox = new HBox(5);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton activeButton = new RadioButton("Active");
        RadioButton inActiveButton = new RadioButton("Inactive");
        ToggleGroup group = new ToggleGroup();
        activeButton.setToggleGroup(group);
        inActiveButton.setToggleGroup(group);
        radioBox.getChildren().addAll(activeButton, inActiveButton);

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button submitButton = new Button("Update");
        buttonBox.getChildren().add(submitButton);

        root.getChildren().addAll(flightList, destBox, radioBox, buttonBox);

        flightList.setOnAction(e -> {
            Flight flight = flightList.getValue();
            destField.setText(flight.getDest());
            if (flight.getStatus() == 'A') {
                activeButton.setSelected(true);
            } else {
                inActiveButton.setSelected(true);
            }
        });
        submitButton.setOnAction(e -> {
            Flight flight = flightList.getValue();
            boolean isActive = activeButton.isSelected();
            String dest = destField.getText();
            char status;
            if (isActive) {
                status = 'A';
            } else {
                status = 'I';
            }
            flight.setDest(dest);
            flight.setStatus(status);
            flightFileWrite(flight, 'D');
            flightFileWrite(flight, 'A');
            System.out.println("Flight " + flight.getfID() + " updated");
        });

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void searchFlightLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);
        Label searchLabel = new Label("Enter Flight ID:");
        TextField searchField = new TextField();

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(searchLabel, searchField);

        Button submitButton = new Button("Search");
        submitButton.setOnAction(e -> {
            try {
                int fID = Integer.parseInt(searchField.getText());
                Flight flight = flights.searchByID(fID).flight;
                if (flight != null) {
                    System.out.println("Flight Found: " + flight.toString());
                } else {
                    System.out.println("FLIGHT NOT FOUND");
                }
            } catch (Exception e1) {
                System.out.println("FLIGHT NOT FOUND");
            }
        });
        root.getChildren().addAll(buttonBox, submitButton);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @SuppressWarnings("unchecked")
    public void printFlightLayout(char op) {
        ObservableList<Flight> flightList = flightsToObservableList(op);
        TableView<Flight> flightTableView = new TableView<>();

        TableColumn<Flight, Integer> fidColumn = new TableColumn<>("Flight ID");
        fidColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getfID()).asObject());

        TableColumn<Flight, String> desColumn = new TableColumn<>("Destination");
        desColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDest()));

        TableColumn<Flight, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getStatus())));

        flightTableView.getColumns().addAll(fidColumn, desColumn, statusColumn);
        flightTableView.setItems(flightList);

        Stage stage = new Stage();
        stage.setScene(new Scene(flightTableView, 600, 400));
        stage.show();
    }

    public void checkInPassengerLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);

        HBox passBox = new HBox(10);
        Label passLabel = new Label("Enter Passenger ID:");
        TextField passField = new TextField();
        passBox.getChildren().addAll(passLabel, passField);
        passBox.setAlignment(Pos.CENTER);

        ComboBox<Flight> flightList = new ComboBox<Flight>();
        for (int i = 0; i < flights.size(); i++) {
            flightList.getItems().add(flights.get(i).flight);
        }

        HBox buttonBox = new HBox(5);
        buttonBox.setAlignment(Pos.CENTER);
        Button checkInButton = new Button("Check-In");
        buttonBox.getChildren().add(checkInButton);

        checkInButton.setOnAction(e -> {
            try {
                int pID = Integer.parseInt(passField.getText());
                Passenger passenger = searchPassengerByID(pID);
                Passenger tempPassenger = passenger.clone();
                Flight flight = flightList.getValue();
                if (checkInPassenger(passenger, flight)) {
                    logFileWrite(passenger, "Check-IN");
                    passFileWriteUpdate(passenger, tempPassenger);
                    System.out.println("Passenger " + passenger.getpName() + " checked-in successfully");
                } else {
                    System.out.println("Passenger not found or already checked-in");
                }
            } catch (Exception e1) {
                System.out.println("Invalid passenger ID");
            }
        });
        root.getChildren().addAll(passBox, flightList, buttonBox);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void boardPassengerLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);
        Label boardLabel = new Label("Enter Passenger ID:");
        TextField boardField = new TextField();
        HBox boardBox = new HBox(5);
        boardBox.setAlignment(Pos.CENTER);
        boardBox.getChildren().addAll(boardLabel, boardField);
        Button boardButton = new Button("Board");
        boardButton.setOnAction(e -> {
            try {
                int pID = Integer.parseInt(boardField.getText());
                Passenger passenger = searchPassengerByID(pID);
                Flight flight = flights.searchByID(passenger.getfID()).flight;
                if (boardPassenger(passenger, flight)) {
                    System.out.println(passenger.toString());
                    logFileWrite(passenger, "Board");
                    passFileWrite(passenger, 'D');
                    System.out.println("Passenger " + passenger.getpName() + " boarded successfully");
                } else {
                    System.out.println("Passenger not found or already boarded");
                }

            } catch (Exception e1) {
                System.out.println("Invalid flight ID");
            }
        });

        root.getChildren().addAll(boardBox, boardButton);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void cancelPassengerLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);
        ComboBox<Flight> flightComboBox = new ComboBox<Flight>();
        for (int i = 0; i < flights.size(); i++) {
            flightComboBox.getItems().add(flights.get(i).flight);
        }
        ComboBox<Passenger> passengerComboBox = new ComboBox<Passenger>();
        flightComboBox.setOnAction(e -> {
            passengerComboBox.getItems().clear();
            DNode<Flight> flightNode = flights.searchByID(flightComboBox.getValue().getfID());
            for (int i = 0; i < flightNode.boardedPassengers.size(); i++) {
                passengerComboBox.getItems().add(flightNode.boardedPassengers.get(i));
            }
        });

        Button boardButton = new Button("Cancel");
        boardButton.setOnAction(e -> {
            try {
                Passenger passenger = passengerComboBox.getValue();
                Flight flight = flights.searchByID(passenger.getfID()).flight;
                if (cancelPassenger(passenger, flight)) {
                    System.out.println(passenger.toString());
                    logFileWrite(passenger, "Cancel");
                    passFileWrite(passenger, 'A');
                    System.out.println("Passenger " + passenger.getpName() + " canceled successfully");
                } else {
                    System.out.println("Passenger not found or not boarded");
                }

            } catch (Exception e1) {
                System.out.println("Invalid flight ID");
            }
        });

        root.getChildren().addAll(flightComboBox, passengerComboBox, boardButton);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void undoLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(600, 400);
        Label boardLabel = new Label("Enter Flight ID:");
        TextField boardField = new TextField();
        HBox boardBox = new HBox(5);
        boardBox.setAlignment(Pos.CENTER);
        boardBox.getChildren().addAll(boardLabel, boardField);
        Button boardButton = new Button("Undo");
        boardButton.setOnAction(e -> {
            DNode<Flight> flightNode = flights.searchByID(Integer.parseInt(boardField.getText()));
            handleUndo(flightNode);
        });

        root.getChildren().addAll(boardBox, boardButton);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public void statisticsLayout() {
        Stage stage = new Stage();
        DNode<Flight> node = flights.get(0);

        Label flightName = new Label("Flight " + node.flight.getfID());
        flightName.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        Label label1 = new Label("VIP Check-In: " + flights.numOfVipCheckIn(node));
        Label label2 = new Label("Regular Check-In: " + flights.numOfRegCheckIn(node));
        Label label3 = new Label("VIP Boarded: " + flights.numOfVipBoarded(node));
        Label label4 = new Label("Regular Boarded: " + flights.numOfRegBoarded(node));
        Label label5 = new Label("VIP Canceled: " + flights.numOfVipCanceled(node));
        Label label6 = new Label("Regular Canceled: " + flights.numOfRegCanceled(node));

        Button prevButton = new Button("Prev");
        Button nextButton = new Button("Next");

        HBox buttonBox = new HBox(10, prevButton, nextButton);

        VBox vbox = new VBox(15, flightName, label1, label2, label3, label4, label5, label6, buttonBox);

        Scene scene = new Scene(vbox, 600, 400);

        int[] currIndex = new int[1];
        nextButton.setOnAction(e -> {
            if (currIndex[0] > flights.size() - 1) {
                currIndex[0] = 0;
            } else {
                flightName.setText("Flight " + flights.get(++currIndex[0]).flight.getfID());
                label1.setText("VIP Check-In: " + flights.numOfVipCheckIn(flights.get(currIndex[0])));
                label2.setText("Regular Check-In: " + flights.numOfRegCheckIn(flights.get(currIndex[0])));
                label3.setText("VIP Boarded: " + flights.numOfVipBoarded(flights.get(currIndex[0])));
                label4.setText("Regular Boarded: " + flights.numOfRegBoarded(flights.get(currIndex[0])));
                label5.setText("VIP Canceled: " + flights.numOfVipCanceled(flights.get(currIndex[0])));
                label6.setText("Regular Canceled: " + flights.numOfRegCanceled(flights.get(currIndex[0])));
            }
        });
        prevButton.setOnAction(e -> {
            if (currIndex[0] < 0) {
                currIndex[0] = flights.size() - 1;
            } else {
                flightName.setText("Flight " + flights.get(--currIndex[0]).flight.getfID());
                label1.setText("VIP Check-In: " + flights.numOfVipCheckIn(flights.get(currIndex[0])));
                label2.setText("Regular Check-In: " + flights.numOfRegCheckIn(flights.get(currIndex[0])));
                label3.setText("VIP Boarded: " + flights.numOfVipBoarded(flights.get(currIndex[0])));
                label4.setText("Regular Boarded: " + flights.numOfRegBoarded(flights.get(currIndex[0])));
                label5.setText("VIP Canceled: " + flights.numOfVipCanceled(flights.get(currIndex[0])));
                label6.setText("Regular Canceled: " + flights.numOfRegCanceled(flights.get(currIndex[0])));
            }
        });

        stage.setScene(scene);
        stage.setTitle("Statistics");
        stage.show();
    }
    // < ------------ Helper Methods --------------->

    int nextPassId() {
        return ++maxPassID;
    }

    int nextFlightId() {
        return ++maxFlightID;
    }

    void passFileWrite(Passenger passenger, char op) {
        if (op == 'A') {
            try {
                FileWriter fileWriter = new FileWriter(passFile, true);
                fileWriter.append(passenger.toString() + "\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (op == 'D') {
            ArrayList<String> lines = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(passFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals(passenger.toString())) {
                        lines.add(line);
                    }
                }
                FileWriter fileWriter = new FileWriter(passFile);
                for (int i = 0; i < lines.size(); i++) {
                    fileWriter.write(lines.get(i) + "\n");
                }
                reader.close();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void deleteAllpassengersByFID(int fId) {
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(passFile));
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(fId + ",")) {
                    lines.add(line);
                }
            }
            FileWriter fileWriter = new FileWriter(passFile);
            for (int i = 0; i < lines.size(); i++) {
                fileWriter.write(lines.get(i) + "\n");
            }
            reader.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void flightFileWrite(Flight flight, char op) {
        if (op == 'A') {
            try {
                FileWriter fileWriter = new FileWriter(flightsFile, true);
                fileWriter.append(flight.toString() + "\n");
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (op == 'D') {
            ArrayList<String> lines = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(flightsFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.contains(flight.getfID() + ",")) {
                        lines.add(line);
                    }
                }
                FileWriter fileWriter = new FileWriter(flightsFile);
                for (int i = 0; i < lines.size(); i++) {
                    fileWriter.write(lines.get(i) + "\n");
                }
                reader.close();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void passFileWriteUpdate(Passenger newPassenger, Passenger oldPassenger) {
        try {
            ArrayList<String> lines = new ArrayList<>();
            Scanner scanner = new Scanner(passFile);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                if (!line.equals(oldPassenger.toString())) {
                    lines.add(line);
                } else if (line.equals(oldPassenger.toString())) {
                    lines.add(newPassenger.toString());
                }
            }
            FileWriter fileWriter = new FileWriter(passFile);
            for (int i = 0; i < lines.size(); i++) {
                fileWriter.write(lines.get(i) + "\n");
            }
            scanner.close();
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void logFileWrite(Passenger passenger, String operation) {
        String name = passenger.getpName();
        String flight = "Flight " + passenger.getfID();
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            fileWriter.append(LocalDateTime.now() + " | " + operation + " | " + name + " | " + passenger.getfID()
                    + " | " + operation + " " + name + " " + flight + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Passenger> readFileToObservableList(File file) {
        ObservableList<Passenger> observableList = FXCollections.observableArrayList();
        try (Scanner in = new Scanner(file)) {
            in.nextLine();
            while (in.hasNextLine()) {
                String parts[] = in.nextLine().split(",");
                int pID = Integer.parseInt(parts[0]);
                String pName = parts[1];
                int fID = Integer.parseInt(parts[2]);
                char status = parts[3].charAt(0);
                Passenger passenger = new Passenger(pID, pName, fID, status);
                observableList.add(passenger);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return observableList;
    }

    ObservableList<Flight> flightsToObservableList(char op) {
        ObservableList<Flight> observableList = FXCollections.observableArrayList();
        try (Scanner in = new Scanner(flightsFile)) {
            in.nextLine();
            while (in.hasNextLine()) {
                String parts[] = in.nextLine().split(",");
                int fID = Integer.parseInt(parts[0]);
                String dest = parts[1];
                char status = parts[2].charAt(0);
                Flight flight = new Flight(fID, dest, status);
                if (op == 'A') {
                    if (status == 'A') {
                        observableList.add(flight);
                    }
                } else if (op == 'I') {
                    if (status == 'I') {
                        observableList.add(flight);
                    }
                } else {
                    observableList.add(flight);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return observableList;
    }

    Passenger searchPassengerByID(int passengerID) {
        for (int i = 0; i < flights.size(); i++) {
            DNode<Flight> flight = flights.get(i);
            Queue<Passenger> regQueue = flight.regQueue;
            for (int j = 0; j < regQueue.size(); j++) {
                Passenger passenger = regQueue.deQueue();
                if (passenger.getpID() == passengerID) {
                    regQueue.enQueue(passenger);
                    return passenger;
                }
                regQueue.enQueue(passenger);
            }

            Queue<Passenger> vipQueue = flight.vipQueue;
            for (int j = 0; j < vipQueue.size(); j++) {
                Passenger passenger = vipQueue.deQueue();
                if (passenger.getpID() == passengerID) {
                    vipQueue.enQueue(passenger);
                    return passenger;
                }
                vipQueue.enQueue(passenger);
            }
        }
        System.out.println("NO PASSENGER FOUND BY ID " + passengerID);
        return null;
    }

    Queue<Passenger> removePassQueue(Queue<Passenger> queue, Passenger passenger) {
        Queue<Passenger> queue2 = new Queue<>();
        while (!queue.isEmpty()) {
            Passenger curr = queue.deQueue();
            if (!curr.equals(passenger)) {
                queue2.enQueue(curr);
            }

        }
        return queue2;
    }

    boolean checkInPassenger(Passenger passenger, Flight flight) {
        try {
            if (passenger.getfID() == flight.getfID()) {
                return false;
            }
            DNode<Flight> node = flights.searchByID(flight.getfID());
            passenger.setfID(flight.getfID());
            if (passenger.isVip()) {
                node.vipQueue.enQueue(passenger);
            } else if (!passenger.isVip()) {
                node.regQueue.enQueue(passenger);
            }
            node.undoStack.push(flight, 'C', passenger);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    boolean boardPassenger(Passenger passenger, Flight flight) {
        DNode<Flight> node = flights.searchByID(flight.getfID());
        Queue<Passenger> queue = node.regQueue;
        if (passenger.isVip()) {
            queue = node.vipQueue;
        }
        Queue<Passenger> tempQueue = new Queue<>();
        LinkedList<Passenger> linkedList = node.boardedPassengers;
        boolean found = false;

        while (!queue.isEmpty()) {
            Passenger currentPassenger = queue.deQueue();

            if (!found && currentPassenger.equals(passenger)) {
                if (currentPassenger.isVip()) {
                    linkedList.insertFirst(currentPassenger);
                } else {
                    linkedList.insertLast(currentPassenger);
                }
                node.undoStack.push(flight, 'B', passenger);
                found = true;
            } else {
                tempQueue.enQueue(currentPassenger);
            }
        }
        while (!tempQueue.isEmpty()) {
            queue.enQueue(tempQueue.deQueue());
        }

        if (!found) {
            System.out.println("Passenger not found in the queue.");
        }
        return found;
    }

    boolean cancelPassenger(Passenger passenger, Flight flight) {
        DNode<Flight> node = flights.searchByID(flight.getfID());
        if (node == null) {
            return false;
        }
        LinkedList<Passenger> linkedList = node.boardedPassengers;
        for (int i = 0; i < linkedList.size(); i++) {
            Passenger currentPassenger = linkedList.get(i);
            if (currentPassenger.equals(passenger)) {
                linkedList.delete(i);
                node.undoStack.push(flight, 'R', passenger);
                return true;
            }
        }
        return false;
    }

    void handleUndo(DNode<Flight> flightNode) {
        Stack<Flight, Character, Passenger> undoStack = flightNode.undoStack;
        Stack<Flight, Character, Passenger> redoStack = flightNode.redoStack;

        if (undoStack.isEmpty()) {
            System.out.println("Nothing to undo.");
            return;
        }

        SNode<Flight, Character, Passenger> lastAction = undoStack.pop();
        Flight flight = lastAction.flight;
        char op = lastAction.operation;
        Passenger passenger = lastAction.passenger;

        redoStack.push(flight, op, passenger);

        switch (op) {
            case 'C': // check-in
                if (undoStack.peek().operation == 'C') {
                    checkInPassenger(passenger, undoStack.peek().flight);
                    passFileWrite(passenger, 'D');
                    passFileWrite(passenger, 'A');
                    logFileWrite(passenger, "Undo Check-In");
                } else if (undoStack.peek() == null) {
                    System.out.println("Cant undo first check in");
                    return;
                }
                break;
            case 'B':// boarded
                DNode<Flight> flightNodeData = flights.searchByID(flight.getfID());
                int pid = flightNodeData.boardedPassengers.search(passenger);
                flightNodeData.boardedPassengers.delete(pid);
                if (passenger.isVip()) {
                    flightNodeData.vipQueue.enQueue(passenger);
                } else {
                    flightNodeData.regQueue.enQueue(passenger);
                }
                passFileWrite(passenger, 'A');
                logFileWrite(passenger, "Undo Board");
                break;

            case 'R': // canceled
                flightNodeData = flights.searchByID(flight.getfID());
                pid = flightNode.canceledPassengers.search(passenger);
                flightNodeData.canceledPassengers.delete(pid);
                if (passenger.isVip()) {
                    flightNodeData.boardedPassengers.insertFirst(passenger);
                    ;
                } else {
                    flightNodeData.boardedPassengers.insertLast(passenger);
                }
                passFileWrite(passenger, 'D');
                logFileWrite(passenger, "Undo Cancel");
                break;

            default:
                System.out.println("Invalid operation.");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
