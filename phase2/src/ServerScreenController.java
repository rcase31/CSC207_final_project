import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

import java.util.ArrayList;

/**
 * The root ServerScreenController this controller stores all the attributes that need to be passed to ServerScreen.fxml
 * and initializes the subControllers deliverController, returnController, and orderController. It also controlls the
 * tableServing and ordersSubmittedListView
 *
 * @author Thomas Campbell
 */
public class ServerScreenController implements Observer {

    private OrderTracker orderTracker = OrderTracker.getInstance();
    private ArrayList<Order> submittedOrders;
    private Server server;

    private ServerDeliverController deliverController;
    private  ServerReturnController returnController;
    private ServerOrderController orderController;

    @FXML
    Label serverNameLabel;
    @FXML
    Label currentlySelectedTable;
    @FXML
    Label warningLabel;
    @FXML
    private ListView<String> tablesServingListView;
    @FXML
    ComboBox<String> tablesComboBox;
    @FXML
    ComboBox<String> additionsComboBox;
    @FXML
    ComboBox<String> subtractionsComboBox;
    @FXML
    TreeView<String> menuTree;
    @FXML
    TableView<Order> deliverTable;
    @FXML
    private TableColumn<Order, String> tableDeliverColumn;
    @FXML
    private TableColumn<Order, String> detailsDeliverColumn;
    @FXML
    TableView<Order> returnTable;
    @FXML
    private TableColumn<Order, String> tableReturnColumn;
    @FXML
    private TableColumn<Order, String> detailsReturnColumn;
    @FXML
    ListView<String> additionsListView;
    @FXML
    ListView<String> subtractionsListView;
    @FXML
    ListView<String> ordersSubmittedListView;
    @FXML
    RadioButton splitBillRadioButton;

    /**
     * Constructor for ServerScreen Controller. Adds this object as an observer to the tracker so that it will be
     * updated when there order are added or removed or change state.
     */
    public ServerScreenController(){
        orderTracker.addObserver(this);
        submittedOrders = new ArrayList<>();
    }

    /**
     * Initialize the information that is displayed in on the server screen.
     */
    @FXML
    private void initialize(){

        // Set up options for tablesComboBox
        ArrayList<Integer> allTablesIDs = TableManager.getInstance().getAllIds();
        for (Integer tableID : allTablesIDs){
            tablesComboBox.getItems().add("Table "+ tableID.toString());
        }

        // Set up Deliver Table
        deliverController = new ServerDeliverController(deliverTable, tableDeliverColumn, detailsDeliverColumn);

        // Set up Return Table
        returnController = new ServerReturnController(returnTable, tableReturnColumn, detailsReturnColumn);

        // Set up listener for tablesServingListView ListView
        tablesServingListView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)
                -> changeTableSelected());

        // Set up listeners for all ComboBoxes
        additionsComboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)
                -> addIngredientToAdditions(newValue));

        subtractionsComboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)
                -> addIngredientToSubtractions(newValue));

        tablesComboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)
                -> addTableButton(newValue));

        // Setup order controller which controls selecting a menu item and submitting it
        orderController = new ServerOrderController(menuTree, warningLabel, currentlySelectedTable);
        orderController.setAdditionsSubtractions(additionsComboBox, subtractionsComboBox,
                                                    additionsListView, subtractionsListView);
    }

    /**
     * Registers that a different table has been selected and calls
     */
    private void changeTableSelected(){
        ordersSubmittedListView.getItems().clear();
        updateOrdersSubmittedListView();
    }

    /**
     * Updates the orders submitted to match the selected table.
     */
    private void updateOrdersSubmittedListView(){
        submittedOrders.clear();
        String table = tablesServingListView.getSelectionModel().getSelectedItem();
        if (table == null){return;}
        submittedOrders.addAll(orderTracker.getAllWithLocation(table));
        updateItemsOrderSubmitted();
    }

    /**
     * Updates the items in the orders submitted ListView to match the submitted orders ArrayList
     */
    private void updateItemsOrderSubmitted(){
        ordersSubmittedListView.getItems().clear();
        for (Order order: submittedOrders){
            ordersSubmittedListView.getItems().add(order.getDetails());
        }
    }

    /**
     * Set the Server associated with this server screen.
     * @param server the server to be associated with this screen.
     */
    public void setServer(Server server){
        this.server = server;
        serverNameLabel.setText(server.getName());
        // Set up tables Serving
        tablesServingListView.getItems().addAll(server.getTablesServing());
        // have to update once a server has been added
        update();
    }

    public void clearAdditions(){
        additionsListView.getItems().clear();
    }

    public void clearSubtractions(){
        subtractionsListView.getItems().clear();
    }

    /**
     * update the GUI display based on information from the Tracker which stores order information.
     */
    public void update(){
        deliverController.updateDeliverTable(server);
        returnController.updateReturnTable(tablesServingListView);
        updateOrdersSubmittedListView();
    }

    /**
     * Submit an order into the system. This method joins all the order information gathered from the GUI
     * into a string that the Server class takeOrder() method will recognize and then calls that method.
     */
    public void submitOrder(){
        if (orderTracker.getAllWithStatus(3).size() > 0){
            warningLabel.setText("Deliver Orders Before Submitting New Ones!");
        } else {
            orderController.submitOrder(server);
        }
    }

    /**
     * Adds the ingredient selected in the subtractionsComboBox ComboBox to the list of ingredients to subtract
     * from the order.
     *
     * @param itemToAdd the name of the ingredient to add to subtractionsListView.
     */
    private void addIngredientToSubtractions(String itemToAdd){
        orderController.addIngredientToSubtractions(itemToAdd);
    }

    /**
     * Adds the ingredient selected in the additionsComboBox ComboBox to the list of ingredients to add to
     * the order.
     *
     * @param itemToAdd the name of the ingredient to add to additionsListView.
     */
    private void addIngredientToAdditions(String itemToAdd){
        orderController.addIngredientToAdditions(itemToAdd);
    }

    /**
     * Add a String describing table to the table serving list.
     *
     * @param tableToAdd a String describing the table to add. ex. "Table 1".
     */
    private void addTableButton(String tableToAdd){
        if (tableToAdd != null) {
            addTable(tableToAdd);
        }
    }

    /**
     * Adds and select a table to tablesServingListView if it isn't already there, if it is there it just selects it.
     * @param table the table to add and select.
     */
    private void addTable(String table){
        ObservableList<String> tablesBeingServed = tablesServingListView.getItems();
        if (tablesBeingServed.contains(table)){
            tablesServingListView.getSelectionModel().select(table);
        } else {
            tablesBeingServed.add(table);
            server.addTableServing(table);
            tablesServingListView.getSelectionModel().select(table);
        }
        update();
    }

    /**
     * Removes the selected table form the list of tables serving. If no table is selected, does nothing.
     */
    public void removeTable(){
        String tableToRemove = tablesServingListView.getSelectionModel().getSelectedItem();
        tablesServingListView.getItems().remove(tableToRemove);
        server.removeTableServing(tableToRemove);
        update();
    }

    /**
     * Tells server that an order has been delivered.
     */
    public void deliveredButtonClicked(){
        warningLabel.setText("");
        deliverController.deliveredButtonClicked(server);
    }

    /**
     * Called when the returnedButton is clicked. If there is an order selected on the deliverTable it causes a
     * Message alert box to pop up that requires the user to enter the reason the dish is returned then sends that
     * information to the Server class.
     */
    public void returnButtonClicked(){
        warningLabel.setText("");
        returnController.returnButtonClicked(server);
    }

    /**
     * Tells the server to print the bill, and gets the information about whether to split or not from
     * splitBillRadioButton.
     */
    public void printBill(){
        String tableToGetBillFrom = tablesServingListView.getSelectionModel().getSelectedItem();
        server.printBill(tableToGetBillFrom, splitBillRadioButton.isSelected());
        removeTable();
    }

    /**
     * Loads the TableMap so that the server can select the seat where the current table belongs.
     */
    public void getTableFromMap(){
        try {
            addTable(orderController.getTableFromMap());
        } catch (IOException ex){
            System.out.println("Failed To Load Table Map: " + ex.getMessage());
        }
    }

    /**
     * Method called when Log Out button is pressed. Removes this window and removes this controller from OrderTrackers
     * Observers list then opens the sign in screen.
     *
     * @param actionEvent
     */
    public void logOut(ActionEvent actionEvent) throws IOException {
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        orderTracker.deleteObserver(this);
    }

    /**
     * Undoes a submitted order if it is has not yet been seen by  the cook.
     */
    public void undoSubmission(){
        // Get the selected order
        int orderIndex = ordersSubmittedListView.getSelectionModel().getSelectedIndex();
        if (orderIndex < 0 || orderIndex >= submittedOrders.size()){orderIndex = 0;}
        Order orderToRemove = submittedOrders.remove(orderIndex);

        // Check whether teh cook has seen it or not.
        if (orderToRemove.getStatus(1)) {
            server.undoSubmission(orderToRemove);
            updateItemsOrderSubmitted();
        } else {
            warningLabel.setText("Order Already Being Processed");
        }
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
}