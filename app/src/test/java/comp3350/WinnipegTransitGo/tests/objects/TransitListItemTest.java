package comp3350.WinnipegTransitGo.tests.objects;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

import comp3350.WinnipegTransitGo.objects.TransitListItem;

/**
 * TransitListItemTest class
 * Test's compare method in TransitListItem (which is used for sorting)
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-05
 */

public class TransitListItemTest  extends TestCase
{

    public void testCompare()
    {
        TransitListItem item1;
        TransitListItem item2;

        List<String> time1 = new ArrayList<>();
        time1.add("Due");//means 0
        time1.add("4");

        List<String> time2 = new ArrayList<>();
        time2.add("5");
        time2.add("7");

        item1 = new TransitListItem("0", 0, 0, null, null, null, time1, false, false);
        item2 = new TransitListItem("0", 0, 0, null, null, null, time2, false, false);


        //compare to should return according to the first time element
        assertTrue(item1.compare(item1,item1) == ((Integer) 0).compareTo(0)  );
        assertTrue(item1.compare(item1,item2) == ((Integer) 0).compareTo(5)  );
        assertTrue(item1.compare(item2,item1) == ((Integer) 5).compareTo(0)  );
    }

}
