package cs151.application;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ListDeckController
{
    @FXML
    private TableView<Deck> deckTable;

    @FXML
    private TableColumn<Deck,String> nameColumn;

    @FXML
    private TableColumn<Deck, String>desCol;

    @FXML
    public void handleBack(ActionEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("/cs151/application/homePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 700, 400));
            stage.show();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize()
    {
        //Set up Col
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        desCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        //Load Decks
        List<Deck> decks = DeckStore.getAllDecks();

        //Sort a-->z
        Collections.sort(decks,new Comparator<Deck>()
        {
            @Override
            public int compare(Deck d1, Deck d2)
            {
                return d1.getName().compareToIgnoreCase(d2.getName());
            }
        }
        );

        deckTable.getItems().addAll(decks);

    }



}
