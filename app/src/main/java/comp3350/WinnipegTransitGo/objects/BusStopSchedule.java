package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BusStopSchedule
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class BusStopSchedule {
    @SerializedName("stop")
    private BusStop busStop;

    @SerializedName("route-schedules")
    private List<BusRouteSchedule> busRouteSchedules;

    //region public getters
    public BusStop getBusStop() {
        return busStop;
    }

    public List<BusRouteSchedule> getBusRouteSchedules() {
        return busRouteSchedules;
    }
    //endregion
}
