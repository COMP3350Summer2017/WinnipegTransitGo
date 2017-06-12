package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import com.google.gson.annotations.SerializedName;

import comp3350.WinnipegTransitGo.objects.Temperature;
import comp3350.WinnipegTransitGo.objects.Weather;

/**
 * The response of the Open Weather Map API call
 * Usage:
 * Call<WeatherAPIResponse> response;
 * apiResponse.enqueue(new Callback<WeatherAPIResponse>() {
 * public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
 * Weather weather = response.body().getWeather();
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

public class WeatherAPIResponse {
    private Weather[] weather;

    @SerializedName("main")
    private Temperature temperature;

    //region public getters
    public Weather getWeather() {
        return weather[0]; //TODO: change once you get confirmation
    }

    public Temperature getTemperature() {
        return temperature;
    }
    //endregion
}
