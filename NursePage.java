package application;
//Nurse imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.scene.Scene; 
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NursePage extends BorderPane {
    private Label patientNameLabel = new Label("");
    private Label doctorNameLabel = new Label("");
    private Label doctorEmailLabel = new Label("");
    private Button logoutButton;
    private String[][] allData;
  

    public NursePage(Stage primaryStage, String firstName, String lastName, String email, String doctorUsername) {
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
            VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData, username, primaryStage);
            this.setLeft(leftPanel); // Update the left panel
            this.setRight(createRightPanel(allData, username, doctorUsername)); // Pass the username to createRightPanel
            VBox centerPanel = createCenterPanel(username,"", allData[2]);
            this.setCenter(centerPanel);
        });

        HBox searchBox = new HBox(5); // 5 is the spacing between the text field and button
        searchBox.getChildren().addAll(searchField, searchButton);

        this.setTop(new VBox(patientNameLabel, searchBox));

        // Initialize allData and set up the left panel
        allData = getAllData("");
        VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData, "", primaryStage);
        this.setRight(createRightPanel(allData, "", doctorUsername));
        this.setLeft(leftPanel);

        // Text areas for the center
        VBox centerPanel = createCenterPanel("", "", allData[2]);
        
        // Set the right panel
        

        // Set the main layout areas
        this.setCenter(centerPanel);
    }

    private VBox setupLeftPanel(String firstName, String lastName, String email, String[][] allData, String username, Stage primaryStage) {
        VBox leftPanel = new VBox();
        leftPanel.setPrefWidth(400);
        String [] names = getFirstLastName(username);
        String patientFirst = "";
        String patientLast = "";
        if (names[0] != null) {
        	patientFirst = names[0];
        }
        if (names[1] != null) {
        	patientLast = names[1];
        }
        
        
        Label user = new Label("Patient: " +  patientFirst + " " + patientLast + " Information");
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
        pharmacyContent.setPromptText("Patient information displayed here...");
        Tab pharmacyTab = new Tab("Patient Information", pharmacyContent);

        ScrollPane visitsContent = new ScrollPane();
        VBox visitsBox = new VBox();
        if (allData[2] != null) {
            for (String item : allData[2]) {
                if (item != null) {
                    HBox viewBox = new HBox();
                    Label visit = new Label(item);
                    Button view = new Button("view");
                    
                    // Add event handler to the view button
                    view.setOnAction(event -> {
                        // Call createCenterPanel method passing the label
                        VBox centerPanel = createCenterPanel(username, item, allData[2]);
                        this.setCenter(centerPanel); // Assuming 'this' refers to your parent container
                    });
                    
                    viewBox.getChildren().addAll(visit, view);
                    visitsBox.getChildren().add(viewBox);
                }
            }
        }
        visitsContent.setContent(visitsBox);
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
        
        if (allData[3] != null) {
            StringBuilder patientBuilder = new StringBuilder();
            String[] infoTypes = {"Email", "Phone Number", "Insurance", "Address", "Pharmacy"};
            
            for (int i = 0; i < allData[3].length && i < infoTypes.length; i++) {
                if (allData[3][i] != null) {
                    patientBuilder.append(infoTypes[i]).append(": ").append(allData[3][i]).append("\n");
                }
            }
            
            pharmacyContent.setText(patientBuilder.toString());
        }


        // Doctor's profile box at the bottom
        VBox doctorProfileBox = new VBox(10); // 10 is the spacing between elements
        doctorProfileBox.setStyle("-fx-background-color: #add8e6; -fx-padding: 10;");
        Label doctorNameLabel = new Label("Nurse: " + firstName + " " + lastName);
        Label doctorEmailLabel = new Label("Email: " + email);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));
        doctorProfileBox.getChildren().addAll(doctorNameLabel, doctorEmailLabel, logoutButton);

        // Adding doctor's profile box and logout button to the left panel
        leftPanel.getChildren().addAll(user, tabPane, doctorProfileBox);

        return leftPanel;
    }
    
    private VBox createCenterPanel(String username, String visit, String[] visits) {
        // Text areas for the center
    	String visitSummaryPath = username + "/visits" + "/" + visit + "/summary.txt";
        String healthConcernsPath = username + "/visits" + "/" + visit + "/concern.txt";
        TextArea visitSummaryTextArea = new TextArea();
        visitSummaryTextArea.setPromptText("Visit Summary");
        visitSummaryTextArea.setPrefHeight(390);
        TextArea healthConcernsTextArea = new TextArea();
        healthConcernsTextArea.setPromptText("Health Concerns");
        healthConcernsTextArea.setPrefHeight(390);

        // Read text from files and add to text areas
        if (username != "" && visit !="") {
        	try {
                String visitSummaryText = readFile(visitSummaryPath);
                visitSummaryTextArea.setText(visitSummaryText);

                String healthConcernsText = readFile(healthConcernsPath);
                healthConcernsTextArea.setText(healthConcernsText);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        }
        Button addVisit = new Button("add visit");
        
        addVisit.setOnAction(e -> {
        	if (username == null || username.isEmpty()) {
                System.err.println("Invalid username");
                return; // Exit the method if username is not valid
            }

            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define a format for the date and time
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHH-mm-ss");


            // Format the current date and time using the defined format
            String formattedDateTime = currentDateTime.format(formatter);
            
            

            // Create directory path for the new visit
            String visitDirectoryPath = username + File.separator + "visits" + File.separator + formattedDateTime;
            File visitFolder = new File(visitDirectoryPath);

            try {
                // Ensure parent directory exists
                File parentDir = visitFolder.getParentFile();
                if (!parentDir.exists()) {
                    if (!parentDir.mkdirs()) {
                        System.err.println("Failed to create parent directory: " + parentDir.getAbsolutePath());
                        return;
                    }
                }

                // Check if visit folder already exists
                if (!visitFolder.exists()) {
                    if (visitFolder.mkdir()) {
                        System.out.println("Visit folder created: " + visitFolder.getAbsolutePath());
                        for (int i = 0; i < visits.length; i++) {
                            if (visits[i] == null) {
                                visits[i] = formattedDateTime;
                                break;
                            }
                        }
                     saveToVisits(username, visits);
                    } else {
                        System.err.println("Failed to create visit folder: " + visitFolder.getAbsolutePath());
                    }
                } else {
                    System.out.println("Visit folder already exists: " + visitFolder.getAbsolutePath());
                }
            } catch (SecurityException e1) {
                System.err.println("SecurityException: " + e1.getMessage());
            }

            
            try {
                String summaryFilePath = visitDirectoryPath + "/summary.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(summaryFilePath));
                writer.write(visitSummaryTextArea.getText());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace(); // Handle or log the exception appropriately
            }

            // Write visit concerns to a file
            try {
                String concernsFilePath = visitDirectoryPath + "/concern.txt";
                BufferedWriter writer = new BufferedWriter(new FileWriter(concernsFilePath));
                writer.write(healthConcernsTextArea.getText());
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace(); // Handle or log the exception appropriately
            }

            
        });

        // Center panel layout
        VBox centerPanel = new VBox(visitSummaryTextArea, healthConcernsTextArea, addVisit);

        return centerPanel;
    }

    private VBox createRightPanel(String allData[][], String username, String doctorUsername) {
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
                        updateUI(username, doctorUsername); // Update UI after prescription is removed
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
                    updateUI(username, doctorUsername); // Update UI after prescription is removed
                });

                listPrescription.getChildren().add(newPrescriptionWhole); // Add the new prescription to the list
                prescriptionTextField.clear(); // Clear the text field
                updateUI(username, doctorUsername); // Update the UI
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
                        updateUI(username, doctorUsername); // Update UI after prescription is removed
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
                    updateUI(username, doctorUsername); // Update UI after prescription is removed
                });

                listVaccination.getChildren().add(newVaccinationWhole); // Add the new prescription to the list
                vaccinationTextField.clear(); // Clear the text field
                updateUI(username, doctorUsername); // Update the UI
            }
        });
        
        saveVaccinationButton.setOnAction(event -> {
        	saveToVaccination(username, vaccinations);
        });
         

        HBox vaccinationButtonsBox = new HBox(saveVaccinationButton, addVaccinationButton); 
        editVaccinationBox.getChildren().addAll(VaccinationLabel, listVaccination, vaccinationTextField, vaccinationButtonsBox);
        

        // Chat box
        VBox chatBox = new VBox(new Label("Chat"));

        String[] firstLastName = getFirstLastName(doctorUsername);

        //firstLastname[0] = firstname
        //firstLastname[1] = lastname

        ListView<String> chatListView = new ListView<>();

        TextField chatInputField = new TextField();

        Button sendButton = new Button("Send");

        HBox chatInputBox = new HBox(chatInputField, sendButton);

        if (username != "") {
            chatListView.getItems().addAll(getChatData(username, doctorUsername));
            chatBox.getChildren().addAll(chatListView, chatInputBox);

            sendButton.setOnAction(event -> {
                String[] chatData = getChatData(username, doctorUsername);
                String newMessage = "Nurse " + firstLastName[1] + ": " + chatInputField.getText().trim();

                if (!newMessage.isEmpty()) {
                    // Update the chat data array
                    chatData = updateChatData(chatData, newMessage);

                    // Save the updated chat data to the file
                    saveToChat(username, doctorUsername, chatData);

                    // Refresh the chat list
                    chatListView.getItems().clear();
                    chatListView.getItems().addAll(chatData);

                    // Clear the input field
                    chatInputField.clear();
                }
            });
        }
        List<String> patientInfo = new ArrayList<>(Arrays.asList(new String[5])); // Initialize patientInfo

        Button addPatient = new Button("Change Patient Information");

        addPatient.setOnAction(event -> {
            String[] result = showAddPatientDialog();
            if (result != null) {
                patientInfo.clear();
                patientInfo.addAll(Arrays.asList(result));
                // Print out the updated patient info
                addPatientInfo(patientInfo, username);
            }
        });

        

        // Adding components to the right panel
        rightPanel.getChildren().addAll(editPrescriptionBox, editVaccinationBox, chatBox, addPatient);

        return rightPanel;
    }


    private String[][] getAllData(String username) {
        System.out.println(username);
        String[][] allData = new String[4][]; // Initialize the array with 2 rows
        String prescriptionFilePath = username + "/prescription.txt";
        String vaccinationsFilePath = username + "/vaccinations.txt";
        String visitsFilePath = username + "/visits.txt";
        String patientInfoPath = username + "/patientInfo.txt";

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
            File visitsFile = new File(visitsFilePath);
            if (vaccinationsFile.exists()) {
                System.out.println("visits file exists");
                BufferedReader visitsReader = new BufferedReader(new FileReader(visitsFile));
                String line;
                String[] visitsArray = new String[20]; // Array to store vaccinations, assuming a maximum of 20

                // Read each line from the file and add it to vaccinationsArray
                int index = 0;
                while ((line = visitsReader.readLine()) != null && index < 20) {
                	visitsArray[index++] = line;
                }

                // Store vaccinationsArray in the second index of allData
                allData[2] = visitsArray;

                // Print vaccinations
                System.out.println("visits:");
                for (String visit : allData[2]) {
                    if (visit != null) {
                        System.out.println(visit);
                    }
                }

                visitsReader.close();
            } else {
                System.out.println("Vaccinations file does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Read patient info data
            File patientInfoFile = new File(patientInfoPath);
            if (patientInfoFile.exists()) {
                System.out.println("Patient info file exists");
                BufferedReader patientInfoReader = new BufferedReader(new FileReader(patientInfoFile));
                String line;
                String[] patientInfoArray = new String[20]; // Array to store patient info, assuming a maximum of 20 lines

                // Read each line from the file and add it to patientInfoArray
                int index = 0;
                while ((line = patientInfoReader.readLine()) != null && index < 20) {
                    patientInfoArray[index++] = line;
                }

                // Store patientInfoArray in the third index of allData
                allData[3] = patientInfoArray;

                // Print patient info
                System.out.println("Patient Info:");
                for (String info : allData[3]) {
                    if (info != null) {
                        System.out.println(info);
                    }
                }

                patientInfoReader.close();
            } else {
                System.out.println("Patient info file does not exist");
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
    private void updateUI(String username, String doctorUsername) {
        // Refresh the right panel to reflect changes after deleting a prescription
        this.setRight(createRightPanel(allData, username, doctorUsername));
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
    
    private String[] getFirstLastName(String username) {
        String[] names = new String[3];
        String userDetailsFilePath = username + "/userDetails.txt";

        try {
            File file = new File(userDetailsFilePath);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String username1 = reader.readLine();
                username1 = (username1.split(":")[1].trim());
                String password1 = reader.readLine();
                password1 = (password1.split(":")[1].trim());
                String role1 = reader.readLine();
                role1 = (role1.split(":")[1].trim());
                String first1 = reader.readLine();
                first1 = (first1.split(":")[1].trim());
                String last1 = reader.readLine();
                last1 = (last1.split(":")[1].trim());
                String email1 = reader.readLine();
                email1 = (email1.split(":")[1].trim());
                reader.close();

                names[0] = first1;
                names[1] = last1;
                names[2] = email1;
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
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
    private void saveToVisits(String username, String[] visits) {
      	 String prescriptionFilePath = username + "/visits.txt";
      	 try (BufferedWriter writer = new BufferedWriter(new FileWriter(prescriptionFilePath))) {
      	        // Clear the file before writing
      	        writer.write(""); // Clear the file content
      	        
      	        // Write each prescription to the file
      	        for (String vaccination : visits) {
      	            if (vaccination != null) {
      	                writer.write(vaccination); // Write the prescription
      	                writer.newLine(); // Move to the next line for the next prescription
      	            }
      	        }
      	    } catch (IOException e) {
      	        e.printStackTrace();
      	    }
      	 
      	 
      	 
      	
      }
    
    
    
    
    private String readFile(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
        }
        return contentBuilder.toString();
    }
    
    private void saveToChat(String username, String doctorUsername, String[] chatData) {
        String chatPath = username + "/chats/" + doctorUsername + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(chatPath))) {
            // Clear the file before writing
            writer.write("");
            // Write each message to the file
            for (String message : chatData) {
                if (message != null) {
                    writer.write(message);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] updateChatData(String[] chatData, String newMessage) {
        String[] updatedChatData = new String[chatData.length + 1];
        System.arraycopy(chatData, 0, updatedChatData, 0, chatData.length);
        updatedChatData[chatData.length] = newMessage;
        return updatedChatData;
    }

    private String[] getChatData(String username, String doctorUsername) {
        String chatPath = username + "/chats/" + doctorUsername + ".txt";
        // Read the chat data from the text file
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(chatPath))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            // Handle the exception as needed
        }

        String[] chatData = new String[lineCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(chatPath))) {
            for (int i = 0; i < chatData.length; i++) {
                chatData[i] = reader.readLine();
            }
        } catch (IOException e) {
            // Handle the exception as needed
        }

        return chatData;
    }
    
    private String[] showAddPatientDialog() {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add Patient Information");
        dialog.setHeaderText("Please enter patient information:");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the grid for the input fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Create the input fields
        TextField emailField = new TextField();
        TextField phoneField = new TextField();
        TextField insuranceField = new TextField();
        TextField addressField = new TextField();
        TextField pharmacyField = new TextField();

        // Add labels and fields to the grid
        grid.add(new Label("Email:"), 0, 0);
        grid.add(emailField, 1, 0);
        grid.add(new Label("Phone Number:"), 0, 1);
        grid.add(phoneField, 1, 1);
        grid.add(new Label("Insurance:"), 0, 2);
        grid.add(insuranceField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Pharmacy Name:"), 0, 4);
        grid.add(pharmacyField, 1, 4);

        // Set the content of the dialog pane
        dialog.getDialogPane().setContent(grid);

        // Request focus on the email field by default
        emailField.requestFocus();

        // Convert the result to a String[] when OK is pressed
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new String[]{
                    emailField.getText(),
                    phoneField.getText(),
                    insuranceField.getText(),
                    addressField.getText(),
                    pharmacyField.getText()
                };
            }
            return null;
        });

        // Show the dialog and wait for the user response
        Optional<String[]> result = dialog.showAndWait();
        return result.orElse(null);
    }
    
    private void addPatientInfo(List<String> patientInfo, String username) {
    	String patientPath = username + "/patientInfo.txt";
    	try (BufferedWriter writer = new BufferedWriter(new FileWriter(patientPath))) {
            for (String info : patientInfo) {
                writer.write(info);
                writer.newLine(); // Add newline after each info
            }
            System.out.println("Patient information saved successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while saving patient information: " + e.getMessage());
        }
    }

    
}
