package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Open Weather Map API client.
 * Makes the direct calls to Open Weather Map's web service.
 *
 * usage: same as the client for transit API.
 *  to learn more about Retrofit and how to add functionality,
 *  see TransitAPIProvider.java
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-10
 */

public interface WeatherAPIClient {
    @GET("weather?q=Winnipeg&units=metric")
    Call<WeatherAPIResponse> getWeather(@Query("appid") String apiKey);
}
