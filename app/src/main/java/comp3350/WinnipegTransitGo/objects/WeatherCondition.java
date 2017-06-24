package comp3350.WinnipegTransitGo.objects;

/**
 * WeatherCondition
 *  Weather condition enum.
 *  Holds all possible system specific weather conditions.
 *  Any weather API provider mast map to this enum
 *
 *  Weather condition is mapped to a weather code.
 *  weather code is used to get more information about the weather,
 *  or for setting up weather presentation.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-20
 */

public enum WeatherCondition {
    init("clearsky_day"),
    clear_sky_day("clearsky_day"),
    clear_sky_night("clearsky_night"),
    few_clouds_day("fewclouds_day"),
    few_clouds_night("fewclouds_night"),
    scattered_clouds("scatteredclouds"),
    broken_clouds("brokenclouds"),
    shower_rain("showerrain"),
    rain_day("rain_day"),
    rain_night("rain_night"),
    thunderstorm("thunderstorm"),
    snow("snow"),
    mist("mist");

    private String weatherCode;

    WeatherCondition(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getWeatherCode() {
        return this.weatherCode;
    }
}
