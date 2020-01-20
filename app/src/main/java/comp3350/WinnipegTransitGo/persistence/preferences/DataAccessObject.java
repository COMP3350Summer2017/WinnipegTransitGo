package comp3350.WinnipegTransitGo.persistence.preferences;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLWarning;
import java.sql.Statement;

/**
 * DataAccessObject implements Preferences using HSQL database
 * Provides implementation of Preferences persistence to business logic tier
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-23
 */

public class DataAccessObject implements Preferences
{
    private Statement statement;
    private Connection connection;
    private ResultSet resultSet;

    private String cmdString;
    private int updateCount;

    public void open(String path) throws Exception
    {
        String url;
        // Setup for HSQL
        Class.forName("org.hsqldb.jdbcDriver").newInstance();
        url = "jdbc:hsqldb:file:" + path; // stored on disk mode
        connection = DriverManager.getConnection(url, "SA", "");
        statement = connection.createStatement();
    }

    public void close() throws Exception
    {
        // commit all changes to the database
        cmdString = "shutdown compact";
        resultSet = statement.executeQuery(cmdString);
        connection.close();
    }

    //----------------------------------------------//
    //funtionality

    public void setRadius(int radius) throws Exception
    {
        String values;
        String where;

        // Should check for empty values and not update them
        values = "value="+radius;
        where = "where key='radius'";
        cmdString = "Update PREFERENCES " +" Set " +values +" " +where;

        updateCount = statement.executeUpdate(cmdString);
    }

    public int getRadius() throws Exception
    {
        int result = 0;

        cmdString = "Select * from PREFERENCES where key='radius'";
        resultSet = statement.executeQuery(cmdString);

        resultSet.next();
        result = (int) resultSet.getDouble("value");

        resultSet.close();

        return result;
    }

    public int getRefreshRate() throws Exception
    {
        int result = 30000;//default refreshRate incase of failure

        cmdString = "Select * from PREFERENCES where key='refreshRate'";
        resultSet = statement.executeQuery(cmdString);

        resultSet.next();
        result = (int) resultSet.getDouble("value");

        resultSet.close();

        return result;
    }
    public double getDefaultLongitude() throws Exception
    {
        double result = 0;

        cmdString = "Select * from PREFERENCES where key='defaultLongitude'";
        resultSet = statement.executeQuery(cmdString);

        resultSet.next();
        result = resultSet.getDouble("value");

        resultSet.close();

        return result;
    }

    public double getDefaultLatitude() throws Exception{

        double result = 0;
        cmdString = "Select * from PREFERENCES where key='defaultLatitude'";
        resultSet = statement.executeQuery(cmdString);

        resultSet.next();
        result = resultSet.getDouble("value");

        resultSet.close();

        return result;
    }

}
