/**
 * The Ingredient class. An ingredient object is any ingredient required
 * to make a menu item for the restaurant.
 *
 * @author Jedid Ahn
 * @version 1.0
 */

public class Ingredient {

    private String name;
    private double quantity;
    private String quantityWithUnit;
    private double threshold;
    private String unit;

    /**
     * The Ingredient constructor. Each ingredient has a name, a quantity,
     * a quantity with a unit, a threshold, and a unit.
     *
     * @param name      a String containing the name of the ingredient.
     * @param quantity  a double value containing the quantity of the ingredient.
     * @param threshold a double value containing the threshold in which the ingredient
     *                  must be reordered.
     * @param unit      a String containing the unit of the ingredient.
     */
    Ingredient(String name, double quantity, double threshold, String unit) {
        this.name = name;
        this.quantity = quantity;
        this.threshold = threshold;
        this.unit = unit;
        this.setQuantityWithUnit();
    }


    /**
     * Return the name of this ingredient.
     *
     * @return String
     */
    public String getName() {
        return this.name;
    }


    /**
     * Return the quantity of this ingredient.
     *
     * @return double
     */
    public double getQuantity() {
        return this.quantity;
    }


    /**
     * Set the new quantity of this ingredient after it has been used or reordered.
     *
     * @param quantity a double value containing the new quantity of the ingredient.
     */
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }


    /**
     * Return the quantity and unit of this ingredient as a combined String.
     *
     * @return String
     */
    public String getQuantityWithUnit() {
        return this.quantityWithUnit;
    }


    /**
     * Set the new quantity and unit of this ingredient as a combined String.
     *
     * If the quantity is a whole number, type cast the quantity to an integer.
     * Otherwise, leave the quantity as a double.
     *
     */
    public void setQuantityWithUnit(){
        if (this.unit.equals("packs") || this.unit.equals("cans") || this.unit.equals("bottles")){
            this.quantityWithUnit = ((int)this.quantity) + " " + this.unit;
        }
        else{
            this.quantityWithUnit = this.quantity + " " + this.unit;
        }

    }


    /**
     * Return the threshold of this ingredient.
     *
     * @return double
     */
    public double getThreshold() {
        return this.threshold;
    }


    /**
     * Return the unit of this ingredient.
     *
     * @return String
     */
    public String getUnit() {
        return this.unit;
    }
}