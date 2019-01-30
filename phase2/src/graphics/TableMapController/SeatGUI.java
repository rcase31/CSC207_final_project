package graphics.TableMapController;
import javafx.scene.shape.Rectangle;

/**
 * The SeatGUI class represents a Seat on the TableMap Graphic Interface. It may change color and status
 * (occupied, selected, free).
 *
 * @author Rafaell Casella
 * @version 0.1
 */
public class SeatGUI extends CustomerObjectGUI {

    public SeatGUI(Rectangle rectangle) {
        super(rectangle);
    }

    public boolean equals(Rectangle rectangle){
        return super.rectangle == rectangle;
    }

}