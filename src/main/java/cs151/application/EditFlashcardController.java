package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import java.util.List;

public class EditFlashcardController
{
    @FXML private TextArea frontArea;
    @FXML private TextArea backArea;
    @FXML private ComboBox<String> deckBox;
    @FXML private ComboBox<FlashcardStatus> statusBox;

    private Flashcard flashcard;

    public void setFlashcard(Flashcard flashcard)
    {
        this.flashcard = flashcard;
        deckBox.setValue(flashcard.getDeckName());
        frontArea.setText(flashcard.getFrontText());
        backArea.setText(flashcard.getBackText());
        statusBox.setValue(flashcard.getStatus());
    }

    @FXML
    public void handleSave()
    {
        flashcard.setDeckName(deckBox.getValue());
        flashcard.setFrontText(frontArea.getText());
        flashcard.setBackText(backArea.getText());
        flashcard.setStatus(statusBox.getValue());

        List<Flashcard> flashcards = FlashcardStore.getAllFlashcards();

        for (int i = 0; i < flashcards.size(); i++)
        {
            Flashcard current = flashcards.get(i);

            if (current.getCreationDate().equals(flashcard.getCreationDate()))
            {
                flashcards.set(i, flashcard);
                break;
            }
        }

        FlashcardStore.saveAllFlashcards(flashcards);
        goBackToList();
    }
    @FXML
    public void handleBack()
    {
        goBackToList();
    }
    @FXML
    public void initialize() {
        statusBox.getItems().setAll(FlashcardStatus.values());
        deckBox.getItems().setAll(DeckStore.getAllDeckNames());
    }

    private void goBackToList()
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/listFlashcards.fxml"));
            Stage stage = (Stage) frontArea.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
