package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import comp3350.WinnipegTransitGo.objects.WeatherCondition;

/**
 * Created by Unknown on 2017-06-21.
 */

public interface WeatherAPICallback {
    void temperatureReady(String temperature);

    void weatherReady(WeatherCondition weatherCondition);
}
