import java.util.ArrayList;

/**
 * Stores all the orders in the system and distributes them. Implements Singleton pattern because having multiple
 * instances of this class would make it non-functional because it would have inconsistent information.
 *
 * @author Thomas Campbell
 */
public final class OrderTracker extends Observable{
    private static final OrderTracker INSTANCE = new OrderTracker();
    private  ArrayList<Order> orders = new ArrayList<>();

    /**
     * Constructor for the OrderTracker it is private because this class implements the singleton design pattern
     */
    private OrderTracker(){}

    /**
     * Returns the only instance of OrderTracker
     *
     * @return The only instance of this class.
     */
    public static OrderTracker getInstance(){
        return  INSTANCE;
    }

    /**
     * Add an order to the OrderTracker.
     *
     * @param order the order to add to OrderTracker.
     */
    public void addOrder(Order order){
        orders.add(order);
        setChanged();
    }

    /**
     * Remove an Order from the OrderTracker.
     *
     * @param orderToRemove the Order to remove from the OrderTracker.
     */
    public void removeOrder(Order orderToRemove){
        orders.remove(orderToRemove);
        setChanged();
    }

    /**
     * Return all the orders that have been returned (As in returns all the order that the customer didn't approve of).
     *
     * @return The list of orders that have been returned.
     */
    public ArrayList<Order> getAllReturned(int status){
        ArrayList<Order> correctItems = new ArrayList<>();
        for (Order order: orders){
            if (order.getWasReturned() && order.getStatus(status)){
                correctItems.add(order);
            }
        }
        return correctItems;
    }

    /**
     * Remove all the Orders from the table with the given tableID.
     *
     * @param tableID the table ID of the table you want to remove all orders from.
     */
    public void removeAllWithLocation(Integer tableID){
        String table = "Table " + tableID.toString() + " ";
        ArrayList<Order>  ordersToRemove = new ArrayList<>();
        // Find which orders to remove
        for (Order order: orders){
            if (order.getLocation().contains(table)){
                ordersToRemove.add(order);
            }
        }
        // Remove Orders
        for (Order order: ordersToRemove){
            orders.remove(order);
        }
        setChanged();
    }

    /**
     * Returns all the orders with a given state.
     *
     * @param state the desired state.
     * @return an ArrayList<Order> containing all the orders with the given state.
     */
    public ArrayList<Order> getAllWithStatus(int state){
        ArrayList<Order> correctItems = new ArrayList<>();
        for (Order item : orders){
            if (item.getStatus(state)){
                correctItems.add(item);
            }
        }
        return  correctItems;
    }

    /**
     * Returns all Orders whose location contains the given location String.
     *
     * @param location the location that will be compared to the orders location.
     * @return All orders whose location contains the given location.
     */
    public ArrayList<Order> getAllWithLocation(String location){
        ArrayList<Order> correctItems = new ArrayList<>();
        for (Order item : orders){
            if (item.getLocation().contains(location)||item.getLocation().equals(location)){
                correctItems.add(item);
            }
        }
        return  correctItems;
    }

    /**
     * Returns all orders at the table matching the given tableID
     *
     * @param tableID the tableID of the table you want to get the orders from.
     * @return an ArrayList containing all the orders from the table with the given ID.
     */
    public ArrayList<Order> getAllWithLocation(int tableID) {
        Integer id = tableID;
        ArrayList<Order> correctItems = new ArrayList<>();
        // Convert the tableID into a String that will match the String in the location. The space at the end is to
        // prevent an error if there are more than 9 tables.
        String table = "Table " + id.toString() + " ";
        for (Order item : orders){
            if (item.getLocation().contains(table)){
                correctItems.add(item);
            }
        }
        return  correctItems;
    }

    /**
     * Return all orders from the table matching the tableID and with the status given.
     *
     * @param tableID the ID of the table you want to get the orders from.
     * @param status the status of the orders you want.
     * @return An ArrayList<Order> with all the orders matching the tableID and status.
     */
    public ArrayList<Order> getAllWith(int tableID, int status){
        Integer id = tableID;
        String table = "Table " + id.toString() + " ";
        ArrayList<Order> correctItems = new ArrayList<>();
        for (Order item : orders){
            if (item.getLocation().contains(table)&& item.getStatus(status)){
                correctItems.add(item);
            }
        }
        return correctItems;
    }

    /**
     * Returns all the tables that have not been returned from the table with the given tableID,
     * and with the given status.
     *
     * @param tableID the ID of the table you want to get the orders from.
     * @param status the status of the orders you want.
     * @return An ArrayList<Order> with all non-returned orders matching the tableID and status.
     */
    public ArrayList<Order> getAllNotReturnedWith(int tableID, int status){
        Integer id = tableID;
        String table = "Table " + id.toString() + " ";
        ArrayList<Order> correctItems = new ArrayList<>();
        for (Order item : orders){
            if (item.getLocation().contains(table)&& item.getStatus(status) && !item.getWasReturned()){
                correctItems.add(item);
            }
        }
        return correctItems;
    }
}
