package stackoverflown;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

/**
 * Represents a modern chat dialog box with polished styling and optimal space usage.
 *
 * <p>This custom control creates visually appealing chat bubbles with:
 * <ul>
 * <li>Rounded chat bubble appearance with proper padding</li>
 * <li>Distinct styling for user vs bot messages</li>
 * <li>Compact avatars that don't waste screen space</li>
 * <li>Optimized text wrapping for better readability</li>
 * <li>Modern color scheme and typography</li>
 * </ul>
 * </p>
 *
 * @author Yeo Kai Bin
 * @version 2.0
 * @since 2025
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    // Modern color palette for chat bubbles
    private static final String USER_BUBBLE_STYLE =
            "-fx-background-color: #0084FF; " +
                    "-fx-text-fill: white; " +
                    "-fx-background-radius: 18px; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-family: 'Segoe UI', 'Arial', sans-serif; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 0, 1);";

    private static final String BOT_BUBBLE_STYLE =
            "-fx-background-color: #F1F3F4; " +
                    "-fx-text-fill: #202124; " +
                    "-fx-background-radius: 18px; " +
                    "-fx-font-size: 14px; " +
                    "-fx-font-family: 'Segoe UI', 'Arial', sans-serif; " +
                    "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);";

    /**
     * Constructs a DialogBox with enhanced styling and optimal spacing.
     *
     * @param text the text content for the dialog
     * @param img the image to display alongside the text (can be null)
     * @param isUser true for user messages, false for bot messages
     */
    private DialogBox(String text, Image img, boolean isUser) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configure text display with chat bubble styling
        dialog.setText(text);
        dialog.setWrapText(true);
        dialog.setMaxWidth(280.0); // Optimized for readability

        // Apply appropriate styling based on sender
        if (isUser) {
            dialog.setStyle(USER_BUBBLE_STYLE);
        } else {
            dialog.setStyle(BOT_BUBBLE_STYLE);
        }

        // Configure compact circular avatar
        setupModernAvatar(img, isUser);
    }

    /**
     * Sets up a modern, compact circular avatar with optimized sizing.
     *
     * @param img the image to display (can be null)
     * @param isUser true for user avatar, false for bot avatar
     */
    private void setupModernAvatar(Image img, boolean isUser) {
        // Compact size to save screen real estate
        displayPicture.setFitHeight(35.0);
        displayPicture.setFitWidth(35.0);
        displayPicture.setPreserveRatio(false);

        if (img != null && !img.isError()) {
            displayPicture.setImage(img);
        } else {
            // Sophisticated fallback with user-specific colors
            String fallbackColor = isUser ? "#0084FF" : "#34A853";
            displayPicture.setStyle(
                    "-fx-background-color: " + fallbackColor + "; " +
                            "-fx-background-radius: 17.5px; " +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 2, 0, 0, 1);"
            );
        }

        Circle clip = new Circle(17.5); // radius = fitWidth/2
        clip.setCenterX(17.5);          // center X = radius
        clip.setCenterY(17.5);          // center Y = radius
        displayPicture.setClip(clip);

        // Handle missing images gracefully
        if (img == null) {
            displayPicture.setVisible(false);
            displayPicture.setManaged(false);
        }
    }

    /**
     * Flips dialog layout for bot messages (avatar on left, text on right).
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);
    }

    /**
     * Creates a modern user dialog with blue chat bubble styling.
     *
     * @param text the user's input text
     * @param img the user's profile image
     * @return DialogBox configured for user display (right-aligned)
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox dialog = new DialogBox(text, img, true);
        dialog.setAlignment(Pos.TOP_RIGHT);
        return dialog;
    }

    /**
     * Creates a modern bot dialog with light gray chat bubble styling.
     *
     * @param text the bot's response text
     * @param img the bot's profile image
     * @return DialogBox configured for bot display (left-aligned, flipped)
     */
    public static DialogBox getStackOverflownDialog(String text, Image img) {
        DialogBox dialog = new DialogBox(text, img, false);
        dialog.flip();
        return dialog;
    }
}