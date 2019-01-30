
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * The LogInScreenController class. It checks the login password and lets the user
 * log in only if he exists in the system. Also, it navigates to next screen depending
 * on who logs into the system.
 *
 * @author parthparmar
 */
public class LogInScreenController {


    //Instance of this restaurant
    private RestaurantSetUp newRestaurantSetUp = RestaurantSetUp.getRestaurantSetUp();

    //List of cooks
    private ArrayList<Cook> cooks = newRestaurantSetUp.getAllCooks();

    //List of Servers
    private ArrayList<Server> servers = newRestaurantSetUp.getAllServers();

    //Manager of this restaurant
    private Manager manager = newRestaurantSetUp.getManager();

    //Cook who is currently logged in
    private Cook currentCook;

    //Server who is currently logged in
    private Server currentServer;

    //Manager who is currently logged in
    private Manager currentManager;

    private String type;


    @FXML
    private Label messageLabel;

    @FXML
    private PasswordField passwordField;

    /**
     * Initializes this controller class.
     */
    @FXML
    private void initialize() {

        messageLabel.setVisible(false);

    }


    /**
     * Verifies the password entered by the user.
     * @throws IOException if loader can not load the new screen.
     */
    public void verifyLogin() throws IOException {

        clearError();
        type = null;

        if (checkPassword(passwordField.getText()) && type.equals("Cook")) {
            setUpNewScreen( "CookScreen.fxml", this.currentCook);

        } else if (checkPassword(passwordField.getText()) && type.equals("Server")) {
            setUpNewScreen("ServerScreen.fxml", this.currentServer);

        } else if (checkPassword(passwordField.getText()) && type.equals("Manager")) {
            setUpNewScreen( "ManagerScreen.fxml", this.currentManager);

        } else if (!checkPassword(passwordField.getText()) || type == null) {
            setError("Error:Invalid Password!");

        }

    }

    /**
     * Checkes if the inputted password matches any password in the system.
     * @param password string inputted by the user
     * @return true if the password is correct
     */
    private boolean checkPassword(String password) {
        for (Cook cook : cooks) {
            if (cook.getPassword().equals(password)) {
                this.currentCook = cook;
                type = "Cook";
                return true;
            }
        }

        for (Server server : servers) {
            if (server.getPassword().equals(password)) {
                this.currentServer = server;
                type = "Server";
                return true;
            }
        }
        if (manager.getPassword().equals(password)) {
            this.currentManager = manager;
            type = "Manager";
            return true;
        }

        return false;
    }

    /**
     * Sets up a new screen on the window depending on who logged in.
     * @param file name of the file that the loader needs to load
     * @param person type of the person who is currently logging in
     * @throws IOException if loader fails to load the new screen
     */
    private void setUpNewScreen(String file, Person person) throws IOException {

        passwordField.clear();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(file));
        Parent root = loader.load();
        setPerson(person, loader);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

    }

    /**
     * Sets the parameter person for next window's controller
     * @param person type of person who is logging in
     * @param loader loader object that is going to load the new screen
     */
    private void setPerson(Person person, FXMLLoader loader) {

        switch (person.getCategory()) {
            case "Cook": {
                CookScreenController controller = loader.getController();
                controller.setCook((Cook) person);
                break;
            }
            case "Manager": {
                ManagerScreenController controller = loader.getController();
                controller.setManager((Manager) person);
                break;
            }

            case "Server": {
                ServerScreenController controller = loader.getController();
                controller.setServer((Server) person);
                break;
            }
        }


    }

    /**
     * Sets error label on the screen in case of any error is caused by the user
     * @param error string that needs to be shown on the screen
     */
    private void setError(String error) {
        messageLabel.setText(error);
        messageLabel.setVisible(true);
    }

    /**
     * Clears the error label from the screen.
     */
    private void clearError() {
        messageLabel.setText(null);
        messageLabel.setVisible(false);

    }


}
