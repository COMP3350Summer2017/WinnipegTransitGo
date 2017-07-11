package comp3350.WinnipegTransitGo.acceptance.fakeTransitData;

import java.util.ArrayList;

import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.TransitListItem.TransitListItemBuilder;

/**
 * BusStubs
 *
 * Used to create fake bus information for tests
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 7-11-2017
 */

public class BusStubs {
    //the next bus created should have the following set
    public static boolean has_easy_access = false;
    public static boolean has_bike_rack = false;

    public static TransitListItem getBus60ToDowntown() {

        return new TransitListItemBuilder()
                .setBusNumber(60)
                .setBusStopNumber(11280)
                .setHasEasyAccess(has_easy_access)
                .setHasBikeRack(has_bike_rack)
                .setWalkingDistance("500")
                .setBusStopName("Westbound Dafoe")
                .setDestination("Downtown")
                .setStatus("Early")
                .setAllTimes(new ArrayList<String>())
                .createTransitListItem();
    }

    public static String[] getBus60ExpectedStrings() {
        return new String[]{"60", "11280", "500", "Westbound Dafoe", "Downtown", "Early"};
    }

    public static TransitListItem getBus160ToBalmoralStation() {
        return new TransitListItemBuilder()
                .setBusNumber(160)
                .setBusStopNumber(12345)
                .setHasEasyAccess(has_easy_access)
                .setHasBikeRack(has_bike_rack)
                .setWalkingDistance("200")
                .setBusStopName("Westbound Dafoe")
                .setDestination("Balmoral Station")
                .setStatus("Ok")
                .setAllTimes(new ArrayList<String>())
                .createTransitListItem();
    }

    public static String[] getBus160ExpectedStrings() {
        return new String[]{"160", "12345", "200", "Westbound Dafoe", "Balmoral Station", "Ok"};
    }

    public static TransitListItem getBus78ToPoloPark() {
        return new TransitListItemBuilder()
                .setBusNumber(78)
                .setBusStopNumber(21321)
                .setHasEasyAccess(has_easy_access)
                .setHasBikeRack(has_bike_rack)
                .setWalkingDistance("400")
                .setBusStopName("Eastbound Kenaston")
                .setDestination("Polo Park")
                .setStatus("Late")
                .setAllTimes(new ArrayList<String>())
                .createTransitListItem();
    }

    public static String[] getBus78ExpectedStrings() {
        return new String[]{"78", "21321", "400", "Eastbound Kenaston", "Polo Park", "Late"};
    }
}
