package comp3350.WinnipegTransitGo.presentation;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.CustomExceptions.TransitLimitError;
import comp3350.WinnipegTransitGo.CustomExceptions.TransitNoConnectionException;
import comp3350.WinnipegTransitGo.CustomExceptions.TransitParseException;
import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Created by habib on 7/14/2017.
 */

public class busesCallMaker extends AsyncTask<Integer, Void, List<TransitListItem>> {

    MainActivity mainActivity;
    TransitListPopulator transitListGenerator;
    BusStopApiData busStop;

    boolean exception = false;
    String exceptionMessage = "";

    public busesCallMaker(MainActivity mainActivity, TransitListPopulator transitListGenerator, BusStopApiData busStop)
    {
        this.mainActivity = mainActivity;
        this.transitListGenerator = transitListGenerator;
        this.busStop = busStop;
    }

    @Override
    protected List<TransitListItem> doInBackground(Integer... params) {

        List<TransitListItem> result = new ArrayList<>();

        try
        {
            result = transitListGenerator.getBusesOnABusStop(busStop);
        }
        catch (TransitLimitError e)
        {
            exception = true;
            exceptionMessage = mainActivity.getString(R.string.Transit_Limit_Error);
        }
        catch (TransitNoConnectionException e)
        {
            exception = true;
            exceptionMessage = mainActivity.getString(R.string.Transit_Connection_Error);
        }
        catch (TransitParseException e)
        {
            exception = true;
            exceptionMessage = mainActivity.getString(R.string.Transit_Parse_Error);
        }
        catch (Exception e)
        {
            exception = true;
            exceptionMessage = "Something went wrong while getting buses";
        }

        return result;
    }

    @Override
    protected void onPostExecute( List<TransitListItem> itemList)
    {
        //call the main activity
        mainActivity.updateListView(itemList, exception, exceptionMessage);
    }
}
