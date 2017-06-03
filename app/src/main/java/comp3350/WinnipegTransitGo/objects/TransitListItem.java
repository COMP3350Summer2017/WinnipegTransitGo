package comp3350.WinnipegTransitGo.objects;

import java.util.List;

/**
 * Uses by business logic to send all the info about a bus
 * to gui.
 *
 * @author Nibras
 * @version 1.0
 * @since 2017-05-24
 */

public class TransitListItem
{
    private int busNumber;
    private String busStopName;
    private String busStopNumber;
    private String destination;
    private String busStatus;
    private List<String> times;
    private String busStopDistance;


    public TransitListItem(String walkingDistance, int busNumber, int busStopNumber, String busStopName, String destination, String status, List<String> allTimes)
    {
        this.busNumber=busNumber;
        this.busStopNumber= "#" + busStopNumber;
        this.busStopName=busStopName;
        this.destination=destination;
        busStatus = status;
        times = allTimes;
        busStopDistance = walkingDistance + " mtr";
    }

    public String getBusStopNumber()
    {
        return busStopNumber;
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
    public String getBusStopDistance() { return busStopDistance;}
}
