import java.io.*;
import java.util.ArrayList;

/**
 * The FileManager class. It is responsible for writing and reading from text files.
 *
 * @author parthparmar
 * @author JedAhn
 */
public class FileManager {


    /**
     * Reads from the specific file and returns all the information in an ArrayList
     * @param fileName file to read from
     * @return an ArrayList made up of String
     */
    public ArrayList<String> readFromText(String fileName) {
        ArrayList<String> allItems = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while (!(line = bufferedReader.readLine()).equals("THE END")) {
                if (!line.isEmpty()) {
                    allItems.add(line);
                }
            }
            bufferedReader.close();  // Files should always be closed at the end.
            return allItems;
        } catch (FileNotFoundException f) {
            System.out.println("Unable to find the file: " + fileName);
        } catch (IOException f) {
            f.printStackTrace();
        }

        return null;
    }


    /**
     * A generic method for writing into a .txt file.
     *
     * @param fileName    A String representing the name of the file to write into.
     * @param lineToWrite A String representing a line to write into the file.
     */
    public void writeToText(String fileName, String lineToWrite) {
        File requests = new File(fileName);

        // Step 1: Check if file exists. If not, create the file.
        try {
            // File will get created if file did not exist.
            // If file already exists, existing file will be used.
            boolean fileCreated = requests.createNewFile();

        } catch (IOException e) {
            e.printStackTrace();
        }


        // Step 2: Write into the file.
        try {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.append(lineToWrite);

            bufferedWriter.close();  // Files should always be closed at the end.
        } catch (FileNotFoundException f) {
            System.out.println("Unable to find the file: " + fileName);
        } catch (IOException f) {
            f.printStackTrace();
        }
    }

}
