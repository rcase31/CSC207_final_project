import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The LogManager class. LogManager creates a common FileHandler object
 * for all classes to use, so everything will be written into a single
 * log.txt file.
 *
 * @author Jedid Ahn
 * @version 0.2
 */


public class LogManager {

    private static FileHandler fileHandler;
    private Logger logger;


    /**
     * The LogManager constructor. It will create a static and common
     * fileHandler that will specifically write into log.txt, and a
     * logger instance will add the fileHandler.
     *
     * @param className The name of the class that will use the logger.
     */
    LogManager(String className){
        logger = Logger.getLogger(className);
        if (fileHandler == null){
            try{
                fileHandler = new FileHandler("log.txt");
                fileHandler.setFormatter(new SimpleFormatter());
                logger.addHandler(fileHandler);
                logger.setLevel(Level.ALL);
            } catch (java.io.IOException ex){
                System.out.println(ex.getMessage());
            }
        }
    }


    /**
     * Return the common FileHandler object associated with log.txt.
     *
     * @return FileHandler
     */
    public static FileHandler getFileHandler() {
        return fileHandler;
    }



    /**
     * Return the Logger object associated with log.txt.
     *
     * @return Logger
     */
    public Logger getLogger() {
        return this.logger;
    }
}
