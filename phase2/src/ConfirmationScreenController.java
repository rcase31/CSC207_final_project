import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;


/**
 * The ConfirmationScreenController class. The class controls the confirmation screen
 * and the success screen after an ingredient has been received and its quantity has
 * been added. The system and the inventory will be subsequently updated.
 *
 * The class controls for the confirmButton when pressed by the user on the confirmation
 * screen, as well as the acknowledgeButton on the success screen.
 *
 * @author Jedid Ahn
 * @version 0.5
 */


public class ConfirmationScreenController {

    // Confirm Screen.
    @FXML
    private Button confirmButton;

    // Success Screen.
    @FXML
    private Button acknowledgeButton;

    Inventory inventory = Inventory.getInventory();


    /**
     * Add the quantity received of an ingredient into the inventory, and then go back to
     * the main controller screen by closing both the confirmation screen and the
     * receive ingredient screen when addition is successful.
     *
     * @param ingredientName  A String representing the name of the ingredient received.
     * @param quantityToAdd   A double value representing the quantity received of the ingredient,
     *                        and to add and record in the system.
     * @param existingStage   A Stage object representing the ReceiveIngredientScreen which is
     *                        currently open in the background.
     *
     */
    public void confirm(String ingredientName, double quantityToAdd, Stage existingStage){
        confirmButton.setOnAction(event -> {
            inventory.add(ingredientName, quantityToAdd);

            Stage currentStage = (Stage) confirmButton.getScene().getWindow();

            // Close both stages.
            currentStage.close();
            existingStage.close();

            // Open success window.
            try {
                this.success();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    /**
     * Open the success screen, which is controlled through the ConfirmationScreenController class.
     *
     * @exception IOException To load the FXMLLoader to open the success screen.
     *
     */
    private void success() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SuccessScreen.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 150, 125));
        newStage.initModality(Modality.APPLICATION_MODAL); // To lock access to other open windows.
        newStage.show(); // Open new window.
    }


    /**
     * Close the success screen when the "Ok" button is clicked by the user.
     * The user will then be able to access the main controller screen again.
     *
     */
    @FXML
    private void acknowledged(){
        Stage currentStage = (Stage) acknowledgeButton.getScene().getWindow();
        currentStage.close();
    }
}