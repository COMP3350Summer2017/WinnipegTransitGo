package comp3350.WinnipegTransitGo.presentation;

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

import com.google.android.gms.maps.SupportMapFragment;

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
    private final Handler handler;
    private final Runnable mapRefresh;
    private boolean isUpdatesEnabled;

    public MainActivity(){
        handler = new Handler();
        mapRefresh = new Runnable() {
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

        isUpdatesEnabled = true;
        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        busListViewFragment = new BusListViewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bus_display_container, busListViewFragment).commit();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapManager = MapManager.getInstance(this, mapFragment);

        showWeather();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseService.closeDataAccess();
        mapManager.destroyMap();
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

    //Weather related code goes here
    private void showWeather() {
        TextView tempTV = (TextView) findViewById(R.id.tempText);
        ImageView weatherCondition = (ImageView) findViewById(R.id.weatherImage);

        WeatherProvider wp = new OpenWeatherMapProvider(getResources().getString(R.string.weather_api_key));
        WeatherPresenter weatherPresenter = new WeatherPresenter(tempTV, weatherCondition, wp, this);
        weatherPresenter.presentTemperature();
        weatherPresenter.presentWeather();
    }



    public void updateStopsOnMap(List<BusStop> busStops) {
        mapManager.updateStopsOnMap(busStops);
    }


    @Override
    public void updateListView(List<TransitListItem> displayObjects) {
        busListViewFragment.updateListView(displayObjects);
    }

    public void locationChanged(Location location) {
        if ( ! isUpdatesEnabled ) return;
        busListViewFragment.clearListView();
        listGenerator.populateTransitList(location.getLatitude() + "", location.getLongitude() + "");
    }

    public void beginUpdates() {
        isUpdatesEnabled = true;
        updateLocation();
        handler.postDelayed(mapRefresh, LocationService.getRefreshRate());
    }

    public void stopUpdates() {
        isUpdatesEnabled = false;
        handler.removeCallbacks(mapRefresh);
    }

    public void updateLocation() {
        if (busListViewFragment.isViewAtTop()) {
            mapManager.updateLocationFromCamera();
        }
    }

    public void showDetailedViewForBus(@NonNull TransitListItem item) {
        stopUpdates();
        mapManager.showSingleStop(item.getBusStopNumber());
        BusDetailedFragment newFragment = new BusDetailedFragment();
        Bundle args = new Bundle();
        args.putSerializable(BusDetailedFragment.TRANSIT_ITEM, item);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bus_display_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

}