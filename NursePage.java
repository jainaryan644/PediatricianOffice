package application;


import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class NursePage extends VBox {
    private Stage primaryStage;

    public NursePage(Stage primaryStage) {
        this.primaryStage = primaryStage; // Set the primaryStage field
        setAlignment(Pos.CENTER);
        Label label = new Label("NursePage");
        Button logoutButton = new Button("Logout");

        getChildren().addAll(label, logoutButton);
        logoutButton.setOnAction(e -> logout());
    }

    private void logout() {
        LoginPage loginPage = new LoginPage(primaryStage);
        Scene loginScene = new Scene(loginPage, 600, 800);
        primaryStage.setScene(loginScene);
    }
}
