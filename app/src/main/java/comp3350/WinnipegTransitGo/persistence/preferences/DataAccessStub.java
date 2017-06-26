package comp3350.WinnipegTransitGo.persistence.preferences;


/**
 * DataAccessStub class
 * Provides functionality to save and query database
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class DataAccessStub implements Preferences {
    public static final String prefDatabase = "preferences";
    private int refreshRate; //milliseconds
    private int searchRadius;
    private double defaultLongitude;
    private double defaultLatitude;

    public DataAccessStub() {}


    public void open(String path) {
        refreshRate = 25000;
        searchRadius = 500;
        defaultLongitude = -97.1366;
        defaultLatitude = 49.8075;
    }

    public int getRadius() {
        return searchRadius;
    }

    public void setRadius(int radius) {
        searchRadius = radius;
    }

    public double getDefaultLongitude() {
        return defaultLongitude;
    }

    public double getDefaultLatitude() {
        return defaultLatitude;
    }

    @Override
    public int getRefreshRate() {
        return refreshRate;
    }

    public void close() {
    }
}
