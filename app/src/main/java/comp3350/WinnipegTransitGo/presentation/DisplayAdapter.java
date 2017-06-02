package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.Display;

/**
 * Created by Paul on 2017-05-31.
 */

public class DisplayAdapter extends ArrayAdapter<Display> {

    private ArrayList<Display> listViewRows;
    private Context context;

    public DisplayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.listViewRows = new ArrayList<>();
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View resultView = convertView;
        if (resultView == null) {
            LayoutInflater viewInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView = viewInflater.inflate(comp3350.WinnipegTransitGo.R.layout.listview_row, null);
        }

        // TODO: 2017-05-31 add multiple rows and columns to show all the Display information
        Display currDisplay = listViewRows.get(position);
        if (currDisplay != null) {
            TextView destination = (TextView) resultView.findViewById(R.id.destination);
            TextView busNumber = (TextView) resultView.findViewById(R.id.bus_number);
            TextView busStatus = (TextView) resultView.findViewById(R.id.bus_status);
            TextView timeToNextArrival = (TextView) resultView.findViewById(R.id.time_to_next_arrival);
            TextView busStopName = (TextView) resultView.findViewById(R.id.bus_stop_name);
            TextView busStopNumber = (TextView) resultView.findViewById(R.id.bus_stop_number);
            TextView laterArrivalTime = (TextView) resultView.findViewById(R.id.later_arrival_time);
            if (destination != null) {
                destination.setText(currDisplay.getBusStopDestination());
            }
            if (busNumber != null) {
                busNumber.setText(Integer.toString(currDisplay.getBusNumber()));
            }
            if (busStatus != null) {
                busStatus.setText(currDisplay.getBusStatus());
            }
            if (timeToNextArrival != null) {
                timeToNextArrival.setText(currDisplay.getBusTimeRemaining());
            }
            if (busStopName != null) {
                busStopName.setText(currDisplay.getBusStopName());
            }
            if (busStopNumber != null) {
                busStopNumber.setText(Integer.toString(currDisplay.getBusStopNumber()));
            }
            if (laterArrivalTime != null) {
                StringBuilder builder = new StringBuilder();
                List<String> futureArrivalTimes = currDisplay.getTimes();
                for (String time : futureArrivalTimes) {
                    builder.append(time + "\n");
                }
                laterArrivalTime.setText(builder.toString());
            }
        }
        return resultView;
    }

    @Override
    public void clear() {
        super.clear();
        listViewRows.clear();
    }

    @Override
    public void addAll(@NonNull Collection<? extends Display> collection) {
        listViewRows = (ArrayList<Display>) collection;// TODO: 2017-05-31 add some validity checks for colleciton
        super.addAll(collection);
    }
}
