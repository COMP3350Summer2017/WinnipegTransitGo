package comp3350.WinnipegTransitGo.presentation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.DatabaseService;
import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.businessLogic.TransitListPopulator;
import comp3350.WinnipegTransitGo.businessLogic.location.LocationChangeListener;
import comp3350.WinnipegTransitGo.businessLogic.location.OnLocationChanged;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.database.Database;
import comp3350.WinnipegTransitGo.persistence.transitAPI.ApiListenerCallback;


public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, OnLocationChanged,
        OnCameraMoveStartedListener, OnCameraIdleListener, ApiListenerCallback,
        OnMyLocationButtonClickListener
{
    private GoogleMap map;
    private BusListViewFragment busListViewFragment;
    TransitListPopulator listGenerator;
    boolean usingDeviceLocation = true; //whether or not values should be coming from the device
        //or the point the user moved the camera to
    Location lastLocation;

    List<Marker> busStopMarkers = new ArrayList<>();
    boolean userMovingCamera = false;
    Database database;
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        //create and/or open database
        database = DatabaseService.getDataAccess(Database.prefDatabase);
        listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        busListViewFragment = (BusListViewFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bus_list_view_fragment);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseService.closeDataAccess();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            setupMap();
            setUserLocation();
        }
    }

    private void setupMap() {
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnMyLocationButtonClickListener(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUserLocation();
                } else {
                    setDefaultLocation();
                }
            }
        }
    }


    /**
     * I expect the user to have granted the permission at this point.
     * The exception is guaranteed to never be thrown.
     */
    public void setUserLocation() throws SecurityException {
        setDefaultLocation();
        map.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = LocationChangeListener.getLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                database.getUpdateInterval(),
                database.getMinimumDistanceBetweenUpdates(),
                listener);
    }


    private void setDefaultLocation() {
        LatLng defaultLatLng = new LatLng(database.getDefaultLatitude(), database.getDefaultLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 13));
        listGenerator.populateTransitList(defaultLatLng.latitude + "", defaultLatLng.longitude + "");
    }


    @Override
    public void locationChanged(Location location) {
        if (  usingDeviceLocation ) {
            lastLocation = location;
            LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        }
        busListViewFragment.clearListView();
        listGenerator.populateTransitList(lastLocation.getLatitude() + "", lastLocation.getLongitude() + "");
    }

    private void cameraMoved(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        busListViewFragment.clearListView();
        lastLocation = location;
        listGenerator.populateTransitList(location.getLatitude() + "", location.getLongitude() + "");
    }


    public void updateStopsOnMap(List<BusStop> busStops) {
        removeBusStopMarkers();

        for (BusStop busStop : busStops) {
            double lat = Double.parseDouble(
                    busStop.getLocation().getLatitude()
            );
            double lon = Double.parseDouble(
                    busStop.getLocation().getLongitude()
            );
            String snippet = busStop.getName();
            LatLng stopLocation = new LatLng(lat, lon);
            Marker busStopMarker = map.addMarker(new MarkerOptions()
                    .position(stopLocation)
                    .snippet(snippet)
                    .title(snippet)
            );
            busStopMarkers.add(busStopMarker);
        }
    }

    private void removeBusStopMarkers() {
        for (Marker marker : busStopMarkers) {
            marker.remove();
        }
        busStopMarkers.clear();
    }


    @Override
    public void onCameraMoveStarted(int i) {
        if (i == OnCameraMoveStartedListener.REASON_GESTURE) {
            userMovingCamera = true;
        } else if (userMovingCamera) {
            userMovingCamera = false;
        }
    }


    private void updateLocationFromCamera() {
        LatLng centrePosition = map.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(centrePosition.latitude);
        newLocation.setLongitude(centrePosition.longitude);
        cameraMoved(newLocation);
    }

    @Override
    public void onCameraIdle() {
        if (userMovingCamera) {
            userMovingCamera = false;
            updateLocationFromCamera();
        }
    }

    @Override
    public void updateListView(List<TransitListItem> displayObjects) {
        busListViewFragment.updateListView(displayObjects);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        usingDeviceLocation = true;
        return false;
    }
}
