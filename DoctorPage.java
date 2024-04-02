package application;


import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class DoctorPage extends VBox {
    private Button logoutButton;

    public DoctorPage() {
        setAlignment(Pos.CENTER);
        Label label = new Label("DoctorPage");
        logoutButton = new Button("Logout"); // Remove the type declaration

        getChildren().addAll(label, logoutButton);
    }

    public Button getLogoutButton() {
        return logoutButton;
    }
}
