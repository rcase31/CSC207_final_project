/**
 * The Observer Interface. Implementing this interface will allow a class to add itself to an Observable objects list of
 * observers so that the class will be updated when something changes in Observable.
 */
public interface Observer {
    /**
     * The update function that the Observable class will call when something has changed.
     */
    void update();
}
