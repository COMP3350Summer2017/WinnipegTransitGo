package comp3350.WinnipegTransitGo.presentation;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.location.BusLocationNotifier;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * View class representing bus list view row.
 * Stores the instance of the TransitListItem used
 * to populate the contents
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 23-06-2017
 */

class BusListViewHolder {
    private TransitListItem contents;
    private BusLocationNotifier locationNotifier;

    BusListViewHolder(BusLocationNotifier notifier) {
        contents = null;
        locationNotifier = notifier;
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


        if (distance != null) {
            distance.setText(contents.getBusStopDistance());
        }
        if (destination != null) {
            destination.setText(contents.getBusStopDestination());
        }
        if (busNumber != null) {
            busNumber.setText(Integer.toString(contents.getBusNumber()));
        }
        if (busStatus != null) {
            busStatus.setText(contents.getBusStatus());
        }
        if (timeToNextArrival1 != null) {
            timeToNextArrival1.setText(contents.getTimes().get(0));
        }
        if (timeToNextArrival2 != null && contents.getTimes().size() >= 2) {
            timeToNextArrival2.setText(contents.getTimes().get(1));
        }
        if (timeToNextArrival3 != null && contents.getTimes().size() >= 3) {
            timeToNextArrival3.setText(contents.getTimes().get(2));
        }
        if (busStopName != null) {
            busStopName.setText(contents.getBusStopName());
            busStopName.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationNotifier.showLocationForBus(contents);
                }
            });
        }
        if (busStopNumber != null) {
            busStopNumber.setText(contents.getBusStopNumber());
        }
    }
}
