package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;

/**
 * Layer between presentation and business logic (listview item generator)
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-2
 */

public interface TransitListPopulator {

    void populateTransitList(String latitude, String longitude);
    boolean isValid(int error);

    void getBusStopFeatures(int busStopNumber, BusStopFeaturesListener callBack);
}
