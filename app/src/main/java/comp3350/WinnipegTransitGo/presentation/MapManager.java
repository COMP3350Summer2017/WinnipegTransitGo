package comp3350.WinnipegTransitGo.presentation;

import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp3350.WinnipegTransitGo.objects.BusStop;

/**
 * MapManager
 * Interfaces the MainActivity with GoogleMapFragment
 * Handles refreshing of map on move and placing bus stop markers on the map.
 *
 * There is also going to be only one map throughout the course of the application,
 * and thus a singleton pattern is used.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 19-06-2017
 */

class MapManager
        implements OnMapReadyCallback, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnMyLocationButtonClickListener,
        OnBusStopClickListener
{
    private static MapManager instance;
    private Map<String, Marker> busStopMarkers;
    private GoogleMap map;
    private MainActivity parentActivity;
    private boolean shouldLocationUpdate; //used to perform checks on whether user is moving the map or not

    /**
     * Constructor - sets up parent activity and expected map fragment
     * @param parentActivity - ParentActivity of this fragment
     * @param mapFragment - Fragment contained in activity
     */
    private MapManager(MainActivity parentActivity, SupportMapFragment mapFragment) {
        busStopMarkers = new HashMap<>();
        this.parentActivity = parentActivity;
        shouldLocationUpdate = false;
        mapFragment.getMapAsync(this);
    }

    static MapManager getInstance(MainActivity parentActivity, SupportMapFragment mapFragment) {
        if (instance == null) {
            instance = new MapManager(parentActivity, mapFragment);
        }
        return instance;
    }

    /**
     * getBusStopClickListener
     *
     * Returns an interface version of the class to
     * expose an onClick method to any class wishing
     * to display or highlight a bus stop on the map
     * @return void
     */
    @NonNull
    static OnBusStopClickListener getBusStopClickListener() {
        return instance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LocationSettings.ensureLocationPermission(parentActivity);
        setupMap();
    }

    /**
     * setupMap
     * Sets up the map after permissions have been checked.
     * Sets up camera and location change listeners
     *
     * @throws SecurityException for missing permission
     */
    private void setupMap() throws SecurityException {
        map.setMyLocationEnabled(true);
        map.setOnCameraMoveStartedListener(this);
        map.setOnCameraIdleListener(this);
        map.setOnMyLocationButtonClickListener(this);
        map.moveCamera(CameraUpdateFactory.zoomTo(13));
        setUserPreviousLocation();
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

    void updateLocationFromCamera() {
        if (map == null) return;
        LatLng centrePosition = map.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(centrePosition.latitude);
        newLocation.setLongitude(centrePosition.longitude);
        cameraMoved(newLocation);
    }

    private void removeBusStopMarkers() {
        busStopMarkers.forEach((s, map) -> map.remove());
        busStopMarkers.clear();
    }

    private void setUserPreviousLocation() {
        Location bestLocation = LocationSettings.getLastKnownLocation(parentActivity.getApplicationContext());
        if (bestLocation != null) {
            cameraMoved(bestLocation);
        }
    }

    /**
     * Places the specified bus stops onto the map as a list of markers
     * @param busStops - Information about bus stops to display
     */
    void updateStopsOnMap(@NonNull List<BusStop> busStops) {
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
            busStopMarkers.put("#"+busStop.getNumber()+"", busStopMarker);
        }
    }

    /**
     * Takes a bus number and finds the appropriate busStop
     * marker and highlights it (by showing a popup above it)
     * @param busStopNumber The bus stop number to highlight
     */
    @Override
    public void showLocationForBus(String busStopNumber) {
        Marker m = busStopMarkers.get(busStopNumber);
        if (m != null) {
            m.showInfoWindow();
        }
    }

    /**
     * Removes all busStops from the map except the specified one.
     * Removes all if the specified one is not found.
     * @param busStopNumber - Bus stop to leave on the map
     */
    void showSingleStop(final String busStopNumber) {
        busStopMarkers.forEach((s, marker) -> {
            if ( ! s.equals(busStopNumber) ) {
                marker.remove();
            }
        });
        busStopMarkers.keySet().removeIf(key -> !key.equals(busStopNumber));

    }

    /**
     * Cleanup map when no longer in use
     */
    void destroyMap() {
        removeBusStopMarkers();
        map = null;
        instance = null;
    }
}
