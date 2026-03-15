package cs151.application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import cs151.application.Deck;
import cs151.application.DeckStore;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class DefineDeckController {
    @FXML
    private TextField deckNameField;
    @FXML
    private TextArea descriptionField;

    @FXML
    public void handleSaveDeck(ActionEvent event) {
        String deckName = deckNameField.getText().trim();
        String description = descriptionField.getText();
        if (deckName.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Deck Name cannot be empty.");
            alert.showAndWait();
            return;
        }
        DeckStore.loadDecks();
        if (!DeckStore.isUnique(deckName)) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Deck Name must be unique.");
            alert.showAndWait();
            return;
        }
        Deck deck = new Deck(deckName, description);
        DeckStore.addDeck(deck);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Deck Saved");
        alert.setHeaderText(null);
        alert.setContentText("Deck '" + deckName + "' saved successfully.");
        alert.showAndWait();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("homePage.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
