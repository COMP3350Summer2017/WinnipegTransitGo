package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BusRouteSchedule
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class BusRouteSchedule {
    @SerializedName("route")
    private BusRoute busRoute;

    @SerializedName("scheduled-stops")
    private List<ScheduledStop> scheduledStops;

    //region public getters
    public BusRoute getBusRoute() {
        return busRoute;
    }

    public List<ScheduledStop> getScheduledStops() {
        return scheduledStops;
    }
    //endregion
}
