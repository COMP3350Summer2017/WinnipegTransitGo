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
    private String coverage;
    private List<BusVariant> variants;

    @SerializedName("customer-type")
    private String customerType;

    //region public getters
    public int getKey() { return key; }

    public int getNumber() { return number; }

    public String getName() { return name; }

    public String getCoverage() { return coverage; }

    public String getCustomerType() { return customerType; }

    public List<BusVariant> getVariants() { return variants; }
    //endregion
}
