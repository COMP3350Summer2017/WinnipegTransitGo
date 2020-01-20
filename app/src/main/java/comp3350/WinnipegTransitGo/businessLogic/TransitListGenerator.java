package comp3350.WinnipegTransitGo.businessLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import comp3350.WinnipegTransitGo.CustomExceptions.TransitLimitError;
import comp3350.WinnipegTransitGo.CustomExceptions.TransitNoConnectionException;
import comp3350.WinnipegTransitGo.CustomExceptions.TransitParseException;
import comp3350.WinnipegTransitGo.objects.Bus;
import comp3350.WinnipegTransitGo.objects.BusRoute;
import comp3350.WinnipegTransitGo.objects.BusRouteSchedule;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.BusStopFeature;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.ScheduledStop;
import comp3350.WinnipegTransitGo.objects.Time;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.TransitListItem.TransitListItemBuilder;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPI;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIProvider;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIResponse;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TransitListGenerator class
 * Makes API calls and wraps them in TransitListItem objects
 * This class uses synchrnous api calls, therefore requires
 * to be on a thread other than main. (Called from asyncThread)
 *
 * Usage:
 * TransitListGenerator ld=new TransitListGenerator(this);
 * ld.getBusStops: will give all the bus stop
 * ld.getBusesOnABusStop: will give all the buses on a stop
 *
 * @author Nibras Ohin, Syed Habib
 * @version 1.0
 * @since 2017-05-24
 */

public class TransitListGenerator implements TransitListPopulator {

    private Preferences preferences;
    private TransitAPIProvider api;
    private List<TransitListItem> listItems;

    public TransitListGenerator(String apiKey) {
        listItems = new ArrayList<>();
        api = TransitAPI.getAPI(apiKey);
        preferences = PreferencesService.getDataAccess();
    }

    public TransitListGenerator(TransitAPIProvider transitAPIProvider)
    {
        listItems = new ArrayList<>();
        api = transitAPIProvider;
        this.preferences = PreferencesService.getDataAccess();
    }

    //Gets all the bus stops on the point given, uses retrofit transit api
    //to make synch api calls.
    public List<BusStopApiData> getBusStops(String latitude, String longitude)  throws Exception  {
        listItems.clear();
        String radius;
        try {
            radius = Integer.toString(preferences.getRadius());//get radius from persistance
        }
        catch (Exception e)
        {
            radius = "500";//default value in case of failure
        }
        return makeAPICallsForBusStops(latitude, longitude, radius);
    }

    //This is actual method which makes the api calls, and takes out the required data as needed
    private List<BusStopApiData> makeAPICallsForBusStops(String latitude, String longitude, String radius) throws  Exception {
        Call<TransitAPIResponse> apiResponse = api.getBusStops(radius, latitude, longitude, true);
        Response<TransitAPIResponse> response;
        try {
            response = apiResponse.execute();
        }
        catch (Exception e)
        {
            throw new TransitNoConnectionException("No internet connection");
        }
        if(response.errorBody() != null)
            throw new TransitLimitError("Transit limit reached");

        List<BusStop> nearByBusStops;
        List<BusStopApiData> result = new ArrayList<>();

        nearByBusStops =  response.body().getBusStops();//get all the bus stops

        BusStop currStop;
        for (int i = 0; i < nearByBusStops.size(); i++) {
            currStop = nearByBusStops.get(i);
            result.add(new BusStopApiData(currStop.getNumber(), currStop.getName(), currStop.getWalkingDistance(), currStop.getLocation().getLatitude(), currStop.getLocation().getLongitude()));
        }

        return result;
    }

    //Gets all the bus on a bus Stop provided, uses retrofit transit api
    //to make synch api calls and return the buses in a list
    public List<TransitListItem> getBusesOnABusStop(BusStopApiData busStop) throws Exception
    {
        Call<TransitAPIResponse> apiResponse = api.getBusStopSchedule(busStop.getBusStopNumber());
        retrofit2.Response<TransitAPIResponse> response;

        try
        {
            response = apiResponse.execute();
        }
        catch(Exception e)
        {
            throw new TransitNoConnectionException("No internet connection");
        }

        if(response.errorBody() != null)
            throw new TransitLimitError("Transit limit reached");

        processResponseBusStopSchedule(response.body().getBusStopSchedule(), busStop.getBusStopNumber(),  busStop.getBusStopName(), busStop.getWalkingDistance());

        return listItems;
    }

    //Processes the response from transitApi for buses.
    private void processResponseBusStopSchedule(BusStopSchedule stopSchedule, final int busStopNumber, final String busStopName, final String walkingDistance) throws TransitParseException
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
            Bus busFeatures = scheduledStops.get(0).getBus();

            if(allTiming.get(0).equals("Due"))
            {
                status="";
            }
            insertClosestBus(new TransitListItemBuilder().setWalkingDistance(walkingDistance).setBusNumber(busNumber).setBusStopNumber(busStopNumber).setBusStopName(busStopName).setDestination(destination).setStatus(status).setAllTimes(allTiming).setHasBikeRack(busFeatures.isBikeRackAvailable()).setHasEasyAccess(busFeatures.isEasyAccessAvailable()).createTransitListItem());
        }

        //sort according to the remaining time here
        Collections.sort(listItems, new TransitListItemBuilder().createTransitListItem());
    }


    //insert the bus with the closest stop as there could be same bus on multiple stops
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
                if(newItem.getBusStopDistance() < currItem.getBusStopDistance())//if current is closer
                    listItems.set(j, newItem);
            }
        }

        if(!found) //if new bus
        {
            listItems.add(newItem);
        }
    }


    private String calculateStatus(ScheduledStop schedule) throws TransitParseException{
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
            throw new TransitParseException("Error while parsing time");
        }
        return status;
    }

    private long calculateTimeRemaining(ScheduledStop schedule) throws TransitParseException {
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
            throw new TransitParseException("Error while parsing time");
        }

        return timeRemaining;
    }

    private List<String> parseTime(List<ScheduledStop> scheduledStops) throws TransitParseException {
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

    public void getBusStopFeatures(int busStopNumber, final BusStopFeaturesListener callBack)
    {
        Call<TransitAPIResponse> apiResponse = api.getBusStopFeatures(busStopNumber);
        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                List<BusStopFeature> features = response.body().getBusStopFeatures();

                ArrayList<String> busStopFeatures = new ArrayList<>();

                for (int i=0; i<features.size(); i++)
                    busStopFeatures.add(features.get(i).getName());

                callBack.showBusPopup(busStopFeatures);
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                ArrayList<String> busStopFeatures = new ArrayList<>();
                callBack.showBusPopup(busStopFeatures);
            }
        });
    }
}