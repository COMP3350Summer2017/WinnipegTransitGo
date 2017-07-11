package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.LinkedList;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.acceptance.fakeTransitData.BusStubs;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;


/**
 * BusListInformationTest
 * BusTimes Acceptance Test
 * Testing of Bus Times Fetching
 * Original Provider results would vary based on location and time of day
 * Testing is done using stubs for the API Provider
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-07-03
 */

public class UpdateListTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND_ERROR = "text not found";
    private Solo solo;

    private TransitListPopulator firstBusPopulator;
    private TransitListPopulator secondBusPopulator;

    public UpdateListTest() {
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

        ArrayList<TransitListItem> multipleBus = new ArrayList<>();
        multipleBus.add(BusStubs.getBus60ToDowntown());
        multipleBus.add(BusStubs.getBus160ToBalmoralStation());
        multipleBus.add(BusStubs.getBus78ToPoloPark());
        firstBusPopulator = createTransitPopulatorStub(multipleBus, -1);

        ArrayList<TransitListItem> singleBus = new ArrayList<>();
        singleBus.add(BusStubs.getBus36ToHealthSciences());
        secondBusPopulator = createTransitPopulatorStub(singleBus, -1);
    }

    private void findStrings(String[] expectedStrings) {
        for (String s : expectedStrings) {
            assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(s));
        }
    }

    public void testRefreshRate() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        MainActivity.setTransitListPopulator(firstBusPopulator);
        solo.waitForFragmentById(R.id.map);
        final Location l = new Location("");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().locationChanged(l);
            }
        });
        findStrings(BusStubs.getBus60ExpectedStrings());
        findStrings(BusStubs.getBus160ExpectedStrings());
        findStrings(BusStubs.getBus78ExpectedStrings());


        MainActivity.setTransitListPopulator(secondBusPopulator);
        solo.waitForFragmentById(R.id.map);
        getActivity().beginUpdates();
        findStrings(BusStubs.getBus36ExpectedStrings());
        getActivity().stopUpdates();
    }


    private TransitListPopulator createTransitPopulatorStub(final ArrayList<TransitListItem> items, final int errCode) {
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

            }
        };
    }
}
