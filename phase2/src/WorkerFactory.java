import java.util.ArrayList;

/**
 * The WorkerFactory class. It creates lists of all the different type of
 * workers working at this restaurant.
 *
 * @author parthparmar
 */

public class WorkerFactory {

    private CookManager cookManager = new CookManager();
    private ManagerManager managerManager = new ManagerManager();
    private ServerManager serverManager = new ServerManager();
    private FileManager fileManager = new FileManager();


    /**
     * Gets all the Servers working at this restaurant from serverlist.txt file
     * @return an ArrayList of Servers
     */
    public ArrayList<Server> getAllServers() {

        ArrayList<Server> list;
        ArrayList<String> fileInfo = fileManager.readFromText("serverlist.txt");
        list = serverManager.getWorker(fileInfo);
        return list;


    }


    /**
     * Gets all the Cooks working at this restaurant from cooklist.txt file
     * @return an ArrayList of Cooks
     */
    public ArrayList<Cook> getAllCooks() {
        ArrayList<Cook> list;
        ArrayList<String> fileInfo = fileManager.readFromText("cooklist.txt");
        list = cookManager.getWorker(fileInfo);
        return list;

    }


    /**
     * Gets all the Managers working at this restaurant from managerlist.txt file
     * @return an ArrayList of Managers
     */
    public ArrayList<Manager> getAllManagers() {
        ArrayList<Manager> list;
        ArrayList<String> fileInfo = fileManager.readFromText("managerlist.txt");
        list = managerManager.getWorker(fileInfo);
        return list;


    }

}

