package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.BusStatus;

/**
 * BusTimesDisplayAdapter
 *
 * Adapter for a ListView that shows bus times and
 * an option to set a reminder.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/6/2017
 */

public class BusTimesDisplayAdapter extends ArrayAdapter<String> {

    public BusTimesDisplayAdapter(@NonNull Context context) {
        super(context, R.layout.bus_time_row);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View resultView = convertView;
        if (resultView == null) {
            LayoutInflater viewInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView = viewInflater.inflate(R.layout.bus_time_row, parent, false);
        }
        String bus_time_string = getItem(position);
        if (bus_time_string != null) {
            TextView bus_time = (TextView) resultView.findViewById(R.id.bus_time);
            bus_time.setText(bus_time_string);
            bus_time.setTextColor(BusStatus.getColorForStatus(bus_time_string));
        }
        return resultView;
    }
}
