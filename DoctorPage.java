//package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

public class DoctorPage extends BorderPane {
	private Label patientNameLabel = new Label("");
    private Label doctorNameLabel = new Label("");
    private Label doctorEmailLabel = new Label("");
    private Button logoutButton;
    private TextArea chatTextArea;
    private TextField chatInputField; // TextField for writing new messages
    private Button sendButton; // Button to send a message 

    public DoctorPage(Stage primaryStage, String firstName, String lastName, String email) {
        // Main layout is BorderPane (it has 5 areas: top, right, bottom, left, center)
        super();
        logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));

        patientNameLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2e8b57;"); // Optional styling
        this.setTop(patientNameLabel);

        // Add the search box to the top
        TextField searchField = new TextField();
        searchField.setPromptText("Search Patient");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String username = searchField.getText();
            String[][] allData = getAllData(username);
            // Call setupLeftPanel with allData
            VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData);
            this.setLeft(leftPanel); // Set left panel after retrieving allData
        });

        HBox searchBox = new HBox(5); // 5 is the spacing between the text field and button
        searchBox.getChildren().addAll(searchField, searchButton);

        this.setTop(new VBox(patientNameLabel, searchBox));

       // left panel


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
        sendButton.setOnAction(e -> {
            String message = chatInputField.getText();
            if (!message.isEmpty()) {
                sendMessage("12345", "Doctor", message); // Replace "Doctor" with the actual doctor's name
                chatInputField.clear(); // Clear the text field after sending the message
                loadChatHistory("12345"); // Load the chat history again to display the new message
            }
        });

        // Adding components to the right panel
        rightPanel.getChildren().addAll(editPrescriptionBox, vaccinationsBox, chatBox);

        // Set the main layout areas
        //this.setLeft(leftPanel);
        this.setCenter(centerPanel);
        this.setRight(rightPanel);
    }

    private VBox setupLeftPanel(String firstName, String lastName, String email, String[][] allData) {
        
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(400);

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

        // Add data to prescription content if not null
        if (allData[0] != null) {
            StringBuilder prescriptionBuilder = new StringBuilder();
            for (String item : allData[0]) {
                if (item != null) {
                    prescriptionBuilder.append(item).append("\n");
                }
            }
            prescriptionContent.setText(prescriptionBuilder.toString());
        }

        // Add data to immunization content if not null
        if (allData[1] != null) {
            StringBuilder immunizationBuilder = new StringBuilder();
            for (String item : allData[1]) {
                if (item != null) {
                    immunizationBuilder.append(item).append("\n");
                }
            }
            immunizationContent.setText(immunizationBuilder.toString());
        }

        // Doctor's profile box at the bottom
        VBox doctorProfileBox = new VBox(10); // 10 is the spacing between elements
        doctorProfileBox.setStyle("-fx-background-color: #add8e6; -fx-padding: 10;");
        Label doctorNameLabel = new Label("Doctor: " + firstName + " " + lastName);
        Label doctorEmailLabel = new Label("Email: " + email);
        Button logoutButton = new Button("Logout");
        doctorProfileBox.getChildren().addAll(doctorNameLabel, doctorEmailLabel, logoutButton);

        // Adding doctor's profile box and logout button to the left panel
        leftPanel.getChildren().addAll(tabPane, doctorProfileBox);

        return leftPanel;
    }

    private String[][] getAllData(String username) {
        System.out.println(username);
        String[][] allData = new String[2][]; // Initialize the array with 2 rows
        String prescriptionFilePath = username + "/prescription.txt";
        String vaccinationsFilePath = username + "/vaccinations.txt";

        try {
            // Read prescription data
            File prescriptionFile = new File(prescriptionFilePath);
            if (prescriptionFile.exists()) {
                System.out.println("Prescription file exists");
                BufferedReader prescriptionReader = new BufferedReader(new FileReader(prescriptionFile));
                String line;
                String[] prescriptionsArray = new String[20]; // Array to store prescriptions, assuming a maximum of 20

                // Read each line from the file and add it to prescriptionsArray
                int index = 0;
                while ((line = prescriptionReader.readLine()) != null && index < 20) {
                    prescriptionsArray[index++] = line;
                }

                // Store prescriptionsArray in the first index of allData
                allData[0] = prescriptionsArray;

                // Print prescriptions
                System.out.println("Prescriptions:");
                for (String prescription : allData[0]) {
                    if (prescription != null) {
                        System.out.println(prescription);
                    }
                }

                prescriptionReader.close();
            } else {
                System.out.println("Prescription file does not exist");
            }

            // Read vaccinations data
            File vaccinationsFile = new File(vaccinationsFilePath);
            if (vaccinationsFile.exists()) {
                System.out.println("Vaccinations file exists");
                BufferedReader vaccinationsReader = new BufferedReader(new FileReader(vaccinationsFile));
                String line;
                String[] vaccinationsArray = new String[20]; // Array to store vaccinations, assuming a maximum of 20

                // Read each line from the file and add it to vaccinationsArray
                int index = 0;
                while ((line = vaccinationsReader.readLine()) != null && index < 20) {
                    vaccinationsArray[index++] = line;
                }

                // Store vaccinationsArray in the second index of allData
                allData[1] = vaccinationsArray;

                // Print vaccinations
                System.out.println("Vaccinations:");
                for (String vaccination : allData[1]) {
                    if (vaccination != null) {
                        System.out.println(vaccination);
                    }
                }

                vaccinationsReader.close();
            } else {
                System.out.println("Vaccinations file does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allData;
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
                // Read all lines from the chat file and append to the chat display area
                Files.lines(chatFilePath).forEach(line -> chatTextArea.appendText(line + "\n"));
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions here
                // Maybe show an alert to the user or log the error
            }
        }
    }
    
    private void appendMessageToFile(String patientID, String sender, String message) {
        String filename = patientID + "Chat.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
            writer.write(timestamp + "\n" + sender + ":\n" + message + "\n\n");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions here
        }
    }
    
    private void sendMessage(String patientID, String sender, String message) {
        if (message == null || message.trim().isEmpty()) {
            // Handle empty message case (e.g., show an alert or error)
            return;
        }
        // Assuming 'Doctor' is the sender. Replace with actual doctor's identification.
        appendMessageToFile(patientID, sender, message);
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
