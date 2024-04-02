package application;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class LoginPage extends VBox {

    private Hyperlink forgotPasswordLink;
    private Button loginButton; // Declare loginButton as a member variable
    private TextField usernameField; // Declare usernameField as a member variable
    private PasswordField passwordField; // Declare passwordField as a member variable

    private ToggleGroup group; // Declare ToggleGroup as a member variable

    public LoginPage() {
        super(10);
        setAlignment(Pos.CENTER);
        Label label = new Label("Blue Angels Pediatrics");
        Label login = new Label("Login");

        usernameField = new TextField();
        usernameField.setPrefWidth(200); // Set preferred width for username field
        usernameField.setMaxWidth(200); // Set max width to prevent expansion
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPrefWidth(200); // Set preferred width for password field
        passwordField.setMaxWidth(200); // Set max width to prevent expansion
        passwordField.setPromptText("Password");

        HBox radioBox = new HBox(10);
        radioBox.setAlignment(Pos.CENTER);
        RadioButton patientRadio = new RadioButton("patient");
        RadioButton doctorRadio = new RadioButton("doctor");
        RadioButton nurseRadio = new RadioButton("nurse");
        group = new ToggleGroup(); // Initialize ToggleGroup
        patientRadio.setToggleGroup(group);
        doctorRadio.setToggleGroup(group);
        nurseRadio.setToggleGroup(group);
        radioBox.getChildren().addAll(patientRadio, doctorRadio, nurseRadio);

        loginButton = new Button("Login"); // Assign the button to the member variable
        forgotPasswordLink = new Hyperlink("No Account, Click here");

        // Add action to the login button to clear text fields
        loginButton.setOnAction(e -> {
            usernameField.clear();
            passwordField.clear();
        });

        getChildren().addAll(label, login, usernameField, passwordField, radioBox, loginButton, forgotPasswordLink);
    }

    public String getSelectedRole() {
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
        }
        return null; // No radio button selected
    }

    public Hyperlink getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}

