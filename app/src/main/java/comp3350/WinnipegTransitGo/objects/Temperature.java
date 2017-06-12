package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Unknown on 2017-06-10.
 */

public class Temperature {
    @SerializedName("temp")
    private int temperature;

    private int pressure;

    private int humidity;

    @SerializedName("temp_min")
    private int minimumTemperature;

    @SerializedName("temp_max")
    private int maximumTemperature;

    //region public getters

    public int getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getMinimumTemperature() {
        return minimumTemperature;
    }

    public int getMaximumTemperature() {
        return maximumTemperature;
    }

    //endregion
}
