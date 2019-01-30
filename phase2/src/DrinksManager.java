import java.util.ArrayList;

/**
 * The DrinksManager class. It creates and manages all the information about drinks
 * available at this restaurant.
 *
 * @author parthparmar
 */
public class DrinksManager implements Dish {

    /**
     * Returns an ArrayList of drinks available at this restaurant.
     * @param fileInfo String ArrayList of consisting of all the entries from drinks.txt file.
     * @return an ArrayList of all the drinks
     */
    @Override
    public ArrayList<MenuItem> getDishes(ArrayList<String> fileInfo) {
        ArrayList<MenuItem> itemList = new ArrayList<>();
        String name;
        String description;
        Double price;
        for(String item:fileInfo){
            if (!item.equals("THE END")) {

                name = item.split("\\|")[0].split(":")[1];
                description = item.split("\\|")[1].split(":")[1];
                price = Double.valueOf(item.split("\\|")[2].split(":")[1]);
                itemList.add(new MenuItem(name,description,price));

            }

        }

        return itemList;
    }
}
