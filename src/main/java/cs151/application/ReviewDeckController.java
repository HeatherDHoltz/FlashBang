package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDeckController {

    @FXML private Label deckNameLabel;
    @FXML private ComboBox<String> filterBox;
    @FXML private TextArea frontArea;
    @FXML private TextArea backArea;
    @FXML private ComboBox<FlashcardStatus> statusBox;
    @FXML private Label creationDateLabel;
    @FXML private Label lastReviewLabel;
    @FXML private Button nextButton;
    @FXML private Button prevButton;

    private Deck deck;
    private List<Flashcard> filteredCards = new ArrayList<>();
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        filterBox.getItems().addAll("All", "New", "Learning", "Mastered");
        filterBox.setValue("All");

        statusBox.getItems().addAll(FlashcardStatus.values());

        filterBox.setOnAction(event -> loadCards());
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameLabel.setText(deck.getName());
        loadCards();
    }

    private void loadCards() {
        if (deck == null) {
            return;
        }

        List<Flashcard> allCards = FlashcardStore.getAllFlashcards();
        filteredCards.clear();

        for (Flashcard card : allCards) {
            if (!card.getDeckName().equalsIgnoreCase(deck.getName())) {
                continue;
            }

            String selectedFilter = filterBox.getValue();

            if (selectedFilter.equals("All")
                    || card.getStatus().toString().equalsIgnoreCase(selectedFilter)) {
                filteredCards.add(card);
            }
        }

        currentIndex = 0;
        showCard();
    }

    private void showCard() {
        if (filteredCards.isEmpty()) {
            frontArea.clear();
            backArea.clear();
            statusBox.setValue(null);
            creationDateLabel.setText("No flashcards");
            lastReviewLabel.setText("No flashcards");
            prevButton.setDisable(true);
            nextButton.setDisable(true);
            return;
        }

        Flashcard card = filteredCards.get(currentIndex);

        // Automatically update last reviewed date when card is viewed
        card.setLastReviewDate(LocalDateTime.now());

        List<Flashcard> allCards = FlashcardStore.getAllFlashcards();

        for (int i = 0; i < allCards.size(); i++) {
            if (allCards.get(i).getCreationDate().equals(card.getCreationDate())) {
                allCards.set(i, card);
                break;
            }
        }

        FlashcardStore.saveAllFlashcards(allCards);

        frontArea.setText(card.getFrontText());
        backArea.setText(card.getBackText());
        statusBox.setValue(card.getStatus());
        creationDateLabel.setText(card.getCreationDate().toString());
        lastReviewLabel.setText(card.getLastReviewDate().toString());

        prevButton.setDisable(currentIndex == 0);
        nextButton.setDisable(currentIndex == filteredCards.size() - 1);
    }
    @FXML
    public void handlePrevious() {
        if (currentIndex > 0) {
            currentIndex--;
            showCard();
        }
    }

    @FXML
    public void handleNext() {
        if (currentIndex < filteredCards.size() - 1) {
            currentIndex++;
            showCard();
        }
    }

    @FXML
    public void handleSave() {
        if (filteredCards.isEmpty()) {
            return;
        }

        Flashcard card = filteredCards.get(currentIndex);

        card.setFrontText(frontArea.getText());
        card.setBackText(backArea.getText());
        card.setStatus(statusBox.getValue());
        card.setLastReviewDate(LocalDateTime.now());

        List<Flashcard> allCards = FlashcardStore.getAllFlashcards();

        for (int i = 0; i < allCards.size(); i++) {
            if (allCards.get(i).getCreationDate().equals(card.getCreationDate())) {
                allCards.set(i, card);
                break;
            }
        }

        FlashcardStore.saveAllFlashcards(allCards);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Saved");
        alert.setHeaderText(null);
        alert.setContentText("Flashcard saved successfully.");
        alert.showAndWait();

        loadCards();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/listDecks.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}