package comp3350.WinnipegTransitGo.businessLogic;

/**
 * LocationService
 * <p>
 * Provides methods and functionality for accessing
 * location services and setting required permissions
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 20-06-2017
 */
public class LocationService {

    public static int getRefreshRate() {
        return preferencesService.getDataAccess().getRefreshRate();
    }
}
