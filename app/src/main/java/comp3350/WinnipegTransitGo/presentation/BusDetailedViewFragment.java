package comp3350.WinnipegTransitGo.presentation;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.ReminderTimeProcessing;
import comp3350.WinnipegTransitGo.objects.ReminderParams;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * BusDetailedViewFragment
 * SHows detailed information about a bus route,
 * with more bus times than regular and functionality
 * to set a reminder on each time
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 22-06-2017
 */

public class BusDetailedViewFragment extends Fragment implements OnReminderButtonClick {

    public static final String TRANSIT_ITEM = "1"; //key value, not displayed
    private TransitListItem item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_detailed_fragment, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        item = (TransitListItem) args.getSerializable(BusDetailedViewFragment.TRANSIT_ITEM);
        BusTimesDisplayAdapter busTimesDisplayAdapter = new BusTimesDisplayAdapter(getContext(), this);
        if (item != null) {
            TextView busNumber = (TextView) view.findViewById(R.id.bus_number);
            TextView busStopNumber = (TextView) view.findViewById(R.id.bus_stop_number);
            TextView destination = (TextView) view.findViewById(R.id.destination);

            destination.setText(item.getBusStopDestination());
            busNumber.setText(Integer.toString(item.getBusNumber()));
            busStopNumber.setText(Integer.toString(item.getBusStopNumber()));

            ListView listView = (ListView) view.findViewById(R.id.bus_times_list);
            listView.setAdapter(busTimesDisplayAdapter);
            busTimesDisplayAdapter.addAll(item.getTimes());

        }
    }


    @Override
    public void setReminder(String minutes) {
        ReminderParams params = ReminderTimeProcessing.getReminderDetailsForDepartureTime(minutes);
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setAction(getString(R.string.notification_action));
        notificationIntent.putExtra(AlarmReceiver.BUS_NUMBER_ARG, item.getBusNumber());
        notificationIntent.putExtra(AlarmReceiver.MINUTES_LEFT_ARG, params.minutesToDeparture);

        PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP, params.reminderTimeMillis, broadcast);
    }
}
