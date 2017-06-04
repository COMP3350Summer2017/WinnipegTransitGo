package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * ScheduledStop
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class ScheduledStop {
    String key;
    BusVariant variant;

    @SerializedName("times")
    Time time;

    //TODO: add "bus" objects (iteration 2/3)

    //region public getters
    public String getKey() { return key; }

    public BusVariant getVariant() { return variant; }

    public Time getTime() { return time; }
    //endregion
}
