

import java.util.ArrayList;

/**
 * The Menu class. It is a singleton class since there needs to be only
 * one menu for this restaurant. A menu object has list of all available
 * food items at this restaurant.
 *
 * @author parthparmar
 *
 */


public class Menu {

    private ArrayList<MenuItem> mains = new ArrayList<>();
    private ArrayList<MenuItem> sideDishes = new ArrayList<>();
    private ArrayList<MenuItem> coldDrinks = new ArrayList<>();
    private DishFactory dishFactory = new DishFactory();

    //Menu for this restaurant
    private static Menu newMenu = new Menu();


    /**
     * Constructs a menu object.
     */
    private Menu() {

        makeMenu();

    }


    /**
     * Returns list of names of all the main dishes
     *
     * @return an ArrayList of names of all main dishes
     */
    public ArrayList<String> getAllMains() {
        ArrayList<String> names = new ArrayList<>();
        for (MenuItem item : mains) {
            names.add(item.getName());
        }
        return names;
    }

    /**
     * Returns list of names of all the side dishes
     *
     * @return an ArrayList of names of all side dishes
     */
    public ArrayList<String> getAllSides() {
        ArrayList<String> names = new ArrayList<>();
        for (MenuItem item : sideDishes) {
            names.add(item.getName());
        }
        return names;
    }

    /**
     * Returns list of names of all the drinks
     *
     * @return an ArrayList of names of all drinks
     */
    public ArrayList<String> getAllDrinks() {
        ArrayList<String> names = new ArrayList<>();
        for (MenuItem item : coldDrinks) {
            names.add(item.getName());
        }
        return names;
    }

    /**
     * Gets the instance of  specific main dish from the list
     *
     * @param name name of the main dish
     * @return the new instance of main dish
     */
    public MenuItem getMain(String name) {

        MenuItem item;
        for (MenuItem main : mains) {
            if (main.getName().equals(name)) {
                item = new MenuItem(main.getName(), main.getDescription(), main.getPrice());
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the instance of specific side dish from the list
     *
     * @param name name of the side dish
     * @return the new instance of side dish
     */
    public MenuItem getSide(String name) {
        MenuItem item;
        for (MenuItem side : sideDishes) {
            if (side.getName().equals(name)) {
                item = new MenuItem(side.getName(), side.getDescription(), side.getPrice());
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the instance of specific drink from the list
     *
     * @param name name of the drink
     * @return the new instance of drink
     */
    public MenuItem getDrink(String name) {
        MenuItem item;
        for (MenuItem drink : coldDrinks) {
            if (drink.getName().equals(name)) {
                item = new MenuItem(drink.getName(), drink.getDescription(), drink.getPrice());
                return item;
            }
        }
        return null;
    }

    /**
     * Makes menu consisting of different dishes from dishFactory
     */
    private void makeMenu() {

        mains = dishFactory.getAllDishes("Mains");
        sideDishes = dishFactory.getAllDishes("Sides");
        coldDrinks = dishFactory.getAllDishes("Drinks");
    }


    /**
     * Returns the one and only instance of Menu
     *
     * @return newMenu variable of this class
     */
    public static Menu getMenu() {
        return newMenu;
    }


}
