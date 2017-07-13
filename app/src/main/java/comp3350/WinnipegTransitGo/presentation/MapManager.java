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

    //should the map send locations to the MainActivity based on map gestures?
    private final boolean shouldSendNotification;
    private Map<String, Marker> busStopMarkers; //bus stop markers stored on the map
    private GoogleMap map;
    private MainActivity parentActivity; //parentActivity. Used for sending locations to MainActivity
    private boolean shouldLocationUpdate; //used to perform checks on whether user is moving the map or not

    /**
     * Constructor - sets up parent activity and expected map fragment
     * @param parentActivity - ParentActivity of this fragment
     * @param mapFragment - Fragment contained in activity
     * @param shouldSendNotification - Should the map send an update when location changes
     */
    private MapManager(MainActivity parentActivity, SupportMapFragment mapFragment, boolean shouldSendNotification) {
        busStopMarkers = new HashMap<>();
        this.parentActivity = parentActivity;
        shouldLocationUpdate = false;
        this.shouldSendNotification = shouldSendNotification;
        mapFragment.getMapAsync(this);
    }

    /**
     * Singleton Instance Fetcher for whoever needs the map manager
     *
     * @param parentActivity - Main Activity
     * @param mapFragment - The map fragment being handled
     * @param shouldMapsSendNotifications - should the map send location to the user based on gestures?
     * @return
     */
    static MapManager getInstance(@NonNull MainActivity parentActivity, @NonNull SupportMapFragment mapFragment, boolean shouldMapsSendNotifications) {
        if (instance == null) {
            instance = new MapManager(parentActivity, mapFragment, shouldMapsSendNotifications);
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

            //do we need to send the location to the parent
            if (shouldSendNotification) {
                Location l  = getLocationFromCamera();
                parentActivity.locationChanged(l);
            }
        }
    }

    private void cameraMoved(Location location) {
        LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));
        if (shouldSendNotification) {
            parentActivity.locationChanged(location);
        }
    }

    @Override
    public void onCameraMoveStarted(int i) {
        if (i == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            shouldLocationUpdate = true;
        }
    }

    /**
     * Get location of centre of screen
     */
    Location getLocationFromCamera() {
        if (map == null) return null;
        LatLng centrePosition = map.getCameraPosition().target;
        Location newLocation = new Location("");
        newLocation.setLatitude(centrePosition.latitude);
        newLocation.setLongitude(centrePosition.longitude);
        return newLocation;
    }

    /**
     * Send centre location to parentActivity
     */
    void updateLocationFromCamera() {
        if (map == null) return;
        Location newLocation = getLocationFromCamera();
        cameraMoved(newLocation);
    }

    /**
     * Remove all bus stop markers from the map
     */
    private void removeBusStopMarkers() {
        for (String s: busStopMarkers.keySet()) {
            busStopMarkers.get(s).remove();
        }
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
            busStopMarkers.put(busStop.getNumber()+"", busStopMarker);
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
        Marker marker = null;
        if (busStopMarkers.containsKey(busStopNumber)) {
            marker = busStopMarkers.get(busStopNumber);
        }
        removeBusStopMarkers();
        if (marker != null) {
            String snippet = marker.getSnippet();
            LatLng stopLocation = marker.getPosition();
            marker = map.addMarker(new MarkerOptions()
                    .position(stopLocation)
                    .title(snippet)
                    .snippet(snippet)
            );
            busStopMarkers.put(busStopNumber, marker);
        }
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
