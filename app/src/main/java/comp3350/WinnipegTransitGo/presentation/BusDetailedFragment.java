package comp3350.WinnipegTransitGo.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * BusDetailedFragment
 * SHows detailed information about a bus route,
 * with more bus times than regular and functionality
 * to set a reminder on each time
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 22-06-2017
 */

public class BusDetailedFragment extends Fragment {

    public static final String TRANSIT_ITEM = "1";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bus_detailed_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        TransitListItem item = (TransitListItem) args.getSerializable(BusDetailedFragment.TRANSIT_ITEM);
        if (item != null) {
            TextView busName = (TextView) view.findViewById(R.id.bus_name);
            busName.setText(item.getBusStopName());
        }
    }
}
