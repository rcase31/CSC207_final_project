import java.util.ArrayList;

/**
 * The cookManager class. It creates and manages all the information about cooks working
 * in this restaurant.
 * @author parthparmar
 */
public class CookManager implements Worker {


    /**
     * Returns list of all the cooks working at this restaurant.
     * @param fileInfo String ArrayList consisting of all the entries from cooklist.txt file
     * @return an Arraylist of all the cooks
     */
    @Override
    public ArrayList<Cook> getWorker(ArrayList<String> fileInfo) {
        ArrayList<Cook> allCooks = new ArrayList<>();
        String name;
        int id;
        String password;
        for(String item : fileInfo){
            if (!item.equals("THE END")) {
                name = item.split("\\|")[0].split(":")[1];
                id = Integer.valueOf(item.split("\\|")[1].split(":")[1]);
                password = item.split("\\|")[2].split(":")[1];
                allCooks.add(new Cook(name, id,password));
            }

        }
        return allCooks;


    }
}
