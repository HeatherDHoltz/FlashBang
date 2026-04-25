package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Controller for the Search Flashcards page.
 * Shows all flashcards in a table, supports case-insensitive search,
 * allows reviewing a flashcard, and allows deleting a selected flashcard.
 */
public class ListFlashcardsController
{

    @FXML private TextField searchField;
    @FXML private TableView<Flashcard> table;
    @FXML private TableColumn<Flashcard, String> deckCol;
    @FXML private TableColumn<Flashcard, String> frontCol;
    @FXML private TableColumn<Flashcard, String> backCol;
    @FXML private TableColumn<Flashcard, String> dateCol;
    @FXML private TableColumn<Flashcard, String> statusCol;
    @FXML private TableColumn<Flashcard, String> lastReviewCol;

    private final ObservableList<Flashcard> allFlashcards = FXCollections.observableArrayList();
    private final ObservableList<Flashcard> filteredFlashcards = FXCollections.observableArrayList();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    public void initialize()
    {
        FlashcardStore.ensureSeedData();
        setupColumns();

        allFlashcards.setAll(FlashcardStore.getAllFlashcards());
        filteredFlashcards.setAll(allFlashcards);
        table.setItems(filteredFlashcards);

        table.setRowFactory(tv -> {
            TableRow<Flashcard> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Flashcard selectedCard = row.getItem();
                    openEditFlashcardPage(selectedCard);
                }
            });
            return row;
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> applySearch());
    }

    private void setupColumns()
    {
        deckCol.setCellValueFactory(c -> new SimpleStringProperty(firstLine(c.getValue().getDeckName())));
        frontCol.setCellValueFactory(c -> new SimpleStringProperty(firstLine(c.getValue().getFrontText())));
        backCol.setCellValueFactory(c -> new SimpleStringProperty(firstLine(c.getValue().getBackText())));

        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCreationDate().format(formatter)));

        statusCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getStatus().toString()));

        lastReviewCol.setCellValueFactory(cellData -> {
            LocalDateTime reviewDate = cellData.getValue().getLastReviewDate();
            return new SimpleStringProperty(reviewDate == null ? "" : reviewDate.format(formatter));
        });
    }

    @FXML
    public void handleSearch(ActionEvent event)
    {
        applySearch();
    }

    private void applySearch()
    {
        String query = searchField.getText();
        filteredFlashcards.clear();

        if (query == null || query.trim().isEmpty())
        {
            filteredFlashcards.addAll(allFlashcards);
            return;
        }

        String normalizedQuery = query.trim().toLowerCase();

        for (Flashcard card : allFlashcards)
        {
            if (matches(card, normalizedQuery))
            {
                filteredFlashcards.add(card);
            }
        }
    }

    private boolean matches(Flashcard card, String query) {
        return card.getDeckName().toLowerCase().contains(query)
                || card.getFrontText().toLowerCase().contains(query)
                || card.getBackText().toLowerCase().contains(query)
                || card.getStatus().toString().toLowerCase().contains(query);
    }

    private String firstLine(String text)
    {
        if (text == null || text.isEmpty())
        {
            return "";
        }

        String normalized = text.replace("\r", "");
        int newlineIndex = normalized.indexOf('\n');

        if (newlineIndex >= 0)
        {
            return normalized.substring(0, newlineIndex);
        }

        return normalized;
    }

    @FXML
    public void handleReview(ActionEvent event) {
        Flashcard selectedCard = table.getSelectionModel().getSelectedItem();

        if (selectedCard == null) {
            showError("Please select a flashcard first.");
            return;
        }

        ChoiceDialog<FlashcardStatus> dialog = new ChoiceDialog<>(
                selectedCard.getStatus(),
                FlashcardStatus.values()
        );
        dialog.setTitle("Review Flashcard");
        dialog.setHeaderText("Update Flashcard Status");
        dialog.setContentText("Choose new status:");

        Optional<FlashcardStatus> result = dialog.showAndWait();

        if (result.isPresent()) {
            selectedCard.setStatus(result.get());
            selectedCard.setLastReviewDate(LocalDateTime.now());

            FlashcardStore.saveAllFlashcards(allFlashcards);
            table.refresh();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Flashcard status updated successfully.");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleDelete(ActionEvent event)
    {
        Flashcard selectedCard = table.getSelectionModel().getSelectedItem();

        if (selectedCard == null)
        {
            showError("Please select a flashcard to delete.");
            return;
        }

        allFlashcards.remove(selectedCard);
        filteredFlashcards.remove(selectedCard);
        FlashcardStore.saveAllFlashcards(allFlashcards);
    }

    @FXML
    public void handleBack(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void openEditFlashcardPage(Flashcard selectedCard)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/editFlashcard.fxml"));
            Parent root = loader.load();

            EditFlashcardController controller = loader.getController();
            controller.setFlashcard(selectedCard);

            Stage stage = (Stage) table.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void showError(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
}