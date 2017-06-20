package comp3350.WinnipegTransitGo.businessLogic;


import comp3350.WinnipegTransitGo.persistence.database.Database;
import comp3350.WinnipegTransitGo.persistence.database.DatabaseAccessStub;

/**
 * DatabaseService class
 * Provides functionality to create, get and close database
 * <p>
 * Usage:
 * Should be used by business logic and gui
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class DatabaseService {
    private static Database dataAccessService = null;

    public static Database getDataAccess(String dbName) {
        if (dataAccessService == null) {
            dataAccessService = new DatabaseAccessStub();
            dataAccessService.open(dbName);
        }
        return dataAccessService;
    }

    public static void closeDataAccess() {
        if (dataAccessService != null) {
            dataAccessService.close();
        }
        dataAccessService = null;
    }
}
