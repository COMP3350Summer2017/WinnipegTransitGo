package comp3350.WinnipegTransitGo.presentation;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.SupportMapFragment;

import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.DatabaseService;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        busListViewFragment = (BusListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bus_list_view_fragment);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapManager = new MapManager(this, mapFragment);

        setMapRefreshRate();
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

}
