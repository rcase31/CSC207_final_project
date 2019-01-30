import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Uses JavaFX to open a message box that lets the user enter text.
 *
 * @author Thomas Campbell
 */
public class MessageAlertBox {

    /**
     * Displays the message box window and stops you from interacting with anything else until its closed.
     *
     * @param title the title of the window.
     * @param message The message to display to the user.
     * @return The String that the user inputs into the TextField.
     */
    public static String display(String title, String message){
        Stage window = new Stage();

        // set modality, this makes it so the screen behind this one can't be interacted with.
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);

        // Set label to display message.
        Label label = new Label();
        label.setText(message);

        TextField messageEntered = new TextField();

        Button closeButton = new Button("Enter");
        closeButton.setOnAction(e -> window.close());

        // Set up the layout of the screen.
        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, messageEntered, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10,10,10,10));

        Scene scene = new Scene(layout, 250,150);
        window.setScene(scene);
        window.showAndWait();

        // Return the characters in message entered.
        return messageEntered.getCharacters().toString();
    }
}
