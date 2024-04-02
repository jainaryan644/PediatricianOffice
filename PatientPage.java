package application;


import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class PatientPage extends VBox {
    private Button logoutButton;

    public PatientPage() {
        setAlignment(Pos.CENTER);
        Label label = new Label("PatientPage");
        logoutButton = new Button("Logout"); // Remove the type declaration

        getChildren().addAll(label, logoutButton);
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}
