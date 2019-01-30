import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.StringJoiner;
import java.util.ArrayList;
import javafx.scene.Parent;
import javafx.scene.Scene;

/**
 * This class sets up the functionality of the Order tab in the ServerScreen. It is meant to be a part of a
 * higher controller.
 *
 * @author Thomas Campbell
 */
public class ServerOrderController {
    private TreeView<String> menuTree;
    private ComboBox<String> additionsComboBox;
    private ComboBox<String> subtractionsComboBox;
    private Label warningLabel;
    private Label currentlySelectedTableLabel;
    private ListView<String> additions;
    private ListView<String> subtractions;
    private ArrayList<String> toppings;

    /**
     * Construct a ServerOrderController. This will set up serverScreen to have the ability to take orders.
     *
     * @param menuTree the TreeView that the menu will be displayed on.
     * @param warningLabel a label that displays what went wrong if there was a mistake in the ordering process
     * @param currentlySelectedTable a label displaying the currently selected table
     */
    ServerOrderController(TreeView<String> menuTree,
                                 Label warningLabel,
                                 Label currentlySelectedTable){
        this.menuTree = menuTree;
        this.warningLabel = warningLabel;
        this.currentlySelectedTableLabel = currentlySelectedTable;

        this.currentlySelectedTableLabel.setText("None");
        initializeMenu();
    }

    /**
     * Sets up the up the functionality for adding and subtracting ingredients.
     *
     * @param additionsComboBox comboBox displaying the ingredients that can be added
     * @param subtractionsComboBox comboBox displaying the ingredients that can be removed
     * @param additions the list of ingredients being added
     * @param subtractions the list of ingredients being removed
     */
    public void setAdditionsSubtractions(ComboBox<String> additionsComboBox,
                                         ComboBox<String> subtractionsComboBox,
                                         ListView<String> additions,
                                         ListView<String> subtractions){
        this.additionsComboBox = additionsComboBox;
        this.subtractionsComboBox = subtractionsComboBox;
        this.additions = additions;
        this.subtractions = subtractions;
        initializeAddSubtract();
    }

    /**
     * Use information from menu class to create the menu treeView and its associated tree
     * that the user can select menuItems from.
     */
    private void initializeMenu(){

        Menu menu = Menu.getMenu();
        TreeItem<String> root, mains, sides, drinks;
        root = new TreeItem<>();

        // Set up the mains
        ArrayList<String> mainNames = menu.getAllMains();
        mains = makeBranch("Main", root);
        makeBranches(mainNames, mains);

        // Set up the sides
        ArrayList<String> sidesNames = menu.getAllSides();
        sides = makeBranch("Sides", root);
        makeBranches(sidesNames, sides);

        //Set up the drinks
        ArrayList<String> drinksNames = menu.getAllDrinks();
        drinks = makeBranch("Drinks", root);
        makeBranches(drinksNames, drinks);

        menuTree.setRoot(root);
    }

    /**
     * Add multiple branches to a parent treeItem with String values. Will create a branch for each string
     * in the ArrayList of strings.
     *
     * @param list the list of Strings to be added to the tree.
     * @param parent the parent of the branches being made.
     */
    private void makeBranches(ArrayList<String> list, TreeItem<String> parent){
        for (String name: list){
            makeBranch(name, parent);
        }
    }

    /**
     * Makes a TreeItem<String>  with the given string value and parent.
     *
     * @param value the value the new treeItem will have.
     * @param parent the parent TreeItem to the new TreeItem
     * @return a TreeItem<String> with the parent and value that were passed as parameters
     */
    private TreeItem<String> makeBranch(String value, TreeItem<String> parent) {
        TreeItem<String> item = new TreeItem<>(value);
        item.setExpanded(true);
        parent.getChildren().add(item);
        return item;
    }

    /**
     * initialize the additionsComboBox and subtractionsComboBox comboBoxes using the ingredient information
     * from inventory.
     */
    private void initializeAddSubtract(){
        FileManager fileManager = new FileManager();
        this.toppings = fileManager.readFromText("AddOn.txt");

        menuTree.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue)->
                changeAddSubtractionOptions(newValue));
    }

    /**
     * Change the ingredients that show in the subtraction box when a new item is selected on the menu.
     *
     * @param newSelection the new selection on the menu.
     */
    private void changeAddSubtractionOptions(TreeItem<String> newSelection){
        subtractions.getItems().clear();
        additions.getItems().clear();
        subtractionsComboBox.getItems().clear();
        additionsComboBox.getItems().clear();

        // Find out what type of menuItem it is by looking at the parent
        String menuItemType = newSelection.getParent().getValue();
        MenuItem selectedMenuItem;

        // Check that the selection is a meal and not a type and check that its a main
        if (newSelection.isLeaf() && menuItemType.equals("Main")){
            // Add toppings to Additions box
            additionsComboBox.getItems().addAll(toppings);
            // Create a menuItem of the given type and and add the ingredients used in it to the ComboBox
            selectedMenuItem = Menu.getMenu().getMain(newSelection.getValue());
            Map<String, Double> ingredientsNeeded = selectedMenuItem.getIngredientsUsed();
            for (Map.Entry<String, Double> ingredient : ingredientsNeeded.entrySet()) {
                // Check that its in toppings so can be subtracted, (ex. you can subtract Ham but not Dough)
                if (toppings.contains(ingredient.getKey())) {
                    subtractionsComboBox.getItems().add(ingredient.getKey());
                }
            }
        }
    }

    /**
     * Submit an order into the system. This method joins all the order information gathered from the GUI
     * into a string that the Server class takeOrder() method will recognize and then calls that method.
     *
     * @param server the server that will process this order
     */
    public void submitOrder(Server server){
        StringJoiner orderDetails = new StringJoiner("|");
        // Get the selected order from tree
        TreeItem<String> menuItem = menuTree.getSelectionModel().getSelectedItem();
        // Check that the input is appropriate
        if (menuItem == null || !menuItem.isLeaf()){
            warningLabel.setText("Please Select a menu item not a type.");
            return;
        }
        String MenuItemType = menuItem.getParent().getValue();
        String MenuItemName = menuItem.getValue();
        orderDetails.add(MenuItemType + "-" + MenuItemName);

        if (MenuItemType.equals("Main")) {
            // Add the additions to Order
            ObservableList<String> additionItems = additions.getItems();
            if (additionItems.size() > 0) {
                orderDetails.add("Add-" + joinWithComa(additionItems));
            }
            // Add the subtractions to order
            ObservableList<String> subtractionItems = subtractions.getItems();
            if (subtractionItems.size() > 0) {
                orderDetails.add("Subtract-" + joinWithComa(subtractionItems));
            }
        }

        // check that server isn't null
        if (server == null){return;}

        String orderProblem = server.takeOrder(orderDetails.toString(), currentlySelectedTableLabel.getText());
        warningLabel.setText(orderProblem);

        // If the order is successful then remove items in the additions and subtractions ListViews
        if (orderProblem == null || orderProblem.equals("")) {
            additions.getItems().clear();
            subtractions.getItems().clear();
        }
    }

    /**
     * Join all Strings in an Observable list with a joiner String between them.
     *
     * @param list the Observable list to join.
     * @return All strings joined together with joiner between each string.
     */
    private String joinWithComa(ObservableList<String> list){
        StringJoiner allItems = new StringJoiner(",");
        for (String item: list){
            allItems.add(item);
        }
        return allItems.toString();
    }

    /**
     * Adds the ingredient selected in the subtractionsComboBox ComboBox to the list of ingredients to subtract
     * from the order.
     *
     * @param itemToAdd the item being added to subtractions.
     */
    public void addIngredientToSubtractions(String itemToAdd){
        addIngredientToList(subtractions, itemToAdd);
    }

    /**
     * Adds the ingredient selected in the additionsComboBox ComboBox to the list of ingredients to add to
     * the order.
     *
     * @param itemToAdd the item being added to additions.
     */
    public void addIngredientToAdditions(String itemToAdd){
        addIngredientToList(additions, itemToAdd);
    }

    /**
     * If listAddition is not null it adds that item to the given list.
     *
     * @param list the list to add an item to.
     * @param listAddition the item you want to add to that list.
     */
    private void addIngredientToList(ListView<String> list, String listAddition){
        if (listAddition == null){return;}
        list.getItems().add(listAddition);
    }

    /**
     * Loads the TableMap so that the server can select the seat where the current table belongs.
     *
     * @return a string that represents the chosen table and seat in the format "Table 1 Seat 1".
     * @throws IOException throws when loader fails to load the fxml file TableMap.
     */
    public String getTableFromMap() throws IOException{

        // Launch the Table Map.
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TableMap.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Seat selection");
        stage.initModality(Modality.APPLICATION_MODAL);

        // Pass the table map controller a string builder to put the information about which table is selected into.
        TableMapController tableMapController = loader.getController();
        StringBuilder tableSelected = new StringBuilder();
        tableMapController.setSelection(tableSelected);
        stage.showAndWait();

        String[] selectedInfo = tableSelected.toString().split("_");
        // Check that the output is in the right form
        if (selectedInfo.length == 3) {
            String tableInfo = "Table " + selectedInfo[1];
            currentlySelectedTableLabel.setText(tableInfo + " Seat " + selectedInfo[2]);
            return tableInfo;
        } else {
            return "None";
        }
    }
}
