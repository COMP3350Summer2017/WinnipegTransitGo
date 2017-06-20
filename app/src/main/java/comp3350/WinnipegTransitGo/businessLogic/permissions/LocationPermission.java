package comp3350.WinnipegTransitGo.businessLogic.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 19-06-2017
 */

public class LocationPermission {
    public static void ensureLocationPermission(AppCompatActivity activity) {
        while (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
    }
}
