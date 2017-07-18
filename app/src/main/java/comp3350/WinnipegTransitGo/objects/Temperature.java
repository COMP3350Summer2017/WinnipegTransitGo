package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Temperature
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public class Temperature {
    @SerializedName("temp")
    private int temperature;

    //region public getters

    public int getTemperature() {
        return temperature;
    }


    //endregion
}
