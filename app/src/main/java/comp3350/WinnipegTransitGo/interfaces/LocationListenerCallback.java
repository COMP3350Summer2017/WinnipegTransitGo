package comp3350.WinnipegTransitGo.interfaces;

import android.location.Location;

/**
 * Created by rasheinstein on 2017-05-21.
 */

public interface LocationListenerCallback {
    void locationChanged(Location location);
}
