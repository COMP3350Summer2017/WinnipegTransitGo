package comp3350.WinnipegTransitGo.persistence.transitAPI;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Created by habib on 2017-05-28.
 */

public interface ApiListenerCallback {
    //error = -1 means there is no error
    void updateListView(List<TransitListItem> transitListItems, int error);

    void updateStopsOnMap(List<BusStop> busStops);
}
