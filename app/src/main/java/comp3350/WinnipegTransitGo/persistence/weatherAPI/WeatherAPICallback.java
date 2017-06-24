package comp3350.WinnipegTransitGo.persistence.weatherAPI;

import comp3350.WinnipegTransitGo.objects.WeatherCondition;

/**
 * WeatherAPICallback
 *  Interface for implementing callback from a weather API provider.
 *  This interface specifies available call backs from the API.
 *  Should be implemented by the calling client.
 *      Client implements callback functionality.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-20
 */

public interface WeatherAPICallback {
    // temperature is ready
    void temperatureReady(String temperature);

    // weather is ready
    void weatherReady(WeatherCondition weatherCondition);
}
