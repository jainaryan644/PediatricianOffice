package hw1;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class PatientPage extends BorderPane {

    private Label patientNameLabel = new Label("Patient <Unknown>");
    private Label doctorNameLabel = new Label("<Unknown>");
    private Label doctorEmailLabel = new Label("<Unknown>");
    private Button logoutButton = new Button("Log out");

    public PatientPage() {
        // Styling
        patientNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0000ff;");
        doctorNameLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #0076a3;");
        doctorEmailLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #0076a3;");

        HBox header = createHeader();
        this.setTop(header);
        
        ListView<String> visitDatesList = createVisitDatesList();
        this.setLeft(visitDatesList);

        SplitPane splitPane = createMainContent();
        this.setCenter(splitPane);
        
        this.setBottom(logoutButton);
        BorderPane.setAlignment(logoutButton, Pos.CENTER_LEFT);
        BorderPane.setMargin(logoutButton, new Insets(12, 0, 12, 12));
    }

    private HBox createHeader() {
        HBox headerBox = new HBox();
        headerBox.setPadding(new Insets(15, 12, 15, 12));
        headerBox.setSpacing(10);
        headerBox.setStyle("-fx-background-color: #336699;");

        Circle avatarCircle = new Circle(20, Color.SEAGREEN);
        headerBox.getChildren().addAll(patientNameLabel, avatarCircle, doctorNameLabel, doctorEmailLabel);

        return headerBox;
    }

    private ListView<String> createVisitDatesList() {
        ListView<String> visitDatesList = new ListView<>();
        visitDatesList.getItems().addAll("All");
        visitDatesList.setPrefWidth(200);
        visitDatesList.setPadding(new Insets(10));
        return visitDatesList;
    }

    private SplitPane createMainContent() {
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.HORIZONTAL);

        VBox visitSummaryBox = createVisitSummaryBox();
        VBox rightPanel = createRightPanel();

        splitPane.getItems().addAll(visitSummaryBox, rightPanel);
        splitPane.setDividerPositions(0.5);

        return splitPane;
    }

    private VBox createVisitSummaryBox() {
        VBox visitSummaryBox = new VBox(10);
        visitSummaryBox.setPadding(new Insets(10));

        TextArea visitSummaryTextArea = new TextArea();
        visitSummaryTextArea.setPromptText("Visit Summary");
        visitSummaryTextArea.setPrefHeight(200);

        TextArea healthConcernTextArea = new TextArea();
        healthConcernTextArea.setPromptText("Health Concern");
        healthConcernTextArea.setPrefHeight(200);

        visitSummaryBox.getChildren().addAll(new Label("Visit Summary"), visitSummaryTextArea, new Label("Health Concern"), healthConcernTextArea);

        return visitSummaryBox;
    }

    private VBox createRightPanel() {
        VBox rightPanel = new VBox(10);
        rightPanel.setPadding(new Insets(10));
        rightPanel.setPrefWidth(300);

        GridPane infoGrid = createInfoGrid();
        VBox chatSection = createChatSection();

        rightPanel.getChildren().addAll(infoGrid, chatSection);
        return rightPanel;
    }

    private GridPane createInfoGrid() {
        GridPane infoGrid = new GridPane();
        infoGrid.setHgap(10);
        infoGrid.setVgap(10);

        String[] labels = {"Phone Number", "Email", "Insurance", "Address", "Pharmacy"};
        for (int i = 0; i < labels.length; i++) {
            Label label = new Label(labels[i]);
            TextField textField = new TextField();
            textField.setPromptText("Placeholder");
            Button editButton = new Button("Edit");

            infoGrid.add(label, 0, i);
            infoGrid.add(textField, 1, i);
            infoGrid.add(editButton, 2, i);
        }

        return infoGrid;
    }

    private VBox createChatSection() {
        VBox chatBox = new VBox(10);
        chatBox.setPadding(new Insets(5));

        ListView<String> chatListView = new ListView<>();
        chatListView.setPrefHeight(100); // You can adjust this as needed
        TextField chatInputField = new TextField();
        chatInputField.setPromptText("Type here...");
        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #5aac44; -fx-text-fill: white;");
        
        HBox chatInputBox = new HBox(5, chatInputField, sendButton);
        chatInputBox.setAlignment(Pos.CENTER);
        chatListView.setPrefHeight(140);
        
        chatBox.getChildren().addAll(new Label("Chat"), chatListView, chatInputBox);

        return chatBox;
    }
    public Button getLogoutButton() {
        return logoutButton;
    }
}

