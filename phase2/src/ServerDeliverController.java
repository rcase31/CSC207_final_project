import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.ArrayList;

/**
 * The ServerDeliverController class. This class controls the orders seen on the TableView on the deliver tab of the
 * ServerScreen.
 *
 * @author Thomas Campbell
 */
public class ServerDeliverController {
    private OrderTracker orderTracker = OrderTracker.getInstance();
    private ObservableList<Order> deliverOrders = FXCollections.observableArrayList();
    private TableView<Order> deliverTable;

    /**
     * Constructor for a ServerDeliverController
     * @param deliverTable the TableView that will display the orders that are ready to deliver.
     * @param tableDeliverColumn the column of deliverTable that contains information about the table this order should
     *                           be delivered to.
     * @param detailsDeliverColumn the column of deliverTable that contains information about what menuItem and
     *                            additions/subtractions are in the order.
     */
     ServerDeliverController(TableView<Order> deliverTable,
                                   TableColumn<Order, String> tableDeliverColumn,
                                   TableColumn<Order, String> detailsDeliverColumn){

         this.deliverTable = deliverTable;

        tableDeliverColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        detailsDeliverColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        this.deliverTable.setItems(deliverOrders);

    }

    /**
     * Clears the deliverTable and updates it with all the orders present in the orderTracker with the correct status.
     *
     * @param server the server operating the GUI.
     */
    public void updateDeliverTable(Server server) {
        deliverOrders.clear();
        deliverTable.setItems(deliverOrders);
        ArrayList<Order> orders = orderTracker.getAllWithStatus(3);
        for (Order order : orders) {
            addOrderToList(server, order);
        }
    }

    /**
     * Checks whether the order given is associated with a table that this server is serving and adds it to the orders
     * list if it is.
     *
     * @param server the Server object associated with this server screen.
     * @param order the order thats table is being checked.
     */
    private void addOrderToList(Server server, Order order){
        for (String table :server.getTablesServing()) {
            if (order.getLocation().contains(table + " ")) {
                deliverOrders.add(order);
            }
        }
    }

    /**
     * Tells server that an order has been delivered.
     *
     * @param server the server to notify.
     */
    public void deliveredButtonClicked(Server server){
        Order orderSelected = deliverTable.getSelectionModel().getSelectedItem();
        if (orderSelected == null){return;}
        server.deliveredOrder(orderSelected);
    }
}
