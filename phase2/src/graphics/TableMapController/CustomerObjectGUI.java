package graphics.TableMapController;


import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


/**
 * The CustomerObjectGUI class represents a Rectangle on the TableMap Graphic Interface, depicting either a table or a
 * seat. It may change color and status (occupied, selected, free). This is the parent class for SeatGUI and TableGUI.
 *
 * @author Rafaell Casella
 * @version 0.2
 */
abstract class CustomerObjectGUI {

    private final Color OCCUPIED_COLOR = Color.color(0.85, 0.5, 0.5);
    private final Color AVAILABLE_COLOR = Color.color(0.5, 0.85, 0.5);
    private final Color SELECTED_COLOR = Color.color(0.4, 0.4, 0.95);

    private boolean occupied;
    Rectangle rectangle;
    private String id;
    private boolean selected;

    /**
     * Constructor to this class. It will take the id from the rectangle associated with any instantiation for easier
     * reference. By default, a table or seat begins free of occupation.
     *
     * @param rectangle the rectangle to be associated with this object, as drawn on the graphic interface.
     */
    CustomerObjectGUI(Rectangle rectangle) {
        occupied = false;
        this.rectangle = rectangle;
        id = rectangle.getId();
    }

    /**
     * Converts the string id in this object and returns it as int.
     * Precondition: all fx id's for the table rectangles on the table map must be named (table+seat)_[id number].
     *
     * @return the numerical part of the id of this instantiation.
     */
    public int getIntId() {
        String[] auxArray = this.getId().split("_");
        // gets the last element
        return Integer.parseInt(auxArray[auxArray.length - 1]);
    }

    /**
     * Returns the id of this instantiation.
     *
     * @return the id as a text.
     */
    public String getId() {
        return id;
    }

    /**
     * Tells whether this object as a seat or table is occupied.
     *
     * @return this object is occupied (true/ false).
     */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Tells whether this object as a seat or table is selected.
     *
     * @return this object is selected (true/ false).
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * Selects or un-selects this object and updates its color.
     *
     * @param selected whether this object should be selected.
     */
    void select(boolean selected) {
        this.selected = selected;
        updateColor();
    }

    /**
     * Changes the color of this object as shown on the graphical interface.
     *
     * @param color the color to be changed.
     */
    private void setColor(Color color) {
        rectangle.setFill(color);
    }

    /**
     * Occupies or unoccupy this object.
     *
     * @param occupied whether this object should be occupied.
     */
    public void makeOccupied(boolean occupied) {
        this.occupied = occupied;
        updateColor();
    }

    /**
     * Alternates the selection of this object. If it is already selected, un-selects it; same for the reverse.
     *
     */
    public void toggleSelection() {
        selected = !selected;
        updateColor();
    }

    /**
     * Makes the color of this object reflect its current state (selected/ occupied/ available).
     *
     */
    private void updateColor() {
        if (selected) {
            setColor(SELECTED_COLOR);
        } else if (!occupied) {
            setColor(AVAILABLE_COLOR);
        } else {
            setColor(OCCUPIED_COLOR);
        }
    }
}