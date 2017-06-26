package comp3350.WinnipegTransitGo.tests.businessLogic;

import junit.framework.TestCase;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import comp3350.WinnipegTransitGo.businessLogic.TransitListGenerator;
import comp3350.WinnipegTransitGo.objects.BusRoute;
import comp3350.WinnipegTransitGo.objects.BusRouteSchedule;
import comp3350.WinnipegTransitGo.objects.BusStop;
import comp3350.WinnipegTransitGo.objects.BusStopSchedule;
import comp3350.WinnipegTransitGo.objects.BusVariant;
import comp3350.WinnipegTransitGo.objects.ScheduledStop;
import comp3350.WinnipegTransitGo.objects.Time;
import comp3350.WinnipegTransitGo.objects.TransitListItem;
import comp3350.WinnipegTransitGo.businessLogic.DatabaseService;


/**
 * TransitListGeneratorTest class
 * Test's methods which process api responses in TransitListGenerator class
 *
 *
 * @author Syed Habib
 * @version 1.0
 * @since 2017-06-04
 */

public class TransitListGeneratorTest  extends TestCase
{

    @Override
    public void tearDown()
    {
        DatabaseService.closeDataAccess();
    }

    public void testProcessResponseBusStopSchedule() throws Exception
    {
        //initializing variables
        int busStopNumber = 123123;
        String busStopName = "Some Stop";
        String walkingDistance = "2";

        int busNumber = 100;
        String busName = "TO UOFM";

        BusVariant variant = new BusVariant("",busName);
        List<BusVariant> variants = new ArrayList<>();
        variants.add(variant);

        BusRoute route = new BusRoute(0, busNumber, busName, "normal", variants);
        List<ScheduledStop> scheduledStops = new ArrayList<>();

        //----------------------------------
        //initializing first bus

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentTime = c.getTime();
        final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

        long currentTimeInMillis= c.getTimeInMillis();
        Date currentTimePlus5Minutes=new Date(currentTimeInMillis + (5 * ONE_MINUTE_IN_MILLIS));
        String estimatedTime = sdf.format(currentTimePlus5Minutes);

        String remainingTime = "4";

        Time.ScheduledEstimatedTimes departureTimings = new Time.ScheduledEstimatedTimes(estimatedTime, estimatedTime);
        Time time = new Time(departureTimings, departureTimings);
        scheduledStops.add( new ScheduledStop("0",variant,time));

        BusRouteSchedule routeSchedule = new BusRouteSchedule(route, scheduledStops);
        List<BusRouteSchedule> routeSchedules = new ArrayList<>();
        routeSchedules.add(routeSchedule);


        //-------------------------------------------
        //initializing second bus

        String schedulesTime = "2017-06-05T2:30:00";//        "yyyy-MM-dd'T'HH:mm:ss"  example: 2017-06-05T16:00:07
        estimatedTime = "2017-06-05T2:32:00";// two minute late
        remainingTime = "Due";

        departureTimings = new Time.ScheduledEstimatedTimes(schedulesTime, estimatedTime);
        time = new Time(departureTimings, departureTimings);
        scheduledStops = new ArrayList<>();
        scheduledStops.add( new ScheduledStop("0",variant,time));

        //different route for different buses
        busNumber = 160;
        route = new BusRoute(0, busNumber, busName, "normal", variants);

        routeSchedule = new BusRouteSchedule(route, scheduledStops);
        routeSchedules.add(routeSchedule);

        List<String> expectedRemainingTime = new ArrayList<>();
        expectedRemainingTime.add("Due");
        expectedRemainingTime.add("4");


        BusStopSchedule stopSchedule = new BusStopSchedule(new BusStop(), routeSchedules);
        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);


        //-----------------------------------------------

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("processResponseBusStopSchedule", BusStopSchedule.class, int.class, String.class, String.class);
        method.setAccessible(true);
        method.invoke(transitListGenerator, stopSchedule, busStopNumber, busStopName, walkingDistance);

        //get value of the added object
        Field field = transitListGenerator.getClass().getDeclaredField("listItems");
        field.setAccessible(true);
        List<TransitListItem> output = (List<TransitListItem>) field.get(transitListGenerator);

        //-----------------------------------------------

        //test if the output is as expected
        assertTrue(output.size() == routeSchedules.size());

        TransitListItem item = output.get(0);

        assertTrue (item.getBusNumber() == 160);
        assertTrue (item.getBusStopName().equals(busStopName));
        assertTrue (item.getBusStopNumber().equals("#" + busStopNumber));
        assertTrue (item.getBusStopDestination().equals(busName));
        assertTrue (item.getTimes().size() == scheduledStops.size());
        assertTrue (item.getTimes().get(0).equals(expectedRemainingTime.get(0)));//tests if the items are sorted by expectedRemainingTime
        assertTrue (item.getBusStopDistance().equals("Dist: "+walkingDistance + " mtr"));

        item = output.get(1);

        assertTrue (item.getBusNumber() == 100);
        assertTrue (item.getBusStopName().equals(busStopName));
        assertTrue (item.getBusStopNumber().equals("#" + busStopNumber));
        assertTrue (item.getBusStopDestination().equals(busName));
        assertTrue (item.getTimes().size() == scheduledStops.size());
        assertTrue (item.getTimes().get(0).equals(expectedRemainingTime.get(1)));//tests if the items are sorted by expectedRemainingTime
        assertTrue (item.getBusStopDistance().equals("Dist: "+walkingDistance + " mtr"));

    }

    public void testCalculateStatusLate() throws Exception {
        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);

        String schedulesTime = "2017-06-05T2:30:00";//        "yyyy-MM-dd'T'HH:mm:ss"  example: 2017-06-05T16:00:07
        String estimatedTime = "2017-06-05T2:32:00";// two minute late
        String busStatus = "Late";//according to the estimated and schedules time, bus status should be late

        Time.ScheduledEstimatedTimes departureTimings = new Time.ScheduledEstimatedTimes(schedulesTime, estimatedTime);
        Time time = new Time(departureTimings, departureTimings);

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("calculateStatus", ScheduledStop.class);
        method.setAccessible(true);
        String outputStatus = (String) method.invoke(transitListGenerator, new ScheduledStop("0", null, time));

        assertTrue (outputStatus.equals(busStatus));

    }

    public void testCalculateStatusEarly() throws Exception {
        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);

        String schedulesTime = "2017-06-05T2:30:00";//        "yyyy-MM-dd'T'HH:mm:ss"  example: 2017-06-05T16:00:07
        String estimatedTime = "2017-06-05T2:28:00";// two minute early
        String busStatus = "Early";//according to the estimated and schedules time, bus status should be late

        Time.ScheduledEstimatedTimes departureTimings = new Time.ScheduledEstimatedTimes(schedulesTime, estimatedTime);
        Time time = new Time(departureTimings, departureTimings);

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("calculateStatus", ScheduledStop.class);
        method.setAccessible(true);
        String outputStatus = (String) method.invoke(transitListGenerator, new ScheduledStop("0", null, time));

        assertTrue (outputStatus.equals(busStatus));
    }

    public void testCalculateStatusOnTime() throws Exception {
        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);

        String schedulesTime = "2017-06-05T2:30:00";//        "yyyy-MM-dd'T'HH:mm:ss"  example: 2017-06-05T16:00:07
        String estimatedTime = "2017-06-05T2:30:00";// on time
        String busStatus = "Ok";//according to the estimated and schedules time, bus status should be late

        Time.ScheduledEstimatedTimes departureTimings = new Time.ScheduledEstimatedTimes(schedulesTime, estimatedTime);
        Time time = new Time(departureTimings, departureTimings);

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("calculateStatus", ScheduledStop.class);
        method.setAccessible(true);
        String outputStatus = (String) method.invoke(transitListGenerator, new ScheduledStop("0",null,time));

        assertTrue (outputStatus.equals(busStatus));
    }

    public void testParseTime() throws Exception {
        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);
        List<String> expectedTimings = new ArrayList<String>();
        List<ScheduledStop> scheduledStops = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Calendar c = Calendar.getInstance();
        Date currentTime = c.getTime();
        final long ONE_MINUTE_IN_MILLIS=60000;//millisecs

        String estimatedTime = sdf.format(currentTime);
        expectedTimings.add("Due");//currentTime - currentTime = 0 = due (remainingTime)
        Time.ScheduledEstimatedTimes departureTimings = new Time.ScheduledEstimatedTimes(estimatedTime, estimatedTime);
        Time time = new Time(departureTimings, departureTimings);
        scheduledStops.add(new ScheduledStop("0",null,time));

        //-------------------------------------------

        long currentTimeInMillis= c.getTimeInMillis();
        Date currentTimePlus5Minutes=new Date(currentTimeInMillis + (5 * ONE_MINUTE_IN_MILLIS));
        estimatedTime = sdf.format(currentTimePlus5Minutes);
        expectedTimings.add("4");//currentTime - currentTime = 0 = due (remainingTime) (should be 4 not 5 because takes time to get there)


        departureTimings = new Time.ScheduledEstimatedTimes(estimatedTime, estimatedTime);
        time = new Time(departureTimings, departureTimings);

        scheduledStops.add(new ScheduledStop("0",null,time));

        //---------------------------------------------------------

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("parseTime", List.class);
        method.setAccessible(true);
        List<String> outputTimings = (List<String>) method.invoke(transitListGenerator, scheduledStops);

        assertTrue(outputTimings.size()== (scheduledStops.size()));
        for(int i = 0; i<outputTimings.size(); i++)
            assertTrue(outputTimings.get(i).equals(expectedTimings.get(i)));

    }

    public void testInsertClosestBusForSameBuses() throws Exception
    {
        /*
            Input: Try to insert same bus with closer and further distance
            Expected output: The one with closer walking distance replaces existing one
         */

        TransitListItem itemInList;
        TransitListItem newItem;

        //getBusNumber()
        //getBusStopDestination()
        //getWalkingDistance()
        int busNumber = 60;
        String destination = "UofM";
        String walkingDistance1 = "30";
        String walkingDistance2 = "130";


        itemInList = new TransitListItem(walkingDistance1, busNumber, 0, null, destination, null, null);
        newItem = new TransitListItem(walkingDistance2, busNumber, 0, null, destination, null, null);


        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);
        List<TransitListItem> listItems = new ArrayList<>();
        listItems.add(itemInList);

        //setup the initial listItems
        Field field = transitListGenerator.getClass().getDeclaredField("listItems");
        field.setAccessible(true);
        field.set(transitListGenerator, listItems);

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("insertClosestBus", TransitListItem.class);
        method.setAccessible(true);
        method.invoke(transitListGenerator, newItem);

        //check if the insertion was successful
        List<TransitListItem> output = (List<TransitListItem>) field.get(transitListGenerator);

        //compare to should return according to the first time element
        assertTrue(output.size() == 1);
        assertTrue(output.get(0) == itemInList);//check if has the same object

        //***************************************************//

        //check by putting the further one so that it is replaced
        listItems = new ArrayList<>();
        listItems.add(newItem);

        //setup the initial listItems
        field.set(transitListGenerator, listItems);

        //set access to the private method (using reflection)
        method.invoke(transitListGenerator, itemInList);

        //check if the insertion was successful
        output = (List<TransitListItem>) field.get(transitListGenerator);

        assertTrue(output.size() == 1);
        assertTrue(output.get(0) == itemInList);//check if has the same object
    }

    public void testInsertClosestBusForDifferentBuses() throws Exception
    {
        /*
            Input: Try to insert a new bus
            Expected output: List should contain all the previous and current items
         */

        TransitListItem itemInList;
        TransitListItem newItem;

        //getBusNumber()
        //getBusStopDestination()
        //getWalkingDistance()
        int busNumber = 60;
        String destination = "UofM";
        String destination2 = "Downtown";
        String walkingDistance1 = "30";
        String walkingDistance2 = "130";


        itemInList = new TransitListItem(walkingDistance1, busNumber, 0, null, destination, null, null);
        newItem = new TransitListItem(walkingDistance2, busNumber, 0, null, destination2, null, null);


        TransitListGenerator transitListGenerator = new TransitListGenerator(null, null);
        List<TransitListItem> listItems = new ArrayList<>();
        listItems.add(itemInList);

        //setup the initial listItems
        Field field = transitListGenerator.getClass().getDeclaredField("listItems");
        field.setAccessible(true);
        field.set(transitListGenerator, listItems);

        //set access to the private method (using reflection)
        Method method = transitListGenerator.getClass().getDeclaredMethod("insertClosestBus", TransitListItem.class);
        method.setAccessible(true);
        method.invoke(transitListGenerator, newItem);

        //check if the insertion was successful
        List<TransitListItem> output = (List<TransitListItem>) field.get(transitListGenerator);

        assertTrue(output.size() == 2);
        assertTrue(output.contains(itemInList));
        assertTrue(output.contains(newItem));
    }
}

