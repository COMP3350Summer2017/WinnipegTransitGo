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
 * OpenWeatherMapProvider Business Logic
 *  weather provider implementation of the OpenWeatherMap API
 *  accesses Open Weather Map api to get
 *  weather and temperature information.
 *  Processes API information to be generalized to many different views
 *  (not just android)
 *
 * implements WeatherProvider
 *  Provides generalized getters for temperature and weather condition
 *  (generalizes Open Weather Map)
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public class OpenWeatherMapProvider implements WeatherProvider {
    private static HashMap<String, WeatherCondition> weatherConditionMap = initWeatherConditionMap();
    private WeatherAPIProvider weatherAPI;

    public OpenWeatherMapProvider(String apiKey) {
        weatherAPI = WeatherAPI.getAPI(apiKey);
    }

    @Override
    public void getTemperature(final WeatherAPICallback callback) {
        if (callback == null)
            return;

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
        if (callback == null)
            return;

        Call<WeatherAPIResponse> weatherResponse = weatherAPI.getWeather();
        weatherResponse.enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                if(response.errorBody() != null)
                    return;

                Weather weather = response.body().getWeather();
                if (weather != null && weatherConditionMap.get(weather.getConditionCode()) != null)
                {
                    callback.weatherReady(weatherConditionMap.get(weather.getConditionCode()));
                }
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                Log.w(this.getClass().getName(), "ERR: Open Weather Map Weather cond");
            }
        });
    }

    /** initWeatherConditionMap
     * Generalizes OpenWeatherMap's weather conditions into system specific conditions.
     * Provides translation from OpenWeatherMap's generic weather codes into system
     * specific WeatherCondition (used for generalization of API)
     */
    private static HashMap<String,WeatherCondition> initWeatherConditionMap() {
        HashMap<String, WeatherCondition> weatherConditionMap = new HashMap<String, WeatherCondition>();

        weatherConditionMap.put("01d", WeatherCondition.clear_sky_day);
        weatherConditionMap.put("01n", WeatherCondition.clear_sky_night);
        weatherConditionMap.put("02d", WeatherCondition.few_clouds_day);
        weatherConditionMap.put("02n", WeatherCondition.few_clouds_night);
        weatherConditionMap.put("03d", WeatherCondition.scattered_clouds);
        weatherConditionMap.put("03n", WeatherCondition.scattered_clouds);
        weatherConditionMap.put("04d", WeatherCondition.broken_clouds);
        weatherConditionMap.put("04n", WeatherCondition.broken_clouds);
        weatherConditionMap.put("09d", WeatherCondition.shower_rain);
        weatherConditionMap.put("09n", WeatherCondition.shower_rain);
        weatherConditionMap.put("10d", WeatherCondition.rain_day);
        weatherConditionMap.put("10n", WeatherCondition.rain_night);
        weatherConditionMap.put("11d", WeatherCondition.thunderstorm);
        weatherConditionMap.put("11n", WeatherCondition.thunderstorm);
        weatherConditionMap.put("13d", WeatherCondition.snow);
        weatherConditionMap.put("13n", WeatherCondition.snow);
        weatherConditionMap.put("50d", WeatherCondition.mist);
        weatherConditionMap.put("50n", WeatherCondition.mist);

        return weatherConditionMap;
    }
}
