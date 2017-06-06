package comp3350.WinnipegTransitGo.objects;

/**
 * BusVariant
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-03
 */
public class BusVariant {
    private String key;
    private String name;

    public BusVariant(String key, String name)
    {
        this.key = key;
        this.name = name;
    }

    //region public getters
    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
    //endregion
}
