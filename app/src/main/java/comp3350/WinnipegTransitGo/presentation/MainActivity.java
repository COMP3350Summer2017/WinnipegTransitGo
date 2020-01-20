package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.OpenWeatherMapProvider;
import comp3350.WinnipegTransitGo.businessLogic.PreferencesService;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.businessLogic.UserPreference;
import comp3350.WinnipegTransitGo.businessLogic.WeatherProvider;
import comp3350.WinnipegTransitGo.objects.BusStopApiData;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * MainActivity
 * <p>
 * Home Page for Transit Application
 * Activity contains a mapFragment as well as a list view to
 * show users bus stops as well as times for upcoming buses
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 21-06-2017
 */
public class MainActivity extends AppCompatActivity{
    /**
     * This argument is passed as an intent to decide whether or not we want the
     * app to update using a refresh.
     */
    public static final String SHOULD_REFRESH_MAP = "1";
    private static MapManager mapManager;
    private MainListViewFragment mainListViewFragment;
    private static TransitListPopulator listGenerator;
    private final Runnable timerThread;
    private final Handler handler;
    private boolean isUpdatesEnabled;
    private boolean shouldAutoRefresh;
    private WeatherPresenter weatherPresenter;

    public MainActivity() {
        handler = new Handler();
        timerThread = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, UserPreference.getRefreshRate());
                updateLocation();
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        copyDatabaseToDevice();

        setTransitListPopulator(new TransitListGenerator(getString(R.string.winnipeg_transit_api_key)));
        mainListViewFragment = new MainListViewFragment();
        Bundle args = new Bundle();
        args.putBoolean(SHOULD_REFRESH_MAP, true);
        mainListViewFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bus_display_container, mainListViewFragment).commit();

        shouldAutoRefresh = getIntent().getBooleanExtra(SHOULD_REFRESH_MAP, true);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapManager = MapManager.getInstance(this, mapFragment, shouldAutoRefresh);
        showWeather();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopUpdates();
        PreferencesService.closeDataAccess();
        mapManager.destroyMap();
    }

    //Code to create options menu and and the option to set radius manually
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.set_radius) {
            new OptionsMenu().setRadiusManually(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    //Weather related code goes here
    @Override
    protected void onPause() {
        super.onPause();
        stopUpdates();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //We only begin updates if auto refresh is enabled
        if (shouldAutoRefresh) {
            beginUpdates();
        }
    }

    private void showWeather() {
        TextView tempTV = (TextView) findViewById(R.id.tempText);
        ImageView weatherCondition = (ImageView) findViewById(R.id.weatherImage);

        WeatherProvider wp = new OpenWeatherMapProvider(getResources().getString(R.string.weather_api_key));
        weatherPresenter = new WeatherPresenter(tempTV, weatherCondition, wp, this);
        weatherPresenter.refreshWeather();
    }

    private void updateStopsOnMap(List<BusStopApiData> busStops) {
        mapManager.updateStopsOnMap(busStops);
    }


    public void updateListView(List<TransitListItem> displayObjects, boolean exception, String exceptionMessage) {
        if (!exception)//no errors
            mainListViewFragment.updateListView(displayObjects);
        else
            Toast.makeText(this, exceptionMessage, Toast.LENGTH_SHORT).show();
    }

    public void locationChanged(Location location) {
        getBuses(location);
    }

    private void getBuses(Location location)
    {
        try
        {
            new BusStopsCallMaker(this, listGenerator, location.getLatitude() + "", location.getLongitude() + "").execute();
            System.out.print("reached");
        }
        catch (Exception e) {}
    }

    public void handleBusStops(List<BusStopApiData> busStops, boolean exception, String exceptionMessage)
    {
        updateStopsOnMap(busStops);//should always be called as will need to clean previous markers
        if(exception)
        {
            Toast.makeText(this, exceptionMessage, Toast.LENGTH_SHORT).show();
            return;
        }

        //Make async calls for all the bus stops
        for( int i=0; i<busStops.size(); i++)
        {
            try
            {
                new BusesCallMaker(this, listGenerator, busStops.get(i)).execute();
                System.out.print("reached");
            }
            catch (Exception e) {}
        }
    }

    public void beginUpdates() {
        if ( ! isUpdatesEnabled ) {
            handler.postDelayed(timerThread, 0);
            isUpdatesEnabled = true;
        }
    }

    private void stopUpdates() {
        isUpdatesEnabled = false;
        handler.removeCallbacks(timerThread);
    }

    private void updateLocation() {
        if (mainListViewFragment.isViewAtTop()) {
            mapManager.updateLocationFromCamera();
        }
    }

    /**
     * Swaps out the bus list view fragment when a bus item is
     * clicked and shows a fragment tailored towards bus information for
     * a single bus
     *
     * @param item Bus Item
     */
    public void showDetailedViewForBus(@NonNull TransitListItem item) {
        stopUpdates();
        mapManager.showSingleStop(Integer.toString(item.getBusStopNumber()));
        BusDetailedViewFragment newFragment = new BusDetailedViewFragment();
        Bundle args = new Bundle();
        args.putSerializable(BusDetailedViewFragment.TRANSIT_ITEM, item);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bus_display_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void copyDatabaseToDevice() {
        final String DB_PATH = "db";

        String[] assetNames;
        Context context = getApplicationContext();
        File dataDirectory = context.getDir(DB_PATH, Context.MODE_PRIVATE);
        AssetManager assetManager = getAssets();

        try {

            assetNames = assetManager.list(DB_PATH);
            for (int i = 0; i < assetNames.length; i++) {
                assetNames[i] = DB_PATH + "/" + assetNames[i];
            }

            copyAssetsToDirectory(assetNames, dataDirectory);
            PreferencesService.setDBPathName(dataDirectory.toString() + "/" + PreferencesService.dbName);


        } catch (IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    private void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
        AssetManager assetManager = getAssets();

        for (String asset : assets) {
            String[] components = asset.split("/");
            String copyPath = directory.toString() + "/" + components[components.length - 1];
            char[] buffer = new char[1024];
            int count;

            File outFile = new File(copyPath);

            if (!outFile.exists()) {
                InputStreamReader in = new InputStreamReader(assetManager.open(asset));
                FileWriter out = new FileWriter(outFile);

                count = in.read(buffer);
                while (count != -1) {
                    out.write(buffer, 0, count);
                    count = in.read(buffer);
                }

                out.close();
                in.close();
            }
        }
    }

    public static void setTransitListPopulator(TransitListPopulator transitListPopulator) {
        listGenerator = transitListPopulator;
    }

    public TransitListPopulator getPopulator()
    {
        return listGenerator;
    }
}