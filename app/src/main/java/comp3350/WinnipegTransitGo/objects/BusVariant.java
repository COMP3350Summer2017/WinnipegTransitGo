package comp3350.WinnipegTransitGo.objects;

/**
 * BusVariant
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-03
 */
public class BusVariant {
    private String name;

    public BusVariant(String name) {
        this.name = name;
    }

    //region public getters
    public String getName() {
        return name;
    }
    //endregion
}
