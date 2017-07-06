package comp3350.WinnipegTransitGo.acceptance;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * AcceptanceTests
 *  Contains all acceptance tests.
 *  Handles automatic running of all acceptance tests.
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-07-02
 */

public class AcceptanceTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(WeatherTest.class);
        suite.addTestSuite(BusTimesTest.class);
        return suite;
    }
}
