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
 * Represents a dialog box consisting of an ImageView to represent the speaker's face
 * and a label containing text from the speaker.
 *
 * <p>This custom control creates chat-like dialog boxes for the GUI interface,
 * supporting both user and bot messages with different alignments and styling.
 * Features circular avatars and proper text wrapping to prevent truncation.</p>
 *
 * @author Yeo Kai Bin
 * @version 1.0
 * @since 2025
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    /**
     * Constructs a DialogBox with specified text and image.
     *
     * @param text the text content for the dialog
     * @param img the image to display alongside the text (can be null)
     */
    private DialogBox(String text, Image img) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Configure text display with proper wrapping
        dialog.setText(text);
        dialog.setWrapText(true);
        dialog.setMaxWidth(300.0);

        // Configure circular avatar
        setupCircularAvatar(img);
    }

    /**
     * Sets up the circular avatar with proper clipping and sizing.
     *
     * @param img the image to display (can be null)
     */
    private void setupCircularAvatar(Image img) {
        if (img != null) {
            displayPicture.setImage(img);
        }

        // Set fixed size for consistency
        displayPicture.setFitHeight(50.0);
        displayPicture.setFitWidth(50.0);
        displayPicture.setPreserveRatio(false); // Allow stretching to fit circle

        // Create circular clipping
        Circle clip = new Circle(25.0); // radius = fitWidth/2
        clip.setCenterX(25.0);
        clip.setCenterY(25.0);
        displayPicture.setClip(clip);

        // Handle missing images gracefully
        if (img == null) {
            displayPicture.setVisible(false);
            displayPicture.setManaged(false);
        }
    }

    /**
     * Flips the dialog box such that the ImageView is on the left and text on the right.
     */
    private void flip() {
        ObservableList<Node> tmp = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(tmp);
        getChildren().setAll(tmp);
        setAlignment(Pos.TOP_LEFT);

        // Adjust text alignment for flipped dialog
        dialog.setMaxWidth(300.0);
    }

    /**
     * Creates a dialog box for user messages.
     *
     * @param text the user's input text
     * @param img the user's profile image
     * @return DialogBox configured for user display (right-aligned)
     */
    public static DialogBox getUserDialog(String text, Image img) {
        DialogBox dialog = new DialogBox(text, img);
        dialog.setAlignment(Pos.TOP_RIGHT);
        return dialog;
    }

    /**
     * Creates a dialog box for StackOverflown bot responses.
     *
     * @param text the bot's response text
     * @param img the bot's profile image
     * @return DialogBox configured for bot display (left-aligned, flipped)
     */
    public static DialogBox getStackOverflownDialog(String text, Image img) {
        DialogBox dialog = new DialogBox(text, img);
        dialog.flip();
        return dialog;
    }
}