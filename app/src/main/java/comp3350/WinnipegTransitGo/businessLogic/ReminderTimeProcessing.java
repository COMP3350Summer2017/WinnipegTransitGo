package comp3350.WinnipegTransitGo.businessLogic;

import comp3350.WinnipegTransitGo.objects.ReminderParams;

/**
 * ReminderTimeProcessing
 *
 * Processes departure times for buses and sends
 * appropriate reminder parameters.
 * Only one instance can exist
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 27/06/2017
 */

public class ReminderTimeProcessing {

    private static ReminderTimeProcessing instance;
    public static ReminderTimeProcessing getInstance() {
        if (instance == null) {
            instance = new ReminderTimeProcessing();
        }
        return instance;
    }

    private ReminderTimeProcessing() {

    }

    /**
     * Gets information for notification for a bus departure.
     * Returns a notification 5 minutes before the bus departs and if
     * the bus departs in less than 5 minutes, returns the current time
     * to fire the notification.
     *
     * @param currentTimeMillis - Current time specified by caller
     * @param minutes - Minutes until departure
     * @return ReminderParams - Notification time and bus departure in minutes
     */
    public ReminderParams getReminderDetailsForDepartureTime(long currentTimeMillis, String minutes) {
        ReminderParams params = new ReminderParams();
        long timeOffset = currentTimeMillis + 5000; //5 seconds from now

        if (minutes.equals("Due")) {
            //if the bus is due, leave now
            params.minutesToDeparture = 0;
            params.reminderTimeMillis = timeOffset;
        } else {
            int timeToDeparture;
            //if we can't parse the item, shouldn't happen. Set to now
            try {
                timeToDeparture = Integer.parseInt(minutes);
            } catch (Exception e) {
                timeToDeparture = 0;
            }

            //Minutes to departure could either be 5 minutes or less
            params.minutesToDeparture = (timeToDeparture < 5 ? timeToDeparture : 5);
            if (timeToDeparture > 5) {
                timeOffset += (timeToDeparture - 5 ) * 60000;
            }
            params.reminderTimeMillis = timeOffset;
        }
        return params;
    }
}