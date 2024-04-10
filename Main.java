//package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage( primaryStage);
        Scene loginScene = new Scene(loginPage, 600, 800);
        
        RegisterPage registerPage = new RegisterPage(primaryStage); // Assuming a no-argument constructor or modify as needed
        Scene registerScene = new Scene(registerPage, 600, 800);

        loginPage.setOnRegisterRequested(() -> primaryStage.setScene(registerScene));


        // Set the authenticated role callback for navigation
        loginPage.setOnAuthenticated(role -> {
            Scene targetScene = null;
            switch (role) {
                case "patient":
                    targetScene = new Scene(new PatientPage( primaryStage, loginPage.getAuthenticatedUsername()), 1500, 800);
                    break;
                case "doctor":
                	  targetScene = new Scene(new DoctorPage( primaryStage, loginPage.getAuthenticatedUsername()), 1500, 800);
                    break;
                case "nurse":
                    targetScene = new Scene(new NursePage( primaryStage, loginPage.getAuthenticatedUsername()), 600, 800);
                    break;
            }
            if (targetScene != null) {
                primaryStage.setScene(targetScene);
            }
        });

        primaryStage.setTitle("Blue Angels Pediatrics");
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
