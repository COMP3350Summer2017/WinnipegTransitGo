package comp3350.WinnipegTransitGo.tests.integrationTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by habib on 7/17/2017.
 */

public class AllIntegrationTests {
    private static TestSuite suite;

    public static Test suite()
    {
        suite = new TestSuite("Integration tests");
        suite.addTestSuite(TransitListGeneratorTest.class);
        suite.addTestSuite(UserPreferenceTest.class);
        suite.addTestSuite(DataAccessObjectTest.class);
        return suite;
    }
}
