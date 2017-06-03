package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Created by Paul on 2017-05-31.
 */

public class DisplayAdapter extends ArrayAdapter<TransitListItem> {

    private ArrayList<TransitListItem> listViewRows;
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

        // TODO: 2017-05-31 add multiple rows and columns to show all the TransitListItem information
        TransitListItem currDisplay = listViewRows.get(position);
        if (currDisplay != null) {
            TextView distance = (TextView) resultView.findViewById(R.id.distance);
            TextView busNumber = (TextView) resultView.findViewById(R.id.bus_number);
            TextView busStopName = (TextView) resultView.findViewById(R.id.bus_stop_name);
            TextView busStopNumber = (TextView) resultView.findViewById(R.id.bus_stop_number);

            TextView destination = (TextView) resultView.findViewById(R.id.destination);
            TextView busStatus = (TextView) resultView.findViewById(R.id.bus_status);

            TextView timeToNextArrival1 = (TextView) resultView.findViewById(R.id.first_bus_arrival);
            TextView timeToNextArrival2 = (TextView) resultView.findViewById(R.id.second_bus_arrival);
            TextView timeToNextArrival3 = (TextView) resultView.findViewById(R.id.third_bus_arrival);


//            TextView laterArrivalTime = (TextView) resultView.findViewById(R.id.later_arrival_time);
            if (distance != null) {
                distance.setText(currDisplay.getBusStopDistance());
            }
            if (destination != null) {
                destination.setText(currDisplay.getBusStopDestination());
            }
            if (busNumber != null) {
                busNumber.setText(Integer.toString(currDisplay.getBusNumber()));
            }
            if (busStatus != null) {
                busStatus.setText(currDisplay.getBusStatus());
            }
            if (timeToNextArrival1 != null) {
                timeToNextArrival1.setText(currDisplay.getTimes().get(0));
            }
            if (timeToNextArrival2 != null && currDisplay.getTimes().size() >= 2) {
                timeToNextArrival2.setText(currDisplay.getTimes().get(1));
            }
            if (timeToNextArrival3 != null && currDisplay.getTimes().size() >= 3) {
                timeToNextArrival3.setText(currDisplay.getTimes().get(2));
            }
            if (busStopName != null) {
                busStopName.setText(currDisplay.getBusStopName());
            }
            if (busStopNumber != null) {
                busStopNumber.setText(currDisplay.getBusStopNumber());
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
    public void addAll(@NonNull Collection<? extends TransitListItem> collection) {

        listViewRows = (ArrayList<TransitListItem>) collection;// TODO: 2017-05-31 add some validity checks for colleciton
        super.addAll(collection);
    }
}
