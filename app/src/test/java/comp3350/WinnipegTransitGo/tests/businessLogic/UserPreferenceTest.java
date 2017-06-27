package comp3350.WinnipegTransitGo.tests.businessLogic;

import junit.framework.TestCase;

import comp3350.WinnipegTransitGo.businessLogic.UserPreference;

/**
 * Created by nibras on 2017-06-26.
 * Purpose: Tests the verifyAndSetRadius() method which takes in the radius as a string to set it manually
 */

public class UserPreferenceTest extends TestCase
{
    public void testverifyAndSetRadius() throws Exception
    {
        assertTrue(UserPreference.verifyAndSetRadius("500"));
        assertFalse(UserPreference.verifyAndSetRadius("100"));
        assertFalse(UserPreference.verifyAndSetRadius("Hello"));
        assertFalse(UserPreference.verifyAndSetRadius("0"));
        assertFalse(UserPreference.verifyAndSetRadius("-500"));
        assertFalse(UserPreference.verifyAndSetRadius("300.55"));
        assertTrue(UserPreference.verifyAndSetRadius("1000"));
        assertFalse(UserPreference.verifyAndSetRadius("900000000000000000000000000000000000000000000000000000000000000000000"));
        assertFalse(UserPreference.verifyAndSetRadius("/&*%@#"));
    }

}
