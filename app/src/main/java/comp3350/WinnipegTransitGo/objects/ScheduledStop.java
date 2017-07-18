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
    private BusVariant variant;
    private Bus bus;

    @SerializedName("times")
    private Time time;

    public ScheduledStop(BusVariant variant, Time time, Bus busFeature)
    {
        this. variant = variant;
        this.time = time;
        this.bus = busFeature;
    }

    //region public getters

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
