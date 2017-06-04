package comp3350.WinnipegTransitGo.services.database;


/**
 * DatabaseService class
 * Provides functionality to create, get and close database
 *
 * Usage:
 * Should be used by business logic and gui
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class DatabaseService
{
    private static Database dataAccessService = null;

    public static Database createDataAccess()
    {
        if (dataAccessService == null)
        {
            String dbName = Database.prefDatabase;
            dataAccessService = new DatabaseAccessStub(dbName);
            dataAccessService.open(dbName);
        }
        return dataAccessService;
    }

    public static Database getDataAccess(String dbName)
    {
        if (dataAccessService == null)
        {
            System.out.println("Connection to data access has not been established.");
            System.exit(1);
        }
        return dataAccessService;
    }

    public static void closeDataAccess()
    {
        if (dataAccessService != null)
        {
            dataAccessService.close();
        }
        dataAccessService = null;
    }
}
