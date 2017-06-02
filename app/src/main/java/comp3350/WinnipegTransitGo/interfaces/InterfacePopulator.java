package comp3350.WinnipegTransitGo.interfaces;

/**
 * Layer between presentation and business logic (listview item generator)
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-2
 */

public interface InterfacePopulator {

    void getListOfBusStops(String lat, String lon);
}
