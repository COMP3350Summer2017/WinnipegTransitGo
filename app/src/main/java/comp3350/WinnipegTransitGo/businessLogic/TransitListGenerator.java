package comp3350.WinnipegTransitGo.businessLogic;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.BusRoute;
import comp3350.WinnipegTransitGo.objects.BusRouteSchedule;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.ScheduledStop;
import comp3350.WinnipegTransitGo.objects.Time;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.database.Database;
import comp3350.WinnipegTransitGo.persistence.transitAPI.ApiListenerCallback;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPI;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIProvider;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TransitListGenerator class
 * Makes API calls and wraps them in TransitListItem objects
 * Usage:
 * TransitListGenerator ld=new TransitListGenerator(this);
 * ld.getListOfBusStops(); this makes the api calls
 * On api responses calls the method updateListView
 *
 * @author Nibras Ohin, Syed Habib
 * @version 1.0
 * @since 2017-05-24
 */

public class TransitListGenerator implements TransitListPopulator {

    private Database database;
    private TransitAPIProvider api;
    private ApiListenerCallback apiListener;
    private List<TransitListItem> listItems;
    final private int validData = -1;

    public TransitListGenerator(ApiListenerCallback apiListenerCallback, String apiKey) {
        listItems = new ArrayList<>();
        apiListener = apiListenerCallback;
        api = TransitAPI.getAPI(apiKey);
        database = DatabaseService.getDataAccess();
    }

    public void populateTransitList(String latitude, String longitude) {
        listItems.clear();
        Call<TransitAPIResponse> apiResponse = api.getBusStops(Integer.toString(database.getRadius()), latitude, longitude, true);
        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                if(response.errorBody() == null)
                   processResponseBusStops(response.body().getBusStops());//get all the bus stops
                else
                    apiListener.updateListView(listItems, R.string.Transit_Limit_Error);//tell the listener that something went wrong
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                apiListener.updateListView(listItems, R.string.Transit_Connection_Error);//tell the listener that something went wrong
            }
        });
    }

    private void processResponseBusStops(List<BusStop> nearByBusStops)
    {
        apiListener.updateStopsOnMap(nearByBusStops);
        List<Integer> nearByBusStopNumbers = new ArrayList<>();

        for (int i = 0; i < nearByBusStops.size(); i++) {
            nearByBusStopNumbers.add(nearByBusStops.get(i).getNumber());    //gets the busstop number and adds it to the list
        }//create a list of bus stop numbers

        //for each bus stop get the bus number and there routes
        if(nearByBusStops.size() >0)
            traverseBusStopList(nearByBusStopNumbers, nearByBusStops);
        else //tell apiListener there are no buses
            apiListener.updateListView(listItems, R.string.Transit_No_Stops);//tell the listener that something went wrong
    }

    private void traverseBusStopList(List<Integer> busStopList, List<BusStop> nearByBusStops) {
        for (int i = 0; i < busStopList.size(); i++)
            extractBusInfo(busStopList.get(i), nearByBusStops.get(i).getName(), nearByBusStops.get(i).getWalkingDistance());
    }

    private void extractBusInfo(final int busStopNumber, final String busStopName, final String walkingDistance) {
        Call<TransitAPIResponse> apiResponse = api.getBusStopSchedule(busStopNumber);

        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                if(response.errorBody() == null)
                {
                    processResponseBusStopSchedule(response.body().getBusStopSchedule(), busStopNumber, busStopName, walkingDistance);
                    apiListener.updateListView(listItems, validData);//tell the listener that got more data, update list view
                }
                else
                {
                    apiListener.updateListView(listItems, R.string.Transit_Limit_Error);//tell the listener that something went wrong
                }
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                apiListener.updateListView(listItems, R.string.Transit_Connection_Error);//tell the listener that something went wrong
            }
        });
    }

    private void processResponseBusStopSchedule(BusStopSchedule stopSchedule, final int busStopNumber, final String busStopName, final String walkingDistance)
    {
        int busNumber;
        String destination;
        List<BusRouteSchedule> routeSchedule = stopSchedule.getBusRouteSchedules();

        for (int i = 0; i < routeSchedule.size(); i++) {
            BusRoute route = routeSchedule.get(i).getBusRoute();
            busNumber = route.getNumber();

            //get time and status here
            List<ScheduledStop> scheduledStops = routeSchedule.get(i).getScheduledStops();
            String status = calculateStatus(scheduledStops.get(0));
            destination=(scheduledStops.get(0).getVariant().getName()).toUpperCase();
            List<String> allTiming = parseTime(scheduledStops);

            if(allTiming.get(0).equals("Due"))
            {
                status="";
            }
            insertClosestBus(new TransitListItem(walkingDistance, busNumber, busStopNumber, busStopName, destination, status, allTiming));
        }

        //sort according to the remaining time here
        Collections.sort(listItems, new TransitListItem());
    }


    //insert the bus with the closest stop
    private void insertClosestBus(TransitListItem newItem)
    {
        boolean found = false;
        TransitListItem currItem;
        for(int j=0; j< listItems.size() && !found; j++ )
        {
            currItem = listItems.get(j);
            if(currItem.getBusNumber() == newItem.getBusNumber() && currItem.getBusStopDestination().equals(newItem.getBusStopDestination()))//same bus
            {
                found = true;
                //check which bus is closer (curr stop vs item in list stop)
                if(newItem.getWalkingDistance() < currItem.getWalkingDistance())//if current is closer
                    listItems.set(j, newItem);
            }
        }

        if(!found) //if new bus
        {
            listItems.add(newItem);
        }
    }


    private String calculateStatus(ScheduledStop schedule) {
        Time time = schedule.getTime();

        String scheduledDeparture = time.getScheduledDeparture();
        String estimatedDeparture = time.getEstimatedDeparture();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CANADA);

        String status = "Ok";
        try {
            Date scheduled = sdf.parse(scheduledDeparture);
            Date estimated = sdf.parse(estimatedDeparture);
            long diff = scheduled.getTime() - estimated.getTime();
            long diffMinutes = diff / (60 * 1000);

            if (diffMinutes > 0) //negative means late, positive means early
                status = "Early";
            else if (diffMinutes < 0)
                status = "Late";
        } catch (ParseException e) {
            apiListener.updateListView(listItems, R.string.Transit_Parse_Error);//tell the listener that something went wrong
        }
        return status;
    }

    private long calculateTimeRemaining(ScheduledStop schedule) {
        Time time = schedule.getTime();

        String estimatedDeparture = time.getEstimatedDeparture();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CANADA);

        long timeRemaining = 0;
        try {
            Date estimated = sdf.parse(estimatedDeparture);

            Calendar c = Calendar.getInstance();
            Date currentTime = c.getTime();

            long diff = estimated.getTime() - currentTime.getTime();
            timeRemaining = diff / (60 * 1000); // in minutes
            //negative means early, positive means late
        } catch (ParseException e) {
            apiListener.updateListView(listItems, R.string.Transit_Parse_Error);//tell the listener that something went wrong
        }

        return timeRemaining;
    }

    private List<String> parseTime(List<ScheduledStop> scheduledStops) {
        List<String> ret = new ArrayList<>();

        for (int i = 0; i < scheduledStops.size(); i++) {
            long remainingTime = calculateTimeRemaining(scheduledStops.get(i));
            String stringRT = Long.toString(remainingTime);

            if (remainingTime <= 0)//if the bus is early or due (negative means early)
                stringRT = "Due";
            ret.add(stringRT);
        }
        return ret;
    }

    public boolean isValid(int error)
    {
        return validData == error;
    }
}