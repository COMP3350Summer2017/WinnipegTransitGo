package comp3350.WinnipegTransitGo.presentation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * BusInfoDisplayAdapter Class
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

class BusInfoDisplayAdapter extends ArrayAdapter<TransitListItem> {


    BusInfoDisplayAdapter(Context context) {
        super(context, R.layout.listview_row);
    }

    private BusListViewHolder getNewHolder() {
        return new BusListViewHolder();
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


}

