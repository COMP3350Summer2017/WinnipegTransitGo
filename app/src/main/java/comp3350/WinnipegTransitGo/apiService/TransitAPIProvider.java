package comp3350.WinnipegTransitGo.apiService;

import comp3350.WinnipegTransitGo.objects.BusStop;
import retrofit2.Call;

/**
 * All available Winnipeg Transit API calls
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public interface TransitAPIProvider {
    // get a bus stops by its number
    Call<TransitAPIResponse> getBusStop(int stopNumber);

    // get bus stops near utm location
    Call<TransitAPIResponse> getBusStops(String distance, int x, int y, boolean walking);

    // get bus stops near geographic location
    Call<TransitAPIResponse> getBusStops(String distance, String lat, String lon, boolean walking);

    // get bus stops by route number
    Call<TransitAPIResponse> getBusStops(int route);

    // get the schedule of a bus stop
    Call<TransitAPIResponse> getBusStopSchedule(int stopNumber);

    // get the short form of a variant
    Call<TransitAPIResponse> getVariantShort(String variant);
}
