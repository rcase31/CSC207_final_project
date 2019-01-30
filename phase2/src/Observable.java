import java.util.ArrayList;

/**
 * Stores a list of observables and updates them when something has been changed.
 *
 * @author Thomas Campbell
 */
public class Observable {
    private ArrayList<Observer> observers = new ArrayList<>();
    private boolean changed;

    /**
     * Add an Observer to the list of observers that should be updated.
     * @param observer the observer to add to the list.
     */
    public void addObserver(Observer observer){
        observers.add(observer);
    }

    /**
     * Remove an observer from the list of observers.
     * @param observerToDelete the observer to delete.
     */
    public void deleteObserver(Observer observerToDelete){
        observers.remove(observerToDelete);
    }

    /**
     * Register that something has changed so the observers should be updated.
     */
    void setChanged(){
        changed = true;
        notifyObservers();
    }

    /**
     * Call the update function of each observer.
     */
    private void notifyObservers() {
        if (changed) {
            for (Observer observer : observers) {
                observer.update();
            }
            changed = false;
        }
    }
}
