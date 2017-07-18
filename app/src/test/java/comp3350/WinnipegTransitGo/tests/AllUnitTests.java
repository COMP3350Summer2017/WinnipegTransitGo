package comp3350.WinnipegTransitGo.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import comp3350.WinnipegTransitGo.tests.businessLogic.ReminderTimeProcessingTest;
import comp3350.WinnipegTransitGo.tests.businessLogic.TransitListGeneratorTest;
import comp3350.WinnipegTransitGo.tests.businessLogic.UserPreferenceTest;
import comp3350.WinnipegTransitGo.tests.objects.TransitListItemTest;
import comp3350.WinnipegTransitGo.tests.persistence.preferences.DataAccessStubTest;

/**
 * AllUnitTests class
 * Test Suit, this file contains all the files containing tests.
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-05
 */

public class AllUnitTests
{
    private static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testObjects();
        testBusiness();
        testPersistence();
        return suite;
    }

    private static void testPersistence() { suite.addTestSuite(DataAccessStubTest.class);}

    private static void testObjects()
    {
        suite.addTestSuite(TransitListGeneratorTest.class);
    }

    private static void testBusiness()
    {
        suite.addTestSuite(ReminderTimeProcessingTest.class);
        suite.addTestSuite(TransitListItemTest.class);
        suite.addTestSuite(UserPreferenceTest.class);
    }

}

