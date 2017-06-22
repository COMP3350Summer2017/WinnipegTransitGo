package comp3350.WinnipegTransitGo.objects;

/**
 * Created by Unknown on 2017-06-21.
 */

public enum WeatherCondition {
    init("clearsky"),
    clear_sky("clearsky"),
    few_clouds("fewclouds"),
    scattered_clouds("scatteredclouds"),
    broken_clouds("brokenclouds"),
    shower_rain("showerrain"),
    rain("rain"),
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
