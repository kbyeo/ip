package stackoverflown;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 *
 * <p>This class serves as the entry point for the JavaFX application to avoid
 * module path issues when running JavaFX applications. The main method in this
 * class launches the actual JavaFX Application class.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 */
public class Launcher {
    /**
     * Main entry point that launches the JavaFX application.
     *
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}