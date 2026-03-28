package cs151.application;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomePageController
{
    public void handleCreateDeck(ActionEvent event)
    {
        switchScene(event, "/cs151/application/defineDeck.fxml");
    }

    public void handleViewDecks(ActionEvent event)
    {
        switchScene(event, "/cs151/application/listDecks.fxml");
    }

    public void handleCreateFlashcard(ActionEvent event)
    {
        switchScene(event, "/cs151/application/defineFlashcard.fxml");
    }

    public void handleViewFlashcards(ActionEvent event)
    {
        switchScene(event, "/cs151/application/ListFlashcards.fxml");
    }

    private void switchScene(ActionEvent event, String path)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource(path));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}