package comp3350.WinnipegTransitGo.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

import comp3350.WinnipegTransitGo.tests.businessLogic.TransitListGeneratorTest;
import comp3350.WinnipegTransitGo.tests.objects.TransitListItemTest;

/**
 * AllTests class
 * Test Suit, this file contains all the files containing tests.
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-05
 */

public class AllTests
{
    public static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("All tests");
        testObjects();
        testBusiness();
        return suite;
    }

    private static void testObjects()
    {
        suite.addTestSuite(TransitListGeneratorTest.class);
    }

    private static void testBusiness()
    {
        suite.addTestSuite(TransitListItemTest.class);
    }
}

