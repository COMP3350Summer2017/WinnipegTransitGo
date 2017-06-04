package comp3350.WinnipegTransitGo.services.transitAPI;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Winnipeg Transit API singleton class
 * Provides public API calls
 * Usage:
 * TransitAPIProvider api = TransitAPI.getAPI(key);
 * final Call<TransitAPIResponse> apiResponse = api.getBusStop(10064);
 * *see TransitAPIResponse usage*
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public class TransitAPI implements TransitAPIProvider {
    private static TransitAPIProvider instance = null;

    private final String BASE_URL = "https://api.winnipegtransit.com/v2/";
    private String apiKey;
    private TransitAPIClient transitClient;

    private TransitAPI(String apiKey) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        transitClient = retrofit.create(TransitAPIClient.class);
        this.apiKey = apiKey;
    }

    public static TransitAPIProvider getAPI(String apiKey) {
        if (instance == null)
            instance = new TransitAPI(apiKey);
        return instance;
    }

    //region Transit API public calls
    @Override
    public Call<TransitAPIResponse> getBusStop(int stopNumber) {
        return transitClient.getBusStop(stopNumber, apiKey);
    }

    @Override
    public Call<TransitAPIResponse> getBusStops(String distance, int x, int y, boolean walking) {
        return transitClient.getBusStops(distance, x, y, walking, apiKey);
    }

    @Override
    public Call<TransitAPIResponse> getBusStops(String distance, String lat, String lon, boolean walking) {
        return transitClient.getBusStops(distance, lat, lon, walking, apiKey);
    }

    @Override
    public Call<TransitAPIResponse> getBusStops(int route) {
        return transitClient.getBusStops(route, apiKey);
    }

    @Override
    public Call<TransitAPIResponse> getBusStopSchedule(int stopNumber) {
        return transitClient.getBusStopSchedule(stopNumber, apiKey);
    }

    @Override
    public Call<TransitAPIResponse> getVariantShort(String variant) {
        return transitClient.getBusStopSchedule(variant, apiKey);
    }
    //endregion
}