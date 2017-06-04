package comp3350.WinnipegTransitGo.DatabaseServices;


import comp3350.WinnipegTransitGo.interfaces.Database;

/**
 * DatabaseAccessStub class
 * Provides functionality to save and query database
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-3
 */

public class DatabaseAccessStub implements Database {
    private String dbName;
    private String dbType = "stub";



    public DatabaseAccessStub(String dbName)
    {
        this.dbName = dbName;
    }

    public DatabaseAccessStub()
    {
        this(prefDatabase);
    }

    private int minimumTimeBetweenUpdates; //milliseconds
    private int searchRadius;

    public void open(String dbName)
    {
        minimumTimeBetweenUpdates = 3000;
        searchRadius = 500;
    }

    public int getRadius(){return searchRadius;}
    public void setRadius(int radius){searchRadius = radius;}

    public int getUpdateInterval(){return minimumTimeBetweenUpdates;}
    public void setUpdateInterval(int interval){minimumTimeBetweenUpdates = interval;}


    public void close() {}
}
