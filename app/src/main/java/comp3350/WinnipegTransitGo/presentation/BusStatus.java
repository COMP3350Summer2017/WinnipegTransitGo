package comp3350.WinnipegTransitGo.presentation;

import android.graphics.Color;

/**
 * BusStatusColor
 * <p>
 * Gets colors for showing bus time status
 *
 * @author Nibras, Abdul-Rasheed
 * @version 1.0
 * @since 26/06/2017
 */
class BusStatus {
    private static final String LATE = "Late", EARLY = "Early",
            OK = "Ok", DUE = "Due";

    static int getColorForStatus(String status) {
        int color = Color.GREEN;
        switch (status) {
            case LATE:
                color = Color.RED;
                break;
            case EARLY:
                color = Color.BLUE;
                break;
            case OK:
                color = Color.GREEN;
                break;
            case DUE:
                color = Color.MAGENTA;
                break;
        }
        return color;
    }
}