package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.objects.WeatherCondition;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPICallback;

/**
 * WeatherProvider
 *  interface for providing weather information
 *  Used by the UI to set weather information components
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public interface WeatherProvider {

    // get the temperature
    void getTemperature(WeatherAPICallback callback);

    // get the weather condition
    void getWeatherCondition(WeatherAPICallback callback);
}
