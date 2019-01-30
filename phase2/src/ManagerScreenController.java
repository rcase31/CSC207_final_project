import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;


/**
 * The ManagerScreenController class. The class creates the main GUI design that the manager
 * will see on the screen. It consists mostly of 3 tabs, each with a distinct table and function:
 *      1) Check specific ingredient names and their quantity in the inventory, and output
 *      to a table.
 *      2) Display a table of all ingredients, their names, and their respective quantities
 *      in the inventory.
 *      3) Check all active orders currently in the system. Any order that is taken,
 *      received, or prepared is considered active.
 *
 * @author Jedid Ahn
 * @version 0.2
 */


public class ManagerScreenController {

    private Manager manager;

    @FXML
    private Label managerName;

    // Table 1
    @FXML
    private TableView<Ingredient> checkInventoryTable;
    @FXML
    private TableColumn<Ingredient, String> specificIngredientCol;
    @FXML
    private TableColumn<Ingredient, String> specificQuantityCol;
    @FXML
    private TextField inputField;
    @FXML
    private Label validationMsg;

    private ObservableList<Ingredient> data1;


    // Table 2
    @FXML
    private TableView<Ingredient> printInventoryTable;
    @FXML
    private TableColumn<Ingredient, String> ingredientNameCol;
    @FXML
    private TableColumn<Ingredient, String> quantityCol;


    // Table 3
    @FXML
    private TableView<Order> activeOrdersTable;
    @FXML
    private TableColumn<Order, String> tableNumCol;
    @FXML
    private TableColumn<Order, String> orderDetailsCol;

    private ObservableList<Order> data3;


    /**
     * Set the manager who will control the manager screen, and add their name to the
     * screen. Then, initialize all 3 separate TableViews.
     *
     * @param manager A manager object representing the manager of a restaurant.
     *
     */
    public void setManager(Manager manager) {
        this.manager = manager;
        managerName.setText(this.manager.getName());
        this.initializeTableView1();
        this.initializeTableView2();
        this.initializeTableView3();
    }


    /**
     * Log out of the manager screen when the "Log Out" button is clicked
     * by the user. The user will be sent back to the login page.
     *
     * @param actionEvent An ActionEvent object corresponding to the
     *                    closing of the existing window and opening
     *                    of a new window.
     *
     */
    @FXML
    private void logOut(ActionEvent actionEvent){
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }


    /**
     * Open the receive ingredient screen controlled by the ReceiveIngredientScreenController
     * class when the "Receive Ingredients" button is clicked by the user (in the top left of
     * the screen). The user will be prompted to a new window while the existing window will
     * still be active but locked behind.
     *
     * @exception IOException To load the FXMLLoader to open the receive ingredient screen.
     *
     */
    @FXML
    private void openReceiveIngredientScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ReceiveIngredientScreen.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 275, 200));
        newStage.initModality(Modality.APPLICATION_MODAL); // To block access to other open windows.
        newStage.show();
    }


    /**
     * Initialize the 1st table view to display the name and quantity (with units)
     * of ingredients of interest in the inventory as part of the system.
     *
     */
    private void initializeTableView1(){
        this.data1 = FXCollections.observableArrayList();

        this.specificIngredientCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.specificQuantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityWithUnit"));

        this.checkInventoryTable.setItems(data1);
    }


    /**
     * Check if the ingredient name inputted exists. If not, a validation message
     * in red will inform the user of this.
     *
     * If the ingredient name does exist, add the ingredient object as part of the
     * data to get its name and quantity (with units) for output in the TableView.
     *
     */
    @FXML
    private void validateIngredientName() {
        String ingredientName = this.inputField.getText();
        if (ingredientName.isEmpty()){
            this.validationMsg.setTextFill(Color.RED);
            this.validationMsg.setText("Please input an ingredient name.");
        }
        else if (this.manager.getSpecificIngredient(ingredientName) == null){
            this.validationMsg.setTextFill(Color.RED);
            this.validationMsg.setText("This ingredient name does not exist.");
        }
        else{
            this.data1.add(this.manager.getSpecificIngredient(ingredientName));
            this.checkInventoryTable.setItems(this.data1);
            this.validationMsg.setText("");
        }
        this.inputField.clear();
    }


    /**
     * Clear the entire TableView when the "Clear" button is clicked by the user.
     * Display a validation message in green confirming that the clear was successful.
     *
     */
    @FXML
    private void clearTableView() {
        if (this.checkInventoryTable.getItems().size() > 0) {
            this.checkInventoryTable.getItems().clear();
            this.validationMsg.setTextFill(Color.GREEN);
            this.validationMsg.setText("Table has been cleared.");
        }
    }


    /**
     * Refresh the Check Inventory TableView every time its tab is clicked by the user.
     *
     */
    @FXML
    private void refreshCheckInventoryTable(){
        this.checkInventoryTable.refresh();
    }


    /**
     * Initialize the 2nd table view to display the name and quantity (with units)
     * of every ingredient in the inventory as part of the system.
     *
     */
    private void initializeTableView2(){
        ObservableList<Ingredient> data2 = FXCollections.observableArrayList();
        data2.addAll(this.manager.getIngredients());

        this.ingredientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantityWithUnit"));

        this.printInventoryTable.setItems(data2);
    }


    /**
     * Refresh the Print Inventory TableView every time its tab is clicked by the user.
     *
     */
    @FXML
    private void refreshPrintInventoryTable(){
        this.printInventoryTable.refresh();
    }


    /**
     * Initialize the 3rd table view to show the location (Table # and Seat #)
     * and details (name of MenuItems) of every active order in the system.
     *
     */
    private void initializeTableView3(){
        this.data3 = FXCollections.observableArrayList();

        // Any order that is taken, received, or prepared is considered active.
        data3.addAll(OrderTracker.getInstance().getAllWithStatus(1));
        data3.addAll(OrderTracker.getInstance().getAllWithStatus(2));
        data3.addAll(OrderTracker.getInstance().getAllWithStatus(3));

        this.tableNumCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        this.orderDetailsCol.setCellValueFactory(new PropertyValueFactory<>("details"));

        this.activeOrdersTable.setItems(data3);
    }


    /**
     * Update the Active Orders TableView every time its tab is clicked by the user.
     *
     */
    @FXML
    private void refreshActiveOrdersTable(){
        // Clear the table first.
        this.activeOrdersTable.getItems().clear();

        // Generate the newest data and add to the table.
        this.data3.addAll(OrderTracker.getInstance().getAllWithStatus(1));
        this.data3.addAll(OrderTracker.getInstance().getAllWithStatus(2));
        this.data3.addAll(OrderTracker.getInstance().getAllWithStatus(3));
        this.activeOrdersTable.setItems(data3);
    }
}
