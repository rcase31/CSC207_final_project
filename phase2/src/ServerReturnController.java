import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

/**
 * The ServerReturnController class. This class controls the input from the Return Tab on the server screen. Controls
 * what is displayed in the ReturnTable and allows user to return an order that has been delivered.
 */
public class ServerReturnController {
    private OrderTracker orderTracker = OrderTracker.getInstance();
    private ObservableList<Order> returnOrders = FXCollections.observableArrayList();
    private TableView<Order> returnTable;

    /**
     * Constructor for ServerReturnController. Makes a ServerReturnController that controls the the orders seen in a
     * table and the return button.
     *
     * @param returnTable The Table of orders that have been delivered (orders that can potentially be returned.)
     * @param tableReturnColumn Column of the return table that displays the location of the orders.
     * @param detailsReturnColumn Column of the return table that displays the information about the orders returned.
     */
    ServerReturnController(TableView<Order> returnTable,
                           TableColumn<Order, String> tableReturnColumn,
                           TableColumn<Order, String> detailsReturnColumn){

        this.returnTable = returnTable;

        tableReturnColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        detailsReturnColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        this.returnTable.setItems(returnOrders);
    }

    /**
     * Clears the return table and updates with all the orders present in the orderTracker with the correct status.
     *
     * @param tablesServing a ListView that contains the String representation of all the tables being served
     */
    public void updateReturnTable(ListView<String> tablesServing){
        returnOrders.clear();
        ArrayList<Order> orders = orderTracker.getAllWithStatus(4);
        for (Order order : orders) {
            addOrderToList(tablesServing, order);
        }
    }

    /**
     * Checks whether the order given is associated with a table that this server is serving and adds it to the orders
     * list if it is.
     *
     * @param tablesServing the ListView that contains all the tables being served.
     * @param order the order thats table is being checked.
     */
    private void addOrderToList(ListView<String> tablesServing, Order order){
        for (String table :tablesServing.getItems()) {
            if (order.getLocation().contains(table+" ")) {
                returnOrders.add(order);
            }
        }
    }

    /**
     * Called when the returnedButton is clicked. If there is an order selected on the deliverTable it causes a
     * Message alert box to pop up that requires the user to enter the reason the dish is returned then sends that
     * information to the Server class.
     *
     * @param server The Server to notify that an order has been returned.
     */
    public void returnButtonClicked(Server server){
        Order returnedOrder = returnTable.getSelectionModel().getSelectedItem();
        if (returnedOrder == null){return;}
        String reason = MessageAlertBox.display("Order Returned","Why was this order returned?");
        if (reason.equals("")){return;}
        server.returnOrder(returnedOrder, reason);
    }
}
