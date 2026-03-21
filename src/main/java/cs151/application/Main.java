package cs151.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main entry of application
 * Launches the JavaFX application, and loads the homepage at start
 */
public class Main extends Application {
    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws IOException if FXML file cant be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homePage.fxml"));
        //Create main scene with window size 700x400
        Scene scene = new Scene(fxmlLoader.load(), 700, 400); // Increased window size
        //Sets window title
        stage.setTitle("FlashBang!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}