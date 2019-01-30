
/**
 * The Person class. A Person is an employee of the RestaurantSetUp. They have a name and employee ID.
 *
 * @author Thomas Campbell
 * @version 0.2
 */

public class Person {
    String name;
    int id;
    OrderTracker orderTracker = OrderTracker.getInstance();
    private String password;
    private String category;

    Inventory inventory;
    Menu menu;


    /**
     * Constructs a Person object.
     *
     * @param name The name of this Person.
     * @param id   The employee ID of this Person.
     */
    Person(String name, int id, String password) {
        this.name = name;
        this.id = id;
        this.password = password;
        inventory = Inventory.getInventory();
        menu = Menu.getMenu();
    }

    /**
     * Getter for inventory.
     *
     * @return the  restaurants inventory.
     */
    public Inventory getInventory(){
        return inventory;
    }

    /**
     * Getter for name.
     *
     * @return the name of this Person.
     */
    public String getName() {
        return name;
    }

    /**
     * Record what type of person this is (ex. Server, Cook, Manager).
     *
     * @param category A string describing the type of person this is.
     */
    void setCategory(String category) {
        this.category = category;
    }

    /**
     * Getter for this Persons category.
     *
     * @return This Persons category.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get this persons password.
     *
     * @return this persons password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter for this employees number.
     *
     * @return returns the employees number.
     */
    public int getID() {
        return id;
    }

    /**
     * Return string representation of person.
     *
     * @return string representation of person.
     */
    public String toString() {
        return name + " Employee Number: " + id;
    }

}
