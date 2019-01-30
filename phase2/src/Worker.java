import java.util.ArrayList;

/**
 * The Worker Interface. It is implemented by CookManager,ServerManager and ManagerManager.
 *
 * @author parthparmar
 */

public interface Worker {

    /**
     * Gets list of all workers
     * @param fileInfo ArrayList made up of string from the .txt file
     * @return an ArrayList made up of different types of workers
     */
    ArrayList getWorker(ArrayList<String> fileInfo);
}
