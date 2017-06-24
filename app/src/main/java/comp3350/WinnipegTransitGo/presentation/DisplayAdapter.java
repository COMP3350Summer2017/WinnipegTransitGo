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
import comp3350.WinnipegTransitGo.businessLogic.location.OnBusStopClickListener;
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

class DisplayAdapter extends ArrayAdapter<TransitListItem> {

    private OnBusStopClickListener mapFragment;
    private String minutes = " min";

    DisplayAdapter(Context context, OnBusStopClickListener mapFragment) {
        super(context, R.layout.listview_row);
        this.mapFragment = mapFragment;
    }

    private BusListViewHolder getNewHolder() {
        return new BusListViewHolder(mapFragment);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View resultView =  convertView;
        if (resultView == null) {
            LayoutInflater viewInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            resultView = viewInflater.inflate(R.layout.listview_row, parent, false);
            resultView.setTag(getNewHolder());
        }
        TransitListItem currDisplay = getItem(position);
        if (currDisplay != null) {
            ((BusListViewHolder)resultView.getTag()).populate(resultView, currDisplay);
        }
        return resultView;
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
