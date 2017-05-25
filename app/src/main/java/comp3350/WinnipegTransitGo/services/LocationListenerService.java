package comp3350.WinnipegTransitGo.services;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import comp3350.WinnipegTransitGo.interfaces.LocationListenerCallback;

/**
 * Created by rasheinstein on 2017-05-21.
 */

public class LocationListenerService {

    private static LocationListener locationListener = null;
    private static Location previousLocation = null;

    public static LocationListener getLocationListener(final LocationListenerCallback callback) {
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

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            };
        }
    }
}
