package comp3350.WinnipegTransitGo.businessLogic;

import android.util.Log;

import java.util.HashMap;

import comp3350.WinnipegTransitGo.objects.Temperature;
import comp3350.WinnipegTransitGo.objects.Weather;
import comp3350.WinnipegTransitGo.objects.WeatherCondition;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPI;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPICallback;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPIProvider;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPIResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * OpenWeatherMap Business Logic
 *  accesses Open Weather Map api to get
 *  weather and temperature information.
 *  Processes API information to be generalized to many different views
 *  (not just android)
 *
 * implements WeatherProvider
 *  Provides generalized getters for temperature and weather condition
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public class OpenWeatherMap implements WeatherProvider {
    private static HashMap<String, WeatherCondition> weatherConditionMap = initWeatherConditionMap();
    private WeatherAPIProvider weatherAPI;

    public OpenWeatherMap(String apiKey) {
        weatherAPI = WeatherAPI.getAPI(apiKey);
    }

    @Override
    public void getTemperature(final WeatherAPICallback callback) {
        final Call<WeatherAPIResponse> weatherResponse = weatherAPI.getWeather();
        weatherResponse.enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                if(response.errorBody() != null)
                    return;

                if (response.body().getTemperature() != null)
                {
                    Temperature temp = response.body().getTemperature();
                    callback.temperatureReady(Integer.toString(temp.getTemperature()));
                }
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                Log.w(this.getClass().getName(), "ERR: Open Weather Map Temperature");
            }
        });
    }

    @Override
    public void getWeatherCondition(final WeatherAPICallback callback) {
        Call<WeatherAPIResponse> weatherResponse = weatherAPI.getWeather();
        weatherResponse.enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                if(response.errorBody() != null)
                    return;

                Weather weather = response.body().getWeather();
                if (weather != null && weatherConditionMap.get(weather.getDescription()) != null)
                {
                    callback.weatherReady(weatherConditionMap.get(weather.getDescription()));
                }
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                Log.w(this.getClass().getName(), "ERR: Open Weather Map Weather cond");
            }
        });
    }

    private static HashMap<String,WeatherCondition> initWeatherConditionMap() {
        HashMap<String, WeatherCondition> weatherConditionMap = new HashMap<String, WeatherCondition>();

        weatherConditionMap.put("clear sky", WeatherCondition.clear_sky);
        weatherConditionMap.put("few clouds", WeatherCondition.few_clouds);
        weatherConditionMap.put("scattered clouds", WeatherCondition.scattered_clouds);
        weatherConditionMap.put("broken clouds", WeatherCondition.broken_clouds);
        weatherConditionMap.put("shower rain", WeatherCondition.shower_rain);
        weatherConditionMap.put("rain", WeatherCondition.rain);
        weatherConditionMap.put("thunderstorm", WeatherCondition.thunderstorm);
        weatherConditionMap.put("snow", WeatherCondition.snow);
        weatherConditionMap.put("mist", WeatherCondition.mist);

        return weatherConditionMap;
    }
}
