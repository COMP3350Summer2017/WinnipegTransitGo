package comp3350.WinnipegTransitGo.persistence.database;


/**
 * DatabaseAccessStub class
 * Provides functionality to save and query database
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class DatabaseAccessStub implements Database {
    private static final String prefDatabase = "preferences";
    private String dbName;
    private String dbType = "stub";
    private int refreshRate; //milliseconds
    private int searchRadius;
    private double defaultLongitude;
    private double defaultLatitude;

    public DatabaseAccessStub(String dbName) {
        this.dbName = dbName;
    }

    public DatabaseAccessStub() {
        this(prefDatabase);
    }

    public void open(String dbName) {
        refreshRate = 30000;
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
