package comp3350.WinnipegTransitGo.presentation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.apiService.TransitAPI;
import comp3350.WinnipegTransitGo.apiService.TransitAPIProvider;
import comp3350.WinnipegTransitGo.apiService.TransitAPIResponse;
import comp3350.WinnipegTransitGo.constants.LocationConstants;
import comp3350.WinnipegTransitGo.interfaces.LocationListenerCallback;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.services.LocationListenerService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.google.android.gms.maps.*;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnMapReadyCallback, LocationListenerCallback {

    private GoogleMap map;
    List<Marker> busStopMarkers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        } else {
            setUserLocation();
        }
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
        TransitAPIProvider transitAPI = TransitAPI.getAPI(
                getResources().getString(R.string.winnipeg_transit_api_key)
        );
        Call<TransitAPIResponse> call = transitAPI
                .getBusStops("1000", location.getLatitude()+"", location.getLongitude()+"", true);
        call.enqueue(new Callback<TransitAPIResponse>() {
            @Override
            public void onResponse(Call<TransitAPIResponse> call, Response<TransitAPIResponse> response) {
                removeBusStopMarkers();
                TransitAPIResponse transitAPIResponse = response.body();
                List<BusStop> busStops = transitAPIResponse.getBusStops();
                addBusStopMarkers(busStops);

            }

            @Override
            public void onFailure(Call<TransitAPIResponse> call, Throwable t) {
                System.out.println("Failure ya bish");
            }
        });
    }

    private void addBusStopMarkers(List<BusStop> busStops) {

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

}
