package comp3350.WinnipegTransitGo.businessLogic.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;

import comp3350.WinnipegTransitGo.businessLogic.DatabaseService;

public class LocationService {


    public static Location getLastKnownLocation(AppCompatActivity activity) throws SecurityException {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;


        for (String provider: locationManager.getProviders(true)) {
            Location curr = locationManager.getLastKnownLocation(provider);
            if (curr != null) {
                if (bestLocation == null) {
                    bestLocation = curr;
                }else if (curr.getTime() > bestLocation.getTime()) {
                    bestLocation = curr;
                }
            }

        }
        return bestLocation;
    }

    public static int getRefreshRate() {
        return DatabaseService.getDataAccess("stub").getRefreshRate();
    }
}
