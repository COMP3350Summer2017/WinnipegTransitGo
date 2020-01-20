package comp3350.WinnipegTransitGo.CustomExceptions;

/**
 * Created by habib on 7/14/2017.
 */
//This exception is thrown when there is no interent
//it is meant to be used between business and presentation
public class TransitNoConnectionException extends Exception {

    public TransitNoConnectionException(String message)
    {
        super(message);
    }

}