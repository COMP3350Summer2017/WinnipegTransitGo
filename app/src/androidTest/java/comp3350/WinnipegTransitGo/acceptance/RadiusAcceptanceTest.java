package comp3350.WinnipegTransitGo.acceptance;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;
import comp3350.WinnipegTransitGo.presentation.MainActivity;

/**
 *
 * Radius Acceptance Tests
 *
 * Testing the interaction for user to set their default radius.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 17-07-2017
 */

public class RadiusAcceptanceTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String TEXT_NOT_FOUND_ERROR = "text not found";
    private static final String VIEW_NOT_FOUND_ERROR = "view not found";
    private static final String INCORRECT_RADIUS_ERROR = "Radius not correct";
    private Solo solo;
    private UserPreference userPreference;

    public RadiusAcceptanceTest() {
        super(MainActivity.class);
        Intent it = new Intent();
        it.putExtra(MainActivity.SHOULD_REFRESH_MAP, false);
        setActivityIntent(it);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        userPreference = UserPreference.getUserPreference();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        userPreference.verifyAndSetRadius("500");
        PreferencesService.closeDataAccess();
    }


    public void testSetValidRadius() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        solo.pressMenuItem(0);
        assertTrue(VIEW_NOT_FOUND_ERROR, solo.waitForView(R.id.set_radius_popup));
        solo.clearEditText(0);
        solo.enterText(0, "700");
        solo.clickOnButton("OK");
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("The nearby bus stop radius is set to: 700"));
        assertTrue(INCORRECT_RADIUS_ERROR, userPreference.getRadius() == 700);

        solo.pressMenuItem(0);
        assertTrue(VIEW_NOT_FOUND_ERROR, solo.waitForView(R.id.set_radius_popup));
        solo.clearEditText(0);
        solo.enterText(0, "500");
        solo.clickOnButton("OK");
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText("The nearby bus stop radius is set to: 500"));
        assertTrue(INCORRECT_RADIUS_ERROR, userPreference.getRadius() == 500);
    }


    public void testSetInvalidRadius() {
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        userPreference.verifyAndSetRadius("800");

        solo.pressMenuItem(0);
        assertTrue(VIEW_NOT_FOUND_ERROR, solo.waitForView(R.id.set_radius_popup));
        solo.clearEditText(0);
        solo.enterText(0, "1001");
        solo.clickOnButton("OK");
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(solo.getString(R.string.Radius_inputLimit_message)));
        assertTrue(INCORRECT_RADIUS_ERROR, userPreference.getRadius() == 800);

        solo.pressMenuItem(0);
        assertTrue(VIEW_NOT_FOUND_ERROR, solo.waitForView(R.id.set_radius_popup));
        solo.clearEditText(0);
        solo.enterText(0, "100");
        solo.clickOnButton("OK");
        assertTrue(TEXT_NOT_FOUND_ERROR, solo.waitForText(solo.getString(R.string.Radius_inputLimit_message)));
        assertTrue(INCORRECT_RADIUS_ERROR, userPreference.getRadius() == 800);
    }
}