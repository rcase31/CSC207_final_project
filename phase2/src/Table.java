import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Table class represents a costumer when occupied.
 *
 * @author Rafaell Casella
 * @version 0.7
 */

public class Table {

    private int id;
    private boolean occupied;
    private int numberSeats;
    private HashMap<Integer,Boolean> seats;


    /**
     * Constructor for table. Each time a table is created, a number is assigned to that table automatically, and by
     * default, that table is free.
     *
     * @param id to this table
     */
    Table(int id, int numberSeats) {

        this.id = id;
        occupied = false;
        this.numberSeats = numberSeats;
        seats = new HashMap<>(numberSeats);
        // Make all seats in this table free, by default.
        for (int seatNumber = 1;seatNumber <= numberSeats; seatNumber++ ){
            seats.put(seatNumber,false);
        }

    }


    /**
     * This will iterate through the seats in this table and return the occupied ones.
     *
     * @return an array of Integers listing all occupied seats.
     */
    public ArrayList<Integer> getAllOccupiedSeats(){
        ArrayList<Integer> occupiedSeats = new ArrayList<>();
        for (int seatKey: seats.keySet()){
            if (isSeatOccupied(seatKey)){
                occupiedSeats.add(seatKey);
            }
        }
        return occupiedSeats;
    }


    /**
     * Gets the number of occupied seats in this table, iterating through the seats HashMap.
     *
     * @return the number of occupied seats in this table.
     */
    public int getNumberOccupiedSeats(){
        int total = 0;
        for (int key: seats.keySet()){
            if(isSeatOccupied(key)){
                total++;
            }
        }
        return total;
    }


    /**
     * Tells whether a  particular seat is occupied or not.
     *
     * @param seatNum the seat the caller wants to check.
     * @return whether the particular seat is occupied.
     */
    private boolean isSeatOccupied(int seatNum){
        return seats.get(seatNum);
    }

    /**
     * Makes a particular seat occupied or free.
     * @param seatNum the seat we need to modify the state.
     * @param occupy whether we want to occupy (true) or free it (false).
     */
    private void setSeatOccupancy(int seatNum, boolean occupy){
        seats.put(seatNum, occupy);
    }


    /**
     * Makes this table unoccupied and, as result, also frees all  seats in it.
     *
     */
    private void freeTable(){
        occupied = false;
        for (int seatKey: seats.keySet()){
            setSeatOccupancy(seatKey, false);
        }
    }


    /**
     * Return true if this table is occupied; false otherwise.
     *
     * @return whether the table is occupied.
     */
    public boolean isOccupied() {
        return occupied;
    }


    /**
     * This method is used when another class needs to occupy a particular seat and, as result, the current table
     * will also be occupied.
     *
     * @param seatNumber the seat the caller needs to occupy.
     */
    public void makeSeatOccupied(int seatNumber){
        this.occupied = true;
        setSeatOccupancy(seatNumber, true);
    }

    /**
     * Getter for table number.
     *
     * @return the id of this table.
     */
    public int getId() {
        return id;
    }

    /**
     * Getter for order details.
     *
     * @return the order that this table holds.
     */
    public Order getOrder() {
        return null;
        //return orderDetails;
    }

    /**
     * This method gathers the order and generates the bill.
     *
     * @param splitting whether the caller wants to split the bill or not.
     */
    public void askBill(boolean splitting) {
        // Gets all the orders for this table.
        ArrayList<Order> orders;
        // Variable to store all bills.
        ArrayList<Bill> bills = new ArrayList<>();
        boolean giveGratuity;
        final int GRATUITY_CRITERIA = 8;

        //The table should only give gratuity when there are more than 8 people sitting on it.
        giveGratuity = (this.getNumberOccupiedSeats() >= GRATUITY_CRITERIA);

        if (splitting){
            for(int seatNum = 1; seatNum <= numberSeats; seatNum++){
                // First check whether the seat is occupied.
                if (seats.get(seatNum)){
                    String seatAddress = this.toString() + " Seat " + seatNum;
                    orders = OrderTracker.getInstance().getAllWithLocation(seatAddress);
                    bills.add(new Bill(orders, seatAddress, giveGratuity));
                }
            }
        }else {
            // When we choose not to split the bill, the bills list will have only one bill, naturally.
            orders = OrderTracker.getInstance().getAllWithLocation(id);
            bills.add(new Bill(orders, this.toString(), giveGratuity));
        }
        // The receipt(s) will be printed.
        for(Bill bill: bills){
            bill.printToFile();
        }
        // Client will leave the table
        freeTable();
    }


    /**
     * The default printing style for a table. Does not contain any information except id.
     *
     * @return the corresponding String for a particular table.
     */
    public String toString(){
        return "Table " + id;
    }


}
