package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import retrofit2.Call;

/**
 * All available Weather calls
 * Example usage: see WeatherAPI.java
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-10
 */

public interface WeatherAPIProvider {
    // get weather (Winnipeg)
    Call<WeatherAPIResponse> getWeather();
}
