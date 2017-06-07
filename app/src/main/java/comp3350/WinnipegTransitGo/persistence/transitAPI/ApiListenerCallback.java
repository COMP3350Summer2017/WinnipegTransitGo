package comp3350.WinnipegTransitGo.persistence.transitAPI;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Created by habib on 2017-05-28.
 */

public interface ApiListenerCallback {
    void updateListView(List<TransitListItem> transitListItems);

    void updateStopsOnMap(List<BusStop> busStops);
}
