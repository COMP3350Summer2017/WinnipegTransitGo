package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Bus
 *  Holds information about the specific bus
 *  (not a route, not a stop, this is the real bus you enter)
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-07-03
 */

public class Bus {
    @SerializedName("bike-rack")
    private boolean hasBikeRack;

    @SerializedName("easy-access")
    private boolean hasEasyAccess;

    //region public getters
    public boolean isBikeRackAvailable() {
        return hasBikeRack;
    }

    public boolean isEasyAccessAvailable() {
        return hasEasyAccess;
    }
    //endregion
}
