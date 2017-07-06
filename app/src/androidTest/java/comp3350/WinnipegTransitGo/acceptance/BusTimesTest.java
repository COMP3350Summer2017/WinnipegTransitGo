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
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 * BusTimesTest
 * BusTimes Acceptance Test
 * Testing of Bus Times Fetching
 * Original Provider results would vary based on location and time of day
 * Testing is done using stubs for the API Provider
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 2017-07-03
 */

public class BusTimesTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String REFRESH_ERROR = ":refresh not working";
    private Solo solo;

    private TransitListPopulator oneBusPopulator;
    private TransitListPopulator errorPopulator;
    private TransitListPopulator emptyPopulator;
    private TransitListPopulator multiBusPopulator;

    public BusTimesTest() {
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
        ArrayList<TransitListItem> singleBus = new ArrayList<>();
        singleBus.add(new TransitListItem("625", 60, 11280, "Westbound", "Downtown", "Late", new LinkedList<String>()));
        oneBusPopulator = createTransitPopulatorStub(singleBus, -1);

        errorPopulator = createTransitPopulatorStub(new ArrayList<TransitListItem>(),
                R.string.Transit_Connection_Error);

        emptyPopulator = createTransitPopulatorStub(new ArrayList<TransitListItem>(),
                -1);

        ArrayList<TransitListItem> multipleBus = new ArrayList<>();
        multipleBus.add(new TransitListItem("200", 160, 5291, "Eastbound", "UofM", "Early", new LinkedList<String>()));
        multipleBus.add(new TransitListItem("240", 78, 5465, "Eastbound", "Polo Park", "Ok", new LinkedList<String>()));
        multipleBus.add(new TransitListItem("280", 24, 5778, "WestBound", "UofM", "Late", new LinkedList<String>()));
        multiBusPopulator = createTransitPopulatorStub(multipleBus, -1);
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
        for (String s: expectedStrings) {
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
        for (String s: expectedStrings) {
            assertTrue(REFRESH_ERROR, solo.waitForText(s));
        }
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
        };
    }
}
