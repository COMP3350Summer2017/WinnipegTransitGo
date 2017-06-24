package comp3350.WinnipegTransitGo.objects;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * TransitListItem class
 * Stores all the information about a bus
 * Usage:
 * getter methods
 *
 * @author Nibras Ohin, Syed Habib
 *         import java.util.List;
 *         <p>
 *         /**
 *         Uses by business logic to send all the info about a bus
 *         to gui.
 * @author Nibras
 * @version 1.0
 * @since 2017-05-24
 */

public class TransitListItem implements Comparator<TransitListItem>, Serializable {
    private int busNumber;
    private String busStopName;
    private String busStopNumber;
    private String destination;
    private String busStatus;
    private List<String> times;
    private String busStopDistance;
    private double walkingDistance;

    public TransitListItem(String walkingDistance, int busNumber, int busStopNumber, String busStopName, String destination, String status, List<String> allTimes) {
        this.busNumber = busNumber;
        this.busStopNumber = "#" + busStopNumber;
        this.busStopName = busStopName;
        this.destination = destination;
        busStatus = status;
        times = allTimes;
        busStopDistance = "Dist: "+walkingDistance + " mtr";
        this.walkingDistance = Double.parseDouble(walkingDistance);
    }

    public TransitListItem() {}

    @Override
    public int compare(TransitListItem firstItem, TransitListItem secondItem) {
        int firstValue;
        String firstString = firstItem.getTimes().get(0);//get the first bus
        if (firstString.equals("Due"))
            firstValue = 0;
        else
            firstValue = Integer.parseInt(firstString);

        int secondValue;
        String secondString = secondItem.getTimes().get(0);//get the first bus
        if (secondString.equals("Due"))
            secondValue = 0;
        else
            secondValue = Integer.parseInt(secondString);

        if (firstValue == secondValue)
            return 0;
        return firstValue < secondValue ? -1 : 1;
    }

    public String getBusStopNumber() {
        return busStopNumber;
    }

    public String getBusStopName() {
        int length = 10;
        if (busStopName.length() > length) {
            return this.busStopName;
        }

        return busStopName;
    }


    public int getBusNumber() {
        return busNumber;
    }

    public String getBusStopDestination() {
        return destination;
    }

    public String getBusStatus() {
        return busStatus;
    }

    public List<String> getTimes() {
        return times;
    }

    public String getBusStopDistance() {
        return busStopDistance;
    }

    public Double getWalkingDistance() {
        return walkingDistance;
    }
}
