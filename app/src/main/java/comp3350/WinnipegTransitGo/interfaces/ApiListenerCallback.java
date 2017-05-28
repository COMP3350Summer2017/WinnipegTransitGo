package comp3350.WinnipegTransitGo.interfaces;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.Display;

/**
 * Created by habib on 2017-05-28.
 */

public interface ApiListenerCallback {
    public void updateListView(List<Display> displayObjects);
}
