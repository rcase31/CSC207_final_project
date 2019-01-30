import java.util.ArrayList;

/**
 * The managerManager class. It creates and manages all the information about managers working
 * in this restaurant.
 *
 * @author parthparmar
 */

public class ManagerManager implements Worker {

    /**
     * Returns list of all the managers working at this restaurant.
     * @param fileInfo String ArrayList consisting of all the entries from managerlist.txt file
     * @return an Arraylist of all the managers
     */
    @Override
    public ArrayList<Manager> getWorker(ArrayList<String> fileInfo) {
        ArrayList<Manager> allManager = new ArrayList<>();
        String name;
        int id;
        String password;
        for(String item:fileInfo){
            if (!item.equals("THE END")) {
                name = item.split("\\|")[0].split(":")[1];
                id = Integer.valueOf(item.split("\\|")[1].split(":")[1]);
                password = item.split("\\|")[2].split(":")[1];
                allManager.add(new Manager(name, id,password));
            }

        }
        return allManager;


    }
}
