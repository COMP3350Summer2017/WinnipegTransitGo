package comp3350.WinnipegTransitGo.persistence.transitAPI;

import retrofit2.Call;

/**
 * All available Winnipeg Transit API calls
 * Example usage: see TransitAPI.java
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public interface TransitAPIProvider {

    // get bus stops near geographic location
    Call<TransitAPIResponse> getBusStops(String distance, String lat, String lon, boolean walking);

    // get the schedule of a bus stop
    Call<TransitAPIResponse> getBusStopSchedule(int stopNumber);


    // get the features of the bus stop
    Call<TransitAPIResponse> getBusStopFeatures(int stopNumber);
}
