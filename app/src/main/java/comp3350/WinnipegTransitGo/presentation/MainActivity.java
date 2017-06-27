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
import comp3350.WinnipegTransitGo.businessLogic.preferencesService;
import comp3350.WinnipegTransitGo.businessLogic.OpenWeatherMapProvider;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.businessLogic.WeatherProvider;
import comp3350.WinnipegTransitGo.businessLogic.LocationService;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.transitAPI.ApiListenerCallback;

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
public class MainActivity extends AppCompatActivity
        implements ApiListenerCallback {
    private MapManager mapManager;
    private MainListViewFragment mainListViewFragment;
    private TransitListPopulator listGenerator;
    private final Runnable timerThread;
    private final Handler handler;
    private boolean isUpdatesEnabled;

    public MainActivity() {
        handler = new Handler();
        timerThread = new Runnable() {
            @Override
            public void run() {
                updateLocation();
                handler.postDelayed(this, LocationService.getRefreshRate());
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        copyDatabaseToDevice();

        isUpdatesEnabled = true;
        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        mainListViewFragment = new MainListViewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bus_display_container, mainListViewFragment).commit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapManager = MapManager.getInstance(this, mapFragment);
        setMapRefreshRate();
        showWeather();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferencesService.closeDataAccess();
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
        handler.removeCallbacks(timerThread);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setMapRefreshRate();
    }

    private void showWeather() {
        TextView tempTV = (TextView) findViewById(R.id.tempText);
        ImageView weatherCondition = (ImageView) findViewById(R.id.weatherImage);

        WeatherProvider wp = new OpenWeatherMapProvider(getResources().getString(R.string.weather_api_key));
        WeatherPresenter weatherPresenter = new WeatherPresenter(tempTV, weatherCondition, wp, this);
        weatherPresenter.presentTemperature();
        weatherPresenter.presentWeather();
    }

    private void setMapRefreshRate() {
        final int refreshRate = LocationService.getRefreshRate();
        handler.postDelayed(timerThread, refreshRate);
    }

    public void updateStopsOnMap(List<BusStop> busStops) {
        mapManager.updateStopsOnMap(busStops);
    }


    @Override
    public void updateListView(List<TransitListItem> displayObjects, int error) {

        if (listGenerator.isValid(error))//no errors
            mainListViewFragment.updateListView(displayObjects);
        else
            Toast.makeText(this, this.getString(error), Toast.LENGTH_SHORT).show();
    }

    public void locationChanged(Location location) {
        if (!isUpdatesEnabled) return;
        mainListViewFragment.clearListView();
        listGenerator.populateTransitList(location.getLatitude() + "", location.getLongitude() + "");
    }

    public void beginUpdates() {
        isUpdatesEnabled = true;
        updateLocation();
        handler.postDelayed(timerThread, LocationService.getRefreshRate());
    }

    public void stopUpdates() {
        isUpdatesEnabled = false;
        handler.removeCallbacks(timerThread);
    }

    public void updateLocation() {
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
        mapManager.showSingleStop(item.getBusStopNumber());
        DetailedFragment newFragment = new DetailedFragment();
        Bundle args = new Bundle();
        args.putSerializable(DetailedFragment.TRANSIT_ITEM, item);
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
            preferencesService.setDBPathName(dataDirectory.toString() + "/" + preferencesService.dbName);


        } catch (IOException ioe) {
            Messages.warning(this, "Unable to access application data: " + ioe.getMessage());
        }
    }

    public void copyAssetsToDirectory(String[] assets, File directory) throws IOException {
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

}