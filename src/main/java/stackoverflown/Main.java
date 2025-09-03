package stackoverflown;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main JavaFX Application class for StackOverflown GUI.
 *
 * <p>This class initializes the JavaFX application and sets up the primary
 * stage with the main GUI layout loaded from FXML. It handles the application
 * lifecycle and window configuration.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 */
public class Main extends Application {

    /** The core StackOverflown instance managing business logic */
    private StackOverflown stackOverflown = new StackOverflown();

    /**
     * Starts the JavaFX application by setting up the primary stage.
     *
     * <p>Loads the FXML layout, configures the scene, and displays the main
     * application window. Also injects the StackOverflown instance into the
     * controller for business logic integration.</p>
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("StackOverflown - Personal Task Manager");
            stage.setResizable(false);
            stage.setMinHeight(600.0);
            stage.setMinWidth(400.0);

            fxmlLoader.<MainWindow>getController().setStackOverflown(stackOverflown);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}