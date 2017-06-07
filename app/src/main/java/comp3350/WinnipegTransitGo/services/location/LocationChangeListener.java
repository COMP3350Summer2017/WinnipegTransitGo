package comp3350.WinnipegTransitGo.services.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;


public class LocationChangeListener {

    private static LocationListener locationListener = null;

    public static LocationListener getLocationListener(final OnLocationChanged callback) {
        if (locationListener != null) {
            return locationListener;
        } else {
            return new LocationListener() {
                @Override
                public void onLocationChanged(android.location.Location location) {
                    callback.locationChanged(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle bundle) {
                    // Doesn't apply
                }

                @Override
                public void onProviderEnabled(String s) {
                    // Doesn't apply
                }

                @Override
                public void onProviderDisabled(String s) {
                    // Doesn't apply
                }
            };
        }
    }
}
