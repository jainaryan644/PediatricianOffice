package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
                	String[] nameComponents = getFirstLastName(username);
                	String firstName = null;
                	String lastName = null;
                	String email = null;
                	firstName = nameComponents[0];
                	lastName = nameComponents[1];
                	email = nameComponents[2];
                    switch (role) {
                        case "patient":
                            showPatientPage(firstName, lastName, email, username);
                            break;
                        case "doctor":
                            showDoctorPage(firstName, lastName, email, username);
                            break;
                        case "nurse":
                            showNursePage(firstName, lastName, email, username);
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
    private String[] getFirstLastName(String username) {
        String[] names = new String[3];
        String userDetailsFilePath = username + "/userDetails.txt";

        try {
            File file = new File(userDetailsFilePath);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String username1 = reader.readLine();
                username1 = (username1.split(":")[1].trim());
                String password1 = reader.readLine();
                password1 = (password1.split(":")[1].trim());
                String role1 = reader.readLine();
                role1 = (role1.split(":")[1].trim());
                String first1 = reader.readLine();
                first1 = (first1.split(":")[1].trim());
                String last1 = reader.readLine();
                last1 = (last1.split(":")[1].trim());
                String email1 = reader.readLine();
                email1 = (email1.split(":")[1].trim());
                reader.close();

                names[0] = first1;
                names[1] = last1;
                names[2] = email1;
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
    }
    private boolean validateLogin(String username, String password, String role) {
        String userDetailsFilePath = username + "/userDetails.txt";
        try {
            File file = new File(userDetailsFilePath);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String username1 = reader.readLine();
                username1 = (username1.split(":")[1].trim());

                String password1 = reader.readLine();
                password1 = (password1.split(":")[1].trim());

                String role1 = reader.readLine();
                role1 = (role1.split(":")[1].trim());

                reader.close();

                // Check if provided credentials match the stored ones
                if (username1.equals(username) && password1.equals(password) && role1.equals(role)) {
                    System.out.println("Credentials match");
                    return true;
                } else {
                    System.out.println("Credentials do not match");
                    return false;
                }
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void showDoctorPage(String firstName, String lastName, String email, String doctorUsername) {
        DoctorPage doctorPage = new DoctorPage(primaryStage, firstName, lastName, email, doctorUsername);
        Scene doctorScene = new Scene(doctorPage, 1600, 1200);
        primaryStage.setScene(doctorScene);
    }

    private void showNursePage(String firstName, String lastName, String email, String doctorUsername) {
        NursePage nursePage = new NursePage(primaryStage, firstName, lastName, email, doctorUsername);
        Scene nurseScene = new Scene(nursePage, 1600, 1200);
        primaryStage.setScene(nurseScene);
    }

    private void showPatientPage(String firstName, String lastName, String email, String username) {
        PatientPage patientPage = new PatientPage(primaryStage, firstName, lastName, email, username);
        Scene patientScene = new Scene(patientPage, 1600, 1200);
        primaryStage.setScene(patientScene);
    }

    private void showRegisterPage() {
        RegisterPage registerPage = new RegisterPage(primaryStage);
        Scene registerScene = new Scene(registerPage, 600, 800);
        primaryStage.setScene(registerScene);
    }
}
