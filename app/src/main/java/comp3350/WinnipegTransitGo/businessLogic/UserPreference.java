package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;

/**
 * Created by nibras on 2017-06-24.
 * Purpose: Accepts the search radius which has to be set manually
 *          and sets the search radius if it satisfies required conditions
 * UserPreference class
 * Provides functionality to get/set user preferences from presentation
 *
 *
 * @author Nibras
 * @version 1.0
 * @since 2017-06-24
 */

public class UserPreference
{
    private static UserPreference userPreference=null;
    private static Preferences dataBase = PreferencesService.getDataAccess();

    public static UserPreference getUserPreference()
    {
        if (userPreference == null)
            userPreference = new UserPreference();
        dataBase = PreferencesService.getDataAccess();
        return userPreference;
    }

    public static boolean verifyAndSetRadius(String radius)
    {
        boolean isValid=false;
        try {
            if (Integer.parseInt(radius) >= 200 && Integer.parseInt(radius) <= 1000) {
                    dataBase.setRadius(Integer.parseInt(radius));
                    isValid=true;
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return isValid;
    }

    public  static int getRadius()
    {
        try{ return dataBase.getRadius();}
        catch (Exception e){return  500;}//default value
    }

    public static int getRefreshRate() {
        try {return dataBase.getRefreshRate();}
        catch (Exception e){return  25000;}//default values
    }
}
