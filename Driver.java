import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
    static File filec;
    File newFile = new File("C:\\Users\\Asus\\OneDrive\\Desktop\\new-data.csv");
    boolean isOk;// this to know if user accepted to update/delete or not
    District current_district; // this to know in what district we should navigate
    Location current_location; // this to know in what location we should navigate
    BST districts = new BST();// this for all districts that been read
    BST locations = new BST();// this all locations for a certian district
    BST currentDates = new BST();// this BST is for all dates in a certian location

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project II");
        MenuBar menuBar = new MenuBar();
        Menu file = new Menu("File");
        Menu districts_menu = new Menu("Districts");
        Menu locations_menu = new Menu("Locations");
        Menu martyrs_menu = new Menu("Martyrs");

        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");

        MenuItem insert_district = new MenuItem("Insert");
        MenuItem update_district = new MenuItem("Update");
        MenuItem remove_district = new MenuItem("Remove");
        MenuItem navigate_district = new MenuItem("Navigate"); // inorder

        MenuItem insert_location = new MenuItem("Insert");
        MenuItem update_location = new MenuItem("Update");
        MenuItem remove_location = new MenuItem("Remove");
        MenuItem navigate_location = new MenuItem("Navigate"); // level-by-level

        MenuItem insert_martyr = new MenuItem("Insert");
        MenuItem update_martyr = new MenuItem("Update/Delete");
        MenuItem search = new MenuItem("Search"); // tableview
        MenuItem navigate_martyr = new MenuItem("Navigate"); // inorder + tableview

        file.getItems().addAll(open, save);
        districts_menu.getItems().addAll(insert_district, update_district, remove_district, navigate_district);
        locations_menu.getItems().addAll(insert_location, update_location, remove_location, navigate_location);
        martyrs_menu.getItems().addAll(insert_martyr, update_martyr, search, navigate_martyr);
        menuBar.getMenus().addAll(file, districts_menu, locations_menu, martyrs_menu);
        // File menu
        open.setOnAction(e -> {
            open_file(primaryStage);
        });
        save.setOnAction(e -> {
            save_file();
        });

        // Districts menu
        insert_district.setOnAction(e -> {
            insert_district();
        });
        update_district.setOnAction(e -> {
            update_district();
        });
        remove_district.setOnAction(e -> {
            remove_district();
        });
        navigate_district.setOnAction(e -> {
            navigate_district();
        });

        // Locations menu
        insert_location.setOnAction(e -> {
            insert_location();
        });
        update_location.setOnAction(e -> {
            update_location();
        });
        remove_location.setOnAction(e -> {
            remove_location();
        });
        navigate_location.setOnAction(e -> {
            navigate_location((District) districts.get(0));
        });

        // Martyrs menu
        insert_martyr.setOnAction(e -> {
            addMartyr();
        });
        update_martyr.setOnAction(e -> {
            update_martyr();
        });
        search.setOnAction(e -> {
            martyr_search_by_part();
        });
        navigate_martyr.setOnAction(e -> {
            navigate_martyr();
        });

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    public void open_file(Stage primaryStage) {
        try {
            FileChooser fileChooser = new FileChooser();
            filec = fileChooser.showOpenDialog(primaryStage);
            System.out.println("File: " + filec.getAbsolutePath() + " has been opened.");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            Scanner scanner = new Scanner(filec);
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String name = parts[0];
                LocalDate date = LocalDate.parse(parts[1], formatter);
                int age;
                if (parts[2].equals("")) {
                    age = 0;
                } else {
                    age = Integer.parseInt(parts[2]);
                }
                String location_name = parts[3];
                String district_name = parts[4];

                District district;
                if (districts.find(district_name) == null) {
                    // if the district doesn't exist, create a new district
                    district = new District(district_name);
                    districts.insert(district);
                } else {
                    district = (District) districts.find(district_name);
                }
                // check if the location is already in BST
                Location location = (Location) ((District) districts.find(district_name)).location_pointer
                        .find(location_name);
                if (!district.contains_location(location_name)) {
                    // add the location to the district
                    location = new Location(location_name);
                    district.location_pointer.insert(location);
                }
                // check if the date is already in BST
                Dates martyr_date = (Dates) ((Location) ((District) districts.find(district_name)).location_pointer
                        .find(location_name)).dates_pointer.find(date.toString());
                if (!location.contains_date(date.toString())) {
                    martyr_date = new Dates(date);
                    location.dates_pointer.insert(martyr_date);
                }

                char gender = parts[5].toUpperCase().charAt(0);
                if (!martyr_date.exists(name)) {
                    Martyr martyr = new Martyr(name, date, age, location, district, gender);
                    martyr_date.martyrs.addSorted(new Node(martyr));
                }
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*----------------- main methods ----------------------- */
    private void insert_district() { // method to insert a district
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        Text txt = new Text("Enter District name: ");
        TextField text = new TextField();
        Text result = new Text();
        hbox.getChildren().addAll(txt, text);
        Button add = new Button("Add");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            if (insert_district(text.getText())) { // checks if string can be added to the tree
                result.setText("District " + text.getText() + " has been added.");
            } else {
                result.setText("District already exists or wrong data has been entered.");
            }
            districts.print_tree();
        });
        Scene insertScene = new Scene(vBox, 600, 400);
        insertStage.setScene(insertScene);
        insertStage.show();

    }

    private void update_district() {
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox.getItems().add((District) districts.get(i));
        }
        Text txt = new Text("Enter new District name: ");
        TextField text = new TextField();
        Text result = new Text();
        hbox.getChildren().addAll(txt, text, comboBox);
        Button add = new Button("Update");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            try {
                String current_name = comboBox.getValue().toString();
                District district = comboBox.getValue();
                isOk = false;
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Are you sure you want to update District " + current_name + "?");
                alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        isOk = true;
                    } else {
                        isOk = false;
                    }
                });
                if (!text.getText().equalsIgnoreCase(current_name) && !districts.checkContains(text.getText()) &&
                        !text.getText().isBlank() && !Character.isDigit(text.getText().charAt(0))) {
                    System.out.println(district.toString());
                    district.setName(text.getText());
                    districts.remove(district);
                    districts.insert(district);
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmation Dialog");
                    confirm.setHeaderText("District " + current_name + " has been updated to " + district.getName());
                    confirm.getButtonTypes().setAll(ButtonType.OK);
                    confirm.showAndWait();
                    comboBox.getItems().clear();
                    for (int i = 0; i < districts.size; i++) {
                        comboBox.getItems().add((District) districts.get(i));
                    }
                } else {
                    result.setText("District " + text.getText() + " already has this name");
                }
            } catch (Exception e1) {
                result.setText("Please enter a district");
            }

        });
        Scene insertScene = new Scene(vBox, 600, 400);
        insertStage.setScene(insertScene);
        insertStage.show();

    }

    private void remove_district() {// method to remove a district
        Stage deleteStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox.getItems().add((District) districts.get(i));
        }
        Text result = new Text();
        hbox.getChildren().addAll(comboBox);
        Button add = new Button("Remove");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            String current = comboBox.getValue().toString();
            isOk = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Are you sure you want to delete District " + current + "?");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            });
            System.out.println(isOk);
            if (isOk) {
                if (remove_district(comboBox.getValue().toString())) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmation Dialog");
                    confirm.setHeaderText("District " + current + " has been deleted");
                    confirm.getButtonTypes().setAll(ButtonType.OK);
                    confirm.showAndWait();
                    comboBox.getItems().clear();
                    for (int i = 0; i < districts.size; i++) {
                        comboBox.getItems().add((District) districts.get(i));
                    }
                }
            } else {
                result.setText("District " + current + " was not found or operation has been cancelled.");
            }

        });
        Scene deleteScene = new Scene(vBox, 600, 400);
        deleteStage.setScene(deleteScene);
        deleteStage.show();

    }

    private void navigate_district() {
        Stage navStage = new Stage();
        VBox vBox = new VBox(40);
        HBox hbox = new HBox(30);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        Text txt = new Text(districts.get(0).toString());
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Text martyrs_text = new Text("Number of Martyrs: " + ((District) districts.get(0)).number_of_martyrs());
        Button next = new Button("Next");
        Button prev = new Button("Back");
        Button load = new Button("Load");
        hbox.getChildren().addAll(load, prev, next);
        vBox.getChildren().addAll(txt, martyrs_text, hbox);
        Stack undo = new Stack(districts.size * 2); // this stack is used for prev
        Stack redo = new Stack(districts.size * 2); // this stack is used for next
        for (int i = 0; i < districts.size; i++) {
            undo.push(districts.get(i));
        }
        while (!undo.isEmpty()) {
            redo.push(undo.pop());
        }
        next.setOnAction(e -> {
            try {
                current_district = (District) redo.pop();
                undo.push(current_district);
                txt.setText(current_district.getName());
                martyrs_text.setText("Number of Martyrs: " + current_district.number_of_martyrs());
            } catch (Exception e1) {
                System.out.println("Out of bounds");
            }
        });
        prev.setOnAction(e -> {
            try {
                current_district = (District) undo.pop();
                redo.push(current_district);
                txt.setText(current_district.getName());
                martyrs_text.setText("Number of Martyrs: " + current_district.number_of_martyrs());
            } catch (Exception e2) {
                System.out.println("Out of bounds");
            }
        });
        load.setOnAction(e -> {
            navigate_location(current_district);
        });
        Scene navScene = new Scene(vBox, 600, 400);
        navStage.setScene(navScene);
        navStage.show();

    }

    /* --- Locations --- */
    private void insert_location() {
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox.getItems().add((District) districts.get(i));
        }
        Text txt = new Text("Enter Location name: ");
        TextField text = new TextField();
        Text result = new Text();
        hbox.getChildren().addAll(txt, text, comboBox);
        Button add = new Button("Add");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            try {
                if (insert_location(text.getText(), comboBox.getValue()) && !text.getText().isBlank()
                        && !Character.isDigit(text.getText().charAt(0))) {
                    result.setText("Location " + text.getText() + " has been added.");
                } else {
                    result.setText("Location " + text.getText() + " already exists.");
                }
            } catch (Exception e1) {
                result.setText("Please enter a district");
            }

        });
        Scene insertScene = new Scene(vBox, 600, 400);
        insertStage.setScene(insertScene);
        insertStage.show();
    }

    private void update_location() {
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox_d = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox_d.getItems().add((District) districts.get(i));
        }
        ComboBox<Location> comboBox_l = new ComboBox<>();
        comboBox_d.setOnAction(e1 -> {
            District district = comboBox_d.getValue();
            comboBox_l.setItems(FXCollections.observableArrayList());
            if (district != null) {
                for (int i = 0; i < district.location_pointer.size; i++) {
                    comboBox_l.getItems().add((Location) district.location_pointer.get(i));
                }
            }
        });
        Text txt = new Text("Enter new Location name:");
        TextField text = new TextField();
        Text result = new Text();
        hbox.getChildren().addAll(txt, text, comboBox_d, comboBox_l);
        Button add = new Button("Update");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            try {
                String current_lname = comboBox_l.getValue().toString();
                District current_district = comboBox_d.getValue();
                Location location = comboBox_l.getValue();
                isOk = false;
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setHeaderText("Are you sure you want to update Location " + current_lname + "?");
                alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        isOk = true;
                    } else {
                        isOk = false;
                    }
                });
                if (!text.getText().equalsIgnoreCase(current_lname) && !text.getText().isBlank()
                        && !Character.isDigit(text.getText().charAt(0))) {
                    location.setName(text.getText().trim());
                    current_district.location_pointer.remove(location);
                    current_district.location_pointer.insert(location);
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmation Dialog");
                    confirm.setHeaderText("Location " + current_lname + " has been updated to " + text.getText());
                    confirm.getButtonTypes().setAll(ButtonType.OK);
                    confirm.showAndWait();

                } else {
                    result.setText("District " + text.getText() + " already has this name");
                }
            } catch (Exception e1) {
                result.setText("Please enter a district");
            }

        });
        Scene insertScene = new Scene(vBox, 600, 400);
        insertStage.setScene(insertScene);
        insertStage.show();

    }

    private void remove_location() {// method to remove a Location
        Stage deleteStage = new Stage();
        VBox vBox = new VBox(15);
        HBox hbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox_d = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox_d.getItems().add((District) districts.get(i));
        }
        ComboBox<Location> comboBox_l = new ComboBox<>();
        comboBox_d.setOnAction(e1 -> {
            District district = comboBox_d.getValue();
            comboBox_l.setItems(FXCollections.observableArrayList());
            if (district != null) {
                for (int i = 0; i < district.location_pointer.size; i++) {
                    comboBox_l.getItems().add((Location) district.location_pointer.get(i));
                }
            }
        });
        Text result = new Text();
        hbox.getChildren().addAll(comboBox_d, comboBox_l);
        Button add = new Button("Remove");
        vBox.getChildren().addAll(hbox, add, result);
        add.setOnAction(e -> {
            String current = comboBox_d.getValue().toString();
            isOk = false;
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Are you sure you want to delete District " + current + "?");
            alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    isOk = true;
                } else {
                    isOk = false;
                }
            });
            System.out.println(isOk);
            if (isOk) {
                if (remove_location(comboBox_l.getValue().getName(), comboBox_d.getValue())) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmation Dialog");
                    confirm.setHeaderText("District " + current + " has been deleted");
                    confirm.getButtonTypes().setAll(ButtonType.OK);
                    confirm.showAndWait();
                    comboBox_d.getItems().clear();
                    for (int i = 0; i < districts.size; i++) {
                        comboBox_d.getItems().add((District) districts.get(i));
                    }
                }
            } else {
                result.setText("District " + current + " was not found or operation has been cancelled.");
            }

        });
        Scene deleteScene = new Scene(vBox, 600, 400);
        deleteStage.setScene(deleteScene);
        deleteStage.show();

    }

    private void navigate_location(District district) {
        Stage navStage = new Stage();
        VBox vBox = new VBox(40);
        HBox hbox = new HBox(30);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        Stack undo = new Stack(100); // this stack is used for saving out moves
        Stack redo = new Stack(100); // this stack is used to redo it again
        ComboBox<District> comboBox = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox.getItems().add((District) districts.get(i));
        }
        Text txt = new Text();
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Text earliest_date = new Text();
        Text latest_date = new Text();
        Text max_martyrs = new Text();
        comboBox.setValue(district);
        txt.setText(district.location_pointer.get(0).toString());
        earliest_date.setText("Earliest date: " + ((Location) district.location_pointer.get(0)).dates_pointer.min());
        latest_date.setText("Latest date: " + ((Location) district.location_pointer.get(0)).dates_pointer.max());
        max_martyrs.setText(
                "Max date has Martyrs: "
                        + district.location_pointer.most(((Location) district.location_pointer.get(0)).dates_pointer));
        locations = comboBox.getValue().location_pointer;
        for (int i = 0; i < locations.size; i++) {
            undo.push(locations.get(i));
        }
        while (!undo.isEmpty()) {
            redo.push(undo.pop());
        }
        comboBox.setOnAction(e -> {
            while (!undo.isEmpty()) {
                undo.pop();
            }
            while (!redo.isEmpty()) {
                redo.pop();
            }
            locations = comboBox.getValue().location_pointer;
            for (int i = 0; i < locations.size; i++) {
                undo.push(locations.get(i));
            }
            while (!undo.isEmpty()) {
                redo.push(undo.pop());
            }
            txt.setText(locations.get(0).toString());
            earliest_date
                    .setText("Earliest date: " + ((Location) locations.get(0)).dates_pointer.min());
            latest_date.setText("Latest date: " + ((Location) locations.get(0)).dates_pointer.max());
            max_martyrs.setText(
                    "Max date has Martyrs: " + locations
                            .most(((Location) locations.get(0)).dates_pointer));

        });
        Button next = new Button("Next");
        Button prev = new Button("Back");
        Button load = new Button("Load");
        hbox.getChildren().addAll(load, prev, next);
        vBox.getChildren().addAll(comboBox, txt, earliest_date, latest_date, max_martyrs, hbox);

        next.setOnAction(e -> {
            try {
                current_location = (Location) redo.pop();
                undo.push(current_location);
                txt.setText(current_location.toString());
                earliest_date.setText("Earliest date: " + current_location.dates_pointer.min());
                latest_date.setText("Latest date: " + current_location.dates_pointer.max());
                max_martyrs.setText(
                        "Max date has Martyrs: " + locations.most(current_location.dates_pointer));
            } catch (Exception e1) {
                System.out.println("Out of bounds");
            }

        });
        prev.setOnAction(e -> {
            try {
                current_location = (Location) undo.pop();
                redo.push(current_location);
                txt.setText(current_location.toString());
                earliest_date.setText("Earliest date: " + current_location.dates_pointer.min());
                latest_date.setText("Latest date: " + current_location.dates_pointer.max());
                max_martyrs.setText(
                        "Max date has Martyrs: " + locations.most(current_location.dates_pointer));

            } catch (Exception e1) {
                System.out.println("Out of bounds");
            }

        });
        load.setOnAction(e -> {
           // navigate_martyr(current_location);
        });
        Scene navScene = new Scene(vBox, 600, 400);
        navStage.setScene(navScene);
        navStage.show();

    }

    // ======================= Martyrs methods ==============================
    private void addMartyr() {
        Stage addStage = new Stage();
        VBox root = new VBox(20);
        HBox hbox1 = new HBox(5);
        HBox hbox2 = new HBox(5);
        HBox hbox3 = new HBox(5);
        HBox hbox4 = new HBox(5);
        HBox hbox5 = new HBox(5);
        HBox hbox6 = new HBox(5);
        Text name = new Text("Name:");
        TextField one = new TextField();
        hbox1.getChildren().addAll(name, one);
        hbox1.setAlignment(Pos.CENTER);

        Text age = new Text("Age:");
        Spinner<Integer> two = new Spinner<>(1, 100, 18);
        hbox2.getChildren().addAll(age, two);
        hbox2.setAlignment(Pos.CENTER);

        Text district = new Text("District:");
        ComboBox<District> districtList = new ComboBox<>();
        hbox3.getChildren().addAll(district, districtList);
        hbox3.setAlignment(Pos.CENTER);
        for (int i = 0; i < districts.size; i++) {
            districtList.getItems().add((District) districts.get(i));
        }

        Text location = new Text("Location");
        ComboBox<Location> locationList = new ComboBox<>();
        hbox4.getChildren().addAll(location, locationList);
        hbox4.setAlignment(Pos.CENTER);
        districtList.setOnAction(e -> {
            District dis = districtList.getValue();
            locationList.setItems(FXCollections.observableArrayList());
            if (dis != null) {
                for (int i = 0; i < dis.location_pointer.size; i++) {
                    locationList.getItems().add(((Location) dis.location_pointer.get(i)));
                }
            }
        });

        RadioButton male = new RadioButton("Male");
        RadioButton female = new RadioButton("Female");
        ToggleGroup toggleGroup = new ToggleGroup();
        male.setToggleGroup(toggleGroup);
        female.setToggleGroup(toggleGroup);
        male.setSelected(true);
        hbox5.getChildren().addAll(male, female);
        hbox5.setAlignment(Pos.CENTER);

        Text date = new Text("Date:");
        DatePicker datepick = new DatePicker();
        hbox6.getChildren().addAll(date, datepick);
        hbox6.setAlignment(Pos.CENTER);
        datepick.setOnAction(e -> {
            LocalDate d = datepick.getValue();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
            String formated = d.format(formatter);
            LocalDate parsed = LocalDate.parse(formated, formatter);
            System.out.println(parsed);

        });

        Button add = new Button("Add");
        Text result = new Text("here are the results");
        root.getChildren().addAll(hbox1, hbox2, hbox3, hbox4, hbox5, hbox6, add, result);
        root.setAlignment(Pos.CENTER);

        add.setOnAction(e -> {
            try {
                String martyrName = one.getText().trim();
                if (Character.isDigit(martyrName.charAt(0))) {
                    throw new Exception();
                }
                int martyrAge = two.getValue();
                District martyrDistrict = districtList.getValue();
                Location martyrLocation = locationList.getValue();

                LocalDate martyrDate = datepick.getValue();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy");
                String formated = martyrDate.format(formatter);
                LocalDate parsed = LocalDate.parse(formated, formatter);
                Dates newMartyrDate;
                char gender;
                if (male.isSelected())
                    gender = 'M';
                else
                    gender = 'F';

                Martyr martyr;
                newMartyrDate = (Dates) martyrLocation.dates_pointer.find(parsed);// look if date already exists
                if (newMartyrDate == null) {
                    newMartyrDate = new Dates(parsed);
                    martyrLocation.dates_pointer.insert(newMartyrDate);
                }
                martyr = search_martyr(martyrName);
                if (martyr == null) {
                    martyr = new Martyr(martyrName, parsed, martyrAge, martyrLocation, martyrDistrict, gender);
                    newMartyrDate.martyrs.addSorted(new Node(martyr));
                    result.setText("Martyr: " + martyr.toString() + " has been added.");
                } else if (martyr != null) {
                    result.setText("Martyr is duplicated.");
                }

            } catch (Exception e1) {
                result.setText("Please enter martyr information correctly.");
            }

        });
        Scene addScene = new Scene(root, 600, 400);
        addStage.setScene(addScene);
        addStage.show();
        addStage.setResizable(false);
    }

    private void update_martyr() {
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox oldHbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        oldHbox.setAlignment(Pos.CENTER);
        Text oldText = new Text("Enter old Martyr name:");
        TextField oldTextField = new TextField();
        Text result = new Text();
        oldHbox.getChildren().addAll(oldText, oldTextField);
        Button add = new Button("Update");
        Button delete = new Button("Remove");
        vBox.getChildren().addAll(oldHbox, add, delete, result);
        add.setOnAction(e -> {
            try {
                Martyr martyr = search_martyr(oldTextField.getText().trim());
                if (martyr == null) {
                    result.setText("Martyr " + oldTextField.getText().trim() + " doesn't exist");
                } else {
                    Dates date = search_date_by_martyr(martyr.getName());
                    isOk = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Are you sure you want to update Martyr " + martyr.getName() + "?");
                    alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            isOk = true;
                        } else {
                            isOk = false;
                        }
                    });
                    if (isOk) {
                        date.martyrs.remove(date.martyrs.indexOf(martyr.getName()));
                        addMartyr();
                    }

                }
            } catch (Exception e1) {
                result.setText("Please enter data correctly.");
            }
        });
        delete.setOnAction(e1 -> {
            try {
                Martyr martyr = search_martyr(oldTextField.getText().trim());
                if (martyr == null) {
                    result.setText("Martyr " + oldTextField.getText().trim() + " doesn't exist");
                } else {
                    Dates date = search_date_by_martyr(martyr.getName());
                    isOk = false;
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Are you sure you want to delete Martyr " + martyr.getName() + "?");
                    alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CLOSE);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            isOk = true;
                        } else {
                            isOk = false;
                        }
                    });
                    if (isOk) {
                        date.martyrs.remove(date.martyrs.indexOf(martyr.getName()));
                        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                        confirm.setTitle("Confirmation Dialog");
                        confirm.setHeaderText("Martyr " + oldTextField.getText().trim() + " has been deleted");
                        confirm.getButtonTypes().setAll(ButtonType.OK);
                        confirm.showAndWait();

                    }

                }
            } catch (Exception e3) {
                result.setText("Please enter data correctly.");
            }

        });
        Scene insertScene = new Scene(vBox, 600, 400);
        insertStage.setScene(insertScene);
        insertStage.show();

    }

    @SuppressWarnings("unchecked")
    private void martyr_search_by_part() {
        Stage insertStage = new Stage();
        VBox vBox = new VBox(15);
        HBox oldHbox = new HBox(5);
        vBox.setAlignment(Pos.CENTER);
        oldHbox.setAlignment(Pos.CENTER);
        Text oldText = new Text("Enter Martyr name:");
        TextField oldTextField = new TextField();
        oldHbox.getChildren().addAll(oldText, oldTextField);
        Button search = new Button("Search");
        TableView<Martyr> tableView = new TableView<>();
        vBox.getChildren().addAll(tableView, oldHbox, search);
        VBox.setMargin(search, new Insets(0, 0, 20, 0));

        search.setOnAction(e -> {
            tableView.getItems().clear();
            tableView.getColumns().clear();
            TableColumn<Martyr, String> nameCol = new TableColumn<>("Name");
            nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

            TableColumn<Martyr, Integer> ageCol = new TableColumn<>("Age");
            ageCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAge()).asObject());

            TableColumn<Martyr, LocalDate> dateCol = new TableColumn<>("Date");
            dateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));

            TableColumn<Martyr, Location> locationCol = new TableColumn<>("Location");
            locationCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLocation()));

            TableColumn<Martyr, District> districtCol = new TableColumn<>("District");
            districtCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDistrict()));

            TableColumn<Martyr, Character> genderCol = new TableColumn<>("Gender");
            genderCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGender()));

            tableView.getColumns().addAll(nameCol, ageCol, dateCol, locationCol, districtCol, genderCol);

            tableView.getItems().addAll(search_martyr_part_name(oldTextField.getText().trim()));
        });

        Scene insertScene = new Scene(vBox, 700, 400);
        insertStage.setScene(insertScene);
        insertStage.show();

    }

    @SuppressWarnings("unchecked")
    private void navigate_martyr() {
        Stage navStage = new Stage();
        VBox vBox = new VBox(40);
        HBox hbox = new HBox(30);
        HBox comboHbox = new HBox(10);
        vBox.setAlignment(Pos.CENTER);
        hbox.setAlignment(Pos.CENTER);
        comboHbox.setAlignment(Pos.CENTER);
        ComboBox<District> comboBox_d = new ComboBox<>();
        for (int i = 0; i < districts.size; i++) {
            comboBox_d.getItems().add((District) districts.get(i));
        }
        Stack undo = new Stack(1000); // this stack is used for prev
        Stack redo = new Stack(1000); // this stack is used for next
        ComboBox<Location> comboBox_l = new ComboBox<>();
        comboBox_d.setOnAction(e1 -> {
            District district = comboBox_d.getValue();
            comboBox_l.setItems(FXCollections.observableArrayList());
            if (district != null) {
                for (int i = 0; i < district.location_pointer.size; i++) {
                    comboBox_l.getItems().add((Location) district.location_pointer.get(i));
                }
            }

        });
        comboBox_l.setOnAction(e -> {
            while (!undo.isEmpty()) {
                undo.pop();
            }
            while (!redo.isEmpty()) {
                redo.pop();
            }
            currentDates = comboBox_l.getValue().dates_pointer;
            for (int i = 0; i < currentDates.size; i++) {
                undo.push(currentDates.get(i));
            }
            while (!undo.isEmpty()) {
                redo.push(undo.pop());
            }
        });
        
        Text districText = new Text("District");
        Text locationText = new Text("Location");
        Text txt = new Text();
        txt.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        Text martyrs_avg = new Text(
                "Average of Martyrs ages: ");
        Text youngest = new Text("Youngest Martyr: " );
        Text oldest = new Text("Oldest Martyr: " );
        TableView<Martyr> tableView = new TableView<>();
        TableColumn<Martyr, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        TableColumn<Martyr, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAge()).asObject());
        TableColumn<Martyr, LocalDate> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDate()));
        TableColumn<Martyr, Location> locationCol = new TableColumn<>("Location");
        locationCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getLocation()));
        TableColumn<Martyr, District> districtCol = new TableColumn<>("District");
        districtCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDistrict()));
        TableColumn<Martyr, Character> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getGender()));
        tableView.getColumns().addAll(nameCol, ageCol, dateCol, locationCol, districtCol, genderCol);
        Button next = new Button("Next");
        Button prev = new Button("Back");
        hbox.getChildren().addAll(prev, next);

        comboHbox.getChildren().addAll(districText, comboBox_d, locationText, comboBox_l);
        vBox.getChildren().addAll(comboHbox, txt, martyrs_avg, youngest, oldest, tableView, hbox);
        VBox.setMargin(hbox, new Insets(0, 0, 20, 0));
        VBox.setMargin(comboHbox, new Insets(20, 0, 0, 0));

        next.setOnAction(e -> {
            tableView.getItems().clear();
            try {
                undo.push(redo.pop());
                txt.setText(redo.peek().toString());
                martyrs_avg.setText("Average of Martyrs ages: " + ((Dates) redo.peek()).martyrs_avg_age());
                youngest.setText("Youngest Martyr: " + ((Dates) redo.peek()).youngest_martyr());
                oldest.setText("Oldest Martyr: " + ((Dates) redo.peek()).oldest_martyr());
                tableView.getItems().addAll(((Dates) redo.peek()).martyrs.getList());
            } catch (Exception e1) {
                System.out.println("Out of bounds");
            }
        });
        prev.setOnAction(e -> {
            tableView.getItems().clear();
            try {
                redo.push(undo.pop());
                txt.setText(undo.peek().toString());
                martyrs_avg.setText("Average of Martyrs ages: " + ((Dates) undo.peek()).martyrs_avg_age());
                youngest.setText("Youngest Martyr: " + ((Dates) undo.peek()).youngest_martyr());
                oldest.setText("Oldest Martyr: " + ((Dates) undo.peek()).oldest_martyr());
                tableView.getItems().addAll(((Dates) undo.peek()).martyrs.getList());
            } catch (Exception e2) {
                System.out.println("Out of bounds");
            }
        });

        Scene navScene = new Scene(vBox, 600, 600);
        navStage.setScene(navScene);
        navStage.show();

    }

    void print_data() { // method that prints all data in arranged way
        for (int i = 0; i < districts.size; i++) {
            System.out.println(districts.get(i) + ":");
            BST locations = ((District) districts.get(i)).location_pointer;
            for (int j = 0; j < locations.size; j++) {
                System.out.println("\t" + locations.get(j) + ":");
                BST dates = ((Location) locations.get(j)).dates_pointer;
                for (int k = 0; k < dates.size; k++) {
                    System.out.println("\t\t" + dates.get(k) + ":");
                    LinkedList martyrs = ((Dates) dates.get(k)).martyrs;
                    martyrs.print_list();
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    /*------- utility methods ---------*/
    private boolean insert_district(String s) {
        if (!districts.checkContains(s) && !s.isBlank() && !Character.isDigit(s.charAt(0))) {
            District district = new District(s);
            districts.insert(district);
            return true;
        }
        return false;
    }

    private boolean remove_district(String s) {
        if (districts.checkContains(s)) {
            District district = (District) districts.find(s);
            districts.remove(district);
            return true;
        }
        return false;
    }

    private boolean insert_location(String s, District d) {
        if (!d.contains_location(s)) {
            d.location_pointer.insert(new Location(s));
            return true;
        }
        return false;
    }

    private boolean remove_location(String s, District d) {
        if (d.contains_location(s)) {
            Location location = (Location) d.location_pointer.find(s);
            d.location_pointer.remove(location);
            return true;
        }
        return false;
    }

    // method to search if a martyr exists in the system and return it
    private Martyr search_martyr(String name) {
        for (int i = 0; i < districts.size; i++) {
            for (int j = 0; j < ((District) districts.get(i)).location_pointer.size; j++) {
                for (int k = 0; k < ((Location) ((District) districts.get(i)).location_pointer
                        .get(j)).dates_pointer.size; k++) {
                    if (((Dates) ((Location) ((District) districts.get(i)).location_pointer.get(j)).dates_pointer
                            .get(k)).martyrs.exist(name)) {
                        Martyr martyr = ((Dates) ((Location) ((District) districts.get(i)).location_pointer
                                .get(j)).dates_pointer.get(k)).martyrs.searchMartyr(name);
                        return martyr;
                    }
                }
            }
        }
        return null;
    }

    // this method returns a list that have all martyrs include part of name
    private ObservableList<Martyr> search_martyr_part_name(String name) {
        ObservableList<Martyr> result = FXCollections.observableArrayList();
        for (int i = 0; i < districts.size; i++) {
            for (int j = 0; j < ((District) districts.get(i)).location_pointer.size; j++) {
                for (int k = 0; k < ((Location) ((District) districts.get(i)).location_pointer
                        .get(j)).dates_pointer.size; k++) {
                    result.addAll(
                            ((Dates) ((Location) ((District) districts.get(i)).location_pointer.get(j)).dates_pointer
                                    .get(k)).martyrs.searchByPart(name));
                }
            }
        }
        return result;
    }

    // method to search if a martyr exists in the system and return it
    private Dates search_date_by_martyr(String name) {
        for (int i = 0; i < districts.size; i++) {
            for (int j = 0; j < ((District) districts.get(i)).location_pointer.size; j++) {
                for (int k = 0; k < ((Location) ((District) districts.get(i)).location_pointer
                        .get(j)).dates_pointer.size; k++) {
                    if (((Dates) ((Location) ((District) districts.get(i)).location_pointer.get(j)).dates_pointer
                            .get(k)).martyrs.exist(name)) {
                        Dates date = ((Dates) ((Location) ((District) districts.get(i)).location_pointer
                                .get(j)).dates_pointer
                                .get(k));
                        return date;
                    }
                }
            }
        }
        return null;
    }

    // this method is used to save to data to new file
    private void save_file() {
        try {
            FileWriter writer = new FileWriter(newFile);
            for (int i = 0; i < districts.size; i++) {
                for (int j = 0; j < ((District) districts.get(i)).location_pointer.size; j++) {
                    for (int k = 0; k < ((Location) ((District) districts.get(i)).location_pointer
                            .get(j)).dates_pointer.size; k++) {
                        for (int l = 0; l < ((Dates) ((Location) ((District) districts.get(i)).location_pointer
                                .get(j)).dates_pointer
                                .get(k)).martyrs.size(); l++) {
                            Martyr martyr = (Martyr) ((Dates) ((Location) ((District) districts.get(i)).location_pointer
                                    .get(j)).dates_pointer
                                    .get(k)).martyrs.get(l);
                            writer.write(martyr.toString());
                        }
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}