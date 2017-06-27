package comp3350.WinnipegTransitGo.presentation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * LocationSettings
 *
 * Handles location permissions and fetching.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/06/2017
 */

class LocationSettings {


    static boolean ensureLocationPermission(AppCompatActivity activity) {
        while (! isLocationPermissionSet(activity) ) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        return isLocationPermissionSet(activity);
    }


    private static boolean isLocationPermissionSet(AppCompatActivity activity) {
        return ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Get the user's last known location
     * @return location
     * @throws SecurityException if permissions are not set
     */
    static Location getLastKnownLocation(Context context) throws SecurityException {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;

        for (String provider : locationManager.getProviders(true)) {
            Location curr = locationManager.getLastKnownLocation(provider);
            if (curr != null) {
                if (bestLocation == null) {
                    bestLocation = curr;
                } else if (curr.getTime() > bestLocation.getTime()) {
                    bestLocation = curr;
                }
            }

        }
        return bestLocation;
    }
}
