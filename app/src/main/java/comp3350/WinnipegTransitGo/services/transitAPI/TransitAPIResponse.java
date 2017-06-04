package comp3350.WinnipegTransitGo.services.transitAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.BusVariant;

/**
 * The response of a Winnipeg Transit API call
 * Usage:
 * Call<TransitAPIResponse> response;
 * apiResponse.enqueue(new Callback<TransitAPIResponse>() {
 * public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
 * BusStop busStop = response.body().getBusStop();
 * <p>
 * }
 * <p>
 * public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
 * <p>
 * }
 * });
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public class TransitAPIResponse {
    @SerializedName("stops")
    private List<BusStop> busStops;

    @SerializedName("stop")
    private BusStop busStop;

    @SerializedName("stop-schedule")
    private BusStopSchedule busStopSchedule;

    private BusVariant variant;

    @SerializedName("query-time")
    private String queryTime;

    //region public getters
    public List<BusStop> getBusStops() {
        return busStops;
    }

    public BusStop getBusStop() {
        return busStop;
    }

    public BusStopSchedule getBusStopSchedule() {
        return busStopSchedule;
    }

    public BusVariant getVariant() {
        return variant;
    }

    public String getQueryTime() {
        return queryTime;
    }
    //endregion
}
