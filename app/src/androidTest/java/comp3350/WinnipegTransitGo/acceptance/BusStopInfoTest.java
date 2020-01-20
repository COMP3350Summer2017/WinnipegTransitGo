package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.acceptance.fakeTransitData.BusStubs;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 *
 * BusStopFeatures
 *
 * Tests bus stop features as provided by the
 * Transit API using fake data
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 07-11-2017
 */

public class BusStopInfoTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND_ERROR = "text not found";

    private static final String BIKE_RACK_UNAVAILABLE = "Bike Rack Available - No";
    private static final String EASY_ACCESS_UNAVAILABLE = "Easy Access Available - No";

    private Solo solo;
    private TransitListPopulator busStopShelter;
    private TransitListPopulator busStopBench;
    private TransitListPopulator busStopBenchAndShelter;

    public BusStopInfoTest() {
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
        List<BusStopApiData> busStopApiDataList = new ArrayList<>();
        BusStopApiData busStopApiData = new BusStopApiData(11280, "Westbound","625","1","2");
        busStopApiDataList.add(busStopApiData);

        BusStubs.has_bike_rack = BusStubs.has_easy_access = false;
        busStopShelter = createTransitPopulatorStub(new String[]{"Shelter"}, busStopApiDataList);
        busStopBench = createTransitPopulatorStub(new String[]{"Bench"}, busStopApiDataList);
        busStopBenchAndShelter = createTransitPopulatorStub(new String[]{"Shelter", "Bench"}, busStopApiDataList);
    }

    public void testShelter() {
        setUpCommon(busStopShelter);
        findStrings(BusStubs.getBus60ExpectedStrings());
        solo.clickInList(0, 0); //Click on position 0 in first list
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("Shelter"));
    }

    public void testBench() {
        setUpCommon(busStopBench);
        findStrings(BusStubs.getBus60ExpectedStrings());
        solo.clickInList(0, 0); //Click on position 0 in first list
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("Bench"));
    }

    public void testBenchAndShelter() {
        setUpCommon(busStopBenchAndShelter);
        findStrings(BusStubs.getBus60ExpectedStrings());
        solo.clickInList(0, 0); //Click on position 0 in first list
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(BIKE_RACK_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(EASY_ACCESS_UNAVAILABLE));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("Shelter"));
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("Bench"));
    }


    private TransitListPopulator createTransitPopulatorStub(final String[] features, final List<BusStopApiData> busStopApiDataList) {
        return new TransitListPopulator() {

            @Override
            public List<BusStopApiData> getBusStops(String latitude, String longitude) throws Exception{
                return busStopApiDataList;
            }
            @Override
            public List<TransitListItem> getBusesOnABusStop(BusStopApiData busStop)
            {
                ArrayList<TransitListItem> items = new ArrayList<>();
                items.add(BusStubs.getBus60ToDowntown());
                return items;
            }

            @Override
            public void getBusStopFeatures(int busStopNumber, BusStopFeaturesListener callBack) {
                ArrayList<String> stopFeatures = new ArrayList<>(Arrays.asList(features));
                callBack.showBusPopup(stopFeatures);
            }
        };
    }


}
