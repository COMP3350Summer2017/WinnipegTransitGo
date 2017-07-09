package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.businessLogic.WeatherProvider;
import comp3350.WinnipegTransitGo.objects.WeatherCondition;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPICallback;

/**
 * WeatherPresenter
 *  Handles weather presentation on screen
 *  Can be called by different Views that want to present weather
 *
 *  call hierarchy: uses WeatherProvider to get weather information,
 *  WeatherProvider translates weather information into system specific weather.
 *
 *  implements a WeatherAPICallback since API calls are asynchronous.
 *  once API is done, the callback functions will be called.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-21
 */

public class WeatherPresenter implements WeatherAPICallback {
    private static WeatherProvider weatherProvider;

    private TextView tempTV;
    private ImageView weatherCondIV;
    private Context context;

    public WeatherPresenter(TextView tempTV, ImageView weatherCondIV, WeatherProvider wp, Context context) {
        weatherProvider = wp;
        this.tempTV = tempTV;
        this.weatherCondIV = weatherCondIV;
        this.context = context;
        setRefresh();
    }

    public void refreshWeather() {
        presentTemperature();
        presentWeather();
    }

    /**
     * uses weather provider to make an asynchronous call to a weather API service
     * once API is done, the temperatureReady function will be called.
     */
    public void presentTemperature() {
        if (tempTV != null)
            weatherProvider.getTemperature(this);
    }

    /**
     * uses weather provider to make an asynchronous call to a weather API service
     * once API is done, the weatherReady function will be called.
     */
    public void presentWeather() {
        if (weatherCondIV != null)
            weatherProvider.getWeatherCondition(this);
    }

    // called after API information is ready by a weather provider
    @Override
    public void temperatureReady(String temperature) {
        if (temperature != null) {
            tempTV.setText(temperature);
            tempTV.append(" C°");
        }
    }

    // called after API information is ready by a weather provider
    @Override
    public void weatherReady(WeatherCondition weatherCondition) {
        if (weatherCondition == null)
            return;

        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier(weatherCondition.getWeatherCode(),
                "drawable",
                context.getPackageName());

        if (resourceId != 0) {
            weatherCondIV.setImageDrawable(resources.getDrawable(resourceId));
            weatherCondIV.setTag(weatherCondition.getWeatherCode());
        }
    }

    /**
     * Dependency injection, used mainly for tests, can be used for injecting
     * new weather api providers in the future.
     */
    public static void setWeatherProvider(WeatherProvider newWeatherProvider) {
        weatherProvider = newWeatherProvider;
    }

    private void setRefresh() {
        if (tempTV != null)
            tempTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshWeather();
                }
            });
    }
}
