package graphics.TableMapController;

import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

/**
 * The TableGUI class represents a Rectangle on the TableMap Graphic Interface. It may change color and status
 * (occupied, selected, free), and hold multiple seats.
 *
 * @author Rafaell Casella
 * @version 0.2
 */
public class TableGUI extends CustomerObjectGUI {
    private ArrayList<SeatGUI> seats;
    private Group group;

    /**
     *  Constructor: should receive the respective rectangle for this table; the seats related to this table and the
     *  belonging group (of rectangles on the fxml file).
     *
     * @param rectangle the rectangle object from the fxml file.
     * @param seats the seats related to this table.
     * @param group the group of rectangles that this table (or rectangle) is in.
     */
    public TableGUI(Rectangle rectangle, ArrayList<SeatGUI> seats, Group group) {
        super(rectangle);
        this.seats = seats;
        this.group = group;
    }

    /**
     * Since a TableGUI may hold many seats, this method returns the seat corresponding to the id.
     *
     * @param id the id of the seat that the caller needs the object.
     * @return the instantiation of SeatGUI corresponding to the input id.
     */
    public SeatGUI getSeatGUIbyId(int id) {

        for (SeatGUI seat : seats) {
            if (seat.getIntId() == id)
                return seat;
        }
        return null;
    }

    /**
     * This will check whether table from the Table class is equivalent to the table on the graphic interface.
     *
     * @param tableId the table id we need to compare
     * @return whether these are the same table.
     */
    public boolean equals(int tableId) {
        return this.getIntId() == tableId;
    }

    /**
     * Checks whether this TableGUI instance is member of the group input.
     *
     * @param group the group which the caller needs checking.
     * @return it belong to the input group (true/false).
     */
    public boolean inGroup(Group group) {
        return this.group.equals(group);
    }

    /**
     * Checks whether this table is occupied.
     *
     * @return this table is occupied (true/false).
     */
    @Override
    public boolean isOccupied() {
        boolean ret = false;
        for (SeatGUI seat : seats) {
            ret |= seat.isOccupied();
        }
        return ret;
    }

    /**
     * If at least one of the seats is selected, this should also reflect on the respective table, by changing its
     * color and status.
     *
     */
    private void updateSelection() {
        boolean tempSelected = false;
        for (SeatGUI seat : seats) {
            tempSelected |= seat.isSelected();
        }
        select(tempSelected);
    }

    /**
     * Passes down the rectangle from the event of clicking on the seat, finds the correspondent seat and calls
     * makeOccupied.
     *
     * @param rectangle the rectangle associated with the seat the user just clicked on.
     */
    public SeatGUI updateSeat(Rectangle rectangle) {
        SeatGUI newSeat = null;
        for (SeatGUI seat : seats) {
            if (seat.equals(rectangle)) {
                seat.toggleSelection();
                newSeat = seat;
            }
        }
        updateSelection();
        return newSeat;
    }
}