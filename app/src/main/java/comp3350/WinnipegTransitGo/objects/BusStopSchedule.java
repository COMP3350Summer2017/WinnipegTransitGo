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

    @SerializedName("route-schedules")
    private List<BusRouteSchedule> busRouteSchedules;

    public BusStopSchedule(List<BusRouteSchedule> schedules) {
        busRouteSchedules = schedules;
    }

    //region public getters

    public List<BusRouteSchedule> getBusRouteSchedules() {
        return busRouteSchedules;
    }
    //endregion
}
