package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.graphics.Color;
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
 * DisplayAdapter Class
 * Use as adapter between listView and Presentation
 * Usage:
 * displayAdapter.clear(); //to clean the previous list
 * displayAdapter.addAll(transitListItemObjects); //to add the new list
 * displayAdapter.notifyDataSetChanged(); //to notify data has been changed*
 *
 * @author Paul
 * @version 1.0
 * @since 2017-05-31
 * Created by Paul on 2017-05-31.
 */

public class DisplayAdapter extends ArrayAdapter<TransitListItem> {

    private ArrayList<TransitListItem> listViewRows;
    private Context context;
    private String minutes = " min";


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
            resultView = viewInflater.inflate(comp3350.WinnipegTransitGo.R.layout.listview_row, parent, false);
        }

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
                busStatus.setTextColor(setBusStatusColor(currDisplay));
            }
            if (timeToNextArrival1 != null) {
                if (currDisplay.getTimes().get(0).equals("Due")) {
                    timeToNextArrival1.setText(currDisplay.getTimes().get(0));
                } else {
                    timeToNextArrival1.setText(currDisplay.getTimes().get(0) + minutes);
                }
            }
            if (timeToNextArrival2 != null && currDisplay.getTimes().size() >= 2) {
                timeToNextArrival2.setText(currDisplay.getTimes().get(1) + minutes);
            }
            if (timeToNextArrival3 != null && currDisplay.getTimes().size() >= 3) {
                timeToNextArrival3.setText(currDisplay.getTimes().get(2) + minutes);
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
    }

    @Override
    public void addAll(@NonNull Collection<? extends TransitListItem> collection) {
        listViewRows = (ArrayList<TransitListItem>) collection;
        super.addAll(collection);
    }

    public int setBusStatusColor(TransitListItem transitItem) {
        int color = 0;
        String busStatus = transitItem.getBusStatus();
        int c = Color.parseColor(BusStatus.getStatusColor(busStatus));
        return c;
    }

}

class BusStatus {
    public static final String LATE = "Late", EARLY = "Early",
            OK = "Ok", DUE = "Due";

    public static final String getStatusColor(String status) {
        String color = "";
        switch (status) {
            case LATE:
                color = BusStatusColor.LATE;
                break;
            case EARLY:
                color = BusStatusColor.EARLY;
                break;
            case OK:
                color = BusStatusColor.OK;
                break;
            case DUE:
                color = BusStatusColor.DUE;
                break;
            default:
                color = BusStatusColor.OK;
                break;
        }
        return color;
    }
}

class BusStatusColor {
    public static final String OK = ("#33ff66"), EARLY = ("#3399cc"), LATE = ("#ff0000"), DUE = ("#cc6633");
}
