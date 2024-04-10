//package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class LoginPage extends VBox {

    private Hyperlink forgotPasswordLink;
    private Button loginButton;
    private TextField usernameField;
    private PasswordField passwordField;
    private Text loginStatus;
    private ToggleGroup group;
    private Runnable onRegisterRequested;
    private Consumer<String> onAuthenticated;
    private String authenticatedUsername = null; 


    public LoginPage(Stage primaryStage) {
        super(10);
        setAlignment(Pos.CENTER);
        initializeComponents(primaryStage);
    }
    
    private void initializeComponents(Stage primaryStage) {
        Label label = new Label("Blue Angels Pediatrics");
        Label login = new Label("Login");
        usernameField = new TextField();
        passwordField = new PasswordField();
        setupFields();
        
        loginButton = new Button("Login");
        forgotPasswordLink = new Hyperlink("No Account, Click here");
        forgotPasswordLink.setOnAction(e -> {
            if (onRegisterRequested != null) {
                onRegisterRequested.run();
            }
        });
        loginStatus = new Text();
        
        RadioButton patientRadio = new RadioButton("patient");
        RadioButton doctorRadio = new RadioButton("doctor");
        RadioButton nurseRadio = new RadioButton("nurse");
        group = new ToggleGroup();
        patientRadio.setToggleGroup(group);
        doctorRadio.setToggleGroup(group);
        nurseRadio.setToggleGroup(group);
        HBox radioBox = new HBox(10, patientRadio, doctorRadio, nurseRadio);
        radioBox.setAlignment(Pos.CENTER);
        
        loginButton.setOnAction(e -> authenticateUser(primaryStage));

        getChildren().addAll(label, login, usernameField, passwordField, radioBox, loginButton, forgotPasswordLink, loginStatus);
    }
    

    private void setupFields() {
        usernameField.setPrefWidth(200);
        usernameField.setMaxWidth(200);
        usernameField.setPromptText("Username");

        passwordField.setPrefWidth(200);
        passwordField.setMaxWidth(200);
        passwordField.setPromptText("Password");
    }

    private void authenticateUser(Stage primaryStage) {
        String selectedRole = getSelectedRole();
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (Authentication.authenticate(selectedRole, username, password)) {
        	this.authenticatedUsername = username; 
            loginStatus.setText("Login successful for " + selectedRole);
            Scene targetScene = null;
            switch (selectedRole) {
                case "patient":
                	targetScene = new Scene(new PatientPage(primaryStage, authenticatedUsername), 1500, 800);
                    break;
                case "doctor":
                    targetScene = new Scene(new DoctorPage(primaryStage, authenticatedUsername), 1500, 800);
                    break;
                case "nurse":
                    targetScene = new Scene(new NursePage(primaryStage, authenticatedUsername), 600, 800);
                    break;
            }
            if (targetScene != null) {
                Stage currentStage = (Stage) this.getScene().getWindow();
                currentStage.setScene(targetScene);
            }
            if (onAuthenticated != null) {
                onAuthenticated.accept(selectedRole);
            }
        } else {
            loginStatus.setText("Login failed. Incorrect username or password.");
            usernameField.clear();
            passwordField.clear();
        }
    }
    
    public String getUsername()
    {
    	return usernameField.getText().trim();
    }

    
    public String getAuthenticatedUsername() {
        return authenticatedUsername;
    }

    public void setOnRegisterRequested(Runnable onRegisterRequested) {
        this.onRegisterRequested = onRegisterRequested;
    }

    public void setOnAuthenticated(Consumer<String> onAuthenticated) {
        this.onAuthenticated = onAuthenticated;
    }

    public String getSelectedRole() {
        RadioButton selectedRadioButton = (RadioButton) group.getSelectedToggle();
        return selectedRadioButton != null ? selectedRadioButton.getText() : "";
    }

    public Hyperlink getForgotPasswordLink() {
        return forgotPasswordLink;
    }

    public Button getLoginButton() {
        return loginButton;
    }
}
