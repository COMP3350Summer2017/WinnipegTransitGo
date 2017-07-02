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

    public void presentTemperature() {
        if (tempTV != null)
            weatherProvider.getTemperature(this);
    }

    public void presentWeather() {
        if (weatherCondIV != null)
            weatherProvider.getWeatherCondition(this);
    }

    @Override
    public void temperatureReady(String temperature) {
        if (temperature != null) {
            tempTV.setText(temperature);
            tempTV.append(" C°");
        }
    }

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

    public static void setWeatherProvider(WeatherProvider newWeatherProvider) {
        weatherProvider = newWeatherProvider;
    }

    private void setRefresh() {
        if (tempTV != null)
            tempTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presentTemperature();
                    presentWeather();
                }
            });
    }
}
