package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

/**
 * Controller class for the flashcard Creation screen
 * handles the user input, validation, and saving of flashcards
 * also handles navigation to the homepage
 */
public class FlashcardController
{
    //Dropdown for sleecting a deck
    @FXML private ComboBox<String> deckBox;
    //Drpdown for status
    @FXML private ComboBox<FlashcardStatus> statusBox;
    //Text field for the front
    @FXML private TextArea frontField;
    //Text field for the back
    @FXML private TextArea backField;


    /**
     * Initializes the controller after the FXML is loaded
     * Populates deck dropdown with all available decks
     */
    @FXML
    public void initialize()
    {
        for (Deck d : DeckStore.getAllDecks())
        {
            deckBox.getItems().add(d.getName());
        }
        statusBox.getItems().addAll(FlashcardStatus.values());
        statusBox.setValue(FlashcardStatus.NEW);
    }

    /**
     * Handles the save button click
     * Validates input fields, cerates a flashcard
     * stores it and shows confirmation
     * @param event button click event
     */
    @FXML
    public void handleSave(ActionEvent event)
    {
        String deck = deckBox.getValue();
        String front = frontField.getText().trim();
        String back = backField.getText().trim();
        FlashcardStatus status = statusBox.getValue();

        if (deck == null || front.isEmpty() || back.isEmpty() || status == null)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("All fields required");
            alert.showAndWait();
            return;
        }

        for (Flashcard card : FlashcardStore.getAllFlashcards())
        {
            if (card.getDeckName().equals(deck) &&
                    card.getFrontText().trim().equalsIgnoreCase(front))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Front text must be unique within the selected deck.");
                alert.showAndWait();
                return;
            }
        }

        Flashcard card = new Flashcard(deck, front, back);
        card.setStatus(status);
        FlashcardStore.addFlashcard(card);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Flashcard saved!");
        alert.showAndWait();

        goHome(event);
    }

    /**
     * handles teh back button click
     * Navigates back to the homepage
     * @param event the button click event
     */
    @FXML
    public void handleBack(ActionEvent event)
    {
        goHome(event);
    }

    /**
     * Loads and displays home page
     * @param event used to retrieve the current stage
     */
    private void goHome(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}