package stackoverflown;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controller for the main GUI window of StackOverflown.
 *
 * <p>This class handles the interaction between the user interface components
 * and the core StackOverflown business logic. It manages user input processing,
 * display of chat messages, and application lifecycle events.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private StackOverflown stackOverflown;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/ipUserAvatar.png"));
    private Image stackOverflownImage = new Image(this.getClass().getResourceAsStream("/images/ipBotAvatar.jpg"));

    /**
     * Initializes the MainWindow controller.
     *
     * <p>Sets up the scroll pane to automatically scroll to the bottom when
     * new dialog boxes are added, providing a natural chat-like experience.</p>
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Injects the StackOverflown instance and displays welcome message.
     *
     * @param s the StackOverflown instance to use for processing commands
     */
    public void setStackOverflown(StackOverflown s) {
        stackOverflown = s;

        // Display welcome message
        String welcomeMessage = stackOverflown.getWelcomeMessage();
        dialogContainer.getChildren().add(
                DialogBox.getStackOverflownDialog(welcomeMessage, stackOverflownImage)
        );
    }

    /**
     * Handles user input when Send button is clicked or Enter is pressed.
     *
     * <p>Processes the user command through StackOverflown, displays both the
     * user input and bot response as dialog boxes, and handles application
     * exit if the bye command is used.</p>
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        String response = stackOverflown.getResponse(input);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getStackOverflownDialog(response, stackOverflownImage)
        );

        userInput.clear();

        // Handle application exit
        if (input.trim().toLowerCase().equals("bye")) {
            Platform.runLater(() -> {
                try {
                    Thread.sleep(1500); // Brief delay to show goodbye message
                    Platform.exit();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
}