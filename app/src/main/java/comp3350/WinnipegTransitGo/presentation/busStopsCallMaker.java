package comp3350.WinnipegTransitGo.presentation;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.CustomExceptions.TransitLimitError;
import comp3350.WinnipegTransitGo.CustomExceptions.TransitNoConnectionException;
import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;

/**
 * Created by habib on 7/14/2017.
 */

public class busStopsCallMaker extends AsyncTask<Integer, Void, List<BusStopApiData>> {

    private MainActivity mainActivity;
    private TransitListPopulator transitListGenerator;
    private String latitude;
    private String longitude;

    private boolean exception = false;
    private String exceptionMessage = "";

    public busStopsCallMaker(MainActivity mainActivity, TransitListPopulator transitListGenerator, String latitude, String longitude)
    {
        this.mainActivity = mainActivity;
        this.transitListGenerator = transitListGenerator;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected List<BusStopApiData> doInBackground(Integer... params) {

        List<BusStopApiData> busStops = new ArrayList<>();
        try
        {
           busStops = transitListGenerator.getBusStops(latitude, longitude);
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
        catch (Exception e)
        {
            exception = true;
            exceptionMessage = "Something went wrong while getting bus stops";
        }

        return busStops;
    }

    @Override
    protected void onPostExecute(List<BusStopApiData> busStops)
    {
        if(!exception && busStops.size() < 1 )
        {
            exception = true;
            exceptionMessage = mainActivity.getString(R.string.Transit_No_Stops);
        }

        //call main activity
        mainActivity.handleBusStops(busStops, exception, exceptionMessage);
    }
}
