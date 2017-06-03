package comp3350.WinnipegTransitGo.objects;

import java.util.List;

/**
 * Created by nibras on 2017-05-24.
 */

public class TransitListItem
{
    private int busNumber;
    private String busStopName;
    private int busStopNumber;
    private String destination;
    private String busStatus;
    private List<String> times;
    private String busStopDistance;


    public TransitListItem(String walkingDistance, int busNumber, int busStopNumber, String busStopName, String destination, String status, List<String> allTimes)
    {
        this.busNumber=busNumber;
        this.busStopNumber=busStopNumber;
        this.busStopName=busStopName;
        this.destination=destination;
        busStatus = status;
        times = allTimes;
        busStopDistance = walkingDistance;
    }

    public String getBusStopNumber()
    {
        return "#" + busStopNumber;
    }
    public String getBusStopName()
    {
        return busStopName;
    }
    public int getBusNumber()
    {
        return busNumber;
    }
    public String getBusStopDestination()
    {
        return destination;
    }
    public String getBusStatus()
    {
        return busStatus;
    }
    public List<String> getTimes()
    {
        return times;
    }
    public String getBusStopDistance() { return busStopDistance + " mtr";}
}
