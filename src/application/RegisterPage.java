package application;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
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
    private TextField birthdayField;
    private Hyperlink forgotPasswordLink;

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
        birthdayField.setPromptText("Birthday Year (####)");

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

        group = new ToggleGroup();
        patientRadio.setToggleGroup(group);
        patientRadio.setSelected(true); // This line sets the radio button as selected by default

        radioBox.getChildren().addAll(patientRadio);

        Button confirmButton = new Button("Register");
        forgotPasswordLink = new Hyperlink("Already Registered? Click Here");
        Label error = new Label("");
        confirmButton.setOnAction(event -> createFolderByUsername());
        forgotPasswordLink.setOnAction(e -> {
        	LoginPage loginPage = new LoginPage(primaryStage);
            Scene loginScene = new Scene(loginPage, 600, 800);
            primaryStage.setScene(loginScene);
        });

        getChildren().addAll(label, register, firstNameField, lastNameField, birthdayField, emailField, passwordField, confirmpasswordField, radioBox, confirmButton, forgotPasswordLink, error);
    }

    private void createFolderByUsername() {
    	String [] roles = assigned();
        String birthday = birthdayField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String username = firstName + lastName + birthday;
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
                            String userDetailsText = "Username: " + username  + "\nPassword: " + password + "\nRole: " + role + "\nFirst Name: " + firstName + "\nLast Name: " + lastName + "\nEmail: " + email + "\nBirthday: " + birthday  + "\nAssigned Doctor: " + roles[0] + "\nAssigned Nurse: " + roles[1];
                            Utils.writeToFile(userDetailsFile, userDetailsText);
                        } else {
                            System.out.println("userDetails.txt file already exists: " + userDetailsFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create userDetails.txt file: " + e.getMessage());
                    }

                    // Create prescription.txt file
                    File prescriptionFile = new File(folder, "prescription.txt");
                    try {
                        if (prescriptionFile.createNewFile()) {
                            System.out.println("prescription.txt file created: " + prescriptionFile.getAbsolutePath());
                            // Add initial content or leave it empty
                        } else {
                            System.out.println("prescription.txt file already exists: " + prescriptionFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create prescription.txt file: " + e.getMessage());
                    }

                    // Create vaccination.txt file
                    File vaccinationFile = new File(folder, "vaccinations.txt");
                    try {
                        if (vaccinationFile.createNewFile()) {
                            System.out.println("vaccination.txt file created: " + vaccinationFile.getAbsolutePath());
                            // Add initial content or leave it empty
                        } else {
                            System.out.println("vaccination.txt file already exists: " + vaccinationFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create vaccination.txt file: " + e.getMessage());
                    }
                    
                    File visitsFile = new File(folder, "visits.txt");
                    try {
                        if (visitsFile.createNewFile()) {
                            System.out.println("visits.txt file created: " + visitsFile.getAbsolutePath());
                            // Add initial content or leave it empty
                        } else {
                            System.out.println("visits.txt file already exists: " + visitsFile.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create visits.txt file: " + e.getMessage());
                    }
                    
                    File patientInfo= new File(folder, "patientInfo.txt");
                    try {
                        if (patientInfo.createNewFile()) {
                            System.out.println("visits.txt file created: " + patientInfo.getAbsolutePath());
                            // Add initial content or leave it empty
                        } else {
                            System.out.println("patientInfo.txt file already exists: " + patientInfo.getAbsolutePath());
                        }
                    } catch (IOException e) {
                        System.out.println("Failed to create patientInfo.txt file: " + e.getMessage());
                    }
                    
                    File visitFolder = new File(folder, "visits");
                    
                    if (!visitFolder.exists()) {
                        if (visitFolder.mkdir()) {
                            System.out.println("Visits folder created: " + visitFolder.getAbsolutePath());
                        } else {
                            System.out.println("Failed to create visits folder: " + visitFolder.getAbsolutePath());
                        }
                    } else {
                        System.out.println("Visits folder already exists: " + visitFolder.getAbsolutePath());
                    }
                    
                    File chatsFolder = new File(folder, "chats");

                    if (!chatsFolder.exists()) {
                        if (chatsFolder.mkdir()) {
                            System.out.println("Chats folder created: " + chatsFolder.getAbsolutePath());

                            // Create the two text files inside the chatsFolder
                            try {
                                File role0File = new File(chatsFolder, roles[0] + ".txt");
                                File role1File = new File(chatsFolder, roles[1] + ".txt");

                                if (role0File.createNewFile()) {
                                    System.out.println("File created: " + role0File.getAbsolutePath());
                                } else {
                                    System.out.println("Failed to create file: " + role0File.getAbsolutePath());
                                }

                                if (role1File.createNewFile()) {
                                    System.out.println("File created: " + role1File.getAbsolutePath());
                                } else {
                                    System.out.println("Failed to create file: " + role1File.getAbsolutePath());
                                }
                            } catch (IOException e) {
                                System.out.println("Error creating files: " + e.getMessage());
                            }

                        } else {
                            System.out.println("Failed to create chats folder: " + chatsFolder.getAbsolutePath());
                        }
                    } else {
                        System.out.println("Chats folder already exists: " + chatsFolder.getAbsolutePath());

                        // Create the two text files inside the chatsFolder
                        try {
                            File role0File = new File(chatsFolder, username + "to" + roles[0] + ".txt");
                            File role1File = new File(chatsFolder, username + "to" + roles[1] + ".txt");

                            if (role0File.createNewFile()) {
                                System.out.println("File created: " + role0File.getAbsolutePath());
                            } else {
                                System.out.println("Failed to create file: " + role0File.getAbsolutePath());
                            }

                            if (role1File.createNewFile()) {
                                System.out.println("File created: " + role1File.getAbsolutePath());
                            } else {
                                System.out.println("Failed to create file: " + role1File.getAbsolutePath());
                            }
                        } catch (IOException e) {
                            System.out.println("Error creating files: " + e.getMessage());
                        }
                    }
                    
                   showAlert("Registration Successful", "username is " + username + "\n Doctor is " + roles[0] + "\n Nurse is " + roles[1]);
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
    private String[] assigned() {
        // Read the contents of the nurses.txt and doctors.txt files
        String[] nurses = readLinesFromFile("nurses.txt");
        String[] doctors = readLinesFromFile("doctors.txt");

        // Select a random doctor and a random nurse
        String randomDoctor = doctors[new Random().nextInt(doctors.length)];
        String randomNurse = nurses[new Random().nextInt(nurses.length)];

        // Return the array with the random doctor and nurse
        return new String[] { randomDoctor, randomNurse };
    }

    private String[] readLinesFromFile(String fileName) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.toArray(new String[0]);
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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