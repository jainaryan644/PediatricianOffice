//package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class RegisterPage extends VBox {
    private Button confirmButton;
    private TextField firstNameField;
    private TextField birthdayField;
    private TextField lastNameField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
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
        
        birthdayField = new TextField();
        birthdayField.setPrefWidth(200);
        birthdayField.setMaxWidth(200);
        birthdayField.setPromptText("Birthday mmddyyyy");

        emailField = new TextField();
        emailField.setPrefWidth(200);
        emailField.setMaxWidth(200);
        emailField.setPromptText("Email");

        passwordField = new PasswordField();
        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setPromptText("Password");

        confirmPasswordField = new PasswordField();
        confirmPasswordField.setPrefWidth(200);
        confirmPasswordField.setMaxWidth(200);
        confirmPasswordField.setPromptText("Confirm Password");

        confirmButton = new Button("Register");
        Label error = new Label("");

        confirmButton.setOnAction(event -> createFolderByUsername(primaryStage));

        getChildren().addAll(label, register, firstNameField, lastNameField, birthdayField, emailField, passwordField, confirmPasswordField, confirmButton, error);
    }
    

    private void createFolderByUsername(Stage primaryStage) {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String birthday = birthdayField.getText().trim(); // Assuming the format is yyyyMMdd for simplicity
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        // Assuming you have fields for phone, address, and insurance as well
        

        if (firstName.isEmpty() || lastName.isEmpty() || birthday.isEmpty() || email.isEmpty() || !password.equals(confirmPassword) || password.isEmpty()) {
            showAlert("Validation failed", "Please ensure all fields are correctly filled and passwords match.");
            return; // Exit if validation fails
        }

        String username = (firstName + lastName + birthday).replaceAll("\\s+", "").toLowerCase(); // Username generation

        // Create directory for the patient
        File patientDir = new File(username);
        if (!patientDir.exists()) {
            if (!patientDir.mkdirs()) {
                showAlert("Error", "Could not create directory for user.");
                return;
            }
        }
        
        String Doctor = PatientDoctorLinker.getRoleName("Doctor");
        String Nurse = PatientDoctorLinker.getRoleName("Nurse");

        // Write general information to generalInfo.txt
        File generalInfoFile = new File(patientDir, "generalInfo.txt");
        try (PrintWriter out = new PrintWriter(generalInfoFile)) {
            out.println("First Name: " + firstName);
            out.println("Last Name: " + lastName);
            out.println("Email: " + email);
            out.println("Phone: " + "");
            out.println("Birthday: " + birthday);
            out.println("Password: " + password); // Consider storing hashed password instead
            out.println("Address: " + "");
            out.println("Insurance: " + "");
            out.println("Pharmacy:" + "");
            out.println("Doctor:" + Doctor);
            out.println("Nurse:" + Nurse);
        } catch (IOException e) {
            showAlert("Error", "An error occurred while writing general info: " + e.getMessage());
            return;
        }

        // Append username and password to the patients.txt file
        try (FileWriter fw = new FileWriter("patients.txt", true); BufferedWriter bw = new BufferedWriter(fw); PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "%" + password); // Using % as a delimiter
        } catch (IOException e) {
            showAlert("Error", "An error occurred while writing to the patients file: " + e.getMessage());
        }

        // Create or update username_visits.txt within the patient directory
        File visitsFile = new File(patientDir, username + "_visits.txt");
        if (!visitsFile.exists()) {
            try {
                visitsFile.createNewFile();
            } catch (IOException e) {
                showAlert("Error", "An error occurred while creating the visits file: " + e.getMessage());
            }
        }

        // Show success alert and redirect to LoginPage
        showAlert("Registration Successful", "Your registration is successful. Your username is: " + username);
        redirectToLoginPage(primaryStage);
    }
    


    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private void redirectToLoginPage(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene loginScene = new Scene(loginPage, 600, 800);
        primaryStage.setScene(loginScene);
    }
    
    public Button getConfirmButton() {
        return confirmButton;
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
