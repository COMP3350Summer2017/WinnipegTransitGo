package comp3350.WinnipegTransitGo.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * BusRoute
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class BusRoute {
    private int number;

    public BusRoute(int number)
    {
        this.number = number;
    }

    //region public getters

    public int getNumber() {
        return number;
    }

    //endregion
}
