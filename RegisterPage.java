package application;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class RegisterPage extends VBox {

    private Button confirmButton;

    public RegisterPage() {
        super(10);
        setAlignment(Pos.CENTER);
        Label label = new Label("Blue Angels Pediatrics");
        Label register = new Label("Register");
        
        TextField firstNameField = new TextField();
        firstNameField.setPrefWidth(200); // Set preferred width for username field
        firstNameField.setMaxWidth(200); // Set max width to prevent expansion
        firstNameField.setPromptText("First Name");
        
        TextField lastNameField = new TextField();
        lastNameField.setPrefWidth(200); // Set preferred width for username field
        lastNameField.setMaxWidth(200); // Set max width to prevent expansion
        lastNameField.setPromptText("Last Name");
        
        TextField emailField = new TextField();
        emailField.setPrefWidth(200); // Set preferred width for username field
        emailField.setMaxWidth(200); // Set max width to prevent expansion
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPrefWidth(200); // Set preferred width for password field
        passwordField.setMaxWidth(200); // Set max width to prevent expansion
        passwordField.setPromptText("Password");
        
        PasswordField confirmpasswordField = new PasswordField();
        confirmpasswordField.setPrefWidth(200); // Set preferred width for password field
        confirmpasswordField.setMaxWidth(200); // Set max width to prevent expansion
        confirmpasswordField.setPromptText("Confirm Password");
        
        HBox radioBox = new HBox(10);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton patientRadio = new RadioButton("patient");
        RadioButton doctorRadio = new RadioButton("doctor");
        RadioButton nurseRadio = new RadioButton("nurse");
        ToggleGroup group = new ToggleGroup();
        patientRadio.setToggleGroup(group);
        doctorRadio.setToggleGroup(group);
        nurseRadio.setToggleGroup(group);
        radioBox.getChildren().addAll(patientRadio, doctorRadio, nurseRadio);

        confirmButton = new Button("Register"); // Renamed from loginButton

        getChildren().addAll(label, register, firstNameField, lastNameField,  emailField, passwordField, confirmpasswordField, radioBox, confirmButton); // Adjusted label to "Register"
    }

    public Button getConfirmButton() {
        return confirmButton;
    }
}
