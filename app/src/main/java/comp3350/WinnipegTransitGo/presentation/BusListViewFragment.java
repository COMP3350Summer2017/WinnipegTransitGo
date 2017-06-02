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
 * Created by rasheinstein on 2017-06-02.
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
//        //test start
//        ArrayList<Display> testDisplayObjects = new ArrayList<>();
//        Display d1 = new Display(1, 555, "badBusStop", "north end", "1 min left", "on time", new ArrayList<String>(Arrays.asList("5 minutes", "10 minutes")));
//        Display d2 = new Display(3, 666, "goodBusStop", "U of M", "3 min left", "late", new ArrayList<String>(Arrays.asList("10 minutes", "30 minutes")));
//        Display d3 = new Display(9, 777, "BestBusStop", "Vancouver", "99999 min left", "some day", new ArrayList<String>(Arrays.asList("9999 minutes", "9999 minutes")));
//        testDisplayObjects.add(d1);
//        testDisplayObjects.add(d2);
//        testDisplayObjects.add(d3);
//        this.displayAdapter.clear();
//        this.displayAdapter.addAll(testDisplayObjects);
//        this.displayAdapter.notifyDataSetChanged();

//        Log.i("DisplayObject", "updateListView with test data: size" + testDisplayObjects.size());
        //test end

        // TODO: 2017-06-02 uncomment the below text and use it when everything else is ready
        this.displayAdapter.clear();
        this.displayAdapter.addAll(displayObjects);
        this.displayAdapter.notifyDataSetChanged();
//
//        Log.i("DisplayObject", "updateListView: size" + displayObjects.size());
    }
}
