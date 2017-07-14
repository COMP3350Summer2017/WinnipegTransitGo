package comp3350.WinnipegTransitGo.tests.integrationTests;

import android.content.Context;
import android.content.res.AssetManager;

import junit.framework.TestCase;

import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;

import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.persistence.preferences.DataAccessStub;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPI;
import comp3350.WinnipegTransitGo.presentation.Messages;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

/**
 * TransitListGeneratorTest class
 * Test's methods which process api responses in TransitListGenerator class
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-04
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(TransitListGenerator.class)
public class TransitListGeneratorTest extends TestCase {

    public void setUp() throws Exception {
        TransitAPI mockedApi = mock(TransitAPI.class);

        //using reflection to set stub database, as it is a static variable
        Field field = TransitAPI.class.getDeclaredField("instance");
        field.setAccessible(true);
        field.set(TransitAPI.class, mockedApi);

        //setup stub database
        //PreferencesService.setDataAccess(new DataAccessStub());

        PreferencesService.setDBPathName("C:\\Git\\TransitApplication\\app\\src\\main\\assets\\db\\" + PreferencesService.dbName);

    }






    //*******************************************************************//


    public void testPopulateTransitList() throws Exception {
        //test with stub first then real database

        String radiusInDB = "500";//default radius
        TransitListGenerator classUnderTest = PowerMockito.spy(new TransitListGenerator(null, null));

        // Change to this

        PowerMockito.doNothing().when(classUnderTest, "getBusStops", anyString(), anyString(), eq(radiusInDB));

        String latitude = "1";
        String longitude = "2";
        classUnderTest.populateTransitList(latitude, longitude);

        PowerMockito.verifyPrivate(classUnderTest, times(1)).invoke("getBusStops", anyString(), anyString(), eq(radiusInDB));
    }



    public void tearDown() {
        PreferencesService.closeDataAccess();
    }


}
