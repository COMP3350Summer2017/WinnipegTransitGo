package comp3350.WinnipegTransitGo.presentation.permissions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by rasheinstein on 2017-06-26.
 */

public class PermissionManager {


    public static boolean ensureLocationPermission(AppCompatActivity activity) {
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

}
