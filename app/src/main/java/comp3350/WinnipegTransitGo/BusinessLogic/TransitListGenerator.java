package comp3350.WinnipegTransitGo.BusinessLogic;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import comp3350.WinnipegTransitGo.apiService.TransitAPI;
import comp3350.WinnipegTransitGo.apiService.TransitAPIProvider;
import comp3350.WinnipegTransitGo.apiService.TransitAPIResponse;
import comp3350.WinnipegTransitGo.interfaces.ApiListenerCallback;
import comp3350.WinnipegTransitGo.interfaces.InterfacePopulator;
import comp3350.WinnipegTransitGo.objects.BusRoute;
import comp3350.WinnipegTransitGo.objects.BusRouteSchedule;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.ScheduledStop;
import comp3350.WinnipegTransitGo.objects.Time;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TransitListGenerator class
 * Makes API calls and wraps them in TransitListItem objects
 * Usage:
 *  TransitListGenerator ld=new TransitListGenerator(this);
 *  ld.getListOfBusStops(); this makes the api calls
 *  On api responses calls the method updateListView
 * @author Nibras Ohin, Syed Habib
 * @version 1.0
 * @since 2017-05-24
 */
public class TransitListGenerator implements InterfacePopulator
{

    private String radius; //radius to search nearby bus stops.
    private String latitude;
    private String longitude;

    private TransitAPIProvider api;
    private ApiListenerCallback apiListener;
    private List<TransitListItem> listItems;

    public TransitListGenerator(ApiListenerCallback apiListenerCallback, String apiKey)
    {
        listItems = new ArrayList<TransitListItem>();
        apiListener = apiListenerCallback;
        radius = "200";
        latitude ="49.824370";
        longitude="-97.153068";
        api = TransitAPI.getAPI(apiKey);
    }

    //gets a list of bus stops near the given location
    public void getListOfBusStops()
    {
        Call<TransitAPIResponse> apiResponse = api.getBusStops(radius, latitude, longitude,true);
        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {

                List<BusStop> nearByBusStops = response.body().getBusStops();//get all the bus stops
                List<Integer> nearByBusStopNumbers = new ArrayList<Integer>();

                for(int i=0;i<nearByBusStops.size();i++)
                {
                    nearByBusStopNumbers.add(nearByBusStops.get(i).getNumber());    //gets the busstop number and adds it to the list
                }//create a list of bus stop numbers

                //for each bus stop get the bus number and there routes
                traverseBusStopList(nearByBusStopNumbers, nearByBusStops);
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                Toast.makeText((Context) apiListener, "Some went wrong in transit connection",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void traverseBusStopList(List<Integer> busStopList, List<BusStop> nearByBusStops)
    {
        for(int i=0;i<busStopList.size();i++)
            extractBusInfo(busStopList.get(i), nearByBusStops.get(i).getName());
    }

    //this method will be given a bus stop number and it'll deal with that
    private void extractBusInfo(final int busStopNumber, final String busStopName)
    {
        Call<TransitAPIResponse> apiResponse = api.getBusStopSchedule(busStopNumber);

        apiResponse.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {

                int busNumber;
                String destination;
                BusStopSchedule stopSchedule = response.body().getBusStopSchedule();
                List<BusRouteSchedule> routeSchedule = stopSchedule.getBusRouteSchedules();

                for(int i=0;i<routeSchedule.size();i++) {
                    BusRoute route = routeSchedule.get(i).getBusRoute();
                    busNumber = route.getNumber();
                    destination = route.getName();

                    //get time and status here
                    List<ScheduledStop> scheduledStops = routeSchedule.get(i).getScheduledStops();
                    long timeRemaining = calculateTimeRemaining(scheduledStops.get(0));
                    String status = calculateStatus(scheduledStops.get(0));
                    String timing = Long.toString(timeRemaining);

                    if(timeRemaining <= 0 )//if the bus is early or due (negative means early)
                        timing = "Due";

                    List<String> allTiming = parseTime(scheduledStops);

                    listItems.add(new TransitListItem(busNumber,busStopNumber,busStopName,destination,timing, status, allTiming));
                }
                //sort here
                Collections.sort(listItems, new Comparator<TransitListItem>(){
                    public int compare(TransitListItem o1, TransitListItem o2){
                        int d1;
                        String t1 = o1.getBusTimeRemaining();
                        if(t1.equals("Due"))
                            d1 = 0;
                        else
                            d1 = Integer.parseInt(t1);

                        int d2;
                        String t2 = o2.getBusTimeRemaining();
                        if(t2.equals("Due"))
                            d2 = 0;
                        else
                            d2 = Integer.parseInt(t2);

                        if(d1 == d2)
                            return 0;
                        return d1 < d2 ? -1 : 1;
                    }
                });

                apiListener.updateListView(listItems);//tell the listener that got more data, update list view
            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                Toast.makeText((Context) apiListener, "Some went wrong in transit connection",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private String calculateStatus(ScheduledStop schedule){
        Time time = schedule.getTime();

        String scheduledDeparture = time.getScheduledDeparture();
        String estimatedDeparture = time.getEstimatedDeparture();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        String status = "";
        try
        {
            Date scheduled = sdf.parse(scheduledDeparture);
            Date estimated = sdf.parse(estimatedDeparture);
            long diff = scheduled.getTime() - estimated.getTime();
            long diffMinutes = diff / (60 * 1000);

            if(diffMinutes > 0) //negative means late, positive means early
                status = "Early";
            else if(diffMinutes < 0)
                status = "Late";

            /*            long diffSeconds = diff / 1000;
            long diffHours = diff / (60 * 60 * 1000);*/
        }
        catch (ParseException e)
        {
            Toast.makeText((Context) apiListener, "Some went wrong while parsing transit data",
                    Toast.LENGTH_LONG).show();
        }

        return status;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private long calculateTimeRemaining(ScheduledStop schedule){
        Time time = schedule.getTime();

        String estimatedDeparture = time.getEstimatedDeparture();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        long timeRemaining = 0;
        try
        {
            Date estimated = sdf.parse(estimatedDeparture);

            Calendar c = Calendar.getInstance();
            Date currentTime = c.getTime();

            long diff = estimated.getTime() - currentTime.getTime();
            timeRemaining = diff / (60 * 1000); // in minutes
            //negative means early, positive means late
        }
        catch (ParseException e)
        {
            Toast.makeText((Context) apiListener, "Some went wrong while parsing transit data",
                    Toast.LENGTH_LONG).show();
        }

        return timeRemaining;
    }


    private List<String> parseTime(List<ScheduledStop> scheduledStops)
    {
        List<String> ret = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat output = new SimpleDateFormat("HH:mm");

        try {

            for (int i = 1; i < scheduledStops.size(); i++) {

                Date d = sdf.parse(scheduledStops.get(i).getTime().getEstimatedDeparture());
                ret.add(output.format(d));
            }
        }
        catch (ParseException e)
        {
            Toast.makeText((Context) apiListener, "Some went wrong while parsing transit data",
                    Toast.LENGTH_LONG).show();
        }
        return ret;
    }
}
