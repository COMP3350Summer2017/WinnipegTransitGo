package comp3350.WinnipegTransitGo.interfaces;

import java.util.List;

import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * Layer between presentation and business logic
 * Use to send the async call response to GUI(MainActivity)
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-05-28
 */

public interface ApiListenerCallback {
    public void updateListView(List<TransitListItem> transitListItems);
}
