package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.LinkedList;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.TransitListItem.TransitListItemBuilder;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 * BusInfoTest
 *
 * Tests for the information about the next upcoming bus.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 10-07-2017
 */

public class BusInfoTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND_ERROR = "text not fount";

    private static final String BIKE_RACK_AVAILABLE = "Bike Rack Available - Yes";
    private static final String BIKE_RACK_UNAVAILABLE = "Bike Rack Available - No";
    private static final String EASY_ACCESS_AVAILABLE = "Easy Access Available - Yes";
    private static final String EASY_ACCESS_UNAVAILABLE = "Easy Access Available - No";

    private Solo solo;
    private TransitListPopulator busHasBikeRackPopulator;
    private TransitListPopulator busHasEasyAccessPopulator;
    private TransitListPopulator busHasNoFeaturesPopulator;
    private TransitListPopulator firstBusRackSecondBusEasyAccess;
    private TransitListPopulator firstBusEasyAccessSecondBusBikeRack;
    private TransitListPopulator allBusesEasyAccessBikeRack;
    
    public BusInfoTest() {
        super(MainActivity.class);
        Intent it = new Intent();
        it.putExtra(MainActivity.SHOULD_REFRESH_MAP, false); //We do not want refreshing of the map
        setActivityIntent(it);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        setUpTransitListPopulatorStubs();
    }

    private void setUpTransitListPopulatorStubs() {
        busHasBikeRackPopulator = createTransitPopulatorStub(listWithSingleBikeRack(), -1, new ArrayList<String>());
        busHasEasyAccessPopulator = createTransitPopulatorStub(listWithSingleEasyAccess(), -1, new ArrayList<String>());
    }

    private ArrayList<TransitListItem> listWithSingleBikeRack() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        ret.add(new TransitListItemBuilder()
                .setBusNumber(60)
                .setHasBikeRack(true)
                .setStatus("Late")
                .setBusStopName("Westbound Party Club")
                .setWalkingDistance("540")
                .setAllTimes(new LinkedList<String>())
                .createTransitListItem());
        return ret;
    }

    private ArrayList<TransitListItem> listWithSingleEasyAccess() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        ret.add(new TransitListItemBuilder()
                .setBusNumber(60)
                .setHasEasyAccess(true)
                .setStatus("Late")
                .setBusStopName("Westbound Party Club")
                .setWalkingDistance("540")
                .setAllTimes(new LinkedList<String>())
                .createTransitListItem());
        return ret;
    }

    public void testSingleBikeRack() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(busHasBikeRackPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        String expectedStrings[] = {"60", "Late", "Westbound Party Club", "540"};
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(s));
        }
        View v = solo.getView(R.id.list_view_row);
        solo.clickOnView(v);
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_AVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
    }

    public void testSingleEasyAccess() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(busHasEasyAccessPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        String expectedStrings[] = {"60", "Late", "Westbound Party Club", "540"};
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(s));
        }
        View v = solo.getView(R.id.list_view_row);
        solo.clickOnView(v);
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_AVAILABLE));
    }

    private TransitListPopulator createTransitPopulatorStub(final ArrayList<TransitListItem> items, final int errCode,
                                                            final ArrayList<String> stopFeatures) {
        return new TransitListPopulator() {
            @Override
            public void populateTransitList(String latitude, String longitude) {
                getActivity().updateListView(items, errCode);
            }

            @Override
            public boolean isValid(int error) {
                return error == -1;
            }

            @Override
            public void getBusStopFeatures(int busStopNumber, BusStopFeaturesListener callBack) {
                callBack.showBusPopup(stopFeatures);
            }
        };
    }


}
