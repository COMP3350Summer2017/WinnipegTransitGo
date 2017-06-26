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
    public DataAccessObject(){}

    public void open(String path)
    {
        String url;
        try
        {
            // Setup for HSQL
            Class.forName("org.hsqldb.jdbcDriver").newInstance();
            url = "jdbc:hsqldb:file:" + path; // stored on disk mode
            connection = DriverManager.getConnection(url, "SA", "");
            statement = connection.createStatement();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }
    }

    public void close()
    {
        try
        {	// commit all changes to the database
            cmdString = "shutdown compact";
            resultSet = statement.executeQuery(cmdString);
            connection.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }
    }

    //----------------------------------------------//
    //funtionality

    public void setRadius(int radius)
    {
        String values;
        String where;

        try
        {
            // Should check for empty values and not update them
            values = "value="+radius;
            where = "where key='radius'";
            cmdString = "Update PREFERENCES " +" Set " +values +" " +where;

            updateCount = statement.executeUpdate(cmdString);
            checkWarning(statement, updateCount);
        }
        catch (Exception e)
        {
            processSQLError(e);
        }
    }

    public int getRadius()
    {
        int result = 0;

        try
        {
            cmdString = "Select * from PREFERENCES where key='radius'";
            resultSet = statement.executeQuery(cmdString);

            resultSet.next();
            result = (int) resultSet.getDouble("value");

            resultSet.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }

        return result;
    }

    public int getRefreshRate()
    {
        int result = 30000;//default refreshRate incase of failure

        try
        {
            cmdString = "Select * from PREFERENCES where key='refreshRate'";
            resultSet = statement.executeQuery(cmdString);

            resultSet.next();
            result = (int) resultSet.getDouble("value");

            resultSet.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }

        return result;
    }
    public double getDefaultLongitude()
    {
        double result = 0;

        try
        {
            cmdString = "Select * from PREFERENCES where key='defaultLongitude'";
            resultSet = statement.executeQuery(cmdString);

            resultSet.next();
            result = resultSet.getDouble("value");

            resultSet.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }

        return result;
    }

    public double getDefaultLatitude() {

        double result = 0;

        try
        {
            cmdString = "Select * from PREFERENCES where key='defaultLatitude'";
            resultSet = statement.executeQuery(cmdString);

            resultSet.next();
            result = resultSet.getDouble("value");

            resultSet.close();
        }
        catch (Exception e)
        {
            processSQLError(e);
        }

        return result;
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
