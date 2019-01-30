import java.util.ArrayList;

/**
 * The Manager class. A Manager can check the inventory.
 *
 * @author Thomas Campbell, Jed Ahn
 */
public class Manager extends Person {
    /**
     * Constructs a Manager object.
     *
     * @param name The name of the Manager.
     * @param id   The employee ID of the Manager.
     * @param password this managers password.
     */
    public Manager(String name, int id, String password) {
        super(name, id, password);
        setCategory("Manager");
    }


    /**
     * Return a list of all ingredients in inventory.
     *
     * @return ArrayList<Ingredient>
     */
    public ArrayList<Ingredient> getIngredients(){
        return inventory.getItems();
    }


    /**
     * Return a specific ingredient in inventory.
     *
     * @param name A String representing the name of the ingredient.
     * @return Ingredient
     */
    public Ingredient getSpecificIngredient(String name){
        return inventory.findIngredient(name);
    }
}