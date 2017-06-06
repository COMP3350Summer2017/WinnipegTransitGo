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

    public ScheduledStop(String key, BusVariant variant, Time time)
    {
        this.key = key;
        this. variant = variant;
        this.time = time;
    }

    //region public getters
    public String getKey() {
        return key;
    }

    public BusVariant getVariant() {
        return variant;
    }

    public Time getTime() {
        return time;
    }
    //endregion
}
