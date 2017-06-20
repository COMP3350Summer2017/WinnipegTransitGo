package comp3350.WinnipegTransitGo.presentation;

import android.location.Location;
import android.os.Handler;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.businessLogic.location.LocationService;
import comp3350.WinnipegTransitGo.businessLogic.permissions.LocationPermission;
import comp3350.WinnipegTransitGo.objects.BusStop;

/**
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 19-06-2017
 */

class MapManager implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMyLocationButtonClickListener {
    private List<Marker> busStopMarkers;
    private GoogleMap map;
    private MainActivity parentActivity;
    private boolean shouldLocationUpdate;

    MapManager(MainActivity parentActivity, SupportMapFragment mapFragment) {
        busStopMarkers = new ArrayList<>();
        this.parentActivity = parentActivity;
        shouldLocationUpdate = false;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LocationPermission.ensureLocationPermission(parentActivity);
        setupMap();
    }

    private void setupMap() throws SecurityException{
        map.setMyLocationEnabled(true);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnMyLocationButtonClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(13));
        setUserLocationAndSetMapRefreshRate();
    }

    @Override
    public boolean onMyLocationButtonClick() throws SecurityException {
        shouldLocationUpdate = true;
        return false;
    }

    @Override
    public void onCameraIdle() {
        if (shouldLocationUpdate) {
            shouldLocationUpdate = false;
            updateLocationFromCamera();
        }
    }

    private void cameraMoved(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        parentActivity.locationChanged(location);
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
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

    private void removeBusStopMarkers() {
        for (Marker marker : busStopMarkers) {
            marker.remove();
        }
        busStopMarkers.clear();
    }

    private void setUserLocationAndSetMapRefreshRate(){
        Location bestLocation = LocationService.getLastKnownLocation(parentActivity);
        if (bestLocation != null) {
            cameraMoved(bestLocation);
        }
        setMapRefreshRate();
    }

    private void setMapRefreshRate() {
        final Handler handler = new Handler();
        final int refreshRate = LocationService.getRefreshRate();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (parentActivity.shouldUpdateLocation()) {
                    updateLocationFromCamera();
                }
                handler.postDelayed(this, refreshRate);
            }
        }, refreshRate);
    }

    void updateStopsOnMap(List<BusStop> busStops) {
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
}
