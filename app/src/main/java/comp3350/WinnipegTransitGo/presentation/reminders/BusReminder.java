package comp3350.WinnipegTransitGo.presentation.reminders;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import java.util.Calendar;
import java.util.Date;

import android.net.Uri;
import android.provider.CalendarContract.Events;
import android.support.v7.app.AppCompatActivity;

import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.permissions.PermissionManager;


/**
 * Created by rasheinstein on 2017-06-26.
 */

public class BusReminder {
//    public static void setReminderForBus(AppCompatActivity activity, TransitListItem bus, String time) throws SecurityException{
//
//        if ( ! PermissionManager.checkReminderPermissionSet(activity)) {
//            return;
//        }
//        long calID = 1;
//        long startMillis = 0;
//        Calendar beginTime = Calendar.getInstance();
//        Date startDate = new Date();
//        beginTime.setTime(startDate);
//        startMillis = beginTime.getTimeInMillis() + 300000;
//
//
//
//        ContentResolver contentResolver = activity.getApplicationContext().getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put(Events.DTSTART, startMillis);
//        values.put(Events.DTEND, startMillis);
//        values.put(Events.TITLE, "Bus Departure in 5 ");
//        values.put(Events.CALENDAR_ID, calID);
//        values.put(Events.EVENT_TIMEZONE, "Canada/Central"); //TODO: Change this zone
//        contentResolver.insert(Events.CONTENT_URI, values);
//
//
//
//    }
}
