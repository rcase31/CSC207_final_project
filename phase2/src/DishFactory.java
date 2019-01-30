import java.util.ArrayList;

/**
 * The DishFactory class. It creates list of different type of dishes.
 *
 * @author parthparmar
 */

public class DishFactory {
    private MainDishManager mainDishManager = new MainDishManager();
    private SideDishManager sideDishManager = new SideDishManager();
    private DrinksManager drinksManager = new DrinksManager();
    private FileManager fileManager = new FileManager();

    /**
     * Returns an ArrayList of different type of dishes
     * @param dishType name of dish type
     * @return an ArrayList made up of MenuItems of type dishType
     */
    public ArrayList<MenuItem> getAllDishes(String dishType) {
        ArrayList<MenuItem> dishes;
        ArrayList<String> fileInfo;
        switch (dishType) {
            case "Mains":

                fileInfo = fileManager.readFromText("mainDishes.txt");
                dishes = mainDishManager.getDishes(fileInfo);
                return dishes;

            case "Sides":
                fileInfo = fileManager.readFromText("sideDishes.txt");
                dishes = sideDishManager.getDishes(fileInfo);
                return dishes;

            case "Drinks":
                fileInfo = fileManager.readFromText("drinks.txt");
                dishes = drinksManager.getDishes(fileInfo);
                return dishes;

        }
        return null;

    }
}
