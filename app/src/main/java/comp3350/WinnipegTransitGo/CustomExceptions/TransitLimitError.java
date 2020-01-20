package comp3350.WinnipegTransitGo.CustomExceptions;

/**
 * Created by habib on 7/14/2017.
 */
//This exception is thrown when there limit of api calls is reached,
//it is meant to be used between business and presentation
public class TransitLimitError extends Exception {

        public TransitLimitError(String message)
        {
            super(message);
        }

}
