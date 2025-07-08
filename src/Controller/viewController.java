package Controller;

import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import View.AddTask;
import View.Login;
import View.Schedule;
import View.SignUp;
import View.Tasks;
import View.Welcome;
import Model.koneksi;

public abstract class viewController {

    protected Connection databaseKoneksi;
    protected userController userController = new userController();
    protected Stage stage;

    public viewController() {
        try {
            databaseKoneksi = koneksi.bukaKoneksiDB();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Error", "Gagal terhubung ke database!");
        }
    }

    public void loadController(userController userController) {
        this.userController = userController;
    }

    public userController getUserController() {
        return userController;
    }

    public void closeDatabaseConnection() {
        koneksi.tutupKoneksiDB();
    }

    private viewController getFrame(String viewName, Stage stage) {
        return switch (viewName) {
            case "SignUp" -> new SignUp(stage);
            case "Login" -> new Login(stage);
            case "Welcome" -> new Welcome(stage);
            case "Task" -> new Tasks(stage);
            case "AddTask" -> new AddTask(stage);
            case "Schedule" -> new Schedule(stage);
            default -> null;
        };
    }

    public <T extends viewController> void openFrame(T frame) {
        if (frame == null) {
            showAlert(AlertType.WARNING, "Error", "Frame tidak ditemukan!");
        } else {
            frame.loadController(userController);
            frame.afterOpen();
            this.getStage().close();
            frame.getStage().show();
        }
    }

    public void openFrame(String viewName, Stage stage) {
        viewController frame = getFrame(viewName, stage);
        openFrame(frame);
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void afterOpen() {}

    public void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
