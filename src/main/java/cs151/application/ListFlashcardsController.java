package cs151.application;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.time.format.DateTimeFormatter;

public class ListFlashcardsController {

    @FXML private TableView<Flashcard> table;
    @FXML private TableColumn<Flashcard, String> deckCol;
    @FXML private TableColumn<Flashcard, String> frontCol;
    @FXML private TableColumn<Flashcard, String> backCol;
    @FXML private TableColumn<Flashcard, String> dateCol;

    @FXML
    public void initialize() {
        deckCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDeckName()));
        frontCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFrontText()));
        backCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getBackText()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        dateCol.setCellValueFactory(cellData ->
        {
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getCreationDate().format(formatter)
            );
        });

        table.getItems().addAll(FlashcardStore.getAllFlashcards());
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}