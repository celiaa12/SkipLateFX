package skiplatefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import View.Login;

/**
 *
 * @author User
 */
public class SkipLateFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Login loginFrame = new Login(primaryStage); 

        primaryStage.setTitle("SkipLate Login");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
