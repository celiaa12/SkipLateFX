package View;

import Controller.viewController;
import Model.koneksi;
import Model.koneksi;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddTask extends viewController {
    private Stage stage;
    
    private int idUser = 0;
    private TextField titleField;
    private TextArea detailArea;
    private ComboBox<String> dayBox, monthBox, yearBox, hourBox, minuteBox, categoryBox, tagBox;
    private Label userNameLabel;

    public AddTask(Stage stage) {
        this.stage = stage;
        initUI();
        afterOpen();
    }

    private void initUI() {
        // Top panel
        Label titleLabel = new Label("ADD TASK");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #006666; -fx-font-weight: bold;");

        // Input fields
        titleField = new TextField();
        detailArea = new TextArea();
        dayBox = new ComboBox<>(FXCollections.observableArrayList(getDays()));
        monthBox = new ComboBox<>(FXCollections.observableArrayList(getMonths()));
        yearBox = new ComboBox<>(FXCollections.observableArrayList(getYears()));
        hourBox = new ComboBox<>(FXCollections.observableArrayList(getHours()));
        minuteBox = new ComboBox<>(FXCollections.observableArrayList(getMinutes()));
        categoryBox = new ComboBox<>(FXCollections.observableArrayList("Academic", "Non-Academic", "Organization", "Event", "Job"));
        tagBox = new ComboBox<>(FXCollections.observableArrayList("Tag 1", "Tag 2", "Tag 3", "Tag 4"));

        // Save Button
        Button saveBtn = new Button("Save");
        saveBtn.setOnAction(e -> {
            addTaskToDatabase();
            openFrame("Task", stage);
        });
        saveBtn.setStyle("-fx-background-color: #006666; -fx-text-fill: white; -fx-font-weight: bold;");

        // Logout Button
        Button logoutBtn = new Button("LOG OUT");
        logoutBtn.setStyle("-fx-background-color: #FF3333; -fx-text-fill: white;");
        logoutBtn.setOnAction(e -> {
            if (confirmLogout()) openFrame("Login", stage);
        });

        // User Name Label
        userNameLabel = new Label("Full Name Here");
        userNameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #006666; -fx-font-weight: bold;");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        grid.add(titleLabel, 0, 0);
        grid.add(new Label("Task title"), 0, 1);
        grid.add(titleField, 1, 1);
        grid.add(new Label("Date"), 0, 2);
        HBox dateBox = new HBox(10, dayBox, monthBox, yearBox);
        grid.add(dateBox, 1, 2);
        grid.add(new Label("Time"), 0, 3);
        HBox timeBox = new HBox(10, hourBox, minuteBox);
        grid.add(timeBox, 1, 3);
        grid.add(new Label("Detail"), 0, 4);
        grid.add(detailArea, 1, 4);
        grid.add(new Label("Category"), 0, 5);
        grid.add(categoryBox, 1, 5);
        grid.add(new Label("Tag"), 0, 6);
        grid.add(tagBox, 1, 6);
        grid.add(saveBtn, 1, 7);

        VBox leftBar = new VBox(10, userNameLabel, logoutBtn);
        leftBar.setPadding(new Insets(20));
        leftBar.setStyle("-fx-background-color: #E5E5E5;");
        leftBar.setPrefWidth(150);

        HBox root = new HBox(leftBar, grid);

        Stage stage = new Stage();
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Add Task");
        stage.show();
    }

    public void afterOpen() {
        String sql = "SELECT * FROM user WHERE emailUser = ? AND passwordUser = ?";

        try (PreparedStatement preparedStatement = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            preparedStatement.setString(1, getUserController().getEmail());
            preparedStatement.setString(2, getUserController().getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                idUser = resultSet.getInt("idUser");
                userNameLabel.setText(resultSet.getString("nameUser"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addTaskToDatabase() {
        String taskTitle = titleField.getText();
        String dateTimeString = String.format("%s-%02d-%s %s:%s:00",
                yearBox.getValue(),
                monthBox.getSelectionModel().getSelectedIndex() + 1,
                dayBox.getValue(),
                hourBox.getValue(),
                minuteBox.getValue()
        );
        String detail = detailArea.getText();
        String category = categoryBox.getValue();
        int categoryId = 0;
        int idTag = 0;
        String tag = tagBox.getValue();

        try (PreparedStatement ps1 = koneksi.bukaKoneksiDB().prepareStatement("SELECT * FROM categori WHERE categoriName = ?")) {
            ps1.setString(1, category);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) categoryId = rs1.getInt("idCategori");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement ps2 = koneksi.bukaKoneksiDB().prepareStatement("SELECT * FROM tags WHERE nameTags = ?")) {
            ps2.setString(1, tag);
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) idTag = rs2.getInt("idTags");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insert = "INSERT INTO task (idTask, titleTask, dateTask, detailTask, idCategori, idTag, idUser, statusTask) VALUES (null, ?, ?, ?, ?, ?, ?, 0)";
        try (PreparedStatement ps = koneksi.bukaKoneksiDB().prepareStatement(insert)) {
            ps.setString(1, taskTitle);
            ps.setString(2, dateTimeString);
            ps.setString(3, detail);
            ps.setInt(4, categoryId);
            ps.setInt(5, idTag);
            ps.setInt(6, idUser);
            ps.executeUpdate();
            showInfo("Succeeded Add Task");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean confirmLogout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Yakin ingin keluar?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Konfirmasi Keluar");
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Info");
        alert.showAndWait();
    }

    private java.util.List<String> getDays() {
        return java.util.stream.IntStream.rangeClosed(1, 31).mapToObj(String::valueOf).toList();
    }

    private java.util.List<String> getMonths() {
        return java.util.List.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");
    }

    private java.util.List<String> getYears() {
        return java.util.stream.IntStream.rangeClosed(2015, 2040).mapToObj(String::valueOf).toList();
    }

    private java.util.List<String> getHours() {
        return java.util.stream.IntStream.rangeClosed(0, 23).mapToObj(i -> String.format("%02d", i)).toList();
    }

    private java.util.List<String> getMinutes() {
        return java.util.stream.IntStream.rangeClosed(0, 59).mapToObj(i -> String.format("%02d", i)).toList();
    }                  
}
