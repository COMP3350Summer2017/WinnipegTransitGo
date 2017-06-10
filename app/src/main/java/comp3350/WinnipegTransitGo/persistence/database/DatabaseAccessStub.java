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
    private String dbName;
    private String dbType = "stub";
    private int minimumTimeBetweenUpdates; //milliseconds
    private int searchRadius;
    private int minimumDistanceBetweenUpdates; // meters
    private double defaultLongitude;
    private double defaultLatitude;

    public DatabaseAccessStub(String dbName) {
        this.dbName = dbName;
    }

    public DatabaseAccessStub() {
        this(prefDatabase);
    }

    public void open(String dbName) {
        minimumTimeBetweenUpdates = 1000;
        searchRadius = 1000;
        minimumDistanceBetweenUpdates = 1;
        defaultLongitude = -97.1366;
        defaultLatitude = 49.8075;
    }

    public int getRadius() {
        return searchRadius;
    }

    public void setRadius(int radius) {
        searchRadius = radius;
    }

    public int getUpdateInterval() {
        return minimumTimeBetweenUpdates;
    }

    public void setUpdateInterval(int interval) {
        minimumTimeBetweenUpdates = interval;
    }

    public int getMinimumDistanceBetweenUpdates() {
        return minimumDistanceBetweenUpdates;
    }

    public double getDefaultLongitude() {
        return defaultLongitude;
    }

    public double getDefaultLatitude() {
        return defaultLatitude;
    }

    public void close() {
    }
}
