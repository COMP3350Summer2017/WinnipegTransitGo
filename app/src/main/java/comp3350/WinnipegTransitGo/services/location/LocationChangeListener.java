package comp3350.WinnipegTransitGo.services.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by rasheinstein on 2017-05-21.
 */

public class LocationChangeListener {

    private static LocationListener locationListener = null;
    private static Location previousLocation = null;

    public static LocationListener getLocationListener(final OnLocationChanged callback) {
        if (locationListener != null) {
            return locationListener;
        } else {
            return new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    previousLocation = location;
                    callback.locationChanged(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle bundle) {
                    // Dosen't apply
                }

                @Override
                public void onProviderEnabled(String s) {
                    // Dosen't apply
                }

                @Override
                public void onProviderDisabled(String s) {
                    // Dosen't apply
                }
            };
        }
    }
}
