package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.persistence.database.Database;

/**
 * Created by nibras on 2017-06-24.
 * Purpose: Accepts the search radius which has to be set manually
 *          and sets the search radius if it satisfies required conditions
 */

public class UserPreference
{
    private static UserPreference userPreference=new UserPreference();

    private UserPreference()
    {

    }

    public static boolean verifyAndSetRadius(String radius)
    {
        boolean isValid=false;
        try {
            if (Integer.parseInt(radius) >= 200 && Integer.parseInt(radius) <= 1000) {
                Database dataBase = DatabaseService.getDataAccess();
                dataBase.setRadius(Integer.parseInt(radius));
                isValid=true;
            }
        }
        catch (NumberFormatException e)
        {
            System.out.println(e.getMessage());
        }
        return isValid;
    }

    public static UserPreference getUserPreference()
    {
        return userPreference;
    }
}
