import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;

/**
 * Logs events to a file called log.txt. Each event has a level and timestamp.
 *
 * @author Rafaell Casella, Thomas Campbell
 */
public class Logger {

    /**
     * enum describing the logs "Level" i.e. how severe the problem being logged is.
     */
    enum Level{
        SEVERE ("SEVERE: "),
        WARNING ("WARNING: "),
        INFO ("INFO: "),
        FINE ("FINE: "),
        FINDER ("FINER: "),
        FINEST ("FINEST: ");

        private final String text;
        Level(String text){
            this.text = text;
        }
    }

    /**
     * Attempt to empty the log file and log a warning if it fails.
     */
    public void renew(){
        File file = new File("log.txt");
        if(!file.delete()){
            log(Level.WARNING, "File Not Renewed");
        }
    }

    /**
     * Logs information to the file log.txt
     *
     * @param d the level of the log.
     * @param msg message to be logged.
     */
    public void log(Level d, String msg){
        String LOG_FILE = "log.txt";
        final String NEWLINE = "\n";

        FileManager fileManager = new FileManager();

        String toBeWritten = d.text;
        toBeWritten += getTimeStamp() + NEWLINE;
        toBeWritten += msg + NEWLINE;

        fileManager.writeToText(LOG_FILE, toBeWritten);
    }

    /**
     * Returns the current time stamp as a String.
     *
     * @return a String representation of the current timestamp.
     */
    private String getTimeStamp(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");
        return dateFormat.format(new Date());
    }

}
