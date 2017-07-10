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
    Bus bus;

    @SerializedName("times")
    Time time;

    public ScheduledStop(String key, BusVariant variant, Time time, Bus busFeature)
    {
        this.key = key;
        this. variant = variant;
        this.time = time;
        this.bus = busFeature;
    }

    //region public getters
    public String getKey() {
        return key;
    }

    public BusVariant getVariant() {
        return variant;
    }

    public Bus getBus() {
        return bus;
    }

    public Time getTime() {
        return time;
    }
    //endregion
}
