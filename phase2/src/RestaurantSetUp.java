

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * The RestaurantSetUp class.It stores lists of all the servers and cooks working
 * at this Restaurant. Also, it ensures that all the required config. files are present
 * when the system starts.
 *
 * @author parthparmar
 *
 */

public class RestaurantSetUp {


    private ArrayList<Cook> allcooks;
    private ArrayList<Server> allServers;
    private ArrayList<String> configFiles = new ArrayList<>(Arrays.asList("cooklist.txt","drinks.txt",
            "ingredients.txt","mainDishes.txt","managerlist.txt","menudetails.txt","serverlist.txt",
            "sideDishes.txt","tablelist.txt"));




    private Manager manager;

    private static RestaurantSetUp restaurantSetUp = new RestaurantSetUp();

    private RestaurantSetUp(){

        if(filesExist()) {
            WorkerFactory workerFactory = new WorkerFactory();
            allcooks = workerFactory.getAllCooks();
            allServers = workerFactory.getAllServers();
            manager = workerFactory.getAllManagers().get(0);

        }

        else{
            System.exit(1);
        }

    }

    public ArrayList<Cook> getAllCooks(){
        return this.allcooks;
    }

    public ArrayList<Server> getAllServers(){
        return this.allServers;
    }

    public static RestaurantSetUp getRestaurantSetUp(){
        return restaurantSetUp;
    }

    public Manager getManager(){
        return this.manager;
    }
    /**
     * Checkes if all the required files are present in the phase1 folder
     *
     * @return true if all the required files exist in the folder
     */
    private  boolean filesExist() {

        File file;

        for(String fileName:this.configFiles){
            file = new File(fileName);
            if(!file.exists()){
                System.err.println("Missing config file:"+fileName);
                return false;
            }

            }
        return true;

        }






}




