package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
    	LoginPage loginPage = new LoginPage(primaryStage); // Pass primaryStage to HomePage constructor
        Scene loginScene = new Scene(loginPage, 600, 800);

        primaryStage.setTitle("Blue Angels Pediatrics");
        primaryStage.setScene(loginScene);
        primaryStage.show();
        
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}
