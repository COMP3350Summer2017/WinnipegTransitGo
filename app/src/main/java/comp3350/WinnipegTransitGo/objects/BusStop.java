package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * BusStop
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public class BusStop {
    private int key;
    private int number;
    private String name;
    private String direction;
    private String side;
    private Street street;
    private Distances distances;

    @SerializedName("cross-street")
    Street crossStreet;

    @SerializedName("centre")
    BusStopLocation centreLocation;

    //region public getters
    public int getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getDirection() {
        return direction;
    }

    public String getSide() {
        return side;
    }

    public Street getStreet() {
        return street;
    }

    public Street getCrossStreet() {
        return crossStreet;
    }

    public BusStopLocation getLocation() {
        return centreLocation;
    }

    public String getDistance() {
        return distances.direct;
    }

    public String getWalkingDistance() {
        return distances.walking;
    }
    //endregion

    private class Distances {
        String direct;
        String walking;
    }
}


