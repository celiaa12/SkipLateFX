package View;

import Controller.viewController;
import Model.koneksi;
import Model.koneksi;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends viewController {

    private koneksi koneksi = new koneksi();
    private TextField emailField;
    private PasswordField passwordField;
    private Stage stage;

    public Login(Stage stage) {
        this.stage = stage;
        showLoginUI();
    }

    private void showLoginUI() {
        // Panel kiri (Logo dan branding)
        VBox leftPane = new VBox(10);
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setPadding(new Insets(50));
        leftPane.setStyle("-fx-background-color: #006666;");

        ImageView logo = new ImageView(new Image(getClass().getResource("/icon/icons8-approved-96.png").toExternalForm()));
        Label title = new Label("Skip Late");
        title.setFont(Font.font("Xolonium", 48));
        title.setTextFill(Color.WHITE);

        Label subtitle = new Label("keep you on time");
        subtitle.setFont(Font.font("Charmie", 36));
        subtitle.setTextFill(Color.web("#FADF39"));

        Label copyright = new Label("Copyright © 2023 PBB Company All rights reserved.");
        copyright.setFont(Font.font("SansSerif", 12));
        copyright.setTextFill(Color.web("#78AEAE"));

        leftPane.getChildren().addAll(logo, title, subtitle, copyright);

        // Panel kanan (Form login)
        VBox rightPane = new VBox(10);
        rightPane.setAlignment(Pos.CENTER_LEFT);
        rightPane.setPadding(new Insets(40));
        rightPane.setStyle("-fx-background-color: white;");

        Label loginLabel = new Label("LOGIN");
        loginLabel.setFont(Font.font("SansSerif", 36));
        loginLabel.setTextFill(Color.web("#006666"));

        Label emailLabel = new Label("Email");
        emailField = new TextField();
        emailField.setPrefWidth(300);

        Label passwordLabel = new Label("Password");
        passwordField = new PasswordField();
        passwordField.setPrefWidth(300);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #006666; -fx-text-fill: white;");
        loginButton.setOnAction(e -> handleLogin());

        HBox signUpBox = new HBox(5);
        Label noAccountLabel = new Label("Don't have an account?");
        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #006666; -fx-underline: true;");
        signUpButton.setOnAction(e -> openFrame("SignUp", stage));
        signUpBox.getChildren().addAll(noAccountLabel, signUpButton);
        signUpBox.setAlignment(Pos.CENTER_LEFT);

        rightPane.getChildren().addAll(loginLabel, emailLabel, emailField, passwordLabel, passwordField, loginButton, signUpBox);

        // Gabungkan kedua panel
        HBox root = new HBox(leftPane, rightPane);
        Scene scene = new Scene(root, 800, 500);

        stage.setScene(scene);
        stage.setTitle("Login - SkipLate");
        stage.show();
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        getUserController().setUser(email, password);

        if (validateLogin(email, password)) {
            openFrame("Welcome", stage);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Login failed. Invalid email or password.");
            alert.showAndWait();
        }
    }

    private boolean validateLogin(String email, String password) {
        String sql = "SELECT * FROM user WHERE emailUser = ? AND passwordUser = ?";

        try (PreparedStatement preparedStatement = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }             
}
