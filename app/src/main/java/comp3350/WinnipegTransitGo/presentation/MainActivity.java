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
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraIdleListener;
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.BusinessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.apiService.TransitAPI;
import comp3350.WinnipegTransitGo.apiService.TransitAPIProvider;
import comp3350.WinnipegTransitGo.apiService.TransitAPIResponse;
import comp3350.WinnipegTransitGo.constants.LocationConstants;
import comp3350.WinnipegTransitGo.interfaces.ApiListenerCallback;
import comp3350.WinnipegTransitGo.interfaces.TransitListPopulator;
import comp3350.WinnipegTransitGo.interfaces.LocationListenerCallback;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.services.LocationListenerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback, LocationListenerCallback,
            OnCameraMoveStartedListener, OnCameraIdleListener, ApiListenerCallback

{


    private GoogleMap map;
    List<Marker> busStopMarkers = new ArrayList<>();
    boolean userMovingCamera = false;
    DisplayAdapter displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        TransitListPopulator listGenerator = new TransitListGenerator(this, getString(R.string.winnipeg_transit_api_key));
        listGenerator.populateTransitList();

        // TODO: 2017-06-01 uncomment this
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        displayAdapter = new DisplayAdapter(this, comp3350.WinnipegTransitGo.R.layout.listview_row);
        ListView mainListView = (ListView) findViewById(android.R.id.list);
        mainListView.setAdapter(displayAdapter);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUserLocation();
                } else {
                    //TODO: Request location from user with force
                }
                break;
            }
            default:
                break;
        }
    }


    /**
     * I expect the user to have granted the permission at this point.
     * The exception is guaranteed to never be thrown.
     * */
    public void setUserLocation() throws SecurityException {
        setDefaultLocation();
        map.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = LocationListenerService.getLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                LocationConstants.minimumTimeBetweenUpdates,
                LocationConstants.minimumDistanceBetweenUpdates,
                listener);
    }

    private void setDefaultLocation() {
        LatLng defaultLatLng = new LatLng(LocationConstants.defaultLatitude, LocationConstants.defaultLongitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 13));
    }


    @Override
    public void locationChanged(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 13));
        getBusesForLocation(location);
    }

    private void getBusesForLocation(Location location) {
        TransitAPIProvider transitAPI = TransitAPI.getAPI(
                getResources().getString(R.string.winnipeg_transit_api_key)
        );
        Call<TransitAPIResponse> call = transitAPI
                .getBusStops("1000", location.getLatitude()+"", location.getLongitude()+"", true);
        call.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                TransitAPIResponse transitAPIResponse = response.body();
                List<BusStop> busStops = transitAPIResponse.getBusStops();
                setBusStopMarkers(busStops);

            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                System.out.println("Failure ya bish");
            }
        });
    }

    private void setBusStopMarkers(List<BusStop> busStops) {
        removeBusStopMarkers();

        for (BusStop busStop: busStops) {
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
        for (Marker marker: busStopMarkers) {
            marker.remove();
        }
        busStopMarkers.clear();
    }


    @Override
    public void onCameraMoveStarted(int i) {
        if (i == OnCameraMoveStartedListener.REASON_GESTURE || i == OnCameraMoveStartedListener.REASON_API_ANIMATION) {
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
        getBusesForLocation(newLocation);
    }

    @Override
    public void onCameraIdle() {
        if (userMovingCamera) {
            userMovingCamera = false;
            updateLocationFromCamera();
        }
    }

    @Override
    public void updateListView(List<TransitListItem> transitListItemObjects)
    {
        displayAdapter.clear();
        this.displayAdapter.addAll(transitListItemObjects);
        this.displayAdapter.notifyDataSetChanged();
    }
}
