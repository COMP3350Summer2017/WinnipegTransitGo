package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import retrofit2.Call;

/**
 * All available Open Weather Map API calls
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-10
 */

public interface WeatherAPIProvider {
    Call<WeatherAPIResponse> getWeather();
}
