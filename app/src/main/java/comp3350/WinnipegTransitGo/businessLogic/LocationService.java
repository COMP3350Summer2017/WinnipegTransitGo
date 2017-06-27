package comp3350.WinnipegTransitGo.businessLogic;

/**
 * LocationService
 * <p>
 * Provides methods and functionality for accessing
 * location parameters
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
