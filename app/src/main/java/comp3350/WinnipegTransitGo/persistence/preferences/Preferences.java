package comp3350.WinnipegTransitGo.persistence.preferences;

/**
 * Preferences Interface
 * Provides interface of persistence layer to business logic tier
 * Its implementation is meant to store the preferences of application.
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public interface Preferences {

    void open(String path) throws Exception;

    void close() throws Exception;

    int getRadius() throws Exception;

    void setRadius(int radius) throws Exception;

    double getDefaultLongitude() throws Exception;

    double getDefaultLatitude() throws Exception;

    int getRefreshRate() throws Exception;
}
