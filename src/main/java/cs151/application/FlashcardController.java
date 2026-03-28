package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class FlashcardController
{
    @FXML private ComboBox<String> deckBox;
    @FXML private TextField frontField;
    @FXML private TextField backField;

    @FXML
    public void initialize()
    {
        for (Deck d : DeckStore.getAllDecks())
        {
            deckBox.getItems().add(d.getName());
        }
    }

    @FXML
    public void handleSave(ActionEvent event)
    {
        String deck = deckBox.getValue();
        String front = frontField.getText();
        String back = backField.getText();

        if (deck == null || front.isEmpty() || back.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("All fields required");
            alert.showAndWait();
            return;
        }

        Flashcard card = new Flashcard(deck, front, back);
        FlashcardStore.addFlashcard(card);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Flashcard saved!");
        alert.showAndWait();

        goHome(event);
    }

    @FXML
    public void handleBack(ActionEvent event)
    {
        goHome(event);
    }

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