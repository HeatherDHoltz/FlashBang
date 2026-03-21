package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller for simle UI interactions
 * Handles updating lable text when button is clicked
 */
public class MainController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}