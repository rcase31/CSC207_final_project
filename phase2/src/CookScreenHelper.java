import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;
import java.util.ArrayList;

/**
 * The CookScreenHelper class. It helps CookScreenController class in terms of
 * performing its functionality.
 *
 * @author parthparmar
 */

public class CookScreenHelper {

    @FXML
    private ObservableList<String> firstList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<String> secondList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<String> thirdList = FXCollections.observableArrayList();

    private ArrayList<Order> takenOrders;
    private ArrayList<Order> receivedOrders;
    private ArrayList<Order> preparedOrders;

    private OrderTracker orderTracker = OrderTracker.getInstance();

    /**
     * Returns true if the item was selected from the given ListView.
     * @param listView ListView to perform the check on.
     * @return true if something was selected from this ListView
     */
    public boolean isSelectedFrom(ListView listView) {
        return listView.getSelectionModel().getSelectedItem() != null;
    }

    /**
     * Returns selected item from the given ListView
     * @param listView ListView to return item from
     * @return selected item
     */
    public int getSelectedItem(ListView listView) {
        return listView.getSelectionModel().getSelectedIndex();
    }

    /**
     * Clears the previous selection from the given ListView
     * @param listView ListView to clear selection from
     */
    public void clearSelection(ListView listView) {
        listView.getSelectionModel().clearSelection();
    }

    /**
     * Updates all the observable lists based on the table number and order status
     * @param tableNum table number to get orders from
     */
    public void updateLists(int tableNum) {


        firstList.clear();
        secondList.clear();
        thirdList.clear();

        takenOrders = orderTracker.getAllNotReturnedWith(tableNum, 1);
        receivedOrders = orderTracker.getAllNotReturnedWith(tableNum, 2);
        preparedOrders = orderTracker.getAllNotReturnedWith(tableNum, 3);

        for (Order item : takenOrders) {
            firstList.add(item.getDetails());
        }

        for (Order item : receivedOrders) {
            secondList.add(item.getDetails());
        }

        for (Order item : preparedOrders) {

            thirdList.add(item.getDetails());

        }


    }

    /**
     * Gets all the returned orders of the restaurant
     */
    public void getReturnedOrders() {
        firstList.clear();
        secondList.clear();
        thirdList.clear();
        takenOrders = orderTracker.getAllReturned(1);
        receivedOrders = orderTracker.getAllReturned(2);
        preparedOrders = orderTracker.getAllReturned(3);
        for (Order item : takenOrders) {
            String detail = item.getDetails() + item.getReasonForReturn();
            firstList.add(detail);
        }
        for (Order item : receivedOrders) {
            secondList.add(item.getDetails());
        }

        for (Order item : preparedOrders) {

            thirdList.add(item.getDetails());

        }

    }

    /**
     * Returns firstList which is related to the firstListView
     * @return firstList
     */
    public ObservableList<String> getFirstList() {
        return firstList;
    }

    /**
     * Returns secondList which is related to the secondListView
     * @return secondList
     */
    public ObservableList<String> getSecondList() {
        return secondList;
    }

    /**
     * Returns thirdList which is related to the thirdListView
     * @return thirdList
     */
    public ObservableList<String> getThirdList() {
        return thirdList;
    }

    /**
     * Gets the number of  currently selected tab
     * @param tabPane tab pane on the screen
     * @return number of the selected tab
     */
    public int getCurrentTab(TabPane tabPane) {
        return tabPane.getSelectionModel().getSelectedIndex();
    }

    /**
     * Getter for the preparedOrders
     * @return preparedOrders
     */
    public ArrayList<Order> getPreparedOrders() {
        return preparedOrders;
    }

    /**
     * Getter for the receivedOrders
     * @return receivedOrders
     */
    public ArrayList<Order> getReceivedOrders() {
        return receivedOrders;
    }

    /**
     * Getter for the takenOrders
     * @return takenOrders
     */
    public ArrayList<Order> getTakenOrders() {
        return takenOrders;
    }
}
