package comp3350.WinnipegTransitGo.objects;

/**
 * BusRoute
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class BusRoute {
    private int number;
    private String name;

    public BusRoute(int number, String name) {
        this.number = number;
        this.name = name;
    }

    //region public getters
    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
    //endregion
}
