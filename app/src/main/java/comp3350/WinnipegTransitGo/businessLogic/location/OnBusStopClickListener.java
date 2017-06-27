package comp3350.WinnipegTransitGo.businessLogic.location;

import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * OnBusStopClickListener
 *
 * Interface that specifies a method which any
 * object wanting to display the position of a transitlistitem
 * on the map shows.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 2017/06/24
 */

public interface OnBusStopClickListener {
    void showLocationForBus(String busNumber);
}
