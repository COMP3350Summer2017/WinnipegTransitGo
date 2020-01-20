package comp3350.WinnipegTransitGo.acceptance;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AllAcceptanceTests
 *  Contains all acceptance tests.
 *  Handles automatic running of all acceptance tests.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-07-02
 */

public class AllAcceptanceTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(RadiusAcceptanceTest.class);
        suite.addTestSuite(WeatherTest.class);
        suite.addTestSuite(BusListInformationTest.class);
        suite.addTestSuite(BusInfoTest.class);
        suite.addTestSuite(BusStopInfoTest.class);
        suite.addTestSuite(UpdateListTest.class);
        return suite;
    }
}
