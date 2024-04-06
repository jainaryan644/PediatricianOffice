package application;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterPage extends VBox {
    private TextField usernameField;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmpasswordField;
    private ToggleGroup group;
    private Stage primaryStage;

    public RegisterPage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        setAlignment(Pos.CENTER);

        Label label = new Label("Blue Angels Pediatrics");
        Label register = new Label("Register");

        firstNameField = new TextField();
        firstNameField.setPrefWidth(200);
        firstNameField.setMaxWidth(200);
        firstNameField.setPromptText("First Name");

        lastNameField = new TextField();
        lastNameField.setPrefWidth(200);
        lastNameField.setMaxWidth(200);
        lastNameField.setPromptText("Last Name");

        usernameField = new TextField();
        usernameField.setPrefWidth(200);
        usernameField.setMaxWidth(200);
        usernameField.setPromptText("Username");

        emailField = new TextField();
        emailField.setPrefWidth(200);
        emailField.setMaxWidth(200);
        emailField.setPromptText("Email");

        passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setPromptText("Password");

        confirmpasswordField = new PasswordField();
        confirmpasswordField.setPrefWidth(200);
        confirmpasswordField.setMaxWidth(200);
        confirmpasswordField.setPromptText("Confirm Password");

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

        Button confirmButton = new Button("Register");
        Label error = new Label("");
        confirmButton.setOnAction(event -> createFolderByUsername());

        getChildren().addAll(label, register, firstNameField, lastNameField, usernameField, emailField, passwordField, confirmpasswordField, radioBox, confirmButton, error);
    }

    private void createFolderByUsername() {
  
        String username = usernameField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmpasswordField.getText();
        ToggleButton selectedRole = (ToggleButton) group.getSelectedToggle();
        String role = selectedRole != null ? selectedRole.getText() : ""; 

        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        if (!username.isEmpty() && !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() && !role.isEmpty()) {
            File folder = new File(username);
            if (!folder.exists()) {
                if (folder.mkdir()) {
                    System.out.println("Folder created: " + folder.getAbsolutePath());

                    // Create role.txt file
                    File roleFile = new File(folder, "role.txt");
                    try {
                        if (roleFile.createNewFile()) {
                            System.out.println("role.txt file created: " + roleFile.getAbsolutePath());
                            Utils.writeToFile(roleFile, role);
                        } else {
                            System.out.println("role.txt file already exists: " + roleFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create role.txt file: " + e.getMessage());
                    }

                    // Create userDetails.txt file
                    File userDetailsFile = new File(folder, "userDetails.txt");
                    try {
                        if (userDetailsFile.createNewFile()) {
                            System.out.println("userDetails.txt file created: " + userDetailsFile.getAbsolutePath());
                            String userDetailsText = "Username: " + username + "\nFirst Name: " + firstName + "\nLast Name: " + lastName + "\nEmail: " + email + "\nPassword: " + password + "\nRole: " + role;
                            Utils.writeToFile(userDetailsFile, userDetailsText);
                        } else {
                            System.out.println("userDetails.txt file already exists: " + userDetailsFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create userDetails.txt file: " + e.getMessage());
                    }

                    LoginPage loginPage = new LoginPage(primaryStage);
                    Scene loginScene = new Scene(loginPage, 600, 800);
                    primaryStage.setScene(loginScene);
                } else {
                    System.out.println("Failed to create folder: " + folder.getAbsolutePath());
                }
            } else {
                System.out.println("Folder already exists: " + folder.getAbsolutePath());
            }
        } else {
            System.out.println("One or more fields are empty, cannot create folder and files.");
        }
    }
}

class Utils {
    public static void writeToFile(File file, String content) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            System.out.println("File written successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}