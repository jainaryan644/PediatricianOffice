package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginPage extends VBox {

    private Hyperlink forgotPasswordLink;
    private Button loginButton; 
    private TextField usernameField; 
    private PasswordField passwordField; 
    private ToggleGroup group; 
    private Stage primaryStage;

    public LoginPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setAlignment(Pos.CENTER);
        Label label = new Label("Blue Angels Pediatrics");
        Label login = new Label("Login");

        usernameField = new TextField();
        usernameField.setPrefWidth(200); 
        usernameField.setMaxWidth(200); 
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPrefWidth(200); 
        passwordField.setMaxWidth(200); 
        passwordField.setPromptText("Password");

        HBox radioBox = new HBox(10);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton patientRadio = new RadioButton("patient");
        RadioButton doctorRadio = new RadioButton("doctor");
        RadioButton nurseRadio = new RadioButton("nurse");
        group = new ToggleGroup(); 
        patientRadio.setToggleGroup(group);
        doctorRadio.setToggleGroup(group);
        nurseRadio.setToggleGroup(group);
        radioBox.getChildren().addAll(patientRadio, doctorRadio, nurseRadio);

        loginButton = new Button("Login"); 
        forgotPasswordLink = new Hyperlink("No Account, Click here");
        Label error = new Label("");

        // Add action to the login button to validate login credentials
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if (group.getSelectedToggle() != null) {
                RadioButton selectedRadio = (RadioButton) group.getSelectedToggle();
                String role = selectedRadio.getText();
                if (validateLogin(username, password, role)) {
                    switch (role) {
                        case "patient":
                            showPatientPage();
                            break;
                        case "doctor":
                            showDoctorPage();
                            break;
                        case "nurse":
                            showNursePage();
                            break;
                    }
                } else {
                    error.setText("Invalid username, password, or role.");
                }
            }
        });

        // Add action to the "No Account" hyperlink to navigate to the registration page
        forgotPasswordLink.setOnAction(e -> showRegisterPage());

        getChildren().addAll(label, login, usernameField, passwordField, radioBox, loginButton, forgotPasswordLink, error);
    }

    private boolean validateLogin(String username, String password, String role) {
        String userDetailsFilePath = username + "/userDetails.txt";
        try {
            File file = new File(userDetailsFilePath);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Password:")) {
                        String storedPassword = line.substring("Password:".length()).trim();
                        if (password.equals(storedPassword)) {
                            // Check if role matches
                            if (line.contains("Role: " + role)) {
                                reader.close();
                                return true;
                            }
                        }
                    }
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showDoctorPage() {
        DoctorPage doctorPage = new DoctorPage(primaryStage);
        Scene doctorScene = new Scene(doctorPage, 600, 800);
        primaryStage.setScene(doctorScene);
    }

    private void showNursePage() {
        NursePage nursePage = new NursePage(primaryStage);
        Scene nurseScene = new Scene(nursePage, 600, 800);
        primaryStage.setScene(nurseScene);
    }

    private void showPatientPage() {
        PatientPage patientPage = new PatientPage(primaryStage);
        Scene patientScene = new Scene(patientPage, 600, 800);
        primaryStage.setScene(patientScene);
    }

    private void showRegisterPage() {
        RegisterPage registerPage = new RegisterPage(primaryStage);
        Scene registerScene = new Scene(registerPage, 600, 800);
        primaryStage.setScene(registerScene);
    }
}
