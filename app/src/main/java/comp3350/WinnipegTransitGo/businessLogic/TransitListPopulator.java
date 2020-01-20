package comp3350.WinnipegTransitGo.businessLogic;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;

/**
 * Interface between presentation and business logic (listview item generator)
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-2
 */

public interface TransitListPopulator {

    List<BusStopApiData> getBusStops(String latitude, String longitude) throws Exception;
    List<TransitListItem> getBusesOnABusStop(BusStopApiData busStop) throws Exception;

    void getBusStopFeatures(int busStopNumber, BusStopFeaturesListener callBack);
}
