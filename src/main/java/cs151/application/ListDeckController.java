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

/**
 * Controller for list deck pages
 * Displays all stored decks in a table view, sorted alphabetically by the deck anme
 * allows navigation back to home page
 */
public class ListDeckController
{
    //Tab;le that displays all decks
    @FXML
    private TableView<Deck> deckTable;
//Column for deck names
    @FXML
    private TableColumn<Deck,String> nameColumn;
//Column for deck descitpion
    @FXML
    private TableColumn<Deck, String>desCol;

    /**
     * Naviagtes back to home page
     * @param event home bnutton clicked
     */
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

    /**
     * Initialized table at page load
     * Binds tbale co.lumns to deck properties, Loads deck data
     * Sorts deck alphabetically, dispays the decks in table
     */
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
