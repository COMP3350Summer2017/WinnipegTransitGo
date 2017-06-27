package comp3350.WinnipegTransitGo.businessLogic.reminders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Contains methods relating to Reminders and setting them.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/06/2017
 */

public class ReminderService {
    /**
     * setReminder - Set a reminder for a bus departure 5 minutes before it leaves.
     * If the user tries to set for a bus that leaves in less than 5 minutes,
     * the alarm is triggered immediately.
     *
     * @param context - application context.
     * @param minutesToDeparture - number of minutes until bus departs.
     * @param busNumber - bus currently departing.
     */
    public static void setReminder(Context context, int minutesToDeparture, int busNumber) {
        int mins;
        if (minutesToDeparture < 5) {
            mins = 0;
        }
        else{
            mins = minutesToDeparture - 5;
            minutesToDeparture = 5;
        }
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setAction("comp3350.WinnipegTransitGo.businessLogic.BUS_DEPARTURE");
        notificationIntent.putExtra("busNumber", busNumber);
        notificationIntent.putExtra("minutesLeft", minutesToDeparture);


        PendingIntent broadcast = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + mins*60000 + 5000, broadcast);

    }
}
