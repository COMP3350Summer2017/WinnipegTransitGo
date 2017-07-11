package comp3350.WinnipegTransitGo.acceptance.acceptanceStubData;

import java.util.ArrayList;

import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.TransitListItem.TransitListItemBuilder;

/**
 * Created by rasheinstein on 2017-07-10.
 */

public class BusStubs {
    //the next bus created should have the following set
    public static boolean has_easy_access = true;
    public static boolean has_bike_rack = false;

    public static TransitListItem getBus60ToDowntown() {

        return new TransitListItemBuilder()
                .setBusNumber(60)
                .setHasEasyAccess(has_easy_access)
                .setHasBikeRack(has_bike_rack)
                .setWalkingDistance("500")
                .setBusStopName("Westbound Dafoe")
                .setDestination("Downtown")
                .createTransitListItem();
    }

    public static TransitListItem getBus160ToDowntown() {
        return new TransitListItemBuilder()
                .setBusNumber(160)
                .createTransitListItem();
    }

    public static TransitListItem getBus78ToPoloPark() {
        return new TransitListItemBuilder()
                .setBusNumber(78)
                .createTransitListItem();
    }
}
