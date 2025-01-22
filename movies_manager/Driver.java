import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Driver extends Application {
    MovieCatalog movieCatalog;

    @Override
    public void start(Stage primaryStage) {
        movieCatalog = new MovieCatalog();
        movieCatalog.allocate(17);
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("File");
        Menu movieMenu = new Menu("Movie");
        MenuItem openFileItem = new MenuItem("Open");
        MenuItem saveFileItem = new MenuItem("Save");
        MenuItem exitProgramItem = new MenuItem("Exit");
        MenuItem addMovieItem = new MenuItem("Add");
        MenuItem deleteMovieItem = new MenuItem("Delete");
        MenuItem updateMovieItem = new MenuItem("Update");
        MenuItem searchMovieItem = new MenuItem("Search");
        MenuItem printSortedMovieItem = new MenuItem("Print Sorted");
        MenuItem printTopAndLeastMovieItem = new MenuItem("Print T&L");
        fileMenu.getItems().addAll(openFileItem, saveFileItem, exitProgramItem);
        movieMenu.getItems().addAll(addMovieItem, deleteMovieItem, updateMovieItem, searchMovieItem,
                printSortedMovieItem, printTopAndLeastMovieItem);
        menuBar.getMenus().addAll(fileMenu, movieMenu);
        root.setTop(menuBar);

        openFileItem.setOnAction(e -> {
            try {
                movieCatalog.loadMoviesFromFile(new File("movies.txt"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        saveFileItem.setOnAction(e -> {
            try {
                movieCatalog.saveMoviesToFile(new File("movies.txt"));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });

        exitProgramItem.setOnAction(e -> {
            primaryStage.close();
        });

        addMovieItem.setOnAction(e -> {
            addMovieLayout();
        });

        deleteMovieItem.setOnAction(e -> {
            deleteMovieLayout();
        });

        updateMovieItem.setOnAction(e -> {
            updateMovieLayout();
        });

        searchMovieItem.setOnAction(e -> {
            searchMovieLayout();
        });

        printSortedMovieItem.setOnAction(e -> {
            printSortedMovieLayout();
        });

        printTopAndLeastMovieItem.setOnAction(e -> {
            printTopAndLeastMovieLayout();
        });

        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    void addMovieLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        HBox titleBox = new HBox(10);
        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();
        titleBox.getChildren().addAll(titleLabel, titleField);
        titleBox.setAlignment(Pos.CENTER);

        HBox descBox = new HBox(10);
        Label descLabel = new Label("Descreption: ");
        TextArea textArea = new TextArea();
        textArea.setMaxWidth(150);
        textArea.setMaxHeight(80);
        descBox.getChildren().addAll(descLabel, textArea);
        descBox.setAlignment(Pos.CENTER);

        HBox yearBox = new HBox(10);
        Label yearLabel = new Label("Year: ");
        DatePicker datePicker = new DatePicker();
        yearBox.getChildren().addAll(yearLabel, datePicker);
        yearBox.setAlignment(Pos.CENTER);

        HBox rateBox = new HBox(10);
        Slider ratingSlider = new Slider(1.0, 10.0, 5.0);
        ratingSlider.setBlockIncrement(0.1);
        ratingSlider.setMajorTickUnit(1.0);
        ratingSlider.setMinorTickCount(9);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);

        Label ratingLabel = new Label(String.format("Rating: %.1f", ratingSlider.getValue()));
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ratingLabel.setText(String.format("Rating: %.1f", newVal.doubleValue()));
        });

        rateBox.getChildren().addAll(ratingLabel, ratingSlider);
        rateBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Submit");

        addButton.setOnAction(e -> {
            try {
                String title = titleField.getText();
                String desc = textArea.getText();
                int year = datePicker.getValue().getYear();
                double rate = Math.round(ratingSlider.getValue() * 10) / 10.0;

                Movie movie = new Movie(title, desc, year, rate);
                movieCatalog.put(movie);
            } catch (Exception e1) {
                System.out.println("[ERROR] please enter data correctly ...");
            }
        });

        root.getChildren().addAll(titleBox, descBox, yearBox, rateBox, addButton);
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    void deleteMovieLayout() {
        Stage deleteStage = new Stage();

        Label titleLabel = new Label("Movie Title:");
        TextField titleField = new TextField();

        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(e -> {
            try {
                String title = titleField.getText().trim();
                movieCatalog.erase(title);

            } catch (Exception e1) {
                System.out.println("[ERROR] couldn't find movie by title ...");
            }
        });

        VBox layout = new VBox(10, titleLabel, titleField, deleteButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 600, 400);
        deleteStage.setScene(scene);
        deleteStage.show();
    }

    void updateMovieLayout() {
        Stage stage = new Stage();
        VBox root = new VBox(10);
        HBox titleBox = new HBox(10);
        Label titleLabel = new Label("Title: ");
        TextField titleField = new TextField();
        titleBox.getChildren().addAll(titleLabel, titleField);
        titleBox.setAlignment(Pos.CENTER);

        ComboBox<Movie> moviesComboBox = new ComboBox<>();
        List<Movie> movies = movieCatalog.getAllMovies();
        moviesComboBox.getItems().addAll(movies);

        HBox descBox = new HBox(10);
        Label descLabel = new Label("Descreption: ");
        TextArea textArea = new TextArea();
        textArea.setMaxWidth(150);
        textArea.setMaxHeight(80);
        descBox.getChildren().addAll(descLabel, textArea);
        descBox.setAlignment(Pos.CENTER);

        HBox yearBox = new HBox(10);
        Label yearLabel = new Label("Year: ");
        DatePicker datePicker = new DatePicker();
        yearBox.getChildren().addAll(yearLabel, datePicker);
        yearBox.setAlignment(Pos.CENTER);

        HBox rateBox = new HBox(10);
        Slider ratingSlider = new Slider(1.0, 10.0, 5.0);
        ratingSlider.setBlockIncrement(0.1);
        ratingSlider.setMajorTickUnit(1.0);
        ratingSlider.setMinorTickCount(9);
        ratingSlider.setShowTickMarks(true);
        ratingSlider.setShowTickLabels(true);

        Label ratingLabel = new Label(String.format("Rating: %.1f", ratingSlider.getValue()));
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            ratingLabel.setText(String.format("Rating: %.1f", newVal.doubleValue()));
        });

        rateBox.getChildren().addAll(ratingLabel, ratingSlider);
        rateBox.setAlignment(Pos.CENTER);

        Button addButton = new Button("Update");

        moviesComboBox.setOnAction(e -> {
            Movie movie = moviesComboBox.getValue();
            titleField.setText(movie.getTitle());
            textArea.setText(movie.getDescription());
            datePicker.setValue(LocalDate.of(movie.getReleaseDate(), 1, 1));
            ratingSlider.setValue(movie.getRate());
        });

        addButton.setOnAction(e -> {
            Movie movie = moviesComboBox.getValue();
            movie.setTitle(titleField.getText());
            movie.setDescription(textArea.getText());
            movie.setReleaseDate(datePicker.getValue().getYear());
            movie.setRate(Math.round(ratingSlider.getValue() * 10) / 10.0);
        });

        root.getChildren().addAll(moviesComboBox, titleBox, descBox, yearBox, rateBox, addButton);
        root.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    @SuppressWarnings("unchecked")
    void searchMovieLayout() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term...");

        Button searchButton = new Button("Search");

        TableView<Movie> tableView = new TableView<>();
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Movie, Integer> dateColumn = new TableColumn<>("Date");
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");

        tableView.getColumns().addAll(nameColumn, descriptionColumn, dateColumn, ratingColumn);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        dateColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getReleaseDate()).asObject());
        ratingColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getRate()).asObject());

        ObservableList<Movie> moviesObservableList = FXCollections.observableArrayList();

        searchButton.setOnAction(e -> {
            moviesObservableList.clear();
            List<Movie> list = movieCatalog.getAllMovies();
            ArrayList<Movie> newList = new ArrayList<>();

            String userInput = searchField.getText();

            for (int i = 0; i < list.size(); i++) {
                Movie movie = list.get(i);
                if (movie.getTitle().contains(userInput)) {
                    newList.add(movie);
                }
            }

            moviesObservableList.addAll(newList);
            tableView.getItems().clear();
            tableView.getItems().addAll(moviesObservableList);
        });

        vbox.getChildren().addAll(searchField, searchButton, tableView);
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }

    @SuppressWarnings("unchecked")
    void printSortedMovieLayout() {
        Stage stage = new Stage();
        VBox vbox = new VBox();

        ComboBox<String> searchField = new ComboBox<>();
        searchField.getItems().addAll("Ascending", "Descending");

        TableView<Movie> tableView = new TableView<>();
        TableColumn<Movie, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Movie, String> descriptionColumn = new TableColumn<>("Description");
        TableColumn<Movie, Integer> dateColumn = new TableColumn<>("Date");
        TableColumn<Movie, Double> ratingColumn = new TableColumn<>("Rating");

        tableView.getColumns().addAll(nameColumn, descriptionColumn, dateColumn, ratingColumn);

        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitle()));
        descriptionColumn
                .setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));
        dateColumn.setCellValueFactory(
                cellData -> new SimpleIntegerProperty(cellData.getValue().getReleaseDate()).asObject());
        ratingColumn
                .setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getRate()).asObject());

        ObservableList<Movie> moviesObservableList = FXCollections.observableArrayList();

        searchField.setOnAction(e -> {
            moviesObservableList.clear();
            List<Movie> list = movieCatalog.getAllMoviesSorted();

            String userInput = searchField.getValue();
            if (!userInput.equals("Ascending")) {
                Collections.reverse(list);
            }

            moviesObservableList.addAll(list);
            tableView.getItems().clear();
            tableView.getItems().addAll(moviesObservableList);
        });

        vbox.getChildren().addAll(searchField, tableView);
        stage.setScene(new Scene(vbox, 600, 400));
        stage.show();
    }

    void printTopAndLeastMovieLayout() {
        Stage stage = new Stage();
        Label titleLabel = new Label("Title: AVL Tree ");
        titleLabel.setFont(Font.font("Halevitca", FontWeight.BOLD, 20));
        Label moviesNoLabel = new Label("No. movies: ");
        Label topLabel = new Label("Top Rating: ");
        Label leastLabel = new Label("Least Rating: ");
        Button nextbtn = new Button("Next");
        Button prevbtn = new Button("Back");
        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER);
        btnBox.getChildren().addAll(prevbtn, nextbtn);
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(titleLabel, moviesNoLabel, topLabel, leastLabel, btnBox);

        int[] index = new int[1];
        index[0] = 0;

        nextbtn.setOnAction(e -> {
            if (index[0] >= movieCatalog.size() - 1) {
                index[0] = 0;
            } else {
                index[0]++;
                AVLTree<Movie> tree = movieCatalog.getTreeByIndex(index[0]);
                if (tree.size == 0) {
                    titleLabel.setText("Title: AVL Tree " + index[0]);
                    moviesNoLabel.setText("No. movies: 0");
                    topLabel.setText("Top Rating: " + "Rate: ");
                    leastLabel.setText("Least Rating " + "Rate: ");
                } else {
                    titleLabel.setText("Title: AVL Tree " + index[0]);
                    moviesNoLabel.setText("No. movies: " + tree.size);
                    topLabel.setText("Top Rating: " + tree.getHighestRate().getTitle() + " | Rate: "
                            + tree.getHighestRate().getRate());
                    leastLabel.setText(
                            "Least Rating " + tree.getLowestRate().getTitle() + " | Rate: "
                                    + tree.getLowestRate().getRate());

                }
            }
        });

        prevbtn.setOnAction(e -> {
            if (index[0] <= 0) {
                index[0] = movieCatalog.size() - 1;
            } else {
                index[0]--;
                AVLTree<Movie> tree = movieCatalog.getTreeByIndex(index[0]);
                if (tree.size == 0) {
                    titleLabel.setText("Title: AVL Tree " + index[0]);
                    moviesNoLabel.setText("No. movies: 0");
                    topLabel.setText("Top Rating: " + "Rate: ");
                    leastLabel.setText("Least Rating " + "Rate: ");
                } else {
                    titleLabel.setText("Title: AVL Tree " + index[0]);
                    moviesNoLabel.setText("No. movies: " + tree.size);
                    topLabel.setText("Top Rating: " + tree.getHighestRate().getTitle() + " Rate: "
                            + tree.getHighestRate().getRate());
                    leastLabel.setText(
                            "Least Rating " + tree.getLowestRate().getTitle() + " Rate: "
                                    + tree.getLowestRate().getRate());

                }
            }
        });

        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
