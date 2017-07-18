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
    private Distances distances;

    @SerializedName("centre")
    private BusStopLocation centreLocation;

    public BusStop(int busNumber, String busName, String walkingDistance, String lat, String lon)
    {
        number = busNumber;
        name = busName;
        distances = new Distances(walkingDistance, walkingDistance);
        centreLocation = new BusStopLocation(lat, lon);
    }

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

    public BusStopLocation getLocation() {
        return centreLocation;
    }

    public String getWalkingDistance() {
        return distances.walking;
    }
    //endregion

    private class Distances {
        String direct;
        String walking;

        public Distances(String direct, String walking)
        {
            this.direct = direct;
            this.walking = walking;
        }

    }
}


