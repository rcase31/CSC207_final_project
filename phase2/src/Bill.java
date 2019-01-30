
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * The Bill class is instantiated when a costumer is leaving the table, and calculates the total amount due.
 *
 * @author Rafaell Casella
 * @version 0.5
 */
public class Bill {

    private String date;
    private String customerId;
    private ArrayList<Order> ordersDetails;
    private boolean hasGratuity;
    // The total amount to be paid, including taxes
    private double totalAmount = 0;
    // Ontario HST for meals.
    private final double TAX = 0.13;
    // Gratuity (tip) percentage fixed at 15%
    private final double GRATUITY = 0.15;
    private final double ADD_ON_PRICE = 1.50;
    // Using the constant from order status for when an item is delivered. Check Order class for further reference.
    private final int ORDER_STATUS_DELIVERED = 4;


    /**
     * Constructor for the Bill method. Creating a bill requires to have the orderDetails, and this promptly calculates
     * the amount to be paid (including taxes).
     *
     * @param ordersDetails all the orders made by the server and associated with the caller table.
     * @param customerIdentifier the identification of the client: could be seat or table.
     */
    public Bill(ArrayList<Order> ordersDetails, String customerIdentifier, boolean gratuity) {
        // This name will appear on the top of the bill and as the name of the file; it will tell the table/seat number.
        customerId = customerIdentifier;
        this.ordersDetails = ordersDetails;
        hasGratuity = gratuity;
        // Creates the date with the current time on the system.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_hh mm_");
        Date date = new Date();
        this.date = dateFormat.format(date);
        // Calculating the total amount for this bill
        for (Order order: ordersDetails){
            if (order.getStatus(ORDER_STATUS_DELIVERED)){
                MenuItem foodItem = order.getFoodItem();
                totalAmount += foodItem.getPrice();
                for (int count = 0; count < foodItem.getAddOns().size(); count++){
                    totalAmount += ADD_ON_PRICE;
                }
            }
        }
        addTaxes();
        addGratuity();
    }


    /**
     * Includes taxes to the total amount to be paid.
     */
    private void addTaxes() {
        totalAmount *= (1 + TAX);
    }


    /**
     * Includes GRATUITY (tips) to the total amount to be paid, if this parameter was passed by the caller.
     */
    private void addGratuity() {
        if (hasGratuity){
            totalAmount *= 1 + GRATUITY;
        }
    }


    /**
     * Creates a String out of any instantiation of this class, which has a receipt format.
     *
     * @return the receipt for this bill, listing each item ordered, the total without and with taxes at the end.
     */
    @Override
    public String toString() {
        // All the helper constants related to the bill text format.
        final String HORIZONTAL_BARR = "===============================";
        final String LINE_BREAK = "\r\n";
        final String ADDON_PREFIX = "  + ";

        // This is the total before TAX and GRATUITY
        double totalBeforeTax = totalAmount / (1 + TAX);
        // This the total after TAX and before GRATUITY
        double totalAfterTax = totalAmount;
        double tipValue = 0;
        if (hasGratuity){
            totalBeforeTax /= (1 + GRATUITY);
            // This is the total before gratuity is added, but after taxes.
            totalAfterTax /= (1 + GRATUITY);
            tipValue = totalAfterTax * GRATUITY;
        }
        double taxValue = totalBeforeTax * TAX;

        // On the section we start building the actual text of this bill.
        StringBuilder receipt = new StringBuilder();
        //Building the receipt - adding date followed by a horizontal line.
        receipt.append(date).append(LINE_BREAK);
        receipt.append(HORIZONTAL_BARR + LINE_BREAK);
        // Adding the costumer identifier.
        receipt.append(customerId).append(LINE_BREAK);
        receipt.append(HORIZONTAL_BARR + LINE_BREAK);
        // listing each element from the order.
        for (Order order: ordersDetails){
            MenuItem item = order.getFoodItem();
            if (order.getStatus(ORDER_STATUS_DELIVERED)) {
                receipt.append(printFormattedLine(item.getName(), item.getPrice()));
                for (String addOn:  item.getAddOns()){
                    receipt.append(printFormattedLine(ADDON_PREFIX + addOn, ADD_ON_PRICE));
                }
            }
        }
        // Since we finished listing each element of the menu, we draw a horizontal barr.
        receipt.append(HORIZONTAL_BARR + LINE_BREAK);
        // and list the total without taxes before the amount including taxes
        // SubTotal
        receipt.append(printFormattedLine("Subtotal", totalBeforeTax));
        // Taxes
        receipt.append(printFormattedLine("HST", taxValue));
        // Total
        receipt.append(printFormattedLine("Subtotal + HST", totalAfterTax));
        // An extra horizontal line just for the tip
        receipt.append(HORIZONTAL_BARR + LINE_BREAK);
        // Lists the tip to be paid
        receipt.append(printFormattedLine("Gratuity", tipValue));
        // Lists the final value to be paid
        receipt.append(printFormattedLine("Total", totalAmount));

        // Returns the final string
        return receipt.toString();
    }


    /**
     * Helper method for the toString() method. Formats each line associated with a value to the proper format.
     *
     * @param text  the name to be listed (e.g. "Total", "Coke", etc).
     * @param value the price or amount associated with that line.
     * @return the line to be printed on the receipt format.
     */
    private String printFormattedLine(String text, double value) {
        final String BIG_SPACING = "                               ";
        final String LINE_BREAK = "\r\n";
        final int LEFT_OFFSET = 10;

        DecimalFormat df = new DecimalFormat("'$'0.00");  // As cents are shown with 2 decimal places.
        StringBuilder output = new StringBuilder();

        output.append(text).append(BIG_SPACING);
        output.setLength(BIG_SPACING.length() - LEFT_OFFSET);
        output.append(df.format(value)).append(LINE_BREAK);

        return output.toString();
    }


    /**
     * This method prints this particular bill using the toString() method and calls the registry of the amount paid.
     */
    public void printToFile(){
        final String BILLS_FOLDER = "Bills/";

        FileManager fileManager = new FileManager();
        String fileName = BILLS_FOLDER;
        fileName += date;
        fileName += customerId;
        fileName += ".txt";
        fileManager.writeToText(fileName, this.toString());
        registerPayment();
    }

    /**
     * In order to keep track of all payments done during a particular day, this method registers the amount paid by
     * each table, with a timestamp and the identification of the costumer (table, and id, if the bill was split).
     *
     */
    private void registerPayment(){
        FileManager fileManager = new FileManager();
        final String LINE_BREAK = "\r\n";
        final String TAB = "\t";
        final String PAYMENT_FOLDER = "Payments/";
        DecimalFormat df = new DecimalFormat("'$'0.00");  // As cents are shown with 2 decimal places.

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String fileName = PAYMENT_FOLDER + dateFormat.format(new Date()) + ".txt";
        String toBeWritten = LINE_BREAK + customerId + TAB + date + TAB +  df.format(totalAmount);
        fileManager.writeToText(fileName, toBeWritten);
    }

}