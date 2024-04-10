//package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class DoctorPage extends BorderPane {

    private Label patientNameLabel = new Label("Patient <Unknown>");
    private Label doctorNameLabel = new Label("");
   
    private TextArea visitSummaryTextArea = new TextArea();
    private TextArea healthConcernsTextArea = new TextArea();
    private TextArea prescriptionTextArea = new TextArea();
    private TextArea vaccinationsTextArea = new TextArea();
    private Button logoutButton = new Button("Logout");
    private VBox leftPanel;
    private VBox visitsPanel; // Panel for displaying visit dates
    private ScrollPane visitsScrollPane; // Scroll pane for visit dates
    private Button saveVisitButton = new Button("Save Visit");
    private Button seeVisitsButton = new Button("See Visits");
    private String filePath;
    private TextField searchField;
    private PatientPage pp;
    
    private TextArea chatTextArea;
	private TextField chatInputField; // TextField for writing new messages
	private Button sendButton; // Button to send a message
	

    public DoctorPage(String username) {
        super();
        initializeUIComponents();
    }
    
    private void updateDoctorNameLabel(String doctorName) {
	    doctorNameLabel.setText("Doctor: " + doctorName);
	    System.out.print(doctorName);
	}
    
    private void loadDoctorInformation(String patient) {
    	Path patientFilePath = Paths.get(patient + "/generalInfo.txt");

        if (Files.exists(patientFilePath)) {
	        try (Stream<String> lines = Files.lines(patientFilePath)) {
	            lines.forEach(line -> {
	                String[] parts = line.split(":");
	                if (parts.length == 2) {
	                    String label = parts[0].trim();
	                    String value = parts[1].trim();
	                    if(label.equals("Doctor"))
	                    {
	                    	updateDoctorNameLabel(value);
	                    }
	                }
	            });
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle exception here, perhaps with an alert to the user
	        }

        }
    }

    private void initializeUIComponents() {
        // Combining left panel and visits scroll pane into an HBox
        Node leftPanelNode = setupLeftPanel();
        Node visitsScrollPaneNode = setupVisitsScrollPane();
        HBox combinedLeftSection = new HBox(visitsScrollPaneNode, leftPanelNode); // Combine left panel and visits panel

        this.setTop(setupTopPanel());
        this.setLeft(combinedLeftSection); // Updated to use the combined section
        this.setCenter(setupCenterPanel());
        this.setRight(setupRightPanel());
        this.setBottom(setupBottomPanel());
       

        seeVisitsButton.setOnAction(e->  refreshVisitsPanel());
        saveVisitButton.setOnAction(e -> saveVisitDetailsAndRefreshVisits());
        
    }

    private void saveVisitDetailsAndRefreshVisits() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            String lastDate = "";
            int lastDateIndex = -1;
            // Find the last "Date: " entry to identify the most recent visit
            for (int i = lines.size() - 1; i >= 0; i--) {
                if (lines.get(i).startsWith("Date: ")) {
                    lastDate = lines.get(i).substring(6);
                    lastDateIndex = i;
                    break;
                }
            }

            if (!lastDate.isEmpty() && lastDateIndex != -1) {
                // Update the most recent visit's details
                lines.set(lastDateIndex + 1, "Visit Summary: " + visitSummaryTextArea.getText().trim());
                lines.set(lastDateIndex + 2, "Health Concerns: " + healthConcernsTextArea.getText().trim());
                lines.set(lastDateIndex + 3, "Prescriptions: " + prescriptionTextArea.getText().trim());
                lines.set(lastDateIndex + 4, "Vaccinations: " + vaccinationsTextArea.getText().trim());

                // Write the updated content back to the file
                Files.write(Paths.get(filePath), lines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                
                // Optionally, clear the text areas after saving
                visitSummaryTextArea.clear();
                healthConcernsTextArea.clear();
                prescriptionTextArea.clear();
                vaccinationsTextArea.clear();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private void refreshVisitsPanel() {
        visitsPanel.getChildren().clear(); // Clear existing content

        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (String line : lines) {
                if (line.startsWith("Date: ")) {
                    String date = line.substring(6); // Extract the date part
                    Button dateButton = new Button(date);
                    dateButton.setOnAction(e -> populateVisitDetails(date));
                    visitsPanel.getChildren().add(dateButton);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void populateVisitDetails(String date) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            boolean foundDate = false;
            
            // Clear the text areas before populating new information
            visitSummaryTextArea.clear();
            healthConcernsTextArea.clear();
            prescriptionTextArea.clear();
            vaccinationsTextArea.clear();

            for (String line : lines) {
                if (line.startsWith("Date: ") && line.substring(6).equals(date)) {
                    foundDate = true;
                } else if (foundDate) {
                    if (line.startsWith("Visit Summary: ")) {
                        visitSummaryTextArea.setText(line.substring("Visit Summary: ".length()));
                    } else if (line.startsWith("Health Concerns: ")) {
                        healthConcernsTextArea.setText(line.substring("Health Concerns: ".length()));
                    } else if (line.startsWith("Prescriptions: ")) {
                        prescriptionTextArea.setText(line.substring("Prescriptions: ".length()));
                    } else if (line.startsWith("Vaccinations: ")) {
                        vaccinationsTextArea.setText(line.substring("Vaccinations: ".length()));
                    } else if (line.trim().isEmpty() || line.startsWith("Date: ")) {
                        // Stop if we reach an empty line or the next visit section starts
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    private Node setupBottomPanel() {
        BorderPane bottomPanel = new BorderPane();
        bottomPanel.setPadding(new Insets(10, 0, 10, 0));

        HBox savePanel = new HBox(10, saveVisitButton, seeVisitsButton);
        savePanel.setAlignment(Pos.CENTER);
        bottomPanel.setCenter(savePanel);

        BorderPane.setAlignment(logoutButton, Pos.CENTER_RIGHT);
        bottomPanel.setLeft(logoutButton);


        return bottomPanel;
    }

    private Node setupVisitsScrollPane() {
        visitsPanel = new VBox(5);
        visitsScrollPane = new ScrollPane(visitsPanel);
        visitsScrollPane.setPrefWidth(80);
        visitsScrollPane.setFitToWidth(true);

        return visitsScrollPane;
    }

    private Node setupTopPanel() {
        patientNameLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: #2e8b57;");
        return patientNameLabel;
    }

    private Node setupLeftPanel() {
        leftPanel = new VBox(10);
        leftPanel.setPrefWidth(350);

        // Patient search field and button
        searchField = new TextField();
        searchField.setPromptText("Search Patient by Username");
        Button searchButton = new Button("Search");
        loadDoctorInformation(searchField.getText().trim());
        searchButton.setOnAction(e ->{
        	searchPatient(searchField.getText().trim());     	
        });
        
        HBox searchBox = new HBox(5, searchField, searchButton);
        leftPanel.getChildren().add(searchBox);

        // Tabs without the Visits tab
        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
                createTab("Prescription", "Prescription information displayed here..."),
                createTab("Immunization Records", "Immunization records displayed here..."),
                createTab("Pharmacy Information", "Pharmacy information displayed here...")
        );

        VBox doctorProfileBox = setupDoctorProfileBox();
        leftPanel.getChildren().addAll(tabPane, doctorProfileBox);

        return leftPanel;
    }
    
    private void loadChatHistory(String username) {
    	//chatTextArea.clear(); // Clear previous messages
        Path chatFilePath = Paths.get(username + "/Chat.txt");
        
        if (Files.exists(chatFilePath)) {
            try {
                // Read all lines from the chat file and append to the chat display area
                Files.lines(chatFilePath).forEach(line -> chatTextArea.appendText(line + "\n"));
            } catch (IOException e) {
                e.printStackTrace(); // Handle exceptions here
                // Maybe show an alert to the user or log the error
            }
        }
        sendButton.setOnAction(e -> {
            String message = chatInputField.getText();
            if (!message.isEmpty()) {
                sendMessage(username, "Patient", message); // Replace "Doctor" with the actual doctor's name
                chatInputField.clear(); // Clear the text field after sending the message
                loadChatHistory(username); // Load the chat history again to display the new message
            }
        });
    }
    
    private void appendMessageToFile(String username, String sender, String message) {
        String filename = username + "/Chat.txt";
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

    private Node setupCenterPanel() {
        VBox centerPanel = new VBox();
        centerPanel.setPrefWidth(350);
        visitSummaryTextArea = new TextArea("Visit Summary");
        visitSummaryTextArea.setPrefHeight(390);
        healthConcernsTextArea = new TextArea("Health Concerns");
        healthConcernsTextArea.setPrefHeight(390);
        centerPanel.getChildren().addAll(visitSummaryTextArea, healthConcernsTextArea);
        return centerPanel;
    }

    private Node setupRightPanel() {
        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);
        rightPanel.setPrefWidth(300);

        // Edit Prescription box
        VBox editPrescriptionBox = setupEditPrescriptionBox();

        // Vaccinations box
        VBox vaccinationsBox = setupVaccinationsBox();

        // Chat box
        VBox chatBox = new VBox(new Label("Chat"));
        chatTextArea = new TextArea();
        chatTextArea.setEditable(false);
        chatInputField = new TextField();
        sendButton = new Button("Send");
        HBox chatInputBox = new HBox(chatInputField, sendButton);
        //loadChatHistory("12345");
        chatBox.getChildren().addAll(chatTextArea, chatInputBox);
        loadChatHistory(searchField.getText().trim());
        sendButton.setOnAction(e -> {
            String message = chatInputField.getText();
            if (!message.isEmpty()) {
                sendMessage(searchField.getText().trim(), "Patient", message); // Replace "Doctor" with the actual doctor's name
                chatInputField.clear(); // Clear the text field after sending the message
                loadChatHistory(searchField.getText().trim()); // Load the chat history again to display the new message
            }
        });


        // Adding components to the right panel
        rightPanel.getChildren().addAll(editPrescriptionBox, vaccinationsBox, chatBox);

        return rightPanel;
    }

    private VBox setupEditPrescriptionBox() {
        VBox box = new VBox(5);
        Label label = new Label("Edit Prescription");
        TextArea textArea = new TextArea();
        textArea.setPrefHeight(200);

        box.getChildren().addAll(label, textArea);
        return box;
    }

    private VBox setupVaccinationsBox() {
        VBox box = new VBox(5);
        Label label = new Label("Edit Vaccinations");
        TextArea textArea = new TextArea();
        box.getChildren().addAll(label, textArea);
        return box;
    }

    private VBox setupChatBox() {
        VBox box = new VBox(5);
        Label label = new Label("Chat");
        TextArea chatTextArea = new TextArea();
        TextField chatInputField = new TextField();
        Button sendButton = new Button("Send");
        
        HBox inputBox = new HBox(chatInputField, sendButton);
        box.getChildren().addAll(label, chatTextArea, inputBox);
        return box;
    }

    private Tab createTab(String title, String promptText) {
        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(600);
        textArea.setPromptText(promptText);
        Tab tab = new Tab(title, textArea);
        tab.setClosable(false);
        return tab;
    }

    private VBox setupDoctorProfileBox() {
        VBox doctorProfileBox = new VBox(10);
        
        doctorProfileBox.getChildren().addAll(doctorNameLabel, logoutButton);
        return doctorProfileBox;
    }

    private void searchPatient(String username) {
        File patientDir = new File(username);
        File generalInfoFile = new File(patientDir, "generalInfo.txt");
        
        

        if (!generalInfoFile.exists()) {
            return; // Optionally, show an alert here
        }
        
        //loadChatHistory(searchField.getText().trim());
        //pp= new PatientPage(username);
        loadChatHistory(searchField.getText().trim());
    	loadDoctorInformation(searchField.getText().trim());

        filePath = new File(patientDir, username + "_visits.txt").getAbsolutePath();

        try (Scanner scanner = new Scanner(generalInfoFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (key.equals("First Name") || key.equals("Last Name")) {
                        updatePatientNameLabel(value, key);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // Optionally, show an alert here
        }
    }

    private void updatePatientNameLabel(String value, String key) {
        if (key.equals("First Name")) {
            patientNameLabel.setText("Patient " + value + " " + patientNameLabel.getText().substring(patientNameLabel.getText().indexOf(" ")).trim());
        } else if (key.equals("Last Name")) {
            patientNameLabel.setText(patientNameLabel.getText().replace("<Unknown>", value));
        }
    }
}
