package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginPage loginPage = new LoginPage();
        RegisterPage registerPage = new RegisterPage();
        PatientPage patientPage = new PatientPage(); // Import statement corrected
        DoctorPage doctorPage = new DoctorPage();
        NursePage nursePage = new NursePage();
        
        Scene loginScene = new Scene(loginPage, 600, 800);
        Scene registerScene = new Scene(registerPage, 600, 800);
        Scene patientScene = new Scene(patientPage, 600, 800);
        Scene doctorScene = new Scene(doctorPage, 600, 800);
        Scene nurseScene = new Scene(nursePage, 600, 800);

        // Link actions
        loginPage.getForgotPasswordLink().setOnAction(e -> primaryStage.setScene(registerScene));
        registerPage.getConfirmButton().setOnAction(e -> primaryStage.setScene(loginScene));
        doctorPage.getLogoutButton().setOnAction(e -> primaryStage.setScene(loginScene));
        patientPage.getLogoutButton().setOnAction(e -> primaryStage.setScene(loginScene));
        nursePage.getLogoutButton().setOnAction(e -> primaryStage.setScene(loginScene));
        loginPage.getLoginButton().setOnAction(e -> {
        	System.out.println("logout");
            String selectedRole = loginPage.getSelectedRole();
            if (selectedRole != null) {
                switch (selectedRole) {
                    case "patient":
                        primaryStage.setScene(patientScene);
                        break;
                    case "doctor":
                        primaryStage.setScene(doctorScene);
                        break;
                    case "nurse":
                        primaryStage.setScene(nurseScene);
                        break;
                    default:
                        // Handle unexpected case
                        break;
                }
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
