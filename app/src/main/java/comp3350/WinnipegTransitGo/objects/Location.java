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
    private UTMLocation utm;

    @SerializedName("geographic")
    private GeographicLocation geo;

    //region public getters
    public String getUTMZone() {
        return utm.zone;
    }

    public int getX() {
        return utm.x;
    }

    public int getY() {
        return utm.y;
    }

    public String getLatitude() {
        return geo.latitude;
    }

    public String getLongitude() {
        return geo.longitude;
    }
    //endregion

    class UTMLocation {
        String zone;
        int x;
        int y;
    }

    class GeographicLocation {
        String latitude;
        String longitude;
    }
}
