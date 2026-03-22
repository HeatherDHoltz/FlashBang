package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import cs151.application.Deck;
import cs151.application.DeckStore;
import javafx.stage.Stage;

/**
 * Controller for the define deck page
 * Handles user input for creating a new deck, validates deck info, saves deck, and returns user
 * to homepage
 */
public class DefineDeckController {
    //Text field to enter deck name
    @FXML
    private TextField deckNameField;
    //Text area to enter description
    @FXML
    private TextArea descriptionField;

    /**
     * Saves a new deck after input is validated
     * Checks if deck name isnt empty and that its unique, then stores  and
     * returns user to jomepage
     * @param event buttom click event
     */
    @FXML
    public void handleSaveDeck(ActionEvent event) {
        String deckName = deckNameField.getText().trim();
        String description = descriptionField.getText();
        //check for empty deck name
        if (deckName.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Deck Name cannot be empty.");
            alert.showAndWait();
            return;
        }
        //Load existing decks/check for duplicate names
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
        // Show success message
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Deck Saved");
        alert.setHeaderText(null);
        alert.setContentText("Deck '" + deckName + "' saved successfully.");
        alert.showAndWait();

        // 🔥 Go back to Home page
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Navigates back to the home page without saving the deck
     * @param event buttom click event
     */
    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
