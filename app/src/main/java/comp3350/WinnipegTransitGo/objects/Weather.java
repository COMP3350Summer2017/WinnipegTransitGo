package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Weather
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public class Weather {

    @SerializedName("icon")
    private String conditionCode;

    //region public getters

    public String getConditionCode() {
        return conditionCode;
    }

    //endregion
}
