package comp3350.WinnipegTransitGo.services.database;

/**
 * Database Interface
 * Provides interface of persistence layer to business logic tier
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public interface Database {

    public static final String prefDatabase = "preferences";

    public void open(String dbName);

    public void close();

    public int getRadius();

    public void setRadius(int radius);

    public int getUpdateInterval();

    public void setUpdateInterval(int interval);

    public int getMinimumDistanceBetweenUpdates();

    public double getDefaultLongitude();

    public double getDefaultLatitude();
}
