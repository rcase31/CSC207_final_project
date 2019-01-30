import java.util.ArrayList;
import java.util.Map;

/**
 * The MenuItemManager class. It uses a fileManager object to read from a menudetails.txt
 * file to add the name and amount of all ingredients to be used up in a menu item.
 *
 * @author Jedid Ahn
 * @version 0.1
 */

public class MenuItemManager {

    private FileManager fileManager = new FileManager();

    /**
     * Read a menudetails.txt file and add the name and amount of all ingredients to
     * be used up in every menu item.
     *
     * @param name The name of the menu item.
     * @param ingredientsUsed A Map representing the name of the ingredient used to
     *                        its quantity used up to make the menu item.
     */
    public void addAllIngredientsUsed(String name, Map<String, Double> ingredientsUsed) {
        ArrayList<String> fileInfo = fileManager.readFromText("menudetails.txt");

        for (String line : fileInfo){
            String nameOfItem = line.split("\\|")[0].split(":")[1];

            if (nameOfItem.equals(name)) {
                this.addIngredientUsed(line, ingredientsUsed);
                break;  // exit loop as MenuItem has already been found.
            }
        }
    }


    /**
     * Add an ingredient used to make the menu item and how much of it is to be used.
     * If the menu item is a side dish or a drink, then there will only be 1 ingredient,
     * which will be the name of the item itself and a quantity usage of 1.
     *
     * @param line a String from a .txt file used to get the name of the ingredient and
     *             its usage in this menu item.
     * @param ingredientsUsed A Map representing the name of the ingredient used to
     *                        its quantity used up to make the menu item.
     */
    private void addIngredientUsed(String line, Map<String, Double> ingredientsUsed) {
        String lineOfIngredients = line.split("\\|")[1].split(":")[1];
        String[] listOfIngredients = lineOfIngredients.split(",");
        for (String ingredient : listOfIngredients) {
            String nameOfIngredient = ingredient.split("-")[0];
            Double quantityUsed = Double.valueOf(ingredient.split("-")[1]);
            ingredientsUsed.put(nameOfIngredient, quantityUsed);
        }
    }
}
