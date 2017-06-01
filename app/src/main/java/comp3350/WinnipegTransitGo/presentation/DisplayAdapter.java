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
import java.util.List;

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
            TextView topText = (TextView) resultView.findViewById(comp3350.WinnipegTransitGo.R.id.toptext);
            TextView bottomText = (TextView) resultView.findViewById(comp3350.WinnipegTransitGo.R.id.bottomtext);
            if (topText != null) {
                topText.setText("Bus stop name: " + currDisplay.getBusStopName());
            }
            if (bottomText != null) {
                bottomText.setText("Bus stop number: " + currDisplay.getBusNumber());
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
