import java.util.ArrayList;

/**
 * The Inventory class. An inventory object stores all the different types of ingredients
 * used for the menu at a restaurant. An inventory manager object is also used to read
 * and write into files to maintain the inventory, as well as a logger which adds
 * log entries of ingredients received and the quantity received through fileManager.
 *
 * Inventory uses the singleton design pattern as a restaurant only needs 1 inventory object.
 *
 *
 * @author Jedid Ahn
 * @version 1.3
 */

public class Inventory {

    private ArrayList<Ingredient> items;
    private static Inventory inventory = new Inventory();
    private InventoryManager inventoryManager = new InventoryManager();

    private Logger logger;


    /**
     * The Inventory constructor. Each inventory is specific to a restaurant and contains an
     * ArrayList of Ingredient objects, which is populated through reading an ingredients.txt
     * file. The inventory also contains a logManager object to keep track of ingredients
     * received and log them into log.txt.
     */
    private Inventory() {
        this.items = new ArrayList<>();
        // Read ingredients.txt and populate the inventory.
        this.inventoryManager.addAllIngredients(this.items);

        logger = new Logger();
    }


    /**
     * Log that an ingredient has been received and the quantity received.
     *
     * @param ingredientName  A String representing the name of the ingredient received.
     * @param quantityToAdd   A double value representing the quantity received of the ingredient,
     *                        and to add and record in the inventory.
     *
     */
    private void logIngredientReceived(String ingredientName, double quantityToAdd){
        Ingredient ingredient = inventory.findIngredient(ingredientName);
        String message;

        if (ingredient.getUnit().equals("packs") || ingredient.getUnit().equals("cans") ||
                ingredient.getUnit().equals("bottles")){
            message = (((int)quantityToAdd) + " " + ingredient.getUnit() + " of " + ingredientName +
                    " has been received.");
        }
        else{
            message = (quantityToAdd + " " + ingredient.getUnit() + " of " + ingredientName +
                    " has been received.");
        }
        logger.log(Logger.Level.FINEST, message);
    }


    /**
     * Update the inventory by adding more of an ingredient, due
     * to the ingredient being reordered. A log entry is also
     * added into log.txt detailing the ingredient received
     * and the quantity received.
     *
     * @param name     a String containing the name of the ingredient.
     * @param quantity a double value containing the quantity to add.
     */
    public void add(String name, double quantity) {
        for (Ingredient item : this.items) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() + quantity);
                item.setQuantityWithUnit();
                this.logIngredientReceived(item.getName(), quantity); // Add log entry.
                break; // Exit for loop as ingredient has been found.
            }
        }
    }


    /**
     * Update the inventory by adding less of an ingredient, due
     * to the ingredient being used.
     *
     * @param name     a String containing the name of the ingredient.
     * @param quantity a double value containing the quantity to subtract.
     */
    public void subtract(String name, double quantity) {
        for (Ingredient item : this.items) {
            if (item.getName().equals(name)) {
                item.setQuantity(item.getQuantity() - quantity);
                // Now check new quantity to see if it is below threshold.
                if (item.getQuantity() < item.getThreshold())
                    // Order more of this ingredient by writing into requests.txt.
                    this.inventoryManager.createRequest(item);
                break; // Exit for loop as ingredient has been found.
            }
        }
        logger.log(Logger.Level.FINEST, quantity + " " + name + " has been removed.");
    }


    /**
     * Return an ArrayList of all the ingredients in this Inventory.
     *
     * @return ArrayList<Ingredient>
     */
    public ArrayList<Ingredient> getItems(){
        return this.items;
    }


    /**
     * Return the 1 (and only) instance of the Inventory class.
     *
     * @return Inventory
     */
    public static Inventory getInventory() {
        return inventory;
    }


    /**
     * View the inventory to see how much of a certain ingredient is in stock,
     * and return it's quantity.
     *
     * @param name a String containing the name of the ingredient.
     * @return double
     */
    public double viewInventory(String name) {
        for (Ingredient item : this.items) {
            if (item.getName().equals(name)) {
                return item.getQuantity();
            }
        }
        return 0.00;
    }


    /**
     * Find and return an Ingredient object with a specific name as part of the list
     * of ingredients in this Inventory.
     *
     * @param name a String containing the name of the ingredient.
     * @return Ingredient
     */
    public Ingredient findIngredient(String name){
        for (Ingredient ingredient : this.items){
            if (name.equalsIgnoreCase(ingredient.getName())){
                return ingredient;
            }
        }
        return null;
    }
}