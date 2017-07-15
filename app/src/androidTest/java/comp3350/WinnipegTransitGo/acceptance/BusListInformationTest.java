package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import comp3350.WinnipegTransitGo.CustomExceptions.TransitNoConnectionException;
import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.objects.TransitListItem.TransitListItemBuilder;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 * BusListInformationTest
 * BusTimes Acceptance Test
 * Testing of Bus Times Fetching
 * Original Provider results would vary based on location and time of day
 * Testing is done using stubs for the API Provider
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 2017-07-03
 */

public class BusListInformationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String REFRESH_ERROR = ":refresh not working";
    private Solo solo;

    private TransitListPopulator oneBusPopulator;
    private TransitListPopulator errorPopulator;
    private TransitListPopulator emptyPopulator;
    private TransitListPopulator multiBusPopulator;

    public BusListInformationTest() {
        super(MainActivity.class);
        Intent it = new Intent();
        it.putExtra(MainActivity.SHOULD_REFRESH_MAP, false);
        setActivityIntent(it);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        setupTransitListPopulatorStubs();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    private void setupTransitListPopulatorStubs() {
        List<BusStopApiData> busStopApiDataList = new ArrayList<>();
        BusStopApiData busStopApiData = new BusStopApiData(11280, "Westbound","625","1","2");
        busStopApiDataList.add(busStopApiData);

        ArrayList<TransitListItem> singleBus = new ArrayList<>();
        singleBus.add(new TransitListItemBuilder().setWalkingDistance("625").setBusNumber(60).setBusStopNumber(11280).setBusStopName("Westbound").setDestination("Downtown").setStatus("Late").setAllTimes(new LinkedList<String>()).setHasBikeRack(false).setHasEasyAccess(false).createTransitListItem());
        oneBusPopulator = createTransitPopulatorStub(singleBus, null, busStopApiDataList);


        errorPopulator = createTransitPopulatorStub(new ArrayList<TransitListItem>(),
                new TransitNoConnectionException("No Connection"), busStopApiDataList);

        emptyPopulator = createTransitPopulatorStub(new ArrayList<TransitListItem>(),
                null, busStopApiDataList);

        ArrayList<TransitListItem> multipleBus = new ArrayList<>();
        multipleBus.add(new TransitListItemBuilder()
                .setWalkingDistance("200")
                .setBusNumber(160)
                .setBusStopNumber(5291)
                .setBusStopName("Eastbound")
                .setDestination("UofM")
                .setStatus("Early")
                .setAllTimes(new LinkedList<String>())
                .setHasBikeRack(false)
                .setHasEasyAccess(false)
                .createTransitListItem());
        multipleBus.add(new TransitListItemBuilder()
                .setWalkingDistance("240")
                .setBusNumber(78)
                .setBusStopNumber(5465)
                .setBusStopName("Eastbound")
                .setDestination("Polo Park")
                .setStatus("Ok")
                .setAllTimes(new LinkedList<String>())
                .setHasBikeRack(false)
                .setHasEasyAccess(false)
                .createTransitListItem());
        multipleBus.add(new TransitListItemBuilder()
                .setWalkingDistance("280")
                .setBusNumber(24)
                .setBusStopNumber(5778)
                .setBusStopName("WestBound")
                .setDestination("UofM")
                .setStatus("Late")
                .setAllTimes(new LinkedList<String>())
                .setHasBikeRack(false)
                .setHasEasyAccess(false)
                .createTransitListItem());
        multiBusPopulator = createTransitPopulatorStub(multipleBus, null, busStopApiDataList);
    }

    public void testAPIError() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(errorPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        assertTrue(REFRESH_ERROR, solo.waitForText(getActivity().getString(R.string.Transit_Connection_Error)));
    }

    public void testMultipleBusesShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(multiBusPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        String expectedStrings[] = {"200", "240", "280", "160", "5291", "Eastbound",
                "UofM", "Early", "78", "5465", "Eastbound", "Polo Park", "Ok",
                "24", "5778", "WestBound", "Late"};
        for (String s : expectedStrings) {
            assertTrue(REFRESH_ERROR, solo.waitForText(s));
        }
    }

    public void testNoBusShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(emptyPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        assertTrue(REFRESH_ERROR, solo.waitForText("No bus information available"));
    }

    public void testOneBusShowing() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(oneBusPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        String expectedStrings[] = {"625", "Westbound", "Downtown", "Late", "60", "11280"};
        for (String s : expectedStrings) {
            assertTrue(REFRESH_ERROR, solo.waitForText(s));
        }
    }

    private TransitListPopulator createTransitPopulatorStub(final ArrayList<TransitListItem> items, final Exception exception, final List<BusStopApiData> busStopApiDataList) {
        return new TransitListPopulator() {
            @Override
            public List<BusStopApiData> getBusStops(String latitude, String longitude) throws Exception{
                if(exception != null)
                    throw exception;

                return busStopApiDataList;
            }
            @Override
            public List<TransitListItem> getBusesOnABusStop(BusStopApiData busStop)
            {
                return items;
            }

            @Override
            public void getBusStopFeatures(int busStopNumber, BusStopFeaturesListener callBack) {

            }
        };
    }
}
