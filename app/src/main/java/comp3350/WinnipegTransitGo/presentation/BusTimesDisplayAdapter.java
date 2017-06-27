package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;

/**
 * BusTimesDisplayAdapter
 * <p>
 * Adapter for a ListView that shows bus times and
 * an option to set a reminder.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/6/2017
 */

class BusTimesDisplayAdapter extends ArrayAdapter<String> {

    private OnReminderButtonClick onReminderButtonClick;

    BusTimesDisplayAdapter(@NonNull Context context, OnReminderButtonClick onClickListener) {
        super(context, R.layout.bus_time_row);
        this.onReminderButtonClick = onClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if (resultView == null) {
            LayoutInflater viewInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView = viewInflater.inflate(R.layout.bus_time_row, parent, false);
        }
        final String bus_time_string = getItem(position);
        if (bus_time_string != null) {
            TextView bus_time = (TextView) resultView.findViewById(R.id.bus_time);
            String displayString = bus_time_string;
            if (!bus_time_string.equals("Due")) {
                displayString = getContext().getString(R.string.minutes, bus_time_string);
            }
            bus_time.setText(displayString);
            bus_time.setTextColor(BusStatus.getColorForStatus(bus_time_string));


            Button reminderButton = (Button) resultView.findViewById(R.id.reminder_button);
            reminderButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onReminderButtonClick.setReminder(bus_time_string);
                }
            });
        }
        return resultView;
    }
}
