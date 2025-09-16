package stackoverflown;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Enhanced controller for the main GUI window with modern UX features.
 *
 * <p>Features polished user experience improvements including:
 * <ul>
 * <li>Smooth message animations for better visual feedback</li>
 * <li>Enhanced keyboard interaction and focus management</li>
 * <li>Optimized scrolling behavior for better chat experience</li>
 * <li>Visual feedback for user interactions</li>
 * <li>Responsive design adaptations</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 2.0
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
    private Image userImage;
    private Image stackOverflownImage;

    /**
     * Constructor with enhanced image loading and fallback handling.
     */
    public MainWindow() {
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
     * Initialize with enhanced UX features and visual improvements.
     */
    @FXML
    public void initialize() {
        setupScrollPane();
        setupInputField();
        setupSendButton();
    }

    /**
     * Configure scroll pane for optimal chat experience.
     */
    private void setupScrollPane() {
        dialogContainer.prefWidthProperty().bind(scrollPane.widthProperty().subtract(10));

        // Smooth auto-scrolling with proper timing
        dialogContainer.heightProperty().addListener((observable, oldValue,
                                                      newValue) -> {
            Platform.runLater(() -> {
                Platform.runLater(() -> scrollPane.setVvalue(1.0));
            });
        });

        // Optimize scroll pane appearance
        scrollPane.setStyle("-fx-background: #FFFFFF; -fx-background-color: #FFFFFF;");
        scrollPane.setFitToWidth(true);
    }

    /**
     * Configure input field with enhanced UX features.
     */
    private void setupInputField() {
        // Focus management for better keyboard interaction
        userInput.focusedProperty().addListener((obs, wasFocused,
                                                 isNowFocused) -> {
            if (isNowFocused) {
                userInput.setStyle(userInput.getStyle() +
                        "-fx-border-color: #0084FF; -fx-border-width: 2px;");
            } else {
                userInput.setStyle(userInput.getStyle().replace(
                        "-fx-border-color: #0084FF; -fx-border-width: 2px;", ""));
            }
        });

        // Auto-focus on application start
        Platform.runLater(() -> userInput.requestFocus());
    }

    /**
     * Configure send button with visual feedback.
     */
    private void setupSendButton() {
        // Hover effects for better interaction feedback
        sendButton.setOnMouseEntered(e -> {
            sendButton.setStyle(sendButton.getStyle() + "-fx-background-color: #0066CC;");
        });

        sendButton.setOnMouseExited(e -> {
            sendButton.setStyle(sendButton.getStyle().replace(
                    "-fx-background-color: #0066CC;", "-fx-background-color: #0084FF;"));
        });

        // Press effect
        sendButton.setOnMousePressed(e -> {
            sendButton.setScaleX(0.95);
            sendButton.setScaleY(0.95);
        });

        sendButton.setOnMouseReleased(e -> {
            sendButton.setScaleX(1.0);
            sendButton.setScaleY(1.0);
        });
    }

    /**
     * Inject StackOverflown instance with enhanced welcome experience.
     */
    public void setStackOverflown(StackOverflown s) {
        stackOverflown = s;

        // Add welcome message with smooth animation
        String welcomeMessage = stackOverflown.getWelcomeMessage();
        addBotMessageWithAnimation(welcomeMessage);
    }

    /**
     * Add bot message with smooth fade-in animation.
     */
    private void addBotMessageWithAnimation(String message) {
        DialogBox botDialog = DialogBox.getStackOverflownDialog(message, stackOverflownImage);
        addMessageWithAnimation(botDialog);
    }

    /**
     * Add user message with smooth fade-in animation.
     */
    private void addUserMessageWithAnimation(String message) {
        DialogBox userDialog = DialogBox.getUserDialog(message, userImage);
        addMessageWithAnimation(userDialog);
    }

    /**
     * Add any dialog with smooth animation for better UX.
     */
    private void addMessageWithAnimation(DialogBox dialog) {
        // Start invisible for fade-in effect
        dialog.setOpacity(0.0);
        dialogContainer.getChildren().add(dialog);

        // Create smooth fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dialog);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    /**
     * Legacy add methods for backward compatibility.
     */
    private void addBotMessage(String message) {
        addBotMessageWithAnimation(message);
    }

    private void addUserMessage(String message) {
        addUserMessageWithAnimation(message);
    }

    /**
     * Enhanced input handling with improved UX and visual feedback.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText().trim();
        if (input.isEmpty()) {
            // Visual feedback for empty input
            userInput.setStyle(userInput.getStyle() +
                    "-fx-border-color: #EA4335; -fx-border-width: 2px;");

            // Reset border color after brief delay
            Platform.runLater(() -> {
                try {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        userInput.setStyle(userInput.getStyle().replace(
                                "-fx-border-color: #EA4335; -fx-border-width: 2px;", ""));
                    });
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            return;
        }

        // Clear input immediately and maintain focus
        userInput.clear();
        userInput.requestFocus();

        // Add user message with animation
        addUserMessage(input);

        // Get and add bot response with slight delay for natural feel
        Platform.runLater(() -> {
            String response = stackOverflown.getResponse(input);

            // Small delay makes conversation feel more natural
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Platform.runLater(() -> addBotMessage(response));
        });

        // Handle application exit with enhanced transition
        if (input.toLowerCase().equals("bye")) {
            Platform.runLater(() -> {
                try {
                    Thread.sleep(2000); // Longer delay to show goodbye message
                    Platform.exit();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
    }
}