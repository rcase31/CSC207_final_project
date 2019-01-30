import java.util.HashMap;
import java.util.Map;
import java.lang.StringBuilder;

/**
 * The Order class. An order object contains the menu item that was ordered, all the ingredients
 * that were used to produce the order, the statuses of this order, the location of the
 * table/seat associated with this order, the details of this order including menu item name
 * and add ons and subtractions of ingredients.  It also specifies whether the order was returned,
 * and if so, gives the reason for the return.
 *
 * @author Jedid Ahn
 * @version 1.1
 */

public class Order{

    private MenuItem foodItem;
    private Map<String, Double> allIngredientsUsed;
    private Map<Integer, Boolean> status;
    private String location;
    private String details;
    private boolean wasReturned;
    private String reasonForReturn;

    /**
     * The Order constructor. Each order contains the menu item that was ordered, all the ingredients
     * that were used to produce the order, the statuses of this order, the location of the table/seat
     * associated with this order, the details of this order including menu item name and add ons and
     * subtractions of ingredients.  It also specifies whether the order was returned, and if so,
     * gives the reason for the return.
     *
     * @param foodItem A MenuItem object representing the food item that was ordered.
     */
    Order(MenuItem foodItem) {
        this.foodItem = foodItem;
        this.allIngredientsUsed = new HashMap<>();
        this.status = new HashMap<>();
        this.initializeStatus();
        this.location = "";
        this.details = "";
        this.wasReturned = false; // Assume it was not returned.
        this.reasonForReturn = "";
        registerAllIngredientsUsed();
    }


    /**
     * Initialize the 4 different statuses of an order. The keys represent an
     * Integer from 1-4 corresponding to a specific status, as shown below.
     * All statuses are initialized to false as given in their respective values.
     *
     * 1 - orderTaken
     * 2 - orderReceived
     * 3 - orderPrepared
     * 4 - orderDelivered
     */
    private void initializeStatus(){
        this.status.put(1, false);
        this.status.put(2, false);
        this.status.put(3, false);
        this.status.put(4, false);
    }


    /**
     * Return the status of this order given a specific status number.
     *
     * @param statusNumber An integer corresponding to a specific status of this order.
     *
     * @return boolean
     */
    public boolean getStatus(int statusNumber) {
        return this.status.get(statusNumber);
    }


    /**
     * Change the status of this order given a specific status number. Only 1 status
     * may be true at a time.
     *
     * @param statusNumber An integer corresponding to a specific status of this order.
     */
    public void changeStatus(int statusNumber){
        initializeStatus();
        this.status.replace(statusNumber, true);
    }


    /**
     * Return the location of this order that is associated with a particular table/seat.
     *
     * @return String
     */
    public String getLocation(){
        return this.location;
    }


    /**
     * Return the location of this order that is associated with a particular table/seat.
     *
     * @param location A string representing the location of this order that is associated
     *                 with a particular table/seat.
     */
    public void setLocation(String location){
        this.location = location;
    }


    /**
     * Return a String of the name of the menu item in this order, as well as the names
     * of the add ons and subtractions of ingredients if available.
     *
     * @return String
     */
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append(this.foodItem.getName());

        if (!this.foodItem.getAddOns().isEmpty()){
            details.append(", + ");
            details.append(this.foodItem.getAddOns());
        }

        if (!this.foodItem.getSubtractions().isEmpty()){
            details.append(", - ");
            details.append(this.foodItem.getSubtractions());
        }

        if (this.wasReturned){
            details.append(", returned: Yes,");
            details.append(" reason: ");
            details.append(this.reasonForReturn);
        }

        this.details = details.toString();
        return this.details;
    }


    /**
     * Return the menu item in this order.
     *
     * @return MenuItem
     */
    public MenuItem getFoodItem() {
        return this.foodItem;
    }

    /**
     * Return a Map of every single ingredient used in every single menu item
     * in this order. The key is the name of the ingredient and it's
     * corresponding value is the quantity at which the ingredient is
     * to be used up.
     *
     * @return Map<String ,   Double>
     */
    public Map<String, Double> getAllIngredientsUsed(){
        return allIngredientsUsed;
    }

    /**
     * Initialize map of all ingredients used using information from foodItem.
     */
    private void registerAllIngredientsUsed() {
        String key;
        double value, currentValue; // currentValue represents an old value in this.allIngredientsUsed.
        for (Map.Entry<String, Double> ingredient : this.foodItem.getIngredientsUsed().entrySet()) {
                key = ingredient.getKey();
                value = ingredient.getValue();
                if (this.allIngredientsUsed.containsKey(key)) {
                    currentValue = this.allIngredientsUsed.get(key);
                    // The value associated with the key is overwritten with a new value.
                    this.allIngredientsUsed.put(key, currentValue + value);
                } else {
                    // New key and value are added to the hash map.
                    this.allIngredientsUsed.put(key, value);
                }
        }

         //Add additions
        double amountAdded = 0.3;
        for (String ingredientName: foodItem.getAddOns()){
            if (this.allIngredientsUsed.containsKey(ingredientName)) {
                currentValue = this.allIngredientsUsed.get(ingredientName);
                // The value associated with the key is overwritten with a new value.
                this.allIngredientsUsed.put(ingredientName, currentValue + amountAdded);
            } else {
                // New key and value are added to the hash map.
                this.allIngredientsUsed.put(ingredientName, amountAdded);
            }
        }

        //Remove Subtractions
        for (String ingredientName: foodItem.getSubtractions()){
            if (this.allIngredientsUsed.containsKey(ingredientName)) {
                allIngredientsUsed.remove(ingredientName);
            }
        }
    }

    /**
     * Return whether the menu item was returned or not.
     *
     * @return boolean
     */
    public boolean getWasReturned() {
        return this.wasReturned;
    }


    /**
     * Return the reason for returning the order.
     *
     * @return String
     */
    public String getReasonForReturn() {
        return this.reasonForReturn;
    }


    /**
     * Change the return status of the order and get the reason for the return
     * if the order was returned.
     *
     * @param wasReturned A boolean representing whether the order was returned or not.
     * @param reason A String explaining why the order had to be returned, or
     *               confirming that the return is complete.
     */
    public void setWasReturned(boolean wasReturned, String reason) {
        this.wasReturned = wasReturned;
        this.reasonForReturn = reason;
    }
}