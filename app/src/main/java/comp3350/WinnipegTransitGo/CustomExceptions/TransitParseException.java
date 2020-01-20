package comp3350.WinnipegTransitGo.CustomExceptions;

/**
 * Created by habib on 7/14/2017.
 */
//This exception is thrown when there is some parsing error in transit api's response
//it is meant to be used between business and presentation

public class TransitParseException extends Exception {

    public TransitParseException(String message)
    {
        super(message);
    }

}