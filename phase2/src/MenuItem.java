import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The MenuItem class. A MenuItem object is a dish/food served as part of
 * the entire menu of the restaurant.
 *
 * @author Jedid Ahn
 * @version 1.1
 */

public class MenuItem {

    private String name;
    private String description;
    private double price;
    private Map<String, Double> ingredientsUsed;
    private ArrayList<String> addOns;
    private ArrayList<String> subtractions;

    /**
     * The 1st MenuItem constructor. Each menu item consists of a name, a description,
     * and a price. The menu item can be modified to add or subtract certain ingredients
     * to the customer's liking. The menu item also has an indicator if it was asked
     * to be returned by a table.
     *
     * @param name        a String representing the name of the menu item.
     * @param description a String giving a description of the menu item.
     * @param price       a double representing the price of the menu item.
     */
    MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.ingredientsUsed = new HashMap<>();

        // Create a MenuItemManager to read menudetails.txt to populate the ingredients used.
        MenuItemManager menuItemManager = new MenuItemManager();
        menuItemManager.addAllIngredientsUsed(this.name, this.ingredientsUsed);

        this.addOns = new ArrayList<>();
        this.subtractions = new ArrayList<>();
    }


    /**
     * Return the name of this menu item.
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }


    /**
     * Return the description of this menu item.
     *
     * @return String
     */
    public String getDescription() {
        return this.description;
    }


    /**
     * Return the price of this menu item.
     *
     * @return double
     */
    public double getPrice() {
        return this.price;
    }


    /**
     * Return the ingredient names that were added on as extra with this menu item.
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getAddOns() {
        return this.addOns;
    }


    /**
     * Return the ingredient names that were subtracted within this menu item.
     *
     * @return ArrayList<String>
     */
    public ArrayList<String> getSubtractions() {
        return this.subtractions;
    }


    /**
     * Return a Map of all the ingredients used in this menu item. The key is the name
     * of the ingredient and it's corresponding value is the quantity at which the
     * ingredient is to be used up.
     *
     * @return Map<String   ,       Double>
     */
    public Map<String, Double> getIngredientsUsed() {
        return this.ingredientsUsed;
    }


    /**
     * Store the name of the ingredient to add on to the existing menu item.
     *
     * @param ingredientName The name of the ingredient to add to the existing menu item.
     */
    public void addExtra(String ingredientName) {
        this.addOns.add(ingredientName);
    }


    /**
     * Store the name of the ingredient to remove from the existing menu item.
     *
     * @param ingredientName The name of the ingredient to remove from the existing menu item.
     */
    public void subtractItem(String ingredientName) {
        this.subtractions.add(ingredientName);
    }
}