package View;

import Controller.viewController;
import Model.koneksi;
import Model.TaskModel;
import Model.TaskModel;
import Model.koneksi;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Schedule extends viewController {
    private Stage stage;
    private int idUser = 0;
    private Label nameLabel;
    private Label dateLabel;
    private TableView<TaskModel> tableView;

    public Schedule(Stage stage) {
        this.stage = stage;
        VBox root = new VBox();

        HBox header = createHeader();
        HBox topButtons = createTopButtons();
        BorderPane mainLayout = new BorderPane();
        VBox leftPanel = createLeftPanel(stage);
        VBox centerPanel = createCenterPanel();

        mainLayout.setLeft(leftPanel);
        mainLayout.setCenter(centerPanel);

        root.getChildren().addAll(header, topButtons, mainLayout);

        Scene scene = new Scene(root, 900, 600);
        stage.setScene(scene);
        stage.setTitle("Schedule");
        stage.show();

        afterOpen();
    }

    private HBox createHeader() {
        Label title = new Label("SCHEDULE");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: #006666;");

        Button addButton = new Button("+");
        addButton.setStyle("-fx-font-size: 24px; -fx-background-color: #006666; -fx-text-fill: white;");
        addButton.setOnAction(e -> openFrame("AddTask", stage));

        HBox header = new HBox(10, addButton, title);
        header.setPadding(new Insets(20));
        header.setAlignment(Pos.CENTER_LEFT);
        return header;
    }

    private HBox createTopButtons() {
        dateLabel = new Label("Menampilkan hari");
        dateLabel.setStyle("-fx-font-size: 18px;");

        Button dayBtn = new Button("Day");
        Button weekBtn = new Button("Week");
        Button monthBtn = new Button("Month");

        dayBtn.setOnAction(e -> showDayTasks());
        weekBtn.setOnAction(e -> showWeekTasks());
        monthBtn.setOnAction(e -> showMonthTasks());

        HBox box = new HBox(10, dateLabel, dayBtn, weekBtn, monthBtn);
        box.setPadding(new Insets(10));
        return box;
    }

    private VBox createCenterPanel() {
        tableView = new TableView<>();

        TableColumn<TaskModel, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<TaskModel, String> titleCol = new TableColumn<>("Task Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<TaskModel, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TaskModel, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<TaskModel, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        tableView.getColumns().addAll(statusCol, titleCol, dateCol, timeCol, descCol);

        VBox center = new VBox(tableView);
        center.setPadding(new Insets(10));
        return center;
    }

    private VBox createLeftPanel(Stage stage) {
        nameLabel = new Label("Full Name Here");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #006666;");

        Button scheduleBtn = new Button("SCHEDULE");
        scheduleBtn.setOnAction(e -> openFrame("Schedule", stage));

        Button tasksBtn = new Button("TASKS");
        tasksBtn.setOnAction(e -> openFrame("Task", stage));

        Button logoutBtn = new Button("LOG OUT");
        logoutBtn.setStyle("-fx-background-color: #FF3333; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin keluar?", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Konfirmasi Keluar");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    openFrame("Login", stage);
                    stage.close();
                }
            });
        });

        VBox left = new VBox(10, nameLabel, tasksBtn, scheduleBtn, logoutBtn);
        left.setPadding(new Insets(20));
        left.setPrefWidth(200);
        left.setStyle("-fx-background-color: #E5E5E5;");
        return left;
    }

        public void afterOpen() {
        String sql = "SELECT * FROM user WHERE emailUser = ? AND passwordUser = ?";
        try (PreparedStatement preparedStatement = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            preparedStatement.setString(1, getUserController().getEmail());
            preparedStatement.setString(2, getUserController().getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idUser = resultSet.getInt("idUser");
                String name = resultSet.getString("nameUser");
                Platform.runLater(() -> {
                    nameLabel.setText(name);
                    showDayTasks(); // default menampilkan task harian
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showDayTasks() {
        LocalDate today = LocalDate.now();
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
        loadTasks("DAY(dateTask) = DAY(?)", today);
    }

    private void showWeekTasks() {
        LocalDate today = LocalDate.now();
        String weekInfo = String.format("%dst Week %s %d",
                today.getDayOfMonth() / 7 + 1,
                today.getMonth().toString(),
                today.getYear());
        dateLabel.setText(weekInfo);
        loadTasks("WEEK(dateTask) = WEEK(?)", today);
    }

    private void showMonthTasks() {
        LocalDate today = LocalDate.now();
        dateLabel.setText(today.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        loadTasks("MONTH(dateTask) = MONTH(?)", today);
    }

    private void loadTasks(String whereClause, LocalDate date) {
        tableView.getItems().clear();

        String sql = "SELECT * FROM task WHERE idUser = ? AND " + whereClause;
        try (PreparedStatement preparedStatement = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            preparedStatement.setInt(1, idUser);
            preparedStatement.setObject(2, java.sql.Date.valueOf(date));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int status = resultSet.getInt("statusTask");
                String title = resultSet.getString("titleTask");
                Timestamp dateTime = resultSet.getTimestamp("dateTask");
                String description = resultSet.getString("detailTask");

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

                String statusStr = (status == 0) ? "Not Yet" : "Done";

                TaskModel task = new TaskModel(
                        statusStr,
                        title,
                        dateFormat.format(dateTime),
                        timeFormat.format(dateTime),
                        description
                );
                tableView.getItems().add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openFrame(String viewName) {
        // Implementasi sesuai kebutuhan (misalnya mengganti scene atau membuka window baru)
        System.out.println("Navigasi ke " + viewName);
    }            
}
