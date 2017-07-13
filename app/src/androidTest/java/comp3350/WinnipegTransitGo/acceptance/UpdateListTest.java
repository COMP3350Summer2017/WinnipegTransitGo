package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.LinkedList;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 * Created by habib on 7/10/2017.
 */


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

public class UpdateListTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String REFRESH_ERROR = ":refresh not working";
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
        multipleBus.add(new TransitListItem.TransitListItemBuilder()
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
        multipleBus.add(new TransitListItem.TransitListItemBuilder()
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
        multipleBus.add(new TransitListItem.TransitListItemBuilder()
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
        firstBusPopulator = createTransitPopulatorStub(multipleBus, -1);

        ArrayList<TransitListItem> singleBus = new ArrayList<>();
        singleBus.add(new TransitListItem.TransitListItemBuilder().setWalkingDistance("625").setBusNumber(60).setBusStopNumber(11280).setBusStopName("Westbound").setDestination("Downtown").setStatus("Late").setAllTimes(new LinkedList<String>()).setHasBikeRack(false).setHasEasyAccess(false).createTransitListItem());
        secondBusPopulator = createTransitPopulatorStub(singleBus, -1);
    }

    public void testRefereshRate() {
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
        String expectedStrings[] = {"200", "240", "280", "160", "5291", "Eastbound",
                "UofM", "Early", "78", "5465", "Eastbound", "Polo Park", "Ok",
                "24", "5778", "WestBound", "Late"};
        for (String s : expectedStrings) {
            assertTrue(REFRESH_ERROR, solo.waitForText(s));
        }

        System.out.println("Finished the first part");


        MainActivity.setTransitListPopulator(secondBusPopulator);
        solo.waitForFragmentById(R.id.map);

        MainActivity.setUpdate(true);

        expectedStrings = new String[]{"625", "60", "11280", "Westbound", "Downtown", "Late"};

        for (String s : expectedStrings) {
            assertTrue(REFRESH_ERROR, solo.waitForText(s));
        }

        MainActivity.setUpdate(false);
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
