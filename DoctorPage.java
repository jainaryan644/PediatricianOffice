package application;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class DoctorPage extends BorderPane {

    private Label patientNameLabel = new Label("Patient <Unknown>");
    private Label doctorNameLabel = new Label("<Unknown>");
    private Label doctorEmailLabel = new Label("<Unknown>");
    private Button logoutButton;
    private ListView<String> chatListView;
    private TextField chatInputField;
    private TextArea chatDisplayArea;
    private Button sendButton;
    
    private synchronized void appendMessageToFile(String patientID, String sender, String message) {
        String filename = patientID + "_chat.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String entry = String.format("%s: %s\n", sender, message);
            writer.write(entry);
        } catch (IOException ex) {
            ex.printStackTrace();
            // You might want to show an alert dialog here instead
        }
    }

    private void loadChatHistory(String patientID) {
        String filename = patientID + "_chat.txt";
		chatDisplayArea.clear(); // Clear previous content

        try (Stream<String> stream = Files.lines(Paths.get(filename))) {
            String history = stream.collect(Collectors.joining("\n"));
            chatDisplayArea.appendText(history);
        } catch (IOException ex) {
            ex.printStackTrace();
            // You might want to handle this case (e.g., show an alert or a placeholder text)
        }
    }
    
    private void sendMessage() {

		String message = chatInputField.getText().trim();
        if (!message.isEmpty()) {
            // Append the message to the chat display area
            chatDisplayArea.appendText(message + "\n");
            // TODO: Append the message to the chat file for persistence
            
            chatInputField.clear(); // Clear the input field after sending the message
        }
    }
    
    private void initializeChatComponents() {
        chatDisplayArea = new TextArea();
        chatDisplayArea.setEditable(false); // Do not allow editing of chat display
        chatDisplayArea.setPrefHeight(200); // Set preferred height, or use binding to adjust dynamically
        
        chatInputField = new TextField();
        chatInputField.setPromptText("Type your message here...");

        sendButton = new Button("Send");
        sendButton.setOnAction(event -> sendMessage());

        // Layout for the chat box
        VBox chatBox = new VBox(5); // 5 is the spacing between children
        chatBox.getChildren().addAll(new Label("Chat"), chatDisplayArea, new HBox(chatInputField, sendButton));

        this.setBottom(chatBox); // Add the chat box to the bottom of the BorderPane
    }
   
    public DoctorPage() {
        // Main layout is BorderPane (it has 5 areas: top, right, bottom, left, center)
        super();
        logoutButton = new Button("Logout");
        
        patientNameLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2e8b57;"); // Optional styling
        this.setTop(patientNameLabel);
        
        //left panel
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
        initializeChatComponents();
        //ListView<String> chatListView = new ListView<>();
        //chatListView = new ListView<>();
        //TextField chatInputField = new TextField();
        //Button sendButton = new Button("Send");
        //sendButton.setOnAction(e -> {
        //    String message = chatInputField.getText().trim();
        //    if (!message.isEmpty()) {
         //       appendMessageToFile("patientID", "Doctor", message); // Use appropriate patientID NEED TO ADD
         //       chatInputField.clear();
        //        displayChatMessages("patientID"); // Use appropriate patientID
        //    }
        //});
        HBox chatInputBox = new HBox(chatInputField, sendButton);
        chatBox.getChildren().addAll(chatListView, chatInputBox);
        

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

     // ...

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
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(event -> {
            // Handle logout action here
            System.out.println("Logout action goes here.");
        });
        doctorProfileBox.getChildren().addAll(doctorNameLabel, doctorEmailLabel, logoutButton);

        // Logout button
       

        // Adding doctor's profile box and logout button to the left panel
        leftPanel.getChildren().addAll(tabPane, doctorProfileBox);

        return leftPanel;
    }

    
    public void setPatientName(String name) {
        if(name == null || name.trim().isEmpty()) {
            patientNameLabel.setText("Patient <Unknown>");
        } else {
            patientNameLabel.setText("Patient " + name);
        }
    }
    
    public Button getLogoutButton() {
        return logoutButton;
    }
}
