package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.persistence.database.Database;

/**
 * Created by nibras on 2017-06-24.
 */

public class UserPreference
{
    private static UserPreference userPreference=new UserPreference();

    private UserPreference()
    {

    }

    public static boolean verifyAndSetRadius(String radius)
    {
        boolean isValid=true;
        if(Integer.parseInt(radius)>=200 && Integer.parseInt(radius)<=1000)
        {
            Database dataBase = DatabaseService.getDataAccess();
            dataBase.setRadius(Integer.parseInt(radius));
        }
        else
            isValid=false;

        return isValid;
    }

    public static UserPreference getUserPreference()
    {
        return userPreference;
    }
}
