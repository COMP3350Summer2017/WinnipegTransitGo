package comp3350.WinnipegTransitGo.CustomExceptions;

/**
 * Created by habib on 7/14/2017.
 */

public class TransitLimitError extends Exception {

        public TransitLimitError(String message)
        {
            super(message);
        }

}
