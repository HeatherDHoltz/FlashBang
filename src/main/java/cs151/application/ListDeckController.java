package cs151.application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Controller for list deck pages
 * Displays all stored decks in a table view, sorted alphabetically by the deck name
 * Allows edit/delete of selected deck and navigation back to home page
 */
public class ListDeckController {

    // Table that displays all decks
    @FXML
    private TableView<Deck> deckTable;

    // Column for deck names
    @FXML
    private TableColumn<Deck, String> nameColumn;

    // Column for deck description
    @FXML
    private TableColumn<Deck, String> desCol;

    /**
     * Navigates back to home page
     * @param event home button clicked
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

    /**
     * Deletes the selected deck from the table and csv file
     */
    @FXML
    public void handleDeleteDeck() {
        Deck selectedDeck = deckTable.getSelectionModel().getSelectedItem();

        if (selectedDeck == null) {
            showAlert("No deck selected", "Please select a deck to delete.");
            return;
        }

        boolean deleted = DeckStore.deleteDeck(selectedDeck.getName());

        if (deleted) {
            refreshDeckTable();
            showAlert("Success", "Deck deleted successfully.");
        } else {
            showAlert("Error", "Deck could not be deleted.");
        }
    }

    /**
     * Edits the selected deck's name and description
     */
    @FXML
    public void handleEditDeck() {
        Deck selectedDeck = deckTable.getSelectionModel().getSelectedItem();

        if (selectedDeck == null) {
            showAlert("No deck selected", "Please select a deck to edit.");
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog(selectedDeck.getName());
        nameDialog.setTitle("Edit Deck");
        nameDialog.setHeaderText("Edit Deck Name");
        nameDialog.setContentText("New deck name:");

        Optional<String> nameResult = nameDialog.showAndWait();
        if (nameResult.isEmpty()) {
            return;
        }

        String newName = nameResult.get().trim();
        if (newName.isEmpty()) {
            showAlert("Invalid Name", "Deck name cannot be empty.");
            return;
        }

        TextInputDialog descriptionDialog = new TextInputDialog(selectedDeck.getDescription());
        descriptionDialog.setTitle("Edit Deck");
        descriptionDialog.setHeaderText("Edit Deck Description");
        descriptionDialog.setContentText("New description:");

        Optional<String> descriptionResult = descriptionDialog.showAndWait();
        if (descriptionResult.isEmpty()) {
            return;
        }

        String newDescription = descriptionResult.get().trim();

        boolean updated = DeckStore.editDeck(
                selectedDeck.getName(),
                newName,
                newDescription
        );

        if (updated) {
            refreshDeckTable();
            showAlert("Success", "Deck updated successfully.");
        } else {
            showAlert("Error", "Deck could not be updated. Name may already exist.");
        }
    }

    /**
     * Initializes table at page load
     * Binds table columns to deck properties, loads deck data
     * Sorts deck alphabetically, displays the decks in table
     */
    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        refreshDeckTable();
    }

    /**
     * Reloads the deck table from storage
     */
    private void refreshDeckTable() {
        List<Deck> decks = DeckStore.getAllDecks();

        Collections.sort(decks, new Comparator<Deck>() {
            @Override
            public int compare(Deck d1, Deck d2) {
                return d1.getName().compareToIgnoreCase(d2.getName());
            }
        });

        deckTable.setItems(FXCollections.observableArrayList(decks));
    }

    /**
     * Shows an alert message
     * @param title alert title
     * @param message alert message
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}