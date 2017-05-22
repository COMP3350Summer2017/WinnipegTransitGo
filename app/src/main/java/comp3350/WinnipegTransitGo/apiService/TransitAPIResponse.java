package comp3350.WinnipegTransitGo.apiService;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import comp3350.WinnipegTransitGo.objects.BusStop;

/**
 * The response of a Winnipeg Transit API call
 * Usage:
 *  Call<TransitAPIResponse> response;
 *  apiResponse.enqueue(new Callback<TransitAPIResponse>() {
 *   public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
 *   BusStop busStop = response.body().getBusStop();
 *
 *   }
 *
 *   public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
 *
 *   }
 *   });
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

    @SerializedName("query-time")
    private String queryTime;

    public List<BusStop> getBusStops() { return busStops; }

    public BusStop getBusStop() { return busStop; }

    public String getQueryTime() { return queryTime; }
}
