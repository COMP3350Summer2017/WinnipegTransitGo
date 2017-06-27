package comp3350.WinnipegTransitGo.businessLogic.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import comp3350.WinnipegTransitGo.businessLogic.preferencesService;

/**
 * LocationService
 * <p>
 * Provides methods and functionality for accessing
 * location services and setting required permissions
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 20-06-2017
 */
public class LocationService {

    public static Location getLastKnownLocation(Context context) throws SecurityException {
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

    public static int getRefreshRate() {
        return preferencesService.getDataAccess().getRefreshRate();
    }
}
