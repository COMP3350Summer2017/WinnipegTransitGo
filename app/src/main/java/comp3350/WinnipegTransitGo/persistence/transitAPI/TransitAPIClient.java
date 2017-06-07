package comp3350.WinnipegTransitGo.persistence.transitAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Winnipeg Transit API client.
 * Makes the direct calls to Winnipeg Transit's web service.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public interface TransitAPIClient {
    @GET("stops/{number}.json?usage=long")
    Call<TransitAPIResponse> getBusStop(@Path("number") int stopNumber,
                                        @Query("api-key") String apiKey);

    @GET("stops.json?usage=short")
    Call<TransitAPIResponse> getBusStops(@Query("distance") String distance,
                                         @Query("x") int x,
                                         @Query("y") int y,
                                         @Query("walking") boolean walking,
                                         @Query("api-key") String apiKey);

    @GET("stops.json?usage=short")
    Call<TransitAPIResponse> getBusStops(@Query("distance") String distance,
                                         @Query("lat") String lat,
                                         @Query("lon") String lon,
                                         @Query("walking") boolean walking,
                                         @Query("api-key") String apiKey);

    @GET("stops.json?usage=short")
    Call<TransitAPIResponse> getBusStops(@Query("route") int route,
                                         @Query("api-key") String apiKey);

    @GET("stops/{number}/schedule.json")
    Call<TransitAPIResponse> getBusStopSchedule(@Path("number") int stopNumber,
                                                @Query("api-key") String apiKey);

    @GET("variants/{variant}.json?usage=short")
    Call<TransitAPIResponse> getBusStopSchedule(@Path("variant") String variant,
                                                @Query("api-key") String apiKey);
}
