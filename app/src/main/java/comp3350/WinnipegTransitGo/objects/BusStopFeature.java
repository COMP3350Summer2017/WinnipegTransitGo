package comp3350.WinnipegTransitGo.objects;

/**
 * BusStopFeature
 *  This class holds the specific feature of a bus stop from the API
 *  cannot be merged with BusStop!
 *      BusStop cannot contain a list of BusStopFeatures according to Winnipeg Transit
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-07-03
 */

public class BusStopFeature {
    private String name;
    private int count;

    //region public getters
    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
    //endregion
}
