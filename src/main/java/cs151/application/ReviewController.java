package cs151.application;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.util.List;

public class ReviewController {

    @FXML private TextField searchField;
    @FXML private TableView<Deck> deckTable;
    @FXML private TableColumn<Deck, String> nameColumn;
    @FXML private TableColumn<Deck, String> desCol;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        desCol.setCellValueFactory(cellData -> {
            String desc = cellData.getValue().getDescription();
            if (desc == null || desc.isEmpty()) {
                return new javafx.beans.property.SimpleStringProperty("");
            }
            return new javafx.beans.property.SimpleStringProperty(desc.split("\n")[0]);
        });

        loadDecks();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> searchDecks());

        deckTable.setRowFactory(tv -> {
            TableRow<Deck> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    openDeckReviewPage(row.getItem());
                }
            });
            return row;
        });
    }

    private void loadDecks() {
        deckTable.setItems(FXCollections.observableArrayList(DeckStore.getAllDecks()));
    }

    private void searchDecks() {
        String query = searchField.getText().trim().toLowerCase();

        List<Deck> allDecks = DeckStore.getAllDecks();

        if (query.isEmpty()) {
            deckTable.setItems(FXCollections.observableArrayList(allDecks));
            return;
        }

        List<Deck> matchedDecks = allDecks.stream()
                .filter(deck ->
                        deck.getName().toLowerCase().contains(query)
                                || deck.getDescription().toLowerCase().contains(query))
                .toList();

        deckTable.setItems(FXCollections.observableArrayList(matchedDecks));
    }

    @FXML
    public void handleOpenReview() {
        Deck selectedDeck = deckTable.getSelectionModel().getSelectedItem();

        if (selectedDeck == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a deck first.");
            alert.showAndWait();
            return;
        }

        openDeckReviewPage(selectedDeck);
    }

    private void openDeckReviewPage(Deck deck) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/cs151/application/reviewDeck.fxml"));
            Parent root = loader.load();

            ReviewDeckController controller = loader.getController();
            controller.setDeck(deck);

            Stage stage = (Stage) deckTable.getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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