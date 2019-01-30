import java.util.ArrayList;

/**
 * The Dish interface. It is implemented by MainDishManager, SideDishManager and DrinksManager.
 *
 * @author parthparmar
 */

public interface Dish {

    /**
     * Gets all the dishes based on information passed from the ArrayString
     * @param fileInfo An ArrayList of string made up of entries from .txt files
     * @return ArrayList made up of MenuItem objects
     */
    ArrayList<MenuItem> getDishes(ArrayList<String> fileInfo);
}
