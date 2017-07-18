package comp3350.WinnipegTransitGo.tests.integrationTests;

import junit.framework.TestCase;

import java.net.URL;

import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.persistence.preferences.DataAccessStub;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;

/**
 * TransitListItemTest class
 * Test's compare method in TransitListItem (which is used for sorting)
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-05
 */

public class DataAccessObjectTest extends TestCase
{
    private Preferences preferences;
    private int origDatabaseRadius;

    public void setUp() throws  Exception {
        String extension = ".script";
        URL resources = getClass().getResource("/PREFERENCES.script");
        String path = resources.getPath();
        PreferencesService.setDBPathName(path.substring(0, path.length()- extension.length()));
        preferences = PreferencesService.getDataAccess();
        origDatabaseRadius = preferences.getRadius();
    }

    public void tearDown() throws Exception{
        preferences.setRadius(origDatabaseRadius);
        preferences.close();
    }

    public void testSetAndGetRadius() throws Exception
    {
        int radius = 200;
        preferences.setRadius(radius);

        int returnedRadius = preferences.getRadius();

        assertTrue(radius == returnedRadius);
    }

}
