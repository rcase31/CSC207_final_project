import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import java.util.ArrayList;

/**
 * The CookScreenController class. It is responsible for performing all the
 * actions corresponding to the CookScreen. It displays all the orders in the system.
 * It divides all the orders based on table number and order status.
 *
 * @author parthparmar
 */
public class CookScreenController implements Observer {


    private ListView<String> firstListView;
    private ListView<String> secondListView;
    private ListView<String> thirdListView;

    //cook is currently logged in.
    private Cook cook;

    //Order Tracker of this restaurant
    private OrderTracker orderTracker = OrderTracker.getInstance();
    private CookScreenHelper cookScreenHelper = new CookScreenHelper();

    @FXML
    private Label errorLabel;
    @FXML
    private TabPane tablePane;
    @FXML
    private ListView<String> oneReceiveListView;
    @FXML
    private ListView<String> onePrepareListView;
    @FXML
    private ListView<String> oneReadyListView;
    @FXML
    private ListView<String> twoReceiveListView;
    @FXML
    private ListView<String> twoPrepareListView;
    @FXML
    private ListView<String> twoReadyListView;
    @FXML
    private ListView<String> threeReceiveListView;
    @FXML
    private ListView<String> threePrepareListView;
    @FXML
    private ListView<String> threeReadyListView;
    @FXML
    private ListView<String> fourReceiveListView;
    @FXML
    private ListView<String> fourPrepareListView;
    @FXML
    private ListView<String> fourReadyListView;
    @FXML
    private ListView<String> fiveReceiveListView;
    @FXML
    private ListView<String> fivePrepareListView;
    @FXML
    private ListView<String> fiveReadyListView;
    @FXML
    private ListView<String> returnReceiveListView;
    @FXML
    private ListView<String> returnPrepareListView;
    @FXML
    private ListView<String> returnReadyListView;
    @FXML
    private Label personLabel;


    private ObservableList<String> firstList = cookScreenHelper.getFirstList();
    private ObservableList<String> secondList = cookScreenHelper.getSecondList();
    private ObservableList<String> thirdList = cookScreenHelper.getThirdList();

    @FXML
    private void initialize() {

        errorLabel.setVisible(false);
        orderTracker.addObserver(this);

    }

    /**
     * Updates all the lists currently on the screen based on the event
     * this class observes from orderTracker
     *
     */
    public void update() {
        updateTabLists();
    }


    /**
     * LogsOut the user from the system
     * @param actionEvent actionEvent object corresponding to LogOut button
     */
    public void logOut(ActionEvent actionEvent) {

        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
        orderTracker.deleteObserver(this);

    }

    /**
     * Receives orders in the system and updates all the lists accordingly.
     */
    public void receivedPressed() {


        clearErrorLabel();
        getListViews();
        int i;
        if (cookScreenHelper.isSelectedFrom(firstListView)) {
            i = cookScreenHelper.getSelectedItem(firstListView);

            if(i!=0){
                setErrorLabel("Please receive older orders first!");

            }
            else{
                cook.orderReceived(cookScreenHelper.getTakenOrders().get(i));
                updateTabLists();
            }

        } else {
            setErrorLabel("Error : Please select an item from the list above.");
        }

    }


    /**
     * Updates orders once they are made and updates all the lists accordingly.
     */
    public void preparedPressed() {

        clearErrorLabel();
        getListViews();
        int i;
        if (cookScreenHelper.isSelectedFrom(secondListView)) {
            i = cookScreenHelper.getSelectedItem(secondListView);
            if (cookScreenHelper.getCurrentTab(tablePane) == 5) {
                cook.orderFilled(cookScreenHelper.getReceivedOrders().get(i));
            } else {
                cook.orderFilled(cookScreenHelper.getReceivedOrders().get(i));
            }
            updateTabLists();

        } else {
            setErrorLabel("Error : Please select an item from the list above.");
        }
    }

    /**
     * Rests orders to their previous status. Cook can use this in case he changes the status of
     * the order mistakenly.
     *
     */
    public void resetPressed() {
        clearErrorLabel();
        getListViews();
        int i;
        if (cookScreenHelper.isSelectedFrom(secondListView)) {
            i = cookScreenHelper.getSelectedItem(secondListView);
            cook.resetOrder(cookScreenHelper.getReceivedOrders().get(i));
            updateTabLists();
        } else if (cookScreenHelper.isSelectedFrom(thirdListView)) {
            i = cookScreenHelper.getSelectedItem(thirdListView);
            cook.resetOrder(cookScreenHelper.getPreparedOrders().get(i));
            updateTabLists();
        } else if (cookScreenHelper.isSelectedFrom(firstListView)) {
            setErrorLabel("Error : Items in this list can not be reset!");
            cookScreenHelper.clearSelection(firstListView);
        } else {
            setErrorLabel("Error: Please select an item to reset!");
        }

    }

    /**
     * Gets ListViews corresponding to currently open tab on the screen.
     */
    private void getListViews() {
        int i = cookScreenHelper.getCurrentTab(tablePane);
        if (i == 0) {
            firstListView = oneReceiveListView;
            secondListView = onePrepareListView;
            thirdListView = oneReadyListView;
        } else if (i == 1) {
            firstListView = twoReceiveListView;
            secondListView = twoPrepareListView;
            thirdListView = twoReadyListView;
        } else if (i == 2) {
            firstListView = threeReceiveListView;
            secondListView = threePrepareListView;
            thirdListView = threeReadyListView;
        } else if (i == 3) {
            firstListView = fourReceiveListView;
            secondListView = fourPrepareListView;
            thirdListView = fourReadyListView;
        } else if (i == 4) {
            firstListView = fiveReceiveListView;
            secondListView = fivePrepareListView;
            thirdListView = fiveReadyListView;
        } else if (i == 5) {
            firstListView = returnReceiveListView;
            secondListView = returnPrepareListView;
            thirdListView = returnReadyListView;

        }

    }


    /**
     * Updates all the ListViews once there is any change in the order status.
     */
    public void updateTabLists(){

        ArrayList<ListView<String>> currentListViews = getCurrentListViews();

        currentListViews.get(0).setItems(firstList);
        currentListViews.get(1).setItems(secondList);
        currentListViews.get(2).setItems(thirdList);

    }

    /**
     * Returns an ArrayList made up of ListViews corresponding to the currently open
     * tab on the screen.
     * @return an ArrayList made up of ListViews
     */
    private ArrayList<ListView<String>> getCurrentListViews(){
        ArrayList<ListView<String>> lists = new ArrayList<>();
        int i = cookScreenHelper.getCurrentTab(tablePane);
        if(i==0){
            cookScreenHelper.updateLists(1);
            lists.add(oneReceiveListView);
            lists.add(onePrepareListView);
            lists.add(oneReadyListView);

        } else if(i==1) {
            cookScreenHelper.updateLists(2);
            lists.add(twoReceiveListView);
            lists.add(twoPrepareListView);
            lists.add(twoReadyListView);

        } else if(i==2){
            cookScreenHelper.updateLists(3);
            lists.add(threeReceiveListView);
            lists.add(threePrepareListView);
            lists.add(threeReadyListView);

        } else if(i==3){
            cookScreenHelper.updateLists(4);
            lists.add(fourReceiveListView);
            lists.add(fourPrepareListView);
            lists.add(fourReadyListView);

        } else if(i==4){
            cookScreenHelper.updateLists(5);
            lists.add(fiveReceiveListView);
            lists.add(fivePrepareListView);
            lists.add(fiveReadyListView);

        }else if(i==5){
            cookScreenHelper.getReturnedOrders();
            lists.add(returnReceiveListView);
            lists.add(returnPrepareListView);
            lists.add(returnReadyListView);

        }

        return lists;
    }


    /**
     * Sets cook parameter to the currently logged in cook.
     * @param cook cook who is currently logged in
     */
    public void setCook(Cook cook) {
        this.cook = cook;
        personLabel.setText(this.cook.getName());

    }

    /**
     * Sets error on the screen
     * @param msg error message to show on the screen
     */
    private void setErrorLabel(String msg) {

        errorLabel.setText(msg);
        errorLabel.setVisible(true);

    }

    /**
     * Clears the error from the screen
     */
    private void clearErrorLabel() {

        errorLabel.setText(null);
        errorLabel.setVisible(false);

    }


}
