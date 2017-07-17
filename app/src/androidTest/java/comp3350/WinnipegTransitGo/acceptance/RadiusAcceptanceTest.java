package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.location.Location;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.acceptance.fakeTransitData.BusStubs;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.BusStopFeaturesListener;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 * Created by rasheinstein on 2017-07-17.
 */

public class RadiusAcceptanceTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND_ERROR = "text not found";
    private static final String VIEW_NOT_FOUND_ERROR = "view not found";
    private Solo solo;


    public RadiusAcceptanceTest() {
        super(MainActivity.class);
        Intent it = new Intent();
        it.putExtra(MainActivity.SHOULD_REFRESH_MAP, false);
        setActivityIntent(it);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }


    public void testSetRadius() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);


        solo.clickOnMenuItem("Set Radius");

        assertTrue(VIEW_NOT_FOUND_ERROR, solo.waitForView(R.id.set_radius_popup));

        solo.clearEditText(0);
        solo.enterText(0, "700");
        solo.clickOnButton("OK");

        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("The nearby bus stop radius is set to: 700"));
    }
}