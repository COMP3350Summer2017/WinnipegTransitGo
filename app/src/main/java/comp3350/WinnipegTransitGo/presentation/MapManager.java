package comp3350.WinnipegTransitGo.presentation;

import android.location.Location;

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

import comp3350.WinnipegTransitGo.businessLogic.location.OnBusStopClickListener;
import comp3350.WinnipegTransitGo.businessLogic.location.LocationService;
import comp3350.WinnipegTransitGo.objects.BusStop;

/**
 * MapManager
 * Interfaces the MainActivity with GoogleMapFragment
 * Handles refreshing of map on move and placing bus stop markers on the map.
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
    private Map<String, Marker> busStopMarkers;
    private GoogleMap map;
    private MainActivity parentActivity;
    private boolean shouldLocationUpdate; //used to perform checks on whether user is moving the map or not

    /**
     * Constructor - sets up parent activity and expected map fragment
     * @param parentActivity - ParentActivity of this fragment
     * @param mapFragment - Fragment contained in activity
     */
    MapManager(MainActivity parentActivity, SupportMapFragment mapFragment) {
        busStopMarkers = new HashMap<>();
        this.parentActivity = parentActivity;
        shouldLocationUpdate = false;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LocationService.ensureLocationPermission(parentActivity);
        setupMap();
    }

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
        for (String key: busStopMarkers.keySet()) {
            busStopMarkers.get(key).remove();
        }
        busStopMarkers.clear();
    }

    private void setUserPreviousLocation() {
        Location bestLocation = LocationService.getLastKnownLocation(parentActivity);
        if (bestLocation != null) {
            cameraMoved(bestLocation);
        }
    }

    /**
     * Places the specified bus stops onto the map as a list of markers
     * @param busStops - Information about bus stops to display
     */
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
            busStopMarkers.put("#"+busStop.getNumber()+"", busStopMarker);
        }
    }


    @Override
    public void showLocationForBus(String busNumber) {
        Marker m = busStopMarkers.get(busNumber);
        if (m != null) {
            m.showInfoWindow();
        }
    }
}
