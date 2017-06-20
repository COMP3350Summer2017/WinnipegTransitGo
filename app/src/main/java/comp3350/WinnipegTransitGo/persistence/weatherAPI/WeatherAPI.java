package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Open Weather Map API singleton class
 * Provides public API calls
 * Usage:
 * WeatherAPIProvider api = WeatherAPI.getAPI(key);
 * final Call<WeatherAPIResponse> apiResponse = api.getWeather();
 * *see WeatherAPIResponse usage*
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-08
 */
public class WeatherAPI implements WeatherAPIProvider {
    private static WeatherAPIProvider instance = null;

    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
    private String apiKey;
    private WeatherAPIClient weatherClient;

    private WeatherAPI(String apiKey) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();
        weatherClient = retrofit.create(WeatherAPIClient.class);
        this.apiKey = apiKey;
    }

    public static WeatherAPIProvider getAPI(String apiKey) {
        if (instance == null)
            instance = new WeatherAPI(apiKey);
        return instance;
    }

    //region Weather API public calls

    @Override
    public Call<WeatherAPIResponse> getWeather() {
        return weatherClient.getWeather(apiKey);
    }

    //endregion
}
