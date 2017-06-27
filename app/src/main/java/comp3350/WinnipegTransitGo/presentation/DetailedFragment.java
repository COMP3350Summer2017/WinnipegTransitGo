package comp3350.WinnipegTransitGo.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.reminders.OnReminderButtonClick;
import comp3350.WinnipegTransitGo.businessLogic.reminders.ReminderService;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

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

public class DetailedFragment extends Fragment implements OnReminderButtonClick {

    public static final String TRANSIT_ITEM = "1";
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
        BusTimesDisplayAdapter busTimesDisplayAdapter = new BusTimesDisplayAdapter(getContext(), this);
        if (item != null) {
            TextView busNumber = (TextView) view.findViewById(R.id.bus_number);
            TextView busStopNumber = (TextView) view.findViewById(R.id.bus_stop_number);
            TextView destination = (TextView) view.findViewById(R.id.destination);

            destination.setText(item.getBusStopDestination());
            busNumber.setText(Integer.toString(item.getBusNumber()));
            busStopNumber.setText(item.getBusStopNumber());

            ListView listView = (ListView) view.findViewById(R.id.bus_times_list);
            listView.setAdapter(busTimesDisplayAdapter);
            busTimesDisplayAdapter.addAll(item.getTimes());

        }
    }


    @Override
    public void setReminder(String minutes) {
        int minutesFromNow;
        try {
            minutesFromNow = Integer.parseInt(minutes);
        } catch (Exception e) {
            minutesFromNow = 0;
        }
        ReminderService.setReminder(getContext(), minutesFromNow, item.getBusNumber());


    }
}
