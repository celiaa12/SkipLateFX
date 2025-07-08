package View;

import Controller.viewController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp extends viewController {
    private Stage stage;
    
    private TextField fullNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;

    public SignUp(Stage stage) {
        this.stage = stage;
        
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15));
        ImageView logo = new ImageView(getClass().getResource("/icon/icons8-approved-24-2.png").toExternalForm());
        Label title = new Label("Skip Late");
        title.setStyle("-fx-font-family: 'Xolonium'; -fx-font-size: 18px; -fx-text-fill: #006666;");
        header.getChildren().addAll(logo, title);

        // Form Title
        Label signUpLabel = new Label("SIGN UP");
        signUpLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #006666;");

        // Form Inputs
        fullNameField = new TextField();
        fullNameField.setPromptText("Full Name");

        emailField = new TextField();
        emailField.setPromptText("Email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");

        Button signUpButton = new Button("Sign Up");
        signUpButton.setStyle("-fx-background-color: #006666; -fx-text-fill: white;");
        signUpButton.setPrefWidth(345);
        signUpButton.setOnAction(e -> handleSignUp(stage));

        VBox form = new VBox(10, signUpLabel,
                new Label("Full Name"), fullNameField,
                new Label("Email"), emailField,
                new Label("Password"), passwordField,
                new Label("Confirm Password"), confirmPasswordField,
                signUpButton
        );
        form.setPadding(new Insets(20));
        form.setAlignment(Pos.TOP_CENTER);

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(form);

        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Sign Up");
        stage.show();
    }

    private void handleSignUp(Stage stage) {
        String fullname = fullNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String password2 = confirmPasswordField.getText();

        if (!password.equals(password2)) {
            showAlert(Alert.AlertType.ERROR, "Password tidak cocok.");
            return;
        }

        try {
            String sql = "INSERT INTO user (idUser, nameUser, emailUser, passwordUser) VALUES (NULL, ?, ?, ?)";
            try (PreparedStatement preparedStatement = databaseKoneksi.prepareStatement(sql)) {
                preparedStatement.setString(1, fullname);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
            }

            showAlert(Alert.AlertType.INFORMATION, "User baru berhasil dibuat!");
            openFrame("Login", stage);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Gagal menyimpan data: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }                
}
