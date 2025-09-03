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
 * display of chat messages, and application lifecycle events with proper
 * message display and circular avatars.</p>
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

    // Handle missing images gracefully
    private Image userImage;
    private Image stackOverflownImage;

    /**
     * Constructor that initializes images with fallback handling.
     */
    public MainWindow() {
        // Try to load images, use null if not found (will be handled in DialogBox)
        try {
            userImage = new Image(this.getClass().getResourceAsStream("/images/ipUserAvatar.png"));
        } catch (Exception e) {
            userImage = null;
        }

        try {
            stackOverflownImage = new Image(this.getClass().getResourceAsStream("/images/ipBotAvatar.jpg"));
        } catch (Exception e) {
            stackOverflownImage = null;
        }
    }

    /**
     * Initializes the MainWindow controller.
     *
     * <p>Sets up the scroll pane for proper auto-scrolling behavior that ensures
     * new messages are always visible and long messages are fully displayed.</p>
     */
    @FXML
    public void initialize() {
        // Set up dialog container for proper content sizing
        dialogContainer.prefWidthProperty().bind(scrollPane.widthProperty().subtract(2));

        // Add listener to auto-scroll when content height changes
        dialogContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> scrollPane.setVvalue(1.0));
        });
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
        addBotMessage(welcomeMessage);
    }

    /**
     * Adds a bot message to the dialog container.
     *
     * @param message the message text to display
     */
    private void addBotMessage(String message) {
        DialogBox botDialog = DialogBox.getStackOverflownDialog(message, stackOverflownImage);
        dialogContainer.getChildren().add(botDialog);
    }

    /**
     * Adds a user message to the dialog container.
     *
     * @param message the message text to display
     */
    private void addUserMessage(String message) {
        DialogBox userDialog = DialogBox.getUserDialog(message, userImage);
        dialogContainer.getChildren().add(userDialog);
    }

    /**
     * Handles user input when Send button is clicked or Enter is pressed.
     *
     * <p>Processes the user command through StackOverflown, displays both the
     * user input and bot response as dialog boxes with proper scrolling,
     * and handles application exit if the bye command is used.</p>
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.trim().isEmpty()) {
            return;
        }

        // Clear input field immediately for better UX
        userInput.clear();

        // Add user message
        addUserMessage(input);

        // Get and add bot response
        String response = stackOverflown.getResponse(input);
        addBotMessage(response);

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