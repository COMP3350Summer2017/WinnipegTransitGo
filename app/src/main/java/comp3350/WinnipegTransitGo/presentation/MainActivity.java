package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.content.res.AssetManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.DatabaseService;
import comp3350.WinnipegTransitGo.businessLogic.OpenWeatherMapProvider;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.businessLogic.WeatherProvider;
import comp3350.WinnipegTransitGo.businessLogic.location.LocationService;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.database.DataAccessObject;
import comp3350.WinnipegTransitGo.persistence.transitAPI.ApiListenerCallback;

/**
 * MainActivity
 *
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
    private BusListViewFragment busListViewFragment;
    private TransitListPopulator listGenerator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        copyDatabaseToDevice();

        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        busListViewFragment = (BusListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bus_list_view_fragment);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapManager = new MapManager(this, mapFragment);

        setMapRefreshRate();
        showWeather();
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
        final Handler handler = new Handler();
        final int refreshRate = LocationService.getRefreshRate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (shouldUpdateLocation()) {
                    mapManager.updateLocationFromCamera();
                }
                handler.postDelayed(this, refreshRate);
            }
        }, refreshRate);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseService.closeDataAccess();
    }


    public void updateStopsOnMap(List<BusStop> busStops) {
        mapManager.updateStopsOnMap(busStops);
    }


    @Override
    public void updateListView(List<TransitListItem> displayObjects) {
        busListViewFragment.updateListView(displayObjects);
    }

    public void clearListView() {
        busListViewFragment.clearListView();
    }

    public void locationChanged(Location location) {
        clearListView();
        listGenerator.populateTransitList(location.getLatitude() + "", location.getLongitude() + "");
    }

    public boolean shouldUpdateLocation() {
        return busListViewFragment.isViewAtTop();
    }

    //Code to create options menu and and the option to set radius manually
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.set_radius)
        {
            new OptionsMenu().setRadiusManually(MainActivity.this);
        }
        return super.onOptionsItemSelected(item);
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

            //Main.setDBPathName(dataDirectory.toString() + "/" + Main.dbName);
            DataAccessObject.dbPath = dataDirectory.toString() + "/" + "SC";
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