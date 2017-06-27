package comp3350.WinnipegTransitGo.tests.persistence.preferences;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
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

public class PreferencesTest extends TestCase
{
    Preferences preferences;

    public void setUp() {
        preferences = new DataAccessStub();
        String dbPath = "database/PREFERENCES";
        preferences.open(dbPath);//could be null for stub
    }

    public void tearDown() {
        preferences.close();
    }

    public void testSetAndGetRadius()
    {
        int radius = 200;
        preferences.setRadius(radius);

        int returnedRadius = preferences.getRadius();

        assertTrue(radius == returnedRadius );
    }

}
