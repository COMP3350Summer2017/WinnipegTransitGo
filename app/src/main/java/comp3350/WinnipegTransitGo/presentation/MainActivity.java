package comp3350.WinnipegTransitGo.presentation;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

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

        if (savedInstanceState != null) {
            return;
        }

        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        busListViewFragment = new BusListViewFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.bus_display_container, busListViewFragment).commit();

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
                    updateLocation();
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


    public void showDetailedViewForBus(@NonNull TransitListItem item) {
        BusDetailedFragment newFragment = new BusDetailedFragment();
        Bundle args = new Bundle();
        args.putSerializable(BusDetailedFragment.TRANSIT_ITEM, item);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.bus_display_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void updateLocation() {
        mapManager.updateLocationFromCamera();
    }

    public MapManager getMapManager() {
        return mapManager;
    }
}
