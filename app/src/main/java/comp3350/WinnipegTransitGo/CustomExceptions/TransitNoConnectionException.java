package comp3350.WinnipegTransitGo.CustomExceptions;

/**
 * Created by habib on 7/14/2017.
 */

public class TransitNoConnectionException extends Exception {

    public TransitNoConnectionException(String message)
    {
        super(message);
    }

}