import java.util.Map;

/**
 * The Cook class. A Cook records that they have seen an order and notifies a Server when the order has been filled.
 *
 * @author Thomas Campbell
 * @version 0.3
 */
public class Cook extends Person {
    private Logger logger;

    /**
     * Constructs a Cook object.
     *
     * @param name The name of the Cook.
     * @param id   The employee ID of the Cook.
     * @param password This employees password.
     */
    public Cook(String name, int id, String password) {
        super(name, id, password);
        setCategory("Cook");
        logger = new Logger();
    }

    /**
     * Register that an order has been received and change its status in the OrderTracker.
     *
     * @param order the order that has been received
     */
    public void  orderReceived(Order order){
        // Change the order status to 2 which corresponds to being received by the Cook
        order.changeStatus(2);
        orderTracker.setChanged();
        logger.log(Logger.Level.FINEST, name + " Received order " + order.getDetails() +
                " from "+ order.getLocation());
    }

    /**
     * Register that an order has been filled and change its Status in the OrderTracker
     *
     * @param order the order that has been filled
     */
    public void orderFilled(Order order){
        //Change order status to 3, which corresponds to being ready to deliver.
        order.changeStatus(3);
        orderTracker.setChanged();
        // Only remove ingredients if the order is being made for the first time.
        if (!order.getWasReturned()) {
            removeIngredientsUsed(order);
        }
        logger.log(Logger.Level.FINEST, name+ " filled order "+ order.getDetails()+
                " from " + order.getLocation() + ".");
    }

    /**
     * Remove the ingredients that were used form the inventory after a cook has filled an order.
     *
     * @param order the order that has just been filled.
     */
    private void removeIngredientsUsed(Order order){
        Map<String, Double> ingredientsUsed = order.getAllIngredientsUsed();
        // Loop through the ingredients and subtract amount used from inventory.
        for (Map.Entry<String, Double> entry : ingredientsUsed.entrySet()) {
            inventory.subtract(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Reset order to previous status in case of a mistake.
     *
     * @param order the order to roll back the status of.
     */
    public void resetOrder(Order order){
        // set the status of the order to the preceding value.
        if (order.getStatus(3)){
            order.changeStatus(2);
        } else if (order.getStatus(2)){
            order.changeStatus(1);
        }
        logger.log(Logger.Level.FINE, "Cook reset order " + order.getDetails());
    }
}