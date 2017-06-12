package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Unknown on 2017-06-10.
 */

public class Weather {
    private int id;

    @SerializedName("main")
    private String condition;

    private String description;

    @SerializedName("icon")
    private String iconCode;

    //region public getters

    public int getId() {
        return id;
    }

    public String getCondition() {
        return condition;
    }

    public String getDescription() {
        return description;
    }

    public String getIconCode() {
        return iconCode;
    }

    //endregion
}
