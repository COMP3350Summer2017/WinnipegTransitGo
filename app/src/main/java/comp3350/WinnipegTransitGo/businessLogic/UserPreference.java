package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.persistence.preferences.Preferences;

/**
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
    private static UserPreference userPreference = null;

    private UserPreference() {}

    public static UserPreference getUserPreference()
    {
        if (userPreference == null)
            userPreference = new UserPreference();
        return userPreference;
    }

    public static boolean verifyAndSetRadius(String radius)
    {
        boolean isValid=true;
        if(Integer.parseInt(radius)>=200 && Integer.parseInt(radius)<=1000)
        {
            Preferences dataBase = preferencesService.getDataAccess();
            dataBase.setRadius(Integer.parseInt(radius));
        }
        else
            isValid=false;

        return isValid;
    }
}
