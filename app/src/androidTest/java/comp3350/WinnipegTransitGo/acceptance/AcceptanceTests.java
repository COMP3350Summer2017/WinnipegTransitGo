package comp3350.WinnipegTransitGo.acceptance;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by Unknown on 2017-07-02.
 */

public class AcceptanceTests {
    public static TestSuite suite;

    public static Test suite() {
        suite = new TestSuite("Acceptance tests");
        suite.addTestSuite(WeatherTest.class);
        return suite;
    }
}
