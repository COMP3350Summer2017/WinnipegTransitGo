package comp3350.WinnipegTransitGo.persistence.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by habib on 6/23/2017.
 */

public class DataAccessObject implements Database
{
    private Statement statement;
    private Connection c1;
    private ResultSet rs2, rs3, rs4, rs5;

    private String dbName  = "SC";
    private String dbType;

    public static String dbPath = "db/SC";

    private String cmdString;
    private int updateCount;
    private String result;
    private static String EOF = "  ";

    public DataAccessObject(){}

    public void open()
    {
        String url;
        try
        {
            // Setup for HSQL
            dbType = "HSQL";
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            url = "jdbc:hsqldb:file:" + dbPath; // stored on disk mode
            c1 = DriverManager.getConnection(url, "SA", "");
            statement = c1.createStatement();


            defaultLongitude = -97.1366;
            defaultLatitude = 49.8075;
        }
        catch (Exception e)
        {
            processSQLError(e);
        }
        System.out.println("Opened " +dbType +" database " +dbPath);
    }

    public void close()
    {
        try
        {	// commit all changes to the database
            cmdString = "shutdown compact";
            rs2 = statement.executeQuery(cmdString);
            c1.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }
        System.out.println("Closed " +dbType +" database " +dbName);
    }

    //----------------------------------------------//
    //funtionality


    private int minimumTimeBetweenUpdates; //milliseconds
    private int minimumDistanceBetweenUpdates; // meters
    private double defaultLongitude;
    private double defaultLatitude;


    public void setRadius(int radius)
    {
        String values;
        String where;

        result = null;
        try
        {
            // Should check for empty values and not update them
            values = "value="+radius;
            where = "where key='radius'";
            cmdString = "Update PREFERENCES " +" Set " +values +" " +where;

            updateCount = statement.executeUpdate(cmdString);
            result = checkWarning(statement, updateCount);
        }
        catch (Exception e)
        {
            result = processSQLError(e);
        }
    }

    public int getRadius()
    {
        int result = 0;

        try
        {
            cmdString = "Select * from PREFERENCES where key='radius'";
            rs2 = statement.executeQuery(cmdString);

            rs2.next();
            result = rs2.getInt("value");

            rs2.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }

        return result;
    }

    public int getUpdateInterval() {
        return minimumTimeBetweenUpdates;
    }

    public void setUpdateInterval(int interval) { minimumTimeBetweenUpdates = interval;}

    public int getMinimumDistanceBetweenUpdates() {
        return minimumDistanceBetweenUpdates;
    }

    public double getDefaultLongitude() {
        return defaultLongitude;
    }

    public double getDefaultLatitude() {
        return defaultLatitude;
    }









    //-----------------------------------//
    //safety checks

    public String checkWarning(Statement st, int updateCount)
    {
        String result;

        result = null;
        try
        {
            SQLWarning warning = st.getWarnings();
            if (warning != null)
            {
                result = warning.getMessage();
            }
        }
        catch (Exception e)
        {
            result = processSQLError(e);
        }
        if (updateCount != 1)
        {
            result = "Tuple not inserted correctly.";
        }
        return result;
    }

    public String processSQLError(Exception e)
    {
        String result = "*** SQL Error: " + e.getMessage();

        // Remember, this will NOT be seen by the user!
        e.printStackTrace();

        return result;
    }

}
