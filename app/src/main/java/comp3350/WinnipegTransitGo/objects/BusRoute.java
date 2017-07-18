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
    private int key;
    private int number;
    private String name;

    public BusRoute(int key, int number, String name)
    {
        this.key = key;
        this.number = number;
        this.name = name;
    }

    //region public getters
    public int getKey() {
        return key;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    //endregion
}
