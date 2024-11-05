import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Driver extends Application {
    static int nextID = 0;
    static DLinkedList<Major> majors = new DLinkedList<>();
    TableView<Student> tableView;

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Project II");
        MenuBar menuBar = new MenuBar();
        Menu openFileMenu = new Menu("File");
        MenuItem openStudentFileItem = new MenuItem("Student");
        MenuItem openMajorsFileItem = new MenuItem("Majors");
        MenuItem saveFile = new MenuItem("Save");
        openFileMenu.getItems().addAll(openStudentFileItem, openMajorsFileItem, saveFile);
        Menu studentMenu = new Menu("Student");
        MenuItem insertStudentItem = new MenuItem("Insert");
        MenuItem removeStudentItem = new MenuItem("Delete");
        MenuItem updateStudentItem = new MenuItem("Update");
        MenuItem searchStudentItem = new MenuItem("Search");
        studentMenu.getItems().addAll(insertStudentItem, removeStudentItem, updateStudentItem, searchStudentItem);
        Menu majorMenu = new Menu("Major");
        MenuItem insertMajorItem = new MenuItem("Insert");
        MenuItem removeMajorItem = new MenuItem("Delete");
        MenuItem updateMajorItem = new MenuItem("Update");
        MenuItem searchMajorItem = new MenuItem("Search");
        majorMenu.getItems().addAll(insertMajorItem, removeMajorItem, updateMajorItem, searchMajorItem);
        Menu statsMenu = new Menu("Stats");
        MenuItem displayItem = new MenuItem("Display");
        statsMenu.getItems().addAll(displayItem);
        menuBar.getMenus().addAll(openFileMenu, studentMenu, majorMenu, statsMenu); // Adding menu to the
                                                                                    // Scene

        tableView = new TableView<>();
        TableColumn<Student, Integer> studentIdColumn = new TableColumn<>("ID");
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Student, String> studentNameColumn = new TableColumn<>("Name");
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Student, String> studentMajorColumn = new TableColumn<>("Major");
        studentMajorColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMajor().getName()));
        tableView.getColumns().addAll(studentIdColumn, studentNameColumn, studentMajorColumn);

        openStudentFileItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Textfiles (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                openStudentsFile(file);
                nextID = maxID();
            } catch (Exception error) {
                System.out.println("Couldn't Read this data format, please choose another file");
            }
            tableView.setItems(linkedListToObservableList());
            majors.printList();
        });

        openMajorsFileItem.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Textfiles (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showOpenDialog(primaryStage);
            try {
                openMajorFile(file);
            } catch (Exception error) {
                System.out.println("Couldn't Read this data format, please choose another file");
            }
        });

        saveFile.setOnAction(e -> {
            try {
                saveStudentFile(new File("src/students.txt"));
                saveMajorFile(new File("src/criteria.txt"));
            } catch (Exception error) {
                System.out.println("Error while saving files.");
            }
        });

        insertStudentItem.setOnAction(e -> {
            insertStudentStage();
        });

        removeStudentItem.setOnAction(e -> {
            deleteStudentStage();
        });

        updateStudentItem.setOnAction(e -> {
            updateStudentStage();
        });

        searchStudentItem.setOnAction(e -> {
            searchStudentStage();
        });

        insertMajorItem.setOnAction(e -> {
            insertMajorStage();
        });

        removeMajorItem.setOnAction(e -> {
            deleteMajorStage();
        });

        updateMajorItem.setOnAction(e -> {
            updateMajorStage();
        });

        searchMajorItem.setOnAction(e -> {
            searchMajorStage();
        });

        displayItem.setOnAction(e -> {
            statStage();
        });

        BorderPane root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(tableView);
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();
    }

    void openStudentsFile(File f) throws Exception {// this opens a file that contains studnets with their
                                                    // desired major, we check if they can proceed or not
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            double grade = Double.parseDouble(parts[2]);
            double placement = Double.parseDouble(parts[3]);
            String majorName = parts[4];

            Student student = new Student(id, name, grade, placement);
            // if the student satisfies the major requirments we add it to the single linked
            // list for a DNode of the major
            DNode<Major> nodeRefrence = majors.searchByName(majorName);
            Major major = majors.searchByName(majorName).data;
            if (student.calculateAdmissionMark(major.getTawjihiWeight(), major.getTestWeight()) >= major
                    .getAcceptGrade()) {
                nodeRefrence.studentsList.insertSorted(student);
                student.accepted = true;
                student.setMajor(major);
                System.out.println("Student " + student.getName() + " is Accepted.");
                major.acceptNum++;
            } else {
                student.accepted = false;
                System.out.println("Student " + student.getName() + " is NOT Accepted.");
                major.rejectNum++;
                initialMajorStage(student);
            }
        }
        fileScanner.close();
    }

    static void openMajorFile(File f) throws Exception { // this opens a file that suppose to contaiin majors and their
                                                         // accept marks and tests weights
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");
            String name = parts[0];
            double AG = Double.parseDouble(parts[1]);
            double TW = Double.parseDouble(parts[2]);
            double PT = Double.parseDouble(parts[3]);
            Major major = new Major(name, AG, TW, PT);
            majors.insertSorted(new DNode<Major>(major));
        }
        fileScanner.close();
    }

    void saveStudentFile(File file) throws Exception {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.append("Student ID,Name,Tawjihi Grade,Placement Test Grade,Chosen Major\n");
        for (int i = 0; i < linkedListToObservableList().size(); i++) {
            Student student = linkedListToObservableList().get(i);
            fileWriter.append(student.getId() + "," + student.getName() + "," + student.getGrade() + ","
                    + student.getPlacement() + "," + student.getMajor().getName() + "\n");
        }
        fileWriter.close();
    }

    void saveMajorFile(File file) throws Exception {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.append("Major,Acceptance Grade,Tawjihi Weight,Placement Test Weight\n");
        for (int i = 0; i < majors.size(); i++) {
            Major major = majors.get(i).data;
            fileWriter.append(major.getName() + "," + major.getAcceptGrade() + "," + major.getTawjihiWeight() + ","
                    + major.getTestWeight() + "\n");
        }
        fileWriter.close();
    }

    void insertStudentStage() {
        Stage insertStage = new Stage();
        VBox vbox = new VBox();
        vbox.setPrefSize(600, 400);
        vbox.setSpacing(10);

        HBox nameBox = new HBox();
        nameBox.setSpacing(5);
        Label nameLabel = new Label("Name");
        TextField nameField = new TextField();
        nameBox.getChildren().addAll(nameLabel, nameField);
        nameBox.setAlignment(Pos.CENTER);

        HBox tawjihiBox = new HBox();
        tawjihiBox.setSpacing(5);
        Label tawjihiLabel = new Label("Tawjihi G");
        TextField tawjihiField = new TextField();
        tawjihiBox.getChildren().addAll(tawjihiLabel, tawjihiField);
        tawjihiBox.setAlignment(Pos.CENTER);

        HBox placementBox = new HBox();
        placementBox.setSpacing(5);
        Label placementLabel = new Label("Placement G");
        TextField placementField = new TextField();
        placementBox.getChildren().addAll(placementLabel, placementField);
        placementBox.setAlignment(Pos.CENTER);

        HBox majorBox = new HBox();
        majorBox.setSpacing(5);
        Label majorLabel = new Label("Major");
        ComboBox<Major> majorComboBox = new ComboBox<>();
        for (int i = 0; i < majors.size(); i++) {
            majorComboBox.getItems().add(majors.get(i).data);
        }
        majorBox.getChildren().addAll(majorLabel, majorComboBox);
        majorBox.setAlignment(Pos.CENTER);

        VBox buttonBox = new VBox();
        buttonBox.setSpacing(10);
        Button insertButton = new Button("Insert");
        Label statusLabel = new Label("Label");
        buttonBox.getChildren().addAll(insertButton, statusLabel);
        buttonBox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(nameBox, tawjihiBox, placementBox, majorBox, buttonBox);
        vbox.setAlignment(Pos.CENTER);

        insertButton.setOnAction(e -> {
            try {
                int id = generateID();
                String name = nameField.getText();
                double tawjihiGrade = Double.parseDouble(tawjihiField.getText());
                double placementGrade = Double.parseDouble(placementField.getText());
                DNode<Major> node = majors.searchByName(majorComboBox.getValue().getName());
                Major major = majorComboBox.getValue();
                Student student = new Student(id, name, tawjihiGrade, placementGrade, major);
                if (student.calculateAdmissionMark(major.getTawjihiWeight(), major.getTestWeight()) >= major
                        .getAcceptGrade()) {
                    node.studentsList.insertSorted(student);
                    student.accepted = true;
                    student.setMajor(major);
                    statusLabel.setText("Student " + student.getName() + " has been accepted to " + major.getName());
                    major.acceptNum++;
                    updateTableView(tableView);
                } else {
                    student.accepted = false;
                    statusLabel
                            .setText("Student " + student.getName() + " has NOT been accepted to " + major.getName());
                    major.rejectNum++;
                    initialMajorStage(student);
                    return;
                }
            } catch (Exception error) {
                statusLabel.setText("Please enter data correctly.");
                error.printStackTrace();
            }
        });

        insertStage.setScene(new Scene(vbox, 600, 400));
        insertStage.show();
        insertStage.setResizable(false);
    }

    @SuppressWarnings("unchecked")
    void deleteStudentStage() {
        Stage deleteStage = new Stage();
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        ComboBox<Major> comboBox = new ComboBox<>();
        for (int i = 0; i < majors.size(); i++) {
            comboBox.getItems().add(majors.get(i).data);
        }
        ComboBox<Student> comboBox2 = new ComboBox<>();

        final DNode<Major>[] majorNode = new DNode[1];
        comboBox.setOnAction(e -> {
            comboBox2.getItems().clear();
            majorNode[0] = majors.searchByName(comboBox.getValue().getName());
            for (int i = 0; i < majorNode[0].studentsList.size(); i++) {
                comboBox2.getItems().add(majorNode[0].studentsList.get(i));
            }
        });

        Button deleteButton = new Button("Delete");
        Label deleteLabel = new Label();

        deleteButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to delete this student?");
            alert.setContentText("This action cannot be undone.");
            alert.showAndWait().ifPresent(confirm -> {
                if (confirm == ButtonType.OK) {
                    int index = majorNode[0].studentsList.search(comboBox2.getValue());
                    majorNode[0].studentsList.delete(index);
                    deleteLabel.setText("Student " + comboBox2.getValue().getName() + " has been Deleted.");
                    updateTableView(tableView);
                }
            });
        });

        vBox.getChildren().addAll(comboBox, comboBox2, deleteButton, deleteLabel);
        deleteStage.setScene(new Scene(vBox, 600, 400));
        deleteStage.show();

    }

    @SuppressWarnings("unchecked")
    void updateStudentStage() {
        Stage updateStage = new Stage();
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);

        ComboBox<Major> comboBox = new ComboBox<>();
        for (int i = 0; i < majors.size(); i++) {
            comboBox.getItems().add(majors.get(i).data);
        }
        ComboBox<Student> comboBox2 = new ComboBox<>();

        final DNode<Major>[] majorNode = new DNode[1];
        comboBox.setOnAction(e -> {
            comboBox2.getItems().clear();
            majorNode[0] = majors.searchByName(comboBox.getValue().getName());
            for (int i = 0; i < majorNode[0].studentsList.size(); i++) {
                comboBox2.getItems().add(majorNode[0].studentsList.get(i));
            }
        });

        Button updateButton = new Button("Update");
        Label updateLabel = new Label();

        updateButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to update this student?");
            alert.setContentText("This action will delete the current record and cannot be undone.");
            alert.showAndWait().ifPresent(confirm -> {
                if (confirm == ButtonType.OK) {
                    int index = majorNode[0].studentsList.search(comboBox2.getValue());
                    majorNode[0].studentsList.delete(index);
                    updateLabel.setText("Student " + comboBox2.getValue().getName() + " has been Updated.");
                    insertStudentStage();
                    updateTableView(tableView);
                }
            });
        });

        vBox.getChildren().addAll(comboBox, comboBox2, updateButton, updateLabel);
        updateStage.setScene(new Scene(vBox, 600, 400));
        updateStage.show();

    }

    void searchStudentStage() {
        Stage searchStage = new Stage();
        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        HBox hBox = new HBox(5);
        Label idLabel = new Label("ID");
        TextField idField = new TextField();
        hBox.getChildren().addAll(idLabel, idField);
        hBox.setAlignment(Pos.CENTER);

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            int id = Integer.parseInt(idField.getText());
            Student student = searchById(id);
            if (student == null) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Student is NOT Found");
                alert.showAndWait().ifPresent(confirm -> {
                    if (confirm == ButtonType.OK) {
                        return;
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Student is Found:");
                alert.setContentText(student.toString());
                alert.showAndWait().ifPresent(confirm -> {
                    if (confirm == ButtonType.OK) {
                        return;
                    }
                });
            }
        });

        vBox.getChildren().addAll(hBox, searchButton);
        searchStage.setScene(new Scene(vBox, 600, 400));
        searchStage.show();

    }

    void insertMajorStage() {
        Stage insertStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPrefSize(600, 400);

        HBox majorHBox = new HBox(5);
        majorHBox.setAlignment(Pos.CENTER);
        Label majorLabel = new Label("Major");
        TextField majorTextField = new TextField();
        majorHBox.getChildren().addAll(majorLabel, majorTextField);

        HBox tawjihiHBox = new HBox(5);
        tawjihiHBox.setAlignment(Pos.CENTER);
        Label tawjihiLabel = new Label("Tawjihi W");
        Spinner<Double> tawjihiSpinner = new Spinner<>(0, 1.0, 0.5, 0.1);
        tawjihiHBox.getChildren().addAll(tawjihiLabel, tawjihiSpinner);

        HBox placementHBox = new HBox(5);
        placementHBox.setAlignment(Pos.CENTER);
        Label placementLabel = new Label("Placement W");
        Spinner<Double> placementSpinner = new Spinner<>(0, 1.0, 0.5, 0.1);
        placementHBox.getChildren().addAll(placementLabel, placementSpinner);

        HBox acceptanceHBox = new HBox(5);
        acceptanceHBox.setAlignment(Pos.CENTER);
        Label acceptanceLabel = new Label("Acceptance G");
        TextField acceptanceTextField = new TextField();
        acceptanceHBox.getChildren().addAll(acceptanceLabel, acceptanceTextField);

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button insertButton = new Button("Insert");
        Label statusLabel = new Label();
        buttonBox.getChildren().addAll(insertButton, statusLabel);

        vbox.getChildren().addAll(majorHBox, tawjihiHBox, placementHBox, acceptanceHBox, buttonBox);
        vbox.setAlignment(Pos.CENTER);

        insertButton.setOnAction(e -> {
            try {
                String majorName = majorTextField.getText();
                double tawjihiWeight = tawjihiSpinner.getValue();
                double placementWeight = placementSpinner.getValue();
                double acceptanceGrade = Double.parseDouble(acceptanceTextField.getText());
                Major major = new Major(majorName, acceptanceGrade, tawjihiWeight, placementWeight);
                majors.insertSorted(new DNode<Major>(major));
                statusLabel.setText("Major " + majorName + " has been added.");
            } catch (Exception error) {
                statusLabel.setText("Please enter data correctly");
                error.printStackTrace();
            }
        });

        insertStage.setScene(new Scene(vbox, 600, 400));
        insertStage.show();
    }

    void deleteMajorStage() {
        Stage deleteStage = new Stage();
        VBox vBox = new VBox(10);
        ComboBox<Major> comboBox = new ComboBox<>();
        for (int i = 0; i < majors.size(); i++) {
            comboBox.getItems().add(majors.get(i).data);
        }
        Button deleteButton = new Button("Delete");
        Label deleteLabel = new Label();
        deleteButton.setOnAction(e -> {
            int index = majors.search(comboBox.getValue());
            if (index != -1) {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Do you want to delete this major and all its data?");
                alert.setContentText("All data integrated with this major will be deleted, are you cool with this?");
                alert.showAndWait().ifPresent(conf -> {
                    if (conf == ButtonType.OK) {
                        deleteLabel.setText("Major " + comboBox.getValue().getName() + " has been deleted");
                        majors.delete(index);
                        updateTableView(tableView);
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText("Please select a major.");
            }
        });

        vBox.getChildren().addAll(comboBox, deleteButton, deleteLabel);
        vBox.setAlignment(Pos.CENTER);

        deleteStage.setScene(new Scene(vBox, 600, 400));
        deleteStage.show();
    }

    void updateMajorStage() {
        Stage updateStage = new Stage();
        VBox vBox = new VBox(10);
        ComboBox<Major> comboBox = new ComboBox<>();
        for (int i = 0; i < majors.size(); i++) {
            comboBox.getItems().add(majors.get(i).data);
        }
        HBox majorHBox = new HBox(5);
        majorHBox.setAlignment(Pos.CENTER);
        Label majorLabel = new Label("Major");
        TextField majorTextField = new TextField();
        majorHBox.getChildren().addAll(majorLabel, majorTextField);

        HBox tawjihiHBox = new HBox(5);
        tawjihiHBox.setAlignment(Pos.CENTER);
        Label tawjihiLabel = new Label("Tawjihi W");
        Spinner<Double> tawjihiSpinner = new Spinner<>(0, 1.0, 0.5, 0.1);
        tawjihiHBox.getChildren().addAll(tawjihiLabel, tawjihiSpinner);

        HBox placementHBox = new HBox(5);
        placementHBox.setAlignment(Pos.CENTER);
        Label placementLabel = new Label("Placement W");
        Spinner<Double> placementSpinner = new Spinner<>(0, 1.0, 0.5, 0.1);
        placementHBox.getChildren().addAll(placementLabel, placementSpinner);

        HBox acceptanceHBox = new HBox(5);
        acceptanceHBox.setAlignment(Pos.CENTER);
        Label acceptanceLabel = new Label("Acceptance G");
        TextField acceptanceTextField = new TextField();
        acceptanceHBox.getChildren().addAll(acceptanceLabel, acceptanceTextField);

        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button updateButton = new Button("Update");
        Label statusLabel = new Label();
        buttonBox.getChildren().addAll(updateButton, statusLabel);

        updateButton.setOnAction(e -> {
            try {
                String majorName = majorTextField.getText();
                double tawjihiWeight = tawjihiSpinner.getValue();
                double placementWeight = placementSpinner.getValue();
                double acceptanceGrade = Double.parseDouble(acceptanceTextField.getText());
                Major major = new Major(majorName, acceptanceGrade, tawjihiWeight, placementWeight);

                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setHeaderText("Do you want to update this major and all its data?");
                alert.setContentText(
                        "NOTE: if a previously registered student no longer satisfies the admission criteria he will be rejected.");
                alert.showAndWait().ifPresent(conf -> {
                    if (conf == ButtonType.OK) {
                        DNode<Major> majorNode = majors.searchByName(comboBox.getValue().getName());
                        Major updatedMajor = majorNode.data;
                        updatedMajor.setName(major.getName());
                        updatedMajor.setAcceptGrade(major.getAcceptGrade());
                        updatedMajor.setTawjihiWeight(major.getTawjihiWeight());
                        updatedMajor.setTestWeight(major.getTestWeight());
                        updateTableView(tableView);
                    }
                });
            } catch (Exception error) {
                statusLabel.setText("Please enter data correctly.");
                error.printStackTrace();
            }
        });

        vBox.getChildren().addAll(comboBox, majorHBox, tawjihiHBox, placementHBox, acceptanceHBox, buttonBox);
        vBox.setAlignment(Pos.CENTER);

        updateStage.setScene(new Scene(vBox, 600, 400));
        updateStage.show();
    }

    void searchMajorStage() {
        Stage searchStage = new Stage();
        VBox vBox = new VBox(10);
        HBox hBox = new HBox(5);
        Label searchLabel = new Label("Major");
        TextField searchField = new TextField();
        hBox.getChildren().addAll(searchLabel, searchField);
        hBox.setAlignment(Pos.CENTER);

        Button searchButton = new Button("search");
        searchButton.setOnAction(e -> {
            DNode<Major> majorNode = majors.searchByName(searchField.getText());
            Major major = null;
            if (majorNode != null) {
                major = majorNode.data;
            }
            if (major != null) {
                DNode<Major> node = majors.searchByName(major.getName());
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Major is found:");
                alert.setContentText(major.toString() + node.studentsList.toString());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Major was NOT found.");
                alert.showAndWait();
            }
        });

        vBox.getChildren().addAll(hBox, searchButton);
        vBox.setAlignment(Pos.CENTER);

        searchStage.setScene(new Scene(vBox, 600, 400));
        searchStage.show();
    }

    void initialMajorStage(Student student) {
        DLinkedList<Major> stdMajors = majors.acceptedMajorsForStudent(student);
        Stage initStage = new Stage();
        initStage.setTitle("Student " + student.getName() + " Alternative Major.");
        VBox vBox = new VBox(10);
        Label majorNameLabel = new Label(stdMajors.get(0).data.getName());
        majorNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label acceptanceGradeLabel = new Label(stdMajors.get(0).data.getAcceptGrade() + "");
        HBox hBox = new HBox(5);
        Button nextButton = new Button("Next");
        Button prevButton = new Button("Back");
        Button submitButton = new Button("Submit");
        int[] index = new int[1];
        index[0] = 0;

        if (stdMajors.isEmpty()) {
            majorNameLabel.setText("No Majors Available");
            acceptanceGradeLabel.setText("N/A");
            nextButton.setDisable(true);
            prevButton.setDisable(true);
            submitButton.setDisable(true);
        }

        nextButton.setOnAction(e -> {
            ++index[0];
            if (index[0] == stdMajors.size()) {
                index[0] = 0;
            }
            majorNameLabel.setText(stdMajors.get(index[0]).data.getName());
            acceptanceGradeLabel.setText(stdMajors.get(index[0]).data.getAcceptGrade() + "");
        });
        prevButton.setOnAction(e -> {
            --index[0];
            if (index[0] < 0) {
                index[0] = stdMajors.size() - 1;
            }
            majorNameLabel.setText(stdMajors.get(index[0]).data.getName());
            acceptanceGradeLabel.setText(stdMajors.get(index[0]).data.getAcceptGrade() + "");
        });
        submitButton.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setHeaderText("Do you want to add student " + student.getName() + " to this major?");
            alert.setContentText("Note that the student satisfies the major requirments.");
            alert.showAndWait().ifPresent(con -> {
                DNode<Major> node = majors.searchByName(majorNameLabel.getText());
                node.studentsList.insertSorted(student);
                student.accepted = true;
                student.setMajor(node.data);
                updateTableView(tableView);
                node.data.acceptNum++;
                initStage.close();
            });
        });

        hBox.getChildren().addAll(prevButton, submitButton, nextButton);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(majorNameLabel, acceptanceGradeLabel, hBox);
        vBox.setAlignment(Pos.CENTER);

        initStage.setScene(new Scene(vBox, 500, 400));
        initStage.show();
    }

    void statStage() {
        Stage statStage = new Stage();
        VBox vBox = new VBox(10);
        Label majorNameLabel = new Label(majors.get(0).data.getName());
        majorNameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        Label rejectLabel = new Label("Rejected: " + majors.get(0).data.rejectNum + "");
        Label acceptLabel = new Label("Accepted: " + majors.get(0).data.acceptNum + "");
        Label evaluateLabel = new Label("Acceptance Rate: "
                + evaluateStudents(majors.get(0).data.acceptNum, majors.get(0).data.rejectNum) + "%");
        Label acceptMajorsLabel = new Label("All Majors Accepted: " + majors.allAccepted());
        Label rejectMajorsLabel = new Label("All Majors Rejected: " + majors.allRejected());
        HBox hBox = new HBox(5);
        Button nextButton = new Button("Next");
        Button prevButton = new Button("Back");
        hBox.getChildren().addAll(prevButton, nextButton);
        hBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(majorNameLabel, rejectLabel, acceptLabel, evaluateLabel, rejectMajorsLabel,
                acceptMajorsLabel, hBox);
        vBox.setAlignment(Pos.CENTER);

        int[] index = new int[1];
        index[0] = 0;
        nextButton.setOnAction(e -> {
            ++index[0];
            if (index[0] == majors.size()) {
                index[0] = 0;
            }
            majorNameLabel.setText(majors.get(index[0]).data.getName());
            rejectLabel.setText("Rejected: " + majors.get(index[0]).data.rejectNum + "");
            acceptLabel.setText("Accepted: " + majors.get(index[0]).data.acceptNum + "");
            evaluateLabel.setText("Acceptance Rate: "
                    + evaluateStudents(majors.get(index[0]).data.acceptNum, majors.get(0).data.rejectNum) + "%");
        });
        prevButton.setOnAction(e -> {
            --index[0];
            if (index[0] < 0) {
                index[0] = majors.size() - 1;
            }
            majorNameLabel.setText(majors.get(index[0]).data.getName());
            rejectLabel.setText("Rejected: " + majors.get(index[0]).data.rejectNum + "");
            acceptLabel.setText("Accepted: " + majors.get(index[0]).data.acceptNum + "");
            evaluateLabel.setText("Acceptance Rate: "
                    + evaluateStudents(majors.get(index[0]).data.acceptNum, majors.get(0).data.rejectNum) + "%");
        });

        statStage.setScene(new Scene(vBox, 600, 400));
        statStage.show();
    }

    /* ---- Helper Methods ---- */
    ObservableList<Student> linkedListToObservableList() { // takes a list of all students in system and convert it to
                                                           // observable, helps to update tableview
        ObservableList<Student> observableList = FXCollections.observableList(majors.traverseMajors());
        return observableList;
    }

    void updateTableView(TableView<Student> tableView) { // used each time an opertion is excuted to students
        ObservableList<Student> observableList = FXCollections.observableList(majors.traverseMajors());
        tableView.setItems(observableList);
        tableView.refresh();
    }

    int generateID() { // this returns a unique ID for each student.
        return ++nextID;
    }

    int maxID() { // this method returns the largest ID in the system, to make sure theres no
                  // duplicated id's: O(M * N)
        int max = 0;
        for (int i = 0; i < majors.size(); i++) {
            for (int j = 0; j < majors.get(i).studentsList.size(); j++) {
                if (max < majors.get(i).studentsList.get(j).getId())
                    max = majors.get(i).studentsList.get(j).getId();
            }
        }
        return max;
    }

    int evaluateStudents(int accept, int reject) {
        return accept + reject == 0 ? 0 : accept / (accept + reject) * 100;
    }

    Student searchById(int id) { // this method searches for id through all the system data: O(N * M)
        for (int i = 0; i < majors.size(); i++) {
            LinkedList<Student> studentList = majors.get(i).studentsList;
            for (int j = 0; j < studentList.size(); j++) {
                Student student = (Student) studentList.get(j);
                if (id == student.getId()) {
                    return student;
                }
            }
        }
        return null;
    }

}