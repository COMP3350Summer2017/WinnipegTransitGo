package comp3350.WinnipegTransitGo.objects;

import android.graphics.Color;


/**
 * Created by rasheinstein on 6/25/2017.
 */
public class BusStatus {
    private static final String LATE = "Late", EARLY = "Early",
            OK = "Ok", DUE = "Due";

    public static int getColorForStatus(String status) {
        String color = BusStatusColor.OK;
        switch (status) {
            case LATE:
                color = BusStatusColor.LATE;
                break;
            case EARLY:
                color = BusStatusColor.EARLY;
                break;
            case OK:
                color = BusStatusColor.OK;
                break;
            case DUE:
                color = BusStatusColor.DUE;
                break;
        }
        return Color.parseColor(color);
    }
}
