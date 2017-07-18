package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Geographic and UTM location
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-21
 */
public class BusStopLocation {

    @SerializedName("geographic")
    private GeographicLocation geo;

    public BusStopLocation(String latitude, String longitude)
    {
        this.geo = new GeographicLocation(latitude, longitude);
    }


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

        public GeographicLocation(String latitude, String longitude)
        {
            this.latitude = latitude;
            this.longitude = longitude;
        }

    }
}
