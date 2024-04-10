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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PatientPage extends BorderPane {
    private Label patientNameLabel = new Label("");
    private Label doctorNameLabel = new Label("");
    private Label doctorEmailLabel = new Label("");
    private Button logoutButton;
    private String[][] allData;

    public PatientPage(Stage primaryStage, String firstName, String lastName, String email, String username) {
        super();
        this.setTop(new VBox(patientNameLabel));
        allData = getAllData(username);
        VBox leftPanel = setupLeftPanel(firstName, lastName, email, allData, username, primaryStage);
        this.setRight(createRightPanel(allData, username));
        this.setLeft(leftPanel);
        VBox centerPanel = createCenterPanel(username, "", allData[2]);
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
        TabPane tabPane = new TabPane();
        TextArea prescriptionContent = new TextArea();
        prescriptionContent.setEditable(false);
        prescriptionContent.setPrefHeight(600);
        prescriptionContent.setPromptText("Prescription information displayed here...");
        Tab prescriptionTab = new Tab("Prescription", prescriptionContent);

        TextArea immunizationContent = new TextArea();
        immunizationContent.setEditable(false);
        immunizationContent.setPrefHeight(600);
        immunizationContent.setPromptText("Immunization records displayed here...");
        Tab immunizationTab = new Tab("Immunization Records", immunizationContent);

        TextArea pharmacyContent = new TextArea();
        pharmacyContent.setEditable(false);
        pharmacyContent.setPrefHeight(600);
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
                    view.setOnAction(event -> {
                        VBox centerPanel = createCenterPanel(username, item, allData[2]);
                        this.setCenter(centerPanel);
                    });
                    viewBox.getChildren().addAll(visit, view);
                    visitsBox.getChildren().add(viewBox);
                }
            }
        }
        visitsContent.setContent(visitsBox);
        Tab visitsTab = new Tab("Visits", visitsContent);

        prescriptionTab.setClosable(false);
        immunizationTab.setClosable(false);
        pharmacyTab.setClosable(false);
        visitsTab.setClosable(false);

        tabPane.getTabs().addAll(prescriptionTab, immunizationTab, pharmacyTab, visitsTab);

        if (allData[0] != null) {
            StringBuilder prescriptionBuilder = new StringBuilder();
            for (String item : allData[0]) {
                if (item != null) {
                    prescriptionBuilder.append(item).append("\n");
                }
            }
            prescriptionContent.setText(prescriptionBuilder.toString());
        }

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

        VBox doctorProfileBox = new VBox(10);
        doctorProfileBox.setStyle("-fx-background-color: #add8e6; -fx-padding: 10;");
        Label doctorNameLabel = new Label("Patient: " + firstName + " " + lastName);
        Label doctorEmailLabel = new Label("Email: " + email);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> logout(primaryStage));
        doctorProfileBox.getChildren().addAll(doctorNameLabel, doctorEmailLabel, logoutButton);

        leftPanel.getChildren().addAll(user, tabPane, doctorProfileBox);

        return leftPanel;
    }

    private VBox createCenterPanel(String username, String visit, String[] visits) {
        String visitSummaryPath = username + "/visits" + "/" + visit + "/summary.txt";
        String healthConcernsPath = username + "/visits" + "/" + visit + "/concern.txt";
        TextArea visitSummaryTextArea = new TextArea();
        visitSummaryTextArea.setPromptText("Visit Summary");
        visitSummaryTextArea.setPrefHeight(390);
        TextArea healthConcernsTextArea = new TextArea();
        healthConcernsTextArea.setPromptText("Health Concerns");
        healthConcernsTextArea.setPrefHeight(390);

        if (username != "" && visit != "") {
            try {
                String visitSummaryText = readFile(visitSummaryPath);
                visitSummaryTextArea.setText(visitSummaryText);

                String healthConcernsText = readFile(healthConcernsPath);
                healthConcernsTextArea.setText(healthConcernsText);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        

        VBox centerPanel = new VBox(visitSummaryTextArea, healthConcernsTextArea);

        return centerPanel;
    }
    
    private VBox createRightPanel(String allData[][], String username) {
        VBox chatBox = new VBox(new Label("Chat"));
        String[] firstLastName = getFirstLastName(username);

        VBox rightPanel = new VBox();
        rightPanel.setSpacing(10);
        rightPanel.setPrefWidth(300);

        String[] doctorNurse = new String[6];
        String[] doctorNames = new String[3];
        String[] nurseNames = new String[3];
        doctorNurse = getDoctorNurse(username);

        doctorNames = getFirstLastName(doctorNurse[4]);
        nurseNames = getFirstLastName(doctorNurse[5]);
        HBox radioBox = new HBox(10);
        RadioButton doctorRadio = new RadioButton("Doctor: " + doctorNames[0] + " " + doctorNames[1]);
        RadioButton nurseRadio = new RadioButton("Nurse: " + nurseNames[0] + " " + nurseNames[1]);
        ToggleGroup group = new ToggleGroup();
        doctorRadio.setToggleGroup(group);
        nurseRadio.setToggleGroup(group);
        radioBox.getChildren().addAll(doctorRadio, nurseRadio);
        

        // Retrieve the doctor and nurse usernames
        String doctorUsername = doctorNurse[4];
        String nurseUsername = doctorNurse[5];

        // Create the chat UI components
        ListView<String> chatListView = new ListView<>();
        TextField chatInputField = new TextField();
        Button sendButton = new Button("Send");
        HBox chatInputBox = new HBox(chatInputField, sendButton);

        // Add change listener to radio buttons
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == doctorRadio) {
                chatListView.getItems().clear();
                chatListView.getItems().addAll(getChatData(username, doctorUsername));
            } else if (newValue == nurseRadio) {
                chatListView.getItems().clear();
                chatListView.getItems().addAll(getChatData(username, nurseUsername));
            }
        });

        sendButton.setOnAction(event -> {
            String newMessage = "Patient " + firstLastName[1] + ": " + chatInputField.getText().trim();

            if (!newMessage.isEmpty()) {
                String receiverUsername;
                if (group.getSelectedToggle() == doctorRadio) {
                    receiverUsername = doctorUsername;
                } else {
                    receiverUsername = nurseUsername;
                }
                String[] chatData = getChatData(username, receiverUsername);

                // Update the chat data array
                chatData = updateChatData(chatData, newMessage);

                // Save the updated chat data to the file
                saveToChat(username, receiverUsername, chatData);

                // Refresh the chat list
                chatListView.getItems().clear();
                chatListView.getItems().addAll(chatData);

                // Clear the input field
                chatInputField.clear();
            }
        });

        chatBox.getChildren().addAll(chatListView, chatInputBox);
        rightPanel.getChildren().addAll(radioBox, chatBox);

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
    
    private String[] getDoctorNurse(String username) {
    	String[] names = new String[6];
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
                String birthday1 = reader.readLine();
                birthday1 = (birthday1.split(":")[1].trim());
                String doctor1 = reader.readLine();
                doctor1 = (doctor1.split(":")[1].trim());
                String nurse1 = reader.readLine();
                nurse1 = (nurse1.split(":")[1].trim());
                
                reader.close();

                names[0] = first1;
                names[1] = last1;
                names[2] = email1;
                names[3] = birthday1;
                names[4] = doctor1;
                names[5]= nurse1;
            } else {
                System.out.println("File does not exist");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return names;
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
}
