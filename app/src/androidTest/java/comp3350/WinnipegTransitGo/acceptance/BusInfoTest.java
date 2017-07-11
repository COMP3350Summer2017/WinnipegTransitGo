package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.acceptance.fakeTransitData.BusStubs;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
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
    private static final String TEXT_NOT_FOUND_ERROR = "text not found";

    private static final String BIKE_RACK_AVAILABLE = "Bike Rack Available - Yes";
    private static final String BIKE_RACK_UNAVAILABLE = "Bike Rack Available - No";
    private static final String EASY_ACCESS_AVAILABLE = "Easy Access Available - Yes";
    private static final String EASY_ACCESS_UNAVAILABLE = "Easy Access Available - No";

    private Solo solo;
    private TransitListPopulator busHasBikeRackPopulator;
    private TransitListPopulator busHasEasyAccessPopulator;
    private TransitListPopulator busHasNoFeaturesPopulator;
    private TransitListPopulator busFirstRackSecondEasy;
    
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

    private void setUpCommon(TransitListPopulator pop) {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(pop);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(s));
        }
    }

    private void setUpTransitListPopulatorStubs() {
        busHasBikeRackPopulator = createTransitPopulatorStub(listWithSingleBikeRack(), -1, new ArrayList<String>());
        busHasEasyAccessPopulator = createTransitPopulatorStub(listWithSingleEasyAccess(), -1, new ArrayList<String>());
        busHasNoFeaturesPopulator = createTransitPopulatorStub(listWithNoFeatures(), -1, new ArrayList<String>());
        busFirstRackSecondEasy = createTransitPopulatorStub(listWithFirstRackSecondEasy(), -1, new ArrayList<String>());
    }

    private ArrayList<TransitListItem> listWithSingleBikeRack() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        BusStubs.has_bike_rack = true;
        BusStubs.has_easy_access = false;
        ret.add(BusStubs.getBus60ToDowntown());
        return ret;
    }

    private ArrayList<TransitListItem> listWithSingleEasyAccess() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        BusStubs.has_bike_rack = false;
        BusStubs.has_easy_access = true;
        ret.add(BusStubs.getBus60ToDowntown());
        return ret;
    }

    private ArrayList<TransitListItem> listWithFirstRackSecondEasy() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        BusStubs.has_bike_rack = true;
        BusStubs.has_easy_access = false;
        ret.add(BusStubs.getBus60ToDowntown());
        BusStubs.has_bike_rack = false;
        BusStubs.has_easy_access = true;
        ret.add(BusStubs.getBus160ToBalmoralStation());
        return ret;
    }

    private ArrayList<TransitListItem> listWithNoFeatures() {
        ArrayList<TransitListItem> ret = new ArrayList<>();
        BusStubs.has_bike_rack = false;
        BusStubs.has_easy_access = false;
        ret.add(BusStubs.getBus60ToDowntown());
        ret.add(BusStubs.getBus78ToPoloPark());
        return ret;
    }

    public void testSingleBikeRack() {
        setUpCommon(busHasBikeRackPopulator);
        findStrings(BusStubs.getBus60ExpectedStrings());
        solo.clickInList(0, 0); //Click on position 0 in first list
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_AVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
    }

    public void testSingleEasyAccess() {
        setUpCommon(busHasEasyAccessPopulator);
        findStrings(BusStubs.getBus60ExpectedStrings());
        solo.clickInList(0, 0); //Click on position 0 in first list
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_AVAILABLE));
    }

    public void testNoFeatures() {
        setUpCommon(busHasNoFeaturesPopulator);
        findStrings(BusStubs.getBus60ExpectedStrings());
        for (int i = 1; i <= 2; i++) {
            solo.clickInList(i, 0);
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
            solo.goBack();
        }
    }

    public void testFirstRackSecondEasy() {
        setUpCommon(busFirstRackSecondEasy);
        findStrings(BusStubs.getBus60ExpectedStrings());
        //first bike rack
        solo.clickInList(1, 0);
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_AVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
        solo.goBack();
        //second easy
        solo.clickInList(2, 0);
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_AVAILABLE));
        solo.goBack();
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
