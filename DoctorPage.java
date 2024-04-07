//package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DoctorPage extends BorderPane {
    private Label patientNameLabel = new Label("Patient <Unknown>");
    private Label doctorNameLabel = new Label("<Unknown>");
    private Label doctorEmailLabel = new Label("<Unknown>");
    private Button logoutButton;
    private TextArea chatTextArea;
    private TextField chatInputField; // TextField for writing new messages
    private Button sendButton; // Button to send a message 

    public DoctorPage(Stage primaryStage) {
        // Main layout is BorderPane (it has 5 areas: top, right, bottom, left, center)
        super();
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        patientNameLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2e8b57;"); // Optional styling
        this.setTop(patientNameLabel);

        // left panel
        VBox leftPanel = setupLeftPanel();
        this.setLeft(leftPanel);

        // Text areas for the center
        TextArea visitSummaryTextArea = new TextArea();
        visitSummaryTextArea.setPromptText("Visit Summary");
        visitSummaryTextArea.setPrefHeight(390);
        TextArea healthConcernsTextArea = new TextArea();
        healthConcernsTextArea.setPromptText("Health Concerns");
        healthConcernsTextArea.setPrefHeight(390);

        // Center panel layout
        VBox centerPanel = new VBox(visitSummaryTextArea, healthConcernsTextArea);

        // Right panel components
        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);
        rightPanel.setPrefWidth(300);

        // Edit Prescription box
        VBox editPrescriptionBox = new VBox();
        editPrescriptionBox.setSpacing(5);

        Label prescriptionLabel = new Label("Edit Prescription");
        TextArea prescriptionTextArea = new TextArea();
        prescriptionTextArea.setPrefHeight(200); // You can set the preferred height as needed

        Button savePrescriptionButton = new Button("Save");
        Button submitPrescriptionButton = new Button("Submit");

        HBox prescriptionButtonsBox = new HBox(savePrescriptionButton, submitPrescriptionButton); // 10 is the spacing between buttons

        editPrescriptionBox.getChildren().addAll(prescriptionLabel, prescriptionTextArea, prescriptionButtonsBox);

        // Vaccinations box
        VBox vaccinationsBox = new VBox(new Label("Vaccinations"));
        TextArea vaccinationsTextArea = new TextArea();
        Button saveButton = new Button("Save");
        Button submitButton = new Button("Submit");
        HBox vaccinationButtonsBox = new HBox(saveButton, submitButton);
        vaccinationsBox.getChildren().addAll(vaccinationsTextArea, vaccinationButtonsBox);

        // Chat box
        VBox chatBox = new VBox(new Label("Chat"));
        chatTextArea = new TextArea();
        chatTextArea.setEditable(false);
        chatInputField = new TextField();
        sendButton = new Button("Send");
        HBox chatInputBox = new HBox(chatInputField, sendButton);
        //loadChatHistory("12345");
        chatBox.getChildren().addAll(chatTextArea, chatInputBox);
        loadChatHistory("12345");

        // Adding components to the right panel
        rightPanel.getChildren().addAll(editPrescriptionBox, vaccinationsBox, chatBox);

        // Set the main layout areas
        this.setLeft(leftPanel);
        this.setCenter(centerPanel);
        this.setRight(rightPanel);
    }

    private VBox setupLeftPanel() {
        // Existing setup for tabs and search, etc.
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(400);
        // Search bar at the top of the left panel
        TextField searchField = new TextField();
        searchField.setPromptText("Search User");

        Button searchButton = new Button("Search");
        // Create an HBox to hold the search field and button
        HBox searchBox = new HBox(5); // 5 is the spacing between the text field and button
        searchBox.getChildren().addAll(searchField, searchButton);

        // Tabs for the left panel
        TabPane tabPane = new TabPane();

        // Create a text area for each tab content
        TextArea prescriptionContent = new TextArea();
        prescriptionContent.setEditable(false); // Make the text area uneditable
        prescriptionContent.setPrefHeight(600); // Set preferred height
        prescriptionContent.setPromptText("Prescription information displayed here...");
        Tab prescriptionTab = new Tab("Prescription", prescriptionContent);

        TextArea immunizationContent = new TextArea();
        immunizationContent.setEditable(false); // Make the text area uneditable
        immunizationContent.setPrefHeight(600); // Set preferred height
        immunizationContent.setPromptText("Immunization records displayed here...");
        Tab immunizationTab = new Tab("Immunization Records", immunizationContent);

        TextArea pharmacyContent = new TextArea();
        pharmacyContent.setEditable(false); // Make the text area uneditable
        pharmacyContent.setPrefHeight(600); // Set preferred height
        pharmacyContent.setPromptText("Pharmacy information displayed here...");
        Tab pharmacyTab = new Tab("Pharmacy Information", pharmacyContent);

        TextArea visitsContent = new TextArea();
        visitsContent.setEditable(false); // Make the text area uneditable
        visitsContent.setPrefHeight(600); // Set preferred height
        visitsContent.setPromptText("Visit history displayed here...");
        Tab visitsTab = new Tab("Visits", visitsContent);

        // Make sure the tabs are not closable
        prescriptionTab.setClosable(false);
        immunizationTab.setClosable(false);
        pharmacyTab.setClosable(false);
        visitsTab.setClosable(false);

        // Add all tabs to the tab pane
        tabPane.getTabs().addAll(prescriptionTab, immunizationTab, pharmacyTab, visitsTab);

        // Doctor's profile box at the bottom
        VBox doctorProfileBox = new VBox(10); // 10 is the spacing between elements
        doctorProfileBox.setStyle("-fx-background-color: #add8e6; -fx-padding: 10;");
        doctorProfileBox.getChildren().addAll(doctorNameLabel, doctorEmailLabel, logoutButton);

        // Adding doctor's profile box and logout button to the left panel
        leftPanel.getChildren().addAll(tabPane, doctorProfileBox);

        return leftPanel;
    }

    public void setPatientName(String name) {
        if (name == null || name.trim().isEmpty()) {
            patientNameLabel.setText("Patient <Unknown>");
        } else {
            patientNameLabel.setText("Patient " + name);
        }
    }
    
    private void loadChatHistory(String patientID) {
        chatTextArea.clear(); // Clear previous messages
        Path chatFilePath = Paths.get(patientID + "Chat.txt");
        if (Files.exists(chatFilePath)) {
            try {
                // Read all lines from the chat file
                Files.readAllLines(chatFilePath).forEach(line -> chatTextArea.appendText(line + "\n"));
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions here
            }
        }
    }
    
    private void appendMessageToFile(String patientID, String sender, String message) {
        String filename = patientID + "Chat.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
            writer.write(timestamp + " | " + sender + " | " + message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions here
        }
    }
    
    private void sendMessage(String patientID, String message) {
        if (message == null || message.trim().isEmpty()) {
            // Handle empty message case (e.g., show an alert or error)
            return;
        }
        // Assuming 'Doctor' is the sender. Replace with actual doctor's identification.
        appendMessageToFile(patientID, "Doctor", message);
        // Reload chat history to display the new message
        loadChatHistory(patientID);
        // Clear the input field after sending the message
        chatInputField.clear();
    }


    private void logout(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene loginScene = new Scene(loginPage, 600, 800);
        primaryStage.setScene(loginScene);
    }
}
