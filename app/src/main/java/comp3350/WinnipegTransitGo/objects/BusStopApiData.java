package comp3350.WinnipegTransitGo.objects;

/**
 * Created by habib on 7/14/2017.
 */

public class BusStopApiData {


    private int busStopNumber;
    private String busStopName;
    private String walkingDistance;
    private String latitude;
    private String longitude;

    public BusStopApiData(final int busStopNumber, final String busStopName, final String walkingDistance, String latitude, String longitude)
    {
        this.busStopNumber = busStopNumber;
        this.busStopName = busStopName;
        this.walkingDistance = walkingDistance;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public int getBusStopNumber() {
        return busStopNumber;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public String getWalkingDistance() {
        return walkingDistance;
    }


    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }


}
