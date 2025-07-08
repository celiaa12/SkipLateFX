package View;

import Controller.viewController;
import Model.koneksi;
import Model.koneksi;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Welcome extends viewController {
    private Stage stage;
    private Label nameLabel;

    public Welcome(Stage stage) {
        this.stage = stage;
        
        nameLabel = new Label("Name");
        nameLabel.setFont(Font.font("SansSerif", 36));
        nameLabel.setTextFill(Color.web("#FACF39"));

        Label helloLabel = new Label("Hello");
        helloLabel.setFont(Font.font("SansSerif", 36));
        helloLabel.setTextFill(Color.WHITE);

        Label welcomeLabel = new Label(", Welcome to");
        welcomeLabel.setFont(Font.font("SansSerif", 36));
        welcomeLabel.setTextFill(Color.WHITE);

        Label titleLabel = new Label("Skip Late");
        titleLabel.setFont(Font.font("Xolonium", 48));
        titleLabel.setTextFill(Color.WHITE);

        Label sloganLabel = new Label("we are ready to keep you on time !");
        sloganLabel.setFont(Font.font("Charmie", 36));
        sloganLabel.setTextFill(Color.web("#FACF39"));

        Button nextButton = new Button("Next");
        nextButton.setFont(Font.font("SansSerif", 14));
        nextButton.setStyle("-fx-background-color: white; -fx-text-fill: #006666;");
        nextButton.setOnAction(e -> openFrame("Task", stage));

        ImageView icon = new ImageView("/icon/icons8-approved-96.png");
        icon.setFitHeight(96);
        icon.setFitWidth(96);

        VBox vbox = new VBox(10,
                new VBox(helloLabel, nameLabel, welcomeLabel),
                icon,
                titleLabel,
                sloganLabel,
                nextButton
        );
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setStyle("-fx-background-color: #006666;");

        Scene scene = new Scene(vbox, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Welcome");
        stage.show();

        afterOpen();
    }

    public void afterOpen() {
        String sql = "SELECT nameUser FROM user WHERE emailUser = ? AND passwordUser = ?";
        try (PreparedStatement preparedStatement = koneksi.bukaKoneksiDB().prepareStatement(sql)) {
            preparedStatement.setString(1, getUserController().getEmail());
            preparedStatement.setString(2, getUserController().getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userName = resultSet.getString("nameUser");
                nameLabel.setText(userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }               
}
