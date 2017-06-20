package comp3350.WinnipegTransitGo.presentation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

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
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.persistence.database.Database;
import comp3350.WinnipegTransitGo.persistence.transitAPI.ApiListenerCallback;

public class MainActivity
        extends AppCompatActivity
        implements OnMapReadyCallback,
        OnCameraMoveStartedListener, OnCameraIdleListener, ApiListenerCallback,
        OnMyLocationButtonClickListener
{
    private GoogleMap map;
    private BusListViewFragment busListViewFragment;
    TransitListPopulator listGenerator;

    List<Marker> busStopMarkers = new ArrayList<>();
    SupportMapFragment mapFragment;
    boolean shouldLocationUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(comp3350.WinnipegTransitGo.R.layout.activity_main);


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
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }
        setupMap();
        setUserLocation();
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
                }
            }
        }
    }


    /**
     * I expect the user to have granted the permission at this point.
     * The exception is guaranteed to never be thrown.
     */
    public void setUserLocation() throws SecurityException {
        map.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location bestLocation = null;

        for (String provider: locationManager.getProviders(true)) {
            Location curr = locationManager.getLastKnownLocation(provider);
            if (curr != null && bestLocation == null) {
                bestLocation = curr;
            } else if (curr != null && curr.hasAccuracy() && curr.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = curr;
            }
        }
        if (bestLocation != null) {
            setInitialLocation(bestLocation);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateLocationFromCamera();
                handler.postDelayed(this, 30000);
            }
        }, 30000);
    }



    public void setInitialLocation(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 13));
        busListViewFragment.clearListView();
        listGenerator.populateTransitList(location.getLatitude() + "", location.getLongitude() + "");
    }

    private void cameraMoved(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        busListViewFragment.clearListView();
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
            shouldLocationUpdate = true;
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
        if (shouldLocationUpdate) {
            shouldLocationUpdate = false;
            updateLocationFromCamera();
        }
    }

    @Override
    public void updateListView(List<TransitListItem> displayObjects) {
        busListViewFragment.updateListView(displayObjects);
    }

    @Override
    public boolean onMyLocationButtonClick() throws SecurityException {
        shouldLocationUpdate = true;
        return false;
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
            setRadiusManually();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setRadiusManually()
    {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        final ViewGroup nullParent = null;  //used to get rid of warning of passing null to layoutInflater
        View promptView = layoutInflater.inflate(R.layout.set_radius_dialog, nullParent);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(promptView);
        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Database service access:
                        if(!TextUtils.isEmpty(editText.getText().toString())) {
                            Database dataBase = DatabaseService.getDataAccess(Database.prefDatabase);
                            dataBase.setRadius(Integer.parseInt(editText.getText().toString()));
                            Toast.makeText(MainActivity.this, "The nearby bus stop radius is set to: " + dataBase.getRadius(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
