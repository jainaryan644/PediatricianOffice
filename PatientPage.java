//package application;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.application.Application;
import javafx.geometry.Insets;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;

public class PatientPage extends BorderPane {
	private Label patientNameLabel = new Label("");
	private Label doctorNameLabel = new Label("");
	private Label doctorEmailLabel = new Label("");
	private Button logoutButton;
	private TextArea chatTextArea;
	private TextField chatInputField; // TextField for writing new messages
	private Button sendButton; // Button to send a message
	
	private void updateDoctorNameLabel(String doctorName) {
	    doctorNameLabel.setText("Doctor: " + doctorName);
	    System.out.print(doctorName);
	}
    
    private void loadDoctorInformation(String patient) {
        //String username = ""; // Placeholder - dynamically assign as needed
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
        
	           
        if (Files.exists(patientFilePath)) {
	        try (Stream<String> lines = Files.lines(patientFilePath)) {
	            lines.forEach(line -> {
	                String[] parts = line.split(": ");
	                if (parts.length == 2) {
	                    String label = parts[0].trim();
	                    String value = parts[1].trim();
	                    if(label.equals("First Name"))
	                    {
	                    	patientNameLabel.setText("Hello " + value);
	                    }
	                }
	            });
	            
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle exception here, perhaps with an alert to the user
	        }        
        }
    
    }




	public PatientPage(String username) {
		// Main layout is BorderPane (it has 5 areas: top, right, bottom, left, center)
		super();
		logoutButton = new Button("Logout");
		loadDoctorInformation(username); 
		//logoutButton.setOnAction(e -> logout());

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
		prescriptionTextArea.setPrefHeight(200); // You can set the preferred height as needed																									// between buttons

		editPrescriptionBox.getChildren().addAll(prescriptionLabel, prescriptionTextArea);

		
		
		GridPane grid = new GridPane();
        grid.setVgap(10); // vertical gap in pixels
        grid.setHgap(10); // horizontal gap in pixels
        grid.setPadding(new Insets(10, 10, 10, 10)); // padding around the grid

        // Labels
        Label lblPhoneNumber = new Label("Phone Number");
        Label lblEmail = new Label("Email");
        Label lblInsurance = new Label("Insurance");
        Label lblAddress = new Label("Address");
        Label lblPharmacy = new Label("Pharmacy");

        // TextFields
        TextField txtPhoneNumber = new TextField("Placeholder");
        txtPhoneNumber.setId("Phone");
        TextField txtEmail = new TextField("Placeholder");
        txtEmail.setId("Email");
        TextField txtInsurance = new TextField("Placeholder");
        txtInsurance.setId("Insurance");
        TextField txtAddress = new TextField("Placeholder");
        txtAddress.setId("Address");
        TextField txtPharmacy = new TextField("Placeholder");
        txtPharmacy.setId("Pharmacy");

        // Make TextFields non-editable by default
        txtPhoneNumber.setEditable(false);
        txtEmail.setEditable(false);
        txtInsurance.setEditable(false);
        txtAddress.setEditable(false);
        txtPharmacy.setEditable(false);

        // Edit Buttons
        Button btnEditPhone = new Button("Edit");
        Button btnEditEmail = new Button("Edit");
        Button btnEditInsurance = new Button("Edit");
        Button btnEditAddress = new Button("Edit");
        Button btnEditPharmacy = new Button("Edit");

        // Edit button actions
        btnEditPhone.setOnAction(e -> toggleEditable(txtPhoneNumber));
        btnEditEmail.setOnAction(e -> toggleEditable(txtEmail));
        btnEditInsurance.setOnAction(e -> toggleEditable(txtInsurance));
        btnEditAddress.setOnAction(e -> toggleEditable(txtAddress));
        btnEditPharmacy.setOnAction(e -> toggleEditable(txtPharmacy));

        // Add components to grid
        grid.add(lblPhoneNumber, 0, 0);
        grid.add(txtPhoneNumber, 1, 0);
        grid.add(btnEditPhone, 2, 0);

        grid.add(lblEmail, 0, 1);
        grid.add(txtEmail, 1, 1);
        grid.add(btnEditEmail, 2, 1);

        grid.add(lblInsurance, 0, 2);
        grid.add(txtInsurance, 1, 2);
        grid.add(btnEditInsurance, 2, 2);

        grid.add(lblAddress, 0, 3);
        grid.add(txtAddress, 1, 3);
        grid.add(btnEditAddress, 2, 3);

        grid.add(lblPharmacy, 0, 4);
        grid.add(txtPharmacy, 1, 4);
        grid.add(btnEditPharmacy, 2, 4);
        
        //String username = "aryanjain20031002"; // This should be dynamically generated based on the patient
	    Path patientFilePath = Paths.get(username + "/generalInfo.txt");
	    
	    
        loadPatientInformation(patientFilePath, txtPhoneNumber, txtEmail, txtInsurance, txtAddress, txtPharmacy);
        
     // Chat box
        VBox chatBox = new VBox(new Label("Chat"));
        chatTextArea = new TextArea();
        chatTextArea.setEditable(false);
        chatInputField = new TextField();
        sendButton = new Button("Send");
        HBox chatInputBox = new HBox(chatInputField, sendButton);
        //loadChatHistory("12345");
        chatBox.getChildren().addAll(chatTextArea, chatInputBox);
        loadChatHistory(username);
        sendButton.setOnAction(e -> {
            String message = chatInputField.getText();
            if (!message.isEmpty()) {
                sendMessage(username, "Patient", message); // Replace "Doctor" with the actual doctor's name
                chatInputField.clear(); // Clear the text field after sending the message
                loadChatHistory(username); // Load the chat history again to display the new message
            }
        });

		// Adding components to the right panel
		rightPanel.getChildren().addAll(editPrescriptionBox, grid, chatBox);

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
	
	private void toggleEditable(TextField textField) {
        textField.setEditable(!textField.isEditable());
        if (textField.isEditable()) {
            textField.requestFocus(); // Focus on the text field to start editing
        } else {
            // Handle the logic to save the text field's content when editing is finished
            // This is where you would persist the information to a file or database
            saveInformation(textField.getId(), textField.getText());
        }
    }
	
	private void saveInformation(String fieldId, String content) {
	    // You'll need to pass in the user's first name, last name, and birthday as well
	    String username = "aryanjain20031002"; // This should be dynamically generated based on the patient
	    Path filePath = Paths.get(username + "/generalInfo.txt");

	    try {
	        // Create a stream to read the existing content
	        Stream<String> lines = Files.lines(filePath);
	        List<String> replaced = lines
	            .map(line -> line.startsWith(fieldId + ":") ? fieldId + ": " + content : line)
	            .collect(Collectors.toList());
	        Files.write(filePath, replaced);
	        lines.close(); // Close the stream
	        System.out.println("Information saved.");
	    } catch (IOException e) {
	        e.printStackTrace();
	        // Handle exception here
	    }
	}
	
	private void loadPatientInformation(Path path, TextField txtPhone, TextField txtEmail, TextField txtInsurance, TextField txtAddress, TextField txtPharmacy) {

	    if (Files.exists(path)) {
	        try (Stream<String> lines = Files.lines(path)) {
	            lines.forEach(line -> {
	                String[] parts = line.split(": ");
	                if (parts.length == 2) {
	                    String label = parts[0].trim();
	                    String value = parts[1].trim();
	                    switch (label) {
	                        case "Phone":
	                            txtPhone.setText(value);
	                            break;
	                        case "Email":
	                            txtEmail.setText(value);
	                            break;
	                        case "Insurance":
	                            txtInsurance.setText(value);
	                            break;
	                        case "Address":
	                            txtAddress.setText(value);
	                            break;
	                        case "Pharmacy":
	                            txtPharmacy.setText(value);
	                            break;
	                    }
	                }
	            });
	        } catch (IOException e) {
	            e.printStackTrace();
	            // Handle exception here, perhaps with an alert to the user
	        }
	    } else {
	        // File doesn't exist. Handle this scenario, perhaps by populating the fields with default text or showing an error.
	    }
	}


	
	public void loadChatHistory(String username) {
    	chatTextArea.clear(); // Clear previous messages
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
    }
    
    public void appendMessageToFile(String username, String sender, String message) {
        String filename = username + "/Chat.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            String timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
            writer.write(timestamp + "\n" + sender + ":\n" + message + "\n\n");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace(); // Handle exceptions here
        }
    }
    
    public void sendMessage(String patientID, String sender, String message) {
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

    
	public void setPatientName(String name) {
		if (name == null || name.trim().isEmpty()) {
			patientNameLabel.setText("Patient <Unknown>");
		} else {
			patientNameLabel.setText("Patient " + name);
		}
	}

	private void logout(Stage primaryStage) {
		LoginPage loginPage = new LoginPage();
		Scene loginScene = new Scene(loginPage, 600, 800);
		primaryStage.setScene(loginScene);
	}
}
