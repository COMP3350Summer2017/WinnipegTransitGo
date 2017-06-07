package comp3350.WinnipegTransitGo.persistence.location;

import android.location.Location;

/**
 * Created by rasheinstein on 2017-05-21.
 */

public interface OnLocationChanged {
    void locationChanged(Location location);
}
