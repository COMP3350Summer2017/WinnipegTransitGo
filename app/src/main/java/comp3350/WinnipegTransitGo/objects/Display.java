package comp3350.WinnipegTransitGo.objects;

import java.util.List;

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
    private String busStopDistance;
    String lat="49.8049250";
    String longitude="-97.1569080";
    List<String> times;

    private String apiKey="NB08BPIRd1oFuwRil4";

    public Display(String distance,int busNumber,int busStopNumber, String busStopName, String destination, String timing, String status, List<String> allTimes)
    {
        this.busNumber=busNumber;
        this.busStopNumber=busStopNumber;
        this.busStopName=busStopName;
        this.destination=destination;
        remainingTime = timing;
        busStatus = status;
        times = allTimes;
        busStopDistance=distance;
    }

    public String getBusStopName() {
        return busStopName;
    }
    public String getBusStopDistance() {
        return busStopDistance;
    }

    public int getBusNumber() {
        return busNumber;
    }

    public int getBusStopNumber() {
        return busStopNumber;
    }

    public String getBusStopDestination() {
        return destination;
    }

    public String getBusTimeRemaining() {
        return remainingTime;
    }

    public String getBusStatus() {
        return busStatus;
    }

    public List<String> getTimes() {
        return times;
    }
}
