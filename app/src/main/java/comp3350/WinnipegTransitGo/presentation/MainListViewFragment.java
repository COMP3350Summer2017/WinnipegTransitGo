package comp3350.WinnipegTransitGo.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * MainListViewFragment
 * Fragment class holding list view that displays
 * timing and location information for upcoming buses
 *
 * @author Abdul-Rasheed
 * @version 1.0
 * @since 23-06-2017
 */

public class MainListViewFragment extends Fragment implements AdapterView.OnItemClickListener {

    private BusInfoDisplayAdapter busInfoDisplayAdapter;
    ListView mainListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        busInfoDisplayAdapter = new BusInfoDisplayAdapter(getContext());
        return inflater.inflate(R.layout.bus_list_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainListView = (ListView) view.findViewById(R.id.bus_list);
        mainListView.setAdapter(busInfoDisplayAdapter);
        mainListView.setOnItemClickListener(this);
        TextView emptyText = (TextView) view.findViewById(android.R.id.empty);
        mainListView.setEmptyView(emptyText);
    }

    public void updateListView(List<TransitListItem> displayObjects) {
        clearListView();
        this.busInfoDisplayAdapter.addAll(displayObjects);
        this.busInfoDisplayAdapter.notifyDataSetChanged();
    }

    public void clearListView() {
        this.busInfoDisplayAdapter.clear();
    }

    /**
     * Checks if the first visible item is the first item in the list.
     *
     * @return True or false
     */
    public boolean isViewAtTop() {
        return mainListView.getFirstVisiblePosition() == 0;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransitListItem item = busInfoDisplayAdapter.getItem(position);
        if (item != null) {
            new OptionsMenu().showBusInfo((MainActivity) getActivity(), item);
        }
    }

}
