package comp3350.WinnipegTransitGo.tests.integrationTests;

import junit.framework.TestCase;

import java.net.URL;

import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;
import comp3350.WinnipegTransitGo.persistence.preferences.DataAccessStub;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;

/**
 * Created by nibras on 2017-06-26.
 * Purpose: Tests the verifyAndSetRadius() method which takes in the radius as a string to set it manually
 */

public class UserPreferenceTest extends TestCase
{
    private Preferences preferences;
    private int origDatabaseRadius;

    public void setUp() throws Exception
    {
        String extension = ".script";
        URL resources = getClass().getResource("/PREFERENCES.script");
        String path = resources.getPath();
        PreferencesService.setDBPathName(path.substring(0, path.length()- extension.length()));

        preferences = PreferencesService.getDataAccess();
        origDatabaseRadius = preferences.getRadius();
    }

    public void testverifyAndSetRadius() throws Exception
    {
        assertTrue(UserPreference.verifyAndSetRadius("500"));
        assertFalse(UserPreference.verifyAndSetRadius("100"));
        assertFalse(UserPreference.verifyAndSetRadius("Hello"));
        assertFalse(UserPreference.verifyAndSetRadius(""));
        assertFalse(UserPreference.verifyAndSetRadius("0"));
        assertFalse(UserPreference.verifyAndSetRadius("-500"));
        assertFalse(UserPreference.verifyAndSetRadius("300.55"));
        assertTrue(UserPreference.verifyAndSetRadius("1000"));
        assertFalse(UserPreference.verifyAndSetRadius("900000000000000000000000000000000000000000000000000000000000000000000"));
        assertFalse(UserPreference.verifyAndSetRadius("/&*%@#"));
    }

    public void tearDown() throws Exception
    {
        preferences.setRadius(origDatabaseRadius);//back to original value
        PreferencesService.closeDataAccess();
    }
}
