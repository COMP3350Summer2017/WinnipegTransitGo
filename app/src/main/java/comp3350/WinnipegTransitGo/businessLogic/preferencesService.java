package comp3350.WinnipegTransitGo.businessLogic;


import comp3350.WinnipegTransitGo.persistence.preferences.DataAccessObject;
import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;

/**
 * preferencesService class
 * Provides functionality to create, get and close database, set databasePath
 * <p>
 * Usage:
 * Should be used by business logic and presentation tiers
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class preferencesService {
    private static Preferences dataAccessService = null;
    private static String dbPathName = "database/PREFERENCES";

    public static final String dbName = "PREFERENCES";

    public static Preferences getDataAccess() {
        if (dataAccessService == null) {
            dataAccessService = new DataAccessObject();
            dataAccessService.open(dbPathName);
        }
        return dataAccessService;
    }

    public static void closeDataAccess() {
        if (dataAccessService != null) {
            dataAccessService.close();
        }
        dataAccessService = null;
    }

    public static void setDBPathName(String pathName) {
        System.out.println("Setting DB path to: " + pathName);
        dbPathName = pathName;
    }
}
