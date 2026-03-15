package cs151.application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

public class HomePageController {
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
}
