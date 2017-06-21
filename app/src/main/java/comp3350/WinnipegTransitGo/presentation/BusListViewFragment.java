package comp3350.WinnipegTransitGo.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * BusListViewFragment
 * Fragment class holding list view that displays
 * timing and location information for upcoming buses
 *
 * @author Abdul-Rasheed
 */
public class BusListViewFragment extends Fragment {

    private DisplayAdapter displayAdapter;
    ListView mainListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        displayAdapter = new DisplayAdapter(getContext(), R.layout.listview_row);
        return inflater.inflate(R.layout.bus_list_view_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainListView = (ListView) view.findViewById(R.id.bus_list);
        mainListView.setAdapter(displayAdapter);
    }

    public void updateListView(List<TransitListItem> displayObjects) {
        clearListView();
        this.displayAdapter.addAll(displayObjects);
        this.displayAdapter.notifyDataSetChanged();
    }

    public void clearListView() {
        this.displayAdapter.clear();
    }

    public boolean isViewAtTop() {
        return mainListView.getFirstVisiblePosition() == 0;
    }
}
