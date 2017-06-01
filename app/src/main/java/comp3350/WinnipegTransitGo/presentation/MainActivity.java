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
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.constants.LocationConstants;
import comp3350.WinnipegTransitGo.interfaces.ApiListenerCallback;
import comp3350.WinnipegTransitGo.interfaces.LocationListenerCallback;
import comp3350.WinnipegTransitGo.objects.Display;
import comp3350.WinnipegTransitGo.BusinessLogic.DisplayCreator;
import comp3350.WinnipegTransitGo.services.LocationListenerService;
import comp3350.WinnipegTransitGo.presentation.DisplayAdapter;

public class MainActivity extends Activity implements OnMapReadyCallback, LocationListenerCallback, ApiListenerCallback {

    private GoogleMap map;
    private DisplayAdapter displayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);

        DisplayCreator ld=new DisplayCreator(this);
        ld.getListOfBusStops();
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        displayAdapter = new DisplayAdapter(this, comp3350.WinnipegTransitGo.R.layout.listview_row);

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
                    //User did not give us location access
                }
                return;
            }
        }
    }


    /**
     * I expect the user to have granted the permission at this point.
     * The exception is guaranteed to never be thrown.
     * */
    public void setUserLocation() throws SecurityException {
        map.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener listener = LocationListenerService.getLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                LocationConstants.minimumTimeBetweenUpdates,
                LocationConstants.minimumDistanceBetweenUpdates,
                listener);
    }


    @Override
    public void makeUseOfNewLocation(Location location) {
        LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 13));
    }

    @Override
    public void updateListView(List<Display> displayObjects)
    {
        this.displayAdapter.clear();
        this.displayAdapter.addAll(displayObjects);
        this.displayAdapter.notifyDataSetChanged();

        Log.i("DisplayObject", "updateListView: size" + displayObjects.size());
    }
}
