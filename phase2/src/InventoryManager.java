import java.util.ArrayList;

/**
 * The InventoryManager class. It uses a fileManager object to read from an ingredients.txt
 * file to add ingredients to the inventory of the RestaurantSetUp.
 *
 * @author Jedid Ahn
 * @version 0.1
 */


public class InventoryManager {

    private FileManager fileManager = new FileManager();

    /**
     * Read an ingredients.txt file and add ingredient objects to the ArrayList of ingredient objects
     * as part of the inventory.
     *
     * @param items An ArrayList of Ingredient objects representing the inventory.
     */
    public void addAllIngredients(ArrayList<Ingredient> items) {
        ArrayList<String> fileInfo = fileManager.readFromText("ingredients.txt");
        for (String line : fileInfo){
            this.addIngredient(line, items);
        }
    }


    /**
     * Make a new Ingredient object using a line from a .txt file and add it to the ArrayList
     * of Ingredient objects in the Inventory.
     *
     * @param line a String from a .txt file used to make an Ingredient object.
     * @param items An ArrayList of Ingredient objects representing the inventory.
     */
    private void addIngredient(String line, ArrayList<Ingredient> items) {
        String name = line.split("\\|")[0].split(":")[1];
        Double quantity = Double.valueOf(line.split("\\|")[1].split(":")[1]);
        Double threshold = Double.valueOf(line.split("\\|")[2].split(":")[1]);
        String unit = line.split("\\|")[3].split(":")[1];

        items.add(new Ingredient(name, quantity, threshold, unit));
    }


    /**
     * Create a request for an item in the inventory to be
     * reordered, due to low stock.
     *
     * @param item Ingredient object to be reordered.
     */
    public void createRequest(Ingredient item) {
        String lineToWrite = "Requesting 20 " + item.getUnit() + " of " + item.getName() + ".";
        fileManager.writeToText("requests.txt", lineToWrite);
    }
}