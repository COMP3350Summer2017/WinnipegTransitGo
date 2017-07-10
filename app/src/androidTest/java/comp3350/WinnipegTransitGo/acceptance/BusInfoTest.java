package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

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

    private Solo solo;

    public BusInfoTest() {
        super(MainActivity.class);
        Intent it = new Intent();
        it.putExtra(MainActivity.SHOULD_REFRESH_MAP, false); //We do not want refreshing of the map
        setActivityIntent(it);
    }

    @Override
    protected void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }
}
