package View;

import Controller.viewController;
import Model.koneksi;
import Model.koneksi;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Tasks extends viewController {
    private Stage stage;
    private int idUser;
    private Label userLabel;
    private TableView<TaskItem> tableView;
    private TextField searchField;

    public Tasks(Stage stage) {
        this.stage = stage;
        
        BorderPane root = new BorderPane();

        // Sidebar
        VBox sidebar = createSidebar();
        root.setLeft(sidebar);

        // Top Panel
        HBox topPanel = createTopPanel();
        root.setTop(topPanel);

        // Table Section
        tableView = createTable();
        root.setCenter(new StackPane(tableView));

        // Setup User Info and Table
        afterOpen();

        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("Tasks");
        stage.setScene(scene);
        stage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(15));
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #e5e5e5;");

        userLabel = new Label("Full Name Here");
        userLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Button btnTasks = new Button("TASKS");
        btnTasks.setOnAction(e -> afterOpen());

        Button btnAcademic = new Button("Academic");
        btnAcademic.setOnAction(e -> filterByCategory(1));

        Button btnNonAcademic = new Button("Non-Academic");
        btnNonAcademic.setOnAction(e -> filterByCategory(2));

        Button btnOrganization = new Button("Organization");
        btnOrganization.setOnAction(e -> filterByCategory(3));

        Button btnEvent = new Button("Event");
        btnEvent.setOnAction(e -> filterByCategory(4));

        Button btnJob = new Button("Job");
        btnJob.setOnAction(e -> filterByCategory(5));

        Button btnSchedule = new Button("SCHEDULE");
        btnSchedule.setOnAction(e -> openFrame("Schedule", stage));

        Button btnLogout = new Button("LOG OUT");
        btnLogout.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        btnLogout.setOnAction(e -> {
            boolean confirm = showConfirm("Yakin ingin keluar?");
            if (confirm) openFrame("Login", stage);
        });

        sidebar.getChildren().addAll(userLabel, btnTasks, btnAcademic, btnNonAcademic, btnOrganization, btnEvent, btnJob, btnSchedule, btnLogout);
        return sidebar;
    }

    private HBox createTopPanel() {
        HBox topPanel = new HBox(10);
        topPanel.setPadding(new Insets(10));
        topPanel.setAlignment(Pos.CENTER_RIGHT);

        Button addTaskButton = new Button("+ ADD TASK");
        addTaskButton.setOnAction(e -> openFrame("AddTask", stage));

        searchField = new TextField("Search...");
        searchField.setPrefWidth(200);
        searchField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused && searchField.getText().equals("Search...")) {
                searchField.setText("");
            } else if (!isNowFocused && searchField.getText().isEmpty()) {
                searchField.setText("Search...");
            }
        });

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> performSearch());

        topPanel.getChildren().addAll(addTaskButton, searchField, searchButton);
        return topPanel;
    }

    private TableView<TaskItem> createTable() {
        TableView<TaskItem> table = new TableView<>();

        TableColumn<TaskItem, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<TaskItem, String> titleCol = new TableColumn<>("Task Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<TaskItem, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<TaskItem, String> timeCol = new TableColumn<>("Time");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<TaskItem, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        table.getColumns().addAll(statusCol, titleCol, dateCol, timeCol, descCol);
        return table;
    }

    public void afterOpen() {
        String sql = "SELECT * FROM user WHERE emailUser = ? AND passwordUser = ?";

        try (PreparedStatement ps = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            ps.setString(1, getUserController().getEmail());
            ps.setString(2, getUserController().getPassword());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userLabel.setText(rs.getString("nameUser"));
                idUser = rs.getInt("IdUser");
                loadTasks("SELECT * FROM task WHERE idUser = ?");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadTasks(String sql, Object... params) {
        List<TaskItem> taskList = new ArrayList<>();
        try (PreparedStatement ps = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat tf = new SimpleDateFormat("HH:mm:ss");

            while (rs.next()) {
                int status = rs.getInt("statusTask");
                String statusStr = status == 0 ? "Not Yet" : "Done";
                String title = rs.getString("titleTask");
                Timestamp ts = rs.getTimestamp("dateTask");
                String desc = rs.getString("detailTask");
                taskList.add(new TaskItem(statusStr, title, df.format(ts), tf.format(ts), desc));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.getItems().setAll(taskList);
    }

    private void performSearch() {
        String search = searchField.getText().trim();
        if (search.isEmpty() || search.equals("Search...")) {
            loadTasks("SELECT * FROM task WHERE idUser = ?", idUser);
        } else {
            loadTasks("SELECT * FROM task WHERE idUser = ? AND titleTask LIKE ?", idUser, "%" + search + "%");
        }
    }

    private void filterByCategory(int categoryId) {
        loadTasks("SELECT * FROM task WHERE idUser = ? AND idCategori = ?", idUser, categoryId);
    }

    private boolean showConfirm(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();
        return alert.getResult() == ButtonType.YES;
    }

    public static class TaskItem {
        private final SimpleStringProperty status;
        private final SimpleStringProperty title;
        private final SimpleStringProperty date;
        private final SimpleStringProperty time;
        private final SimpleStringProperty description;

        public TaskItem(String status, String title, String date, String time, String description) {
            this.status = new SimpleStringProperty(status);
            this.title = new SimpleStringProperty(title);
            this.date = new SimpleStringProperty(date);
            this.time = new SimpleStringProperty(time);
            this.description = new SimpleStringProperty(description);
        }

        public String getStatus() { return status.get(); }
        public String getTitle() { return title.get(); }
        public String getDate() { return date.get(); }
        public String getTime() { return time.get(); }
        public String getDescription() { return description.get(); }
    }                 
}
