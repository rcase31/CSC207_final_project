import java.util.ArrayList;

/**
 * The ServerManager class. It creates and manages all the information about servers working
 * in this restaurant.
 *
 * @author parthparmar
 */

public class ServerManager implements Worker {


    /**
     * Returns list of all the servers working at this restaurant.
     *
     * @param fileInfo String ArrayList consisting of all the entries from serverlist.txt file
     * @return an Arraylist of all the servers
     */
    @Override
    public ArrayList<Server> getWorker(ArrayList<String> fileInfo) {

        ArrayList<Server> allServer = new ArrayList<>();

        String name;
        int id;
        String password;
        for (String item : fileInfo) {
            if (!item.equals("THE END")) {
                name = item.split("\\|")[0].split(":")[1];
                id = Integer.valueOf(item.split("\\|")[1].split(":")[1]);
                password = item.split("\\|")[2].split(":")[1];
                allServer.add(new Server(name, id, password));
            }
        }
        return allServer;

    }
}
