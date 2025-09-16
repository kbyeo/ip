package stackoverflown;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Enhanced JavaFX Application with professional window configuration.
 *
 * <p>Features modern application setup including:
 * <ul>
 * <li>Professional window sizing and constraints</li>
 * <li>Modern application styling and theming</li>
 * <li>Enhanced error handling and user feedback</li>
 * <li>Optimized performance settings</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 2.0
 * @since 2025
 */
public class Main extends Application {
    private StackOverflown stackOverflown = new StackOverflown();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            // Professional window configuration
            stage.setScene(scene);
            stage.setTitle("StackOverflown - Personal Task Manager");

            // Optimized window sizing for chat interface
            stage.setMinHeight(500.0);
            stage.setMinWidth(380.0);
            stage.setMaxHeight(800.0);
            stage.setMaxWidth(450.0);

            // Default to optimal size for task management
            stage.setHeight(600.0);
            stage.setWidth(400.0);

            // Allow limited resizing for user preference
            stage.setResizable(true);

            // Center window on screen
            stage.centerOnScreen();

            // Inject business logic
            fxmlLoader.<MainWindow>getController().setStackOverflown(stackOverflown);

            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load GUI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}