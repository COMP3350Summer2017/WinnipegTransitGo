package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Geographic and UTM location
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public class Location {

    @SerializedName("geographic")
    private GeographicLocation geo;

    //region public getters

    public String getLatitude() {
        return geo.latitude;
    }

    public String getLongitude() {
        return geo.longitude;
    }
    //endregion

    class GeographicLocation {
        String latitude;
        String longitude;
    }
}
