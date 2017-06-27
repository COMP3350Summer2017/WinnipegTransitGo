package comp3350.WinnipegTransitGo.persistence.preferences;

/**
 * Preferences Interface
 * Provides interface of persistence layer to business logic tier
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public interface Preferences {

    void open(String path);

    void close();

    int getRadius();

    void setRadius(int radius);

    double getDefaultLongitude();

    double getDefaultLatitude();

    int getRefreshRate();
}
