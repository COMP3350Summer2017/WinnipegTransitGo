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
    private int refreshRate; //milliseconds
    private int searchRadius;

    public void open(String path) {
        refreshRate = 25000;
        searchRadius = 500;
    }

    public int getRadius() {
        return searchRadius;
    }

    public void setRadius(int radius) {
        searchRadius = radius;
    }

    @Override
    public int getRefreshRate() {
        return refreshRate;
    }

    public void close() {
    }
}
