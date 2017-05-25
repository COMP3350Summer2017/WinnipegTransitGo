package comp3350.WinnipegTransitGo.objects;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.apiService.TransitAPI;
import comp3350.WinnipegTransitGo.apiService.TransitAPIProvider;

/**
 * Created by nibras on 2017-05-24.
 */

public class Display
{
    private int busNumber;
    private String busStopName;
    private int busStopNumber;
    private String destination;
    private String remainingTime;
    private String busStatus;
    String lat="49.8049250";
    String longitude="-97.1569080";

    List<String> times=new ArrayList<String>();

    List<BusStop> nearByBusStops=new ArrayList<BusStop>();

    List<Display> displayObjects=new ArrayList<Display>();
    private String apiKey="IyNt0rkZbxXFyrS4KT3t";
    TransitAPIProvider api = TransitAPI.getAPI(apiKey);
    //final Call<TransitAPIResponse> apiResponse=null;// = api.getBusStop(10064);
    //BusStop bb;//=null;

    public Display(int busNumber,int busStopNumber, String busStopName, String destination)
    {
        this.busNumber=busNumber;
        this.busStopNumber=busStopNumber;
        this.busStopName=busStopName;
        this.destination=destination;

    }

    public String getBusStopName()
    {
        return busStopName;
    }
    public int getBusNumber()
    {
        return busStopNumber;
    }
    public String getBusStopDestination()
    {
        return destination;
    }
}
