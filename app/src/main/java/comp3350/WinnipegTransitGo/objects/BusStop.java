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
    int number;
    String name;
    Distances distances;


    @SerializedName("centre")
    Location centreLocation;

    //region public getters

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public Location getLocation() {
        return centreLocation;
    }

    public String getWalkingDistance() {
        return distances.walking;
    }
    //endregion

    private class Distances {
        String walking;
    }
}


