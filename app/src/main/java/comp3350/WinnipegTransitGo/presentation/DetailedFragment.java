package comp3350.WinnipegTransitGo.presentation;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.presentation.reminders.BusReminder;
import comp3350.WinnipegTransitGo.presentation.reminders.ReminderManager;

/**
 * DetailedFragment
 * SHows detailed information about a bus route,
 * with more bus times than regular and functionality
 * to set a reminder on each time
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 22-06-2017
 */

public class DetailedFragment extends Fragment implements ReminderManager {

    public static final String TRANSIT_ITEM = "1";
    private BusTimesDisplayAdapter busTimesDisplayAdapter;
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
        item = (TransitListItem) args.getSerializable(DetailedFragment.TRANSIT_ITEM);
        busTimesDisplayAdapter = new BusTimesDisplayAdapter(getContext(), this);
        if (item != null) {
            TextView busStopName = (TextView) view.findViewById(R.id.bus_stop_name);
            TextView busNumber = (TextView) view.findViewById(R.id.bus_number);
            TextView busStopNumber = (TextView) view.findViewById(R.id.bus_stop_number);
            TextView destination = (TextView) view.findViewById(R.id.destination);

            destination.setText(item.getBusStopDestination());
            busStopName.setText(item.getBusStopName());
            busNumber.setText(Integer.toString(item.getBusNumber()));
            busStopNumber.setText(item.getBusStopNumber());

            ListView listView = (ListView) view.findViewById(R.id.bus_times_list);
            listView.setAdapter(busTimesDisplayAdapter);
            busTimesDisplayAdapter.addAll(item.getTimes());

        }
    }

    @Override
    public void setReminderForActiveBus(String minutes) {
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent notificationIntent = new Intent();
        notificationIntent.setAction("comp3350.WinnipegTransitGo.businessLogic.BUS_DEPARTURE");
        notificationIntent.putExtra("busNumber", item.getBusNumber());
        notificationIntent.putExtra("busStopName", item.getBusStopName());
        notificationIntent.putExtra("minutesLeft", minutes);


        PendingIntent broadcast = PendingIntent.getBroadcast(getContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, broadcast);


    }
}
