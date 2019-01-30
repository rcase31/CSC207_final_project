import java.util.ArrayList;

/**
 * The TableManager class is used to help select tables by their status and stores them.
 *
 * This class will load table list from the text file "tablelist.txt" if there are no tables stored on the tables
 * ArrayList.
 *
 * @author Rafaell Casella
 * @version 0.1
 */
public class TableManager {

    private static TableManager INSTANCE = new TableManager();
    private ArrayList<Table> tables = new ArrayList<>();
    private FileManager fileManager = new FileManager();

    /**
     * Part of the Singleton design implementation.
     *
     * @return the only instance of this class.
     */
    public static TableManager getInstance(){
        return INSTANCE;
    }

    /**
     * The constructor of this class will be called only once, and will load all the tables into the Arraylist in this
     * class.
     *
     */
    private TableManager(){
        loadAllTables();
    }

    /**
     * Returns an Arraylist with all the table ids from all tables in this restaurant.
     *
     * @return the list of all ids.
     */
    public ArrayList<Integer> getAllIds(){
        ArrayList<Integer> allIds = new ArrayList<>();
        for (Table table: tables){
            allIds.add(table.getId());
        }
        return allIds;
    }

    /**
     * Gets the number of seats occupied in this RestaurantSetUp, among all the tables.
     *
     * @return the total amount of occupied seats.
     */
    public int getTotalOccupiedSeats(){
        int total = 0;
        for (Table table: tables){
            total += table.getNumberOccupiedSeats();
        }
        return total;
    }

    /**
     * Search through all the tables and returns the table associated with the given id.
     *
     * @param id the id of the table the caller needs.
     * @return the table associated with the input id.
     */
    public Table getTableById(int id){

        for (Table table: tables){
            if (table.getId() == id)
                return table;
        }
        return null;
    }

    /**
     * Returns a list with all occupied tables.
     *
     * @return all occupied tables.
     */
    public ArrayList<Table> getAllOccupied() {
        ArrayList<Table> allOccupied = new ArrayList<>();
        for (Table table: tables){
            if (table.isOccupied()){
                allOccupied.add(table);
            }
        }
        return allOccupied;
    }

    /**
     * This method will load all the tables on the text file into this class.
     *
     */
    private void loadAllTables(){
        String FILE_NAME = "tablelist.txt";
        String EOF = "THE END";
        String DIVISOR = ":";

        ArrayList<String> info = fileManager.readFromText(FILE_NAME);
        int id;
        int capacity;
        for(String item:info){
            if (!item.equals(EOF)) {
                id = Integer.valueOf(item.split("\\|")[0].split(DIVISOR)[1]);
                capacity = Integer.valueOf(item.split("\\|")[1].split(DIVISOR)[1]);
                tables.add(new Table(id,capacity));
            }
        }
    }
}
