import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;


/**
 * The ReceiveIngredientScreenController class. The class controls the receive ingredient
 * screen when the user wants to input the quantity received of an ingredient and add it
 * into the system.
 *
 * The class controls for the dropdown menu of all ingredient names, the text field in
 * which the quantity received is to be added, and a message to the user through a label
 * if any input is invalid or missing.
 *
 * @author Jedid Ahn
 * @version 0.4
 */


public class ReceiveIngredientScreenController {

    @FXML
    private ComboBox<String> nameDropdown;
    @FXML
    private TextField inputQuantityField;
    @FXML
    private Label unit;
    @FXML
    private Label confirmationMsg;

    private String ingredientName;

    Inventory inventory = Inventory.getInventory();


    /**
     * Initialize the dropdown menu of all ingredient names, and set an action
     * event when an ingredient name from the dropdown is chosen by the user.
     */
    @FXML
    private void initialize() {
        // Make dropdown menu of ingredient names.
        ArrayList<Ingredient> allIngredients = inventory.getItems();
        for (Ingredient ingredient : allIngredients) {
            this.nameDropdown.getItems().add(ingredient.getName());
        }
        // Sort ingredient names in alphabetical order.
        Collections.sort(this.nameDropdown.getItems());

        this.nameDropdown.setOnAction((ActionEvent e) -> {
            this.ingredientName = this.nameDropdown.getSelectionModel().getSelectedItem();
            this.addUnitToLabel(this.ingredientName);
        });
    }


    /**
     * Get the appropriate unit associated with an ingredient with <name>
     * so it can be added as a label next to the quantity text field.
     *
     * @param name A String representing the name of the ingredient.
     */
    private void addUnitToLabel(String name){
        Ingredient ingredient = inventory.findIngredient(name);
        this.unit.setText("  " + ingredient.getUnit());
    }


    /**
     * Validate any input made by the user to ensure all values entered are correct.
     *
     * If not correct, output a red message through a label of the specific error
     * in input.
     *
     * If all input is deemed correct, double check to ensure that the quantity
     * added if ingredients/items are packs, cans, or bottles are whole numbers.
     *
     * @exception IOException Used to call validateQuantity()
     * @exception NumberFormatException To check if the quantity value inputted
     *                                  is a valid number.
     *
     */
    @FXML
    private void validateInput() throws IOException{
        if (this.nameDropdown.getSelectionModel().isEmpty()){
            this.confirmationMsg.setTextFill(Color.RED);
            this.confirmationMsg.setText("Dropdown field empty. Please input a value.");
        }
        else if (this.inputQuantityField.getText().isEmpty()){
            this.confirmationMsg.setTextFill(Color.RED);
            this.confirmationMsg.setText("Quantity field empty.  Please input a value.");
        }
        else{
            try {
                double quantityToAdd = Double.parseDouble(this.inputQuantityField.getText().trim());
                if (quantityToAdd <= 0){
                    this.confirmationMsg.setTextFill(Color.RED);
                    this.confirmationMsg.setText("Quantity cannot be negative or 0.");
                }
                else{
                    this.validateQuantity(quantityToAdd);
                }
            }
            catch (NumberFormatException e) {
                this.confirmationMsg.setTextFill(Color.RED);
                this.confirmationMsg.setText("Quantity is not a valid number.");
            }
        }
    }


    /**
     * Validate the quantity inputted by the user if the ingredients/items have units
     * in packs, cans, or bottles.
     *
     * If units are in packs, cans, or bottles, the quantity has to be a whole number.
     * If it is, continue by opening the confirmation screen.
     * If not, output a red message informing the user of the error.
     *
     * If unit is not in packs, cans, or bottles, the quantity can be a double, and
     * thus the confirmation screen can be opened without validation.
     *
     * @param quantityToAdd A double value representing the quantity to add in the
     *                      inventory of the received ingredient.
     * @exception IOException Used to call openConfirmationScreen()
     *
     */
    private void validateQuantity(double quantityToAdd) throws IOException{
        Ingredient ingredient = inventory.findIngredient(this.ingredientName);
        String ingredientUnit = ingredient.getUnit();

        if (ingredientUnit.equals("packs") || ingredientUnit.equals("cans") ||
                ingredientUnit.equals("bottles")) {
            if (quantityToAdd == ((int)quantityToAdd)){
                this.openConfirmationScreen(quantityToAdd);
            }
            else{
                this.confirmationMsg.setTextFill(Color.RED);
                this.confirmationMsg.setText("Quantity is not a whole number.");
            }
        }
        // The ingredients/items are in kg or L, which can be a double value.
        else {
            this.openConfirmationScreen(quantityToAdd);
        }
    }


    /**
     * Open the confirmation screen, which is controlled through a separate and loaded
     * ConfirmationScreenController class.
     *
     * @exception IOException To load the FXMLLoader to open the confirmation screen.
     *
     */
    private void openConfirmationScreen(double quantityToAdd) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationScreen.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 300, 150));
        newStage.initModality(Modality.APPLICATION_MODAL); // To lock access to other open windows.
        newStage.show(); // Open new window.

        Stage existingStage = (Stage) nameDropdown.getScene().getWindow(); // Get existing stage.

        ConfirmationScreenController controller = loader.getController();
        controller.confirm(this.ingredientName, quantityToAdd, existingStage);
        this.confirmationMsg.setText("");
    }
}