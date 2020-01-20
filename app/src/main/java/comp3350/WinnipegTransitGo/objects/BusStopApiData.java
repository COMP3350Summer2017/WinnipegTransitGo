package comp3350.WinnipegTransitGo.objects;

/**
 * Created by habib on 7/14/2017.
 */
//This class is used to return the bus stop data from business logic to presentation
//This is used for 2 things in presentation:
    //1. To mark the bus
    //2. To get the buses on this stops
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
