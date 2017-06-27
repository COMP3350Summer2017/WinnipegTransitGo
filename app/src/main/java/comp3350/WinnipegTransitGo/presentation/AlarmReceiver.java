package comp3350.WinnipegTransitGo.presentation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import comp3350.WinnipegTransitGo.R;

/**
 * AlarmReceiver
 * <p>
 * Receives alarm scheduled by user when a bus is
 * about to depart
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/06/2017
 */

public class AlarmReceiver extends BroadcastReceiver {

    //value keys, not presented to user
    public static final String MINUTES_LEFT_ARG = "1";
    public static final String BUS_NUMBER_ARG = "2";

    @Override
    public void onReceive(Context context, Intent intent) {


        Intent notificationIntent = new Intent(context, MainActivity.class);
        int time = intent.getIntExtra(MINUTES_LEFT_ARG, 5);
        int busNumber = intent.getIntExtra(BUS_NUMBER_ARG, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        PendingIntent pendingIntent = PendingIntent.
                getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String contentTitle = context.getString(R.string.notification_content_title, time);
        String contentText = context.getString(R.string.notification_content_text, busNumber, time);
        Notification notification = builder
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(Notification.PRIORITY_MAX)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}
