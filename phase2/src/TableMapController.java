import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.ArrayList;
import graphics.TableMapController.*;

/**
 * The table map stage shown for selecting tables out of a map needs a controller class; this class is responsible for
 * that. It's ultimate use is to pass down the selected table (by click) to the server. This is done by changing colors
 * and updating information for the TableManager as seats and tables become occupied or free.
 *
 * @author Rafaell Casella
 * @version 0.1
 */
public class TableMapController {

    @FXML private ProgressBar restaurantCapacity;
    @FXML private Pane allTablesPane;
    @FXML private Button finishButton;

    private SeatGUI currentSeatSelection = null;
    private TableGUI currentTableSelection = null;
    private StringBuilder currentSelectionString = new StringBuilder();

    private TableGUI mouseOverTable;
    private ArrayList<TableGUI> tablesGUI = new ArrayList<>();


    /**
     * This method is the means to which the caller communicates with this class. It passes down a mutable String (aka
     * StringBuilder) and waits to be mutated.
     *
     * @param currentSelection the variable used to communicate with the caller class.
     */
    public void setSelection(StringBuilder currentSelection){
        this.currentSelectionString = currentSelection;
    }

    /**
     * Method called when initializing this graphic interface. This method checks upon all Rectangles present on the
     * corresponding .fxml file, and turn them into a list of tables and seats. This also resets any remaining value
     * for currentSelectionString, which is the variable used to communication with the other caller graphic interface.
     *
     */
    public void initialize() {
        if (tablesGUI.isEmpty()){
            loadAllTables();
            updateAllTableStatus();
        }
        updateCapacityBarr();
        currentSelectionString.setLength(0);

    }


    /**
     * There is capacity barr on the bottom-left of this stage; this method updates it.
     *
     */
    private void updateCapacityBarr(){
        int CAPACITY = 30;
        TableManager tableManager = TableManager.getInstance();
        float occupancyRatio = (float) tableManager.getTotalOccupiedSeats()/ CAPACITY;
        restaurantCapacity.setProgress(occupancyRatio);
    }


    /**
     * Identifies the group where the mouse is currently passing over, and stores it.
     *
     * @param event the event of passing the mouse over the group.
     */
    @FXML private void saveGroup(javafx.event.Event event) {
        Group currentGroup = (Group) event.getSource();
        for (TableGUI table: tablesGUI){
            if(table.inGroup(currentGroup)){
                mouseOverTable = table;
                return;
            }
        }
    }

    /**
     * This method should be called whenever someone clicks on a seat.
     * Makes the seats and tablesGUI blue,red or green (selected/ occupied/ free); also updates the occupancy barr and
     * the variable.
     *
     * @param event clicking on a seat.
     */
    @FXML private void updateSeatStatus(javafx.event.Event event){
        // Un-select the last selected table
        if(currentSeatSelection != null){
            currentSeatSelection.toggleSelection();
            currentTableSelection.toggleSelection();
        }
        // Updates the newly selected seat
        currentTableSelection = mouseOverTable;
        currentSeatSelection = mouseOverTable.updateSeat((Rectangle)event.getSource());
    }


    /**
     *  This method will feed the Arraylist 'table' with all the tablesGUI on the table map. It  iterates through all
     *  groups in this table map; each group will contain the set of one table and many seats. A helper method will
     *  extract all rectangles in each group.
     *
     */
    private void loadAllTables(){
        for (Object obj: allTablesPane.getChildren()){
            if (obj.getClass() == Group.class){
                extractShapes((Group)obj);
            }
        }
    }

    /**
     * Helper method to extract all rectangles from the input group.
     *
     * Each rectangle will represent either a table or
     * a chair. A table will be input for the tablesGUI arraylist, whereas the seats will be allocated inside the table
     * object. The criteria to identify whether a rectangle is a table or a seat is the id, which should contain the
     * string prefix as listed on the constants TABLE_IDENTIFIER and SEAT_IDENTIFIER.
     *
     * @param group A group is fxml object that can "hold" other objects. In this case we  are using it to group table
     *              and their respective seats.
     */
    private void extractShapes(Group group){
        final String SEAT_IDENTIFIER = "seat";
        final String TABLE_IDENTIFIER = "table";

        ArrayList<SeatGUI> seats = new ArrayList<>();
        Rectangle tableRec = null;

        for (Object obj: group.getChildren()){
            if(obj.getClass() == Rectangle.class){
                String temp_id = ((Rectangle)obj).getId();
                if(temp_id.contains(SEAT_IDENTIFIER)){
                    SeatGUI seat = new SeatGUI((Rectangle)obj);
                    seats.add(seat);
                }else if(temp_id.contains(TABLE_IDENTIFIER)){
                    tableRec = (Rectangle) obj;
                }
            }
        }
        if (tableRec != null){
            TableGUI table = new TableGUI(tableRec, seats, group);
            tablesGUI.add(table);
        }
    }

    /**
     * This method checks which tablesGUI were occupied before the the tableMap was called.
     * After checking, updates de color of those occupied tablesGUI to red.
     *
     */
    private void updateAllTableStatus(){
        ArrayList<Table> allOccupied = TableManager.getInstance().getAllOccupied();
        for (Table table: allOccupied){
            TableGUI tableGUI = getTableGUIbyTable(table);
            if (tableGUI != null){
                tableGUI.makeOccupied(true);
                updateAllSeatStatus(table, tableGUI);
            }
        }
    }

    /**
     * This method gets the occupancy information from a particular instance of Table and passes it to the an instance
     * of TableGUI. In other words, updates the current status from the tables in the restaurant to the graphic
     * interface.
     *
     * @param table the table from the TableManager singleton.
     * @param tableGUI the table as the graphic interface.
     */
    private void updateAllSeatStatus(Table table, TableGUI tableGUI){
        for (int i: table.getAllOccupiedSeats()){
            SeatGUI seatGUI = tableGUI.getSeatGUIbyId(i);
            if (seatGUI != null)
                seatGUI.makeOccupied(true);
        }

    }

    /**
     * Helper method for updateAllTAbleStatus() returns the table GUI object using the table object as search parameter.
     * Ultimately uses the table id as means of comparision.
     *
     * @param table the reference table to be retrieved in its graphic form for this form.
     */
    private TableGUI getTableGUIbyTable(Table table){
        for (TableGUI tableGUI: tablesGUI){
            if (tableGUI.equals(table.getId())){
                return tableGUI;
            }
        }
        return null;
    }

    /**
     * Method called when the button Select seat is pressed. This will occupy the seat if there is a selection, as long
     * as the table; it will also feed information to the communication variable currentSelectionString, which will be
     * used on the caller class. Window wont close if there is no selection.
     *
     */
    @FXML private void finishSelection(){
        TableManager tableManager = TableManager.getInstance();
        Table correspondingTable;
        //
        if (currentTableSelection != null){
            // correspondingTable is an instance of the Table class, and not TableGUI.
            correspondingTable = tableManager.getTableById(currentTableSelection.getIntId());
            if (correspondingTable!= null){
                // This will mutate the corresponding Table making its occupancy status saved even if one closes window.
                correspondingTable.makeSeatOccupied(currentSeatSelection.getIntId());
                // Reminder that this variable is the mutable parameter passed down by the caller.
                currentSelectionString.append(currentSeatSelection.getId());
            }
            closeMe();
        }
    }

    /**
     * Closes this window (or stage).
     *
     */
    private void closeMe() {
        ((Stage)finishButton.getScene().getWindow()).close();
    }


}

