package comp3350.WinnipegTransitGo.presentation;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * View class representing bus list view row.
 * Stores the instance of the TransitListItem used
 * to populate the contents
 *
 * @author Paul
 * @version 1.0
 * @since 23-06-2017
 */

class BusListViewHolder {
    private TransitListItem contents;

    BusListViewHolder() {
        contents = null;
    }


    @SuppressLint("SetTextI18n")
    void populate(@NonNull View view, @NonNull TransitListItem currDisplay) {
        contents = currDisplay;
        TextView distance = (TextView) view.findViewById(R.id.distance);
        TextView busNumber = (TextView) view.findViewById(R.id.bus_number);
        TextView busStopName = (TextView) view.findViewById(R.id.bus_stop_name);
        TextView busStopNumber = (TextView) view.findViewById(R.id.bus_stop_number);

        TextView destination = (TextView) view.findViewById(R.id.destination);
        TextView busStatus = (TextView) view.findViewById(R.id.bus_status);

        TextView timeToNextArrival1 = (TextView) view.findViewById(R.id.first_bus_arrival);
        TextView timeToNextArrival2 = (TextView) view.findViewById(R.id.second_bus_arrival);
        TextView timeToNextArrival3 = (TextView) view.findViewById(R.id.third_bus_arrival);

        distance.setText(contents.getBusStopDistance());
        destination.setText(contents.getBusStopDestination());
        busNumber.setText(Integer.toString(contents.getBusNumber()));
        busStatus.setText(contents.getBusStatus());
        busStatus.setTextColor(BusStatus.getColorForStatus(contents.getBusStatus()));
        int times = contents.getTimes().size();
        if (times > 0) {
            timeToNextArrival1.setText(contents.getTimes().get(0));
        }
        if (times > 1) {
            timeToNextArrival2.setText(contents.getTimes().get(1));
        }
        if (times > 2) {
            timeToNextArrival3.setText(contents.getTimes().get(2));
        }
        busStopName.setText(contents.getBusStopName());
        final OnBusStopClickListener clickListener = MapManager.getBusStopClickListener();
        busStopName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.showLocationForBus(contents.getBusStopNumber());
            }
        });
        busStopNumber.setText(contents.getBusStopNumber());
    }
}
