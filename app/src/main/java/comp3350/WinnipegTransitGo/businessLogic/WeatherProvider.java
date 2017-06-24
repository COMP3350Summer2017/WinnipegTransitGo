package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPICallback;

/**
 * WeatherProvider
 *  interface for providing weather information
 *  Used by the UI to set weather information components
 *  uses a weather callback to inform caller when information is ready
 *  can be implemented by any kind of weather API service provider
 *      ex: Yahoo Weather API, Open Weather Map API, etc.
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
