package comp3350.WinnipegTransitGo.tests.integrationTests;

import junit.framework.TestCase;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.objects.Bus;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPI;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIProvider;
import comp3350.WinnipegTransitGo.persistence.transitAPI.TransitAPIResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static retrofit2.Response.success;

/**
 * TransitListGeneratorTest class
 * Test's methods which process api responses in TransitListGenerator class
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-04
 */

public class TransitListGeneratorTest extends TestCase {

    private Preferences preferences;
    int origDatabaseRadius;

    public void setUp() throws Exception {

        String extension = ".script";
        URL resources = getClass().getResource("/PREFERENCES.script");
        String path = resources.getPath();
        PreferencesService.setDBPathName(path.substring(0, path.length()- extension.length()));

        preferences = PreferencesService.getDataAccess();
        origDatabaseRadius = preferences.getRadius();

    }


    public void testPopulateTransitList() throws Exception
    {
        TransitAPIProvider transitAPIProvider = mock(TransitAPIProvider.class);
        String latitude = "1";
        String longitude = "2";
        String radius = "500";
        boolean walkingDistance = true;



        Call<TransitAPIResponse> apiResponse = mock(Call.class);

        TransitAPIResponse transitAPIResponse = mock(TransitAPIResponse.class);
        Response<TransitAPIResponse> response = Response.success(transitAPIResponse);

        List<BusStop> busStops = new ArrayList<>();

        int firstBusNumber = 60;
        String firstBusName = "Downtown";
        String firstWalkDistance = "300";
        BusStop firstStop = new BusStop(firstBusNumber, firstBusName, firstWalkDistance, latitude, longitude);

        int secondBusNumber = 160;
        String secondBusName = "UofM";
        String secondWalkDistance = "400";


        BusStop secondStop = new BusStop(secondBusNumber, secondBusName, secondWalkDistance, latitude, longitude);

        busStops.add(firstStop);
        busStops.add(secondStop);
        when( response.body().getBusStops()).thenReturn(busStops);

        when(apiResponse.execute()).thenReturn(response);
        when(transitAPIProvider.getBusStops(radius, latitude, longitude, walkingDistance)).thenReturn(apiResponse);



        TransitListGenerator transitListGenerator = new  TransitListGenerator(transitAPIProvider);

        List<BusStopApiData> busStopApiDatas = transitListGenerator.getBusStops(latitude, longitude);

        verify(transitAPIProvider, times(1)).getBusStops(radius, latitude, longitude, walkingDistance);
        verify(apiResponse, times(1)).execute();
        verify(transitAPIResponse, times(1)).getBusStops();

        assertTrue( busStopApiDatas != null);
        assertTrue( busStopApiDatas.size() == 2);

        BusStopApiData first = busStopApiDatas.get(0);
        assertTrue(first.getBusStopNumber() == firstStop.getNumber());
        assertTrue(first.getBusStopName().equals(firstStop.getName()));

        BusStopApiData second = busStopApiDatas.get(1);
        assertTrue(second.getBusStopNumber() == secondStop.getNumber());
        assertTrue(second.getBusStopName().equals(secondStop.getName()));
    }


    public void tearDown() throws Exception{
        preferences.setRadius(origDatabaseRadius);//back to original value
        PreferencesService.closeDataAccess();
    }


}
