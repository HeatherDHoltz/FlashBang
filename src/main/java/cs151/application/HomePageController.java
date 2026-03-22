package cs151.application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

/**
 * Controller for the home page
 * Handles navigation from the homepage to other parts of the app to other parts of application
 */
public class HomePageController {
    /**
     * Navigates to define dek page when user presses create deck button
     * @param event create deck button clicked
     */
    public void handleCreateDeck(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("defineDeck.fxml"));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to list deck page, to display all decks
     * @param event view all decks button clicked
     */
    public void handleViewDecks(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/listDecks.fxml"));
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
