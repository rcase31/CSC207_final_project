import java.util.ArrayList;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Server class. A Server takes orders, delivers orders, and returns orders to the Cook when there is a problem.
 *
 * @author Thomas Campbell
 */
public class Server extends Person {
    private final Pattern numberPattern = Pattern.compile("\\d+");
    private ArrayList<String> tablesServing;
    private Logger logger;

    /**
     * Constructs a Server object.
     *
     * @param name The name of the Server.
     * @param id   The employee ID of the Server.
     * @param password this employees password.
     */
    public Server(String name, int id, String password) {
        super(name, id, password);
        setCategory("Server");
        tablesServing = new ArrayList<>();
        logger = new Logger();
    }

    /**
     * Adds a string representation of a table to the list of tables being served by this server.
     *
     * @param table a string representing the table being served.
     */
    public void addTableServing(String table){
        tablesServing.add(table);
    }

    /**
     * Remove a string representation of a table from the list of tables this server is serving.
     *
     * @param table the table to be removed.
     */
    public void removeTableServing(String table){
        tablesServing.remove(table);
    }

    /**
     * Get the list of string representations of tables being served.
     *
     * @return an ArrayList of strings describing the tables being served ex. "Table 1", "Table 2".
     */
    public ArrayList<String> getTablesServing() {
        return tablesServing;
    }

    /**
     * Converts String array describing a main menu item into that menu item.
     *
     * @param orderInformation string array describing a main from the menu.
     * @return the menu item created from this string.
     */
    private MenuItem createMain(String orderInformation) {

        MenuItem newOrderItem = menu.getMain(orderInformation.split("\\|")[0].split("-")[1]);

        if (orderInformation.split("\\|").length > 1) {
            // if after splitting at | if there is more than 1 string then ingredients need to be added or subtracted.
            for (int i = 1; i < orderInformation.split("\\|").length; i++) {
                // addOrSubtract should have value "Add" or "Subtract" and is used to determine if ingredients are being
                // added or subtracted from the menuItem.
                String addOrSubtract = orderInformation.split("\\|")[i].split("-")[0].trim();
                switch (addOrSubtract) {
                    case "Add":
                        String[] itemsToAdd = orderInformation.split(
                                "\\|")[i].split("-")[1].split(",");
                        for (String addItem : itemsToAdd) {
                            newOrderItem.addExtra(addItem);
                        }
                        break;
                    case "Subtract":
                        String[] itemsToSubtract = orderInformation.split(
                                "\\|")[i].split("-")[1].split(",");
                        for (String subtractItem : itemsToSubtract) {
                            newOrderItem.subtractItem(subtractItem);
                        }
                        break;
                    default:
//                        logger.warning("Got " + addOrSubtract +
//                                " Add and Subtract are the only Ingredient Operations supported.");
                        logger.log(Logger.Level.WARNING,"Got " + addOrSubtract +
                                " Add and Subtract are the only Ingredient Operations supported." );
                        break;
                }
            }
        }
        return newOrderItem;
    }

    /**
     * Finds the MenuItem matching the given orderInformation from the menu.
     *
     * @param orderInformation A string that contains the information about the order for proper syntax check
     *                         the README.txt.
     * @return the MenuItem from the Menu that matches the orderInformation.
     */
    private MenuItem findMenuItem(String orderInformation) {
        // The first string in the orderInformation after splitting at "|" and "-" describes the type of order.
        // category should "Main", "Drinks", or "Sides".
        String category = orderInformation.split("\\|")[0].split("-")[0].trim();
        MenuItem newItemOrdered;
        switch (category) {
            case "Main":
                return(createMain(orderInformation));
            case "Drinks":
                String drinkOrdered = orderInformation.split("-")[1];
                // Add all the drinks to the order.
                newItemOrdered = menu.getDrink(drinkOrdered);
                return(newItemOrdered);

            case "Sides":
                String sideOrdered = orderInformation.split("-")[1];
                // Add all the sides to the order.
                newItemOrdered = menu.getSide(sideOrdered);
                return(newItemOrdered);
            default:
//                logger.warning(category + ": MenuItem type not recognized, should be Main, Sides, or Drinks.");
                logger.log(Logger.Level.FINEST,
                        category + ": MenuItem type not recognized, should be Main, Sides, or Drinks.");
                return null;
        }
    }

    /**
     * Takes the order from the table. Converts a String representation of an order into an order for table.
     *
     * @param orderInformation a string with the necessary details about order, format specified in README.txt
     * @return String that is empty when the order is successfully entered and describes the problem if it doesn't.
     */
    public String takeOrder(String orderInformation, String location) {

        // Check that a location was selected
        if (location == null || location.equals("None")){return "No Table selected";}

        // Check the format of orderInformation
        if (orderInformation == null){
            return "No order information received";
        } else if(orderInformation.split("\\|")[0].split("-").length <= 1){
            return "Order Information incorrectly formatted.";
        }

        // Create the new order
        Order order = new Order(findMenuItem(orderInformation));

        // Check that there are enough ingredients to take this order.
        Map<String, Double> ingredientsNeeded = order.getAllIngredientsUsed();
        for (Map.Entry<String, Double> ingredient : ingredientsNeeded.entrySet()) {
            if (ingredient.getValue() > inventory.viewInventory(ingredient.getKey())) {
//                logger.info("Not enough " + ingredient.getKey() + " to make " + order.getDetails());
                return "Not enough "+ ingredient.getKey() + " to make " + order.getDetails();
            }
        }

        // put the order into orderTracker
        order.changeStatus(1);
        order.setLocation(location);
        orderTracker.addOrder(order);

        // Log that the order was processed
//        logger.finest( name + " ordered " + order.getDetails() + " from " + order.getLocation() + ".");
        logger.log(Logger.Level.FINEST,
                name + " ordered " + order.getDetails() + " from " + order.getLocation() + "." );
        return "";
    }

    /**
     * Register that an order was delivered and change its status.
     *
     * @param order the order that was delivered.
     */
    public void deliveredOrder(Order order){
        order.changeStatus(4);

        if (order.getWasReturned()){
            order.setWasReturned(false, "Order Fixed.");
        }

        orderTracker.setChanged();
        logger.log(Logger.Level.FINEST,
                "Server: " + name + " delivered " + order.getDetails() + " to " + order.getLocation() + ".");
    }

    /**
     * Returns an order with dish to put back into system and the reason it ws returned.
     *
     * @param order order being returned
     * @param reason the reason it was returned
     */
    public void returnOrder(Order order, String reason){
        order.setWasReturned(true, reason);
        order.changeStatus(1);
        orderTracker.setChanged();
//        logger.finest(order.getDetails() + " returned because " + reason);
        logger.log(Logger.Level.FINEST, order.getDetails() +" from " + order.getLocation());
    }

    /**
     * Remove an order from the OrderTracker.
     *
     * @param order the order to be removed from the OrderTracker
     */
    public void undoSubmission(Order order){
        orderTracker.removeOrder(order);
        logger.log(Logger.Level.FINE, order.getDetails() +" from " +order.getLocation() +" submission undone.");
    }

    /**
     * Print the bill for a given table.
     *
     * @param table a string representing the table to print the bill from.
     * @param split true if you want to divide the bill up to corresponding seats, false otherwise.
     */
    public void printBill(String table, boolean split){
        if (table == null){return;}
        // Use numberPattern regex to pull the table number out of the string.
        Matcher matcher = numberPattern.matcher(table);
        if(matcher.find()) {
            int tableID = Integer.parseInt(matcher.group());
            TableManager.getInstance().getTableById(tableID).askBill(split);
            orderTracker.removeAllWithLocation(tableID);
        }
    }
}

