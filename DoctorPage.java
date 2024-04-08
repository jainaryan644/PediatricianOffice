package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
    private Label patientNameLabel = new Label("");
    private Label doctorNameLabel = new Label("");
    private Label doctorEmailLabel = new Label("");
    private Button logoutButton;
    private String[][] allData;
  

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
            String username = searchField.getText(); // Capture the value of the search text field
            allData = getAllData(username);
            VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData);
            this.setLeft(leftPanel); // Update the left panel
            this.setRight(createRightPanel(allData, username)); // Pass the username to createRightPanel
        });

        HBox searchBox = new HBox(5); // 5 is the spacing between the text field and button
        searchBox.getChildren().addAll(searchField, searchButton);

        this.setTop(new VBox(patientNameLabel, searchBox));

        // Initialize allData and set up the left panel
        allData = getAllData("");
        VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData);
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
        
        // Set the right panel
        

        // Set the main layout areas
        this.setCenter(centerPanel);
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

    private VBox createRightPanel(String allData[][], String username) {
    	String[] prescriptions = allData[0];
    	String[] vaccinations = allData[1];

        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);
        rightPanel.setPrefWidth(300);

        // Edit Prescription box
        VBox editPrescriptionBox = new VBox();
        editPrescriptionBox.setSpacing(5);
        Label prescriptionLabel = new Label("Edit Prescription");

        // VBox to hold list of prescriptions
        VBox listPrescription = new VBox();

        if (prescriptions != null) {
            for (int i = 0; i < prescriptions.length; i++) {
                String prescription = prescriptions[i];
                if (prescription != null) {
                    HBox prescriptionWhole = new HBox();
                    Label prescriptionTitle = new Label(prescription);
                    Button deleteButton = new Button("Delete");
                    prescriptionWhole.getChildren().addAll(prescriptionTitle, deleteButton);

                    // Attach event handler to delete button
                    final int indexToRemove = i; // capture the index before the lambda
                    deleteButton.setOnAction(event -> {
                        prescriptions[indexToRemove] = null;
                        listPrescription.getChildren().remove(prescriptionWhole);
                        updateUI(username); // Update UI after prescription is removed
                    });

                    listPrescription.getChildren().add(prescriptionWhole); // Add each prescription to the list
                }
            }
        }

        TextField prescriptionTextField = new TextField();
        Button savePrescriptionButton = new Button("Save");
        Button addPrescriptionButton = new Button("Add");

        // Attach event handler to the "Add" button
        addPrescriptionButton.setOnAction(event -> {
            String newPrescription = prescriptionTextField.getText().trim();
            if (!newPrescription.isEmpty()) {
                // Add the new prescription to the array
                for (int i = 0; i < prescriptions.length; i++) {
                    if (prescriptions[i] == null) {
                        prescriptions[i] = newPrescription;
                        break;
                    }
                }

                // Create a new HBox for the new prescription
                HBox newPrescriptionWhole = new HBox();
                Label newPrescriptionTitle = new Label(newPrescription);
                Button newDeleteButton = new Button("Delete");
                newPrescriptionWhole.getChildren().addAll(newPrescriptionTitle, newDeleteButton);

                // Attach event handler to the new delete button
                final int newIndexToRemove = prescriptions.length - 1; // capture the index before the lambda
                newDeleteButton.setOnAction(event2 -> {
                    prescriptions[newIndexToRemove] = null;
                    listPrescription.getChildren().remove(newPrescriptionWhole);
                    updateUI(username); // Update UI after prescription is removed
                });

                listPrescription.getChildren().add(newPrescriptionWhole); // Add the new prescription to the list
                prescriptionTextField.clear(); // Clear the text field
                updateUI(username); // Update the UI
            }
        });
        
        savePrescriptionButton.setOnAction(event -> {
        	saveToPrescription(username, prescriptions);
        });
         

        HBox prescriptionButtonsBox = new HBox(savePrescriptionButton, addPrescriptionButton);
        editPrescriptionBox.getChildren().addAll(prescriptionLabel, listPrescription, prescriptionTextField, prescriptionButtonsBox);
        
        
        VBox editVaccinationBox = new VBox();  
        editVaccinationBox.setSpacing(5);
        Label VaccinationLabel = new Label("Edit Vaccination");

        // VBox to hold list of prescriptions
        VBox listVaccination = new VBox();

        if (vaccinations != null) {
            for (int i = 0; i < vaccinations.length; i++) {
                String vaccination = vaccinations[i];
                if (vaccination != null) {
                    HBox vaccinationWhole = new HBox();
                    Label vaccinationTitle = new Label(vaccination);
                    Button deleteButton = new Button("Delete");
                    vaccinationWhole.getChildren().addAll(vaccinationTitle, deleteButton);

                    // Attach event handler to delete button
                    final int indexToRemove = i; // capture the index before the lambda
                    deleteButton.setOnAction(event -> {
                    	vaccinations[indexToRemove] = null;
                        listVaccination.getChildren().remove(vaccinationWhole);
                        updateUI(username); // Update UI after prescription is removed
                    });

                    listVaccination.getChildren().add(vaccinationWhole); // Add each prescription to the list
                }
            }
        }

        TextField vaccinationTextField = new TextField();
        Button saveVaccinationButton = new Button("Save");
        Button addVaccinationButton = new Button("Add");

        // Attach event handler to the "Add" button
        addVaccinationButton.setOnAction(event -> {
            String newVaccination = vaccinationTextField.getText().trim();
            if (!newVaccination.isEmpty()) {
                // Add the new prescription to the array
                for (int i = 0; i < vaccinations.length; i++) {
                    if (vaccinations[i] == null) {
                    	vaccinations[i] = newVaccination;
                        break;
                    }
                }

                // Create a new HBox for the new prescription
                HBox newVaccinationWhole = new HBox();
                Label newVaccinationTitle = new Label(newVaccination);
                Button newDeleteButton = new Button("Delete");
                newVaccinationWhole.getChildren().addAll(newVaccinationTitle, newDeleteButton);

                // Attach event handler to the new delete button
                final int newIndexToRemove = vaccinations.length - 1; // capture the index before the lambda
                newDeleteButton.setOnAction(event2 -> {
                	vaccinations[newIndexToRemove] = null;
                    listVaccination.getChildren().remove(newVaccinationWhole);
                    updateUI(username); // Update UI after prescription is removed
                });

                listVaccination.getChildren().add(newVaccinationWhole); // Add the new prescription to the list
                vaccinationTextField.clear(); // Clear the text field
                updateUI(username); // Update the UI
            }
        });
        
        saveVaccinationButton.setOnAction(event -> {
        	saveToVaccination(username, vaccinations);
        });
         

        HBox vaccinationButtonsBox = new HBox(saveVaccinationButton, addVaccinationButton); 
        editVaccinationBox.getChildren().addAll(VaccinationLabel, listVaccination, vaccinationTextField, vaccinationButtonsBox);
        

        // Chat box
        VBox chatBox = new VBox(new Label("Chat"));
        ListView<String> chatListView = new ListView<>();
        TextField chatInputField = new TextField();
        Button sendButton = new Button("Send");
        HBox chatInputBox = new HBox(chatInputField, sendButton);
        chatBox.getChildren().addAll(chatListView, chatInputBox);

        // Adding components to the right panel
        rightPanel.getChildren().addAll(editPrescriptionBox, editVaccinationBox, chatBox);

        return rightPanel;
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

    private void logout(Stage primaryStage) {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene loginScene = new Scene(loginPage, 600, 800);
        primaryStage.setScene(loginScene);
    }
    private void updateUI(String username) {
        // Refresh the right panel to reflect changes after deleting a prescription
        this.setRight(createRightPanel(allData, username));
    }
    
    private void saveToPrescription(String username, String[] prescriptions) {
    	 String prescriptionFilePath = username + "/prescription.txt";
    	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(prescriptionFilePath))) {
    	        // Clear the file before writing
    	        writer.write(""); // Clear the file content
    	        
    	        // Write each prescription to the file
    	        for (String prescription : prescriptions) {
    	            if (prescription != null) {
    	                writer.write(prescription); // Write the prescription
    	                writer.newLine(); // Move to the next line for the next prescription
    	            }
    	        }
    	    } catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	 
    	
    }
    
    private void saveToVaccination(String username, String[] vaccinations) {
   	 String prescriptionFilePath = username + "/vaccinations.txt";
   	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(prescriptionFilePath))) {
   	        // Clear the file before writing
   	        writer.write(""); // Clear the file content
   	        
   	        // Write each prescription to the file
   	        for (String vaccination : vaccinations) {
   	            if (vaccination != null) {
   	                writer.write(vaccination); // Write the prescription
   	                writer.newLine(); // Move to the next line for the next prescription
   	            }
   	        }
   	    } catch (IOException e) {
   	        e.printStackTrace();
   	    }
   	 
   	
   }
    
}