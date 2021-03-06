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
    private static final long serialVersionUID = 1L;
    private int busNumber;
    private String busStopName;
    private int busStopNumber;
    private String destination;
    private String busStatus;
    private List<String> times;
    private int busStopDistance;
    private boolean hasBikeRack;
    private boolean hasEasyAccess;

    /**
     *
     * @param walkingDistance - Walking distance to bus stop
     * @param busNumber - Bus number
     * @param busStopNumber - Bus stop id/number
     * @param busStopName - Name of bus stop
     * @param destination - destination
     * @param status - status of stop
     * @param allTimes - Times for upcoming buses
     */
    private TransitListItem(String walkingDistance, int busNumber, int busStopNumber, String busStopName, String destination, String status, List<String> allTimes, boolean
            hasBikeRack, boolean hasEasyAccess) {
        this.busNumber = busNumber;
        this.busStopNumber = busStopNumber;
        this.busStopName = busStopName;
        this.destination = destination;
        busStatus = status;
        times = allTimes;
        busStopDistance = (int) Double.parseDouble(walkingDistance);
        this.hasBikeRack = hasBikeRack;
        this.hasEasyAccess = hasEasyAccess;
    }

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

    public int getBusStopNumber() {
        return busStopNumber;
    }

    public String getBusStopName() {
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

    public int getBusStopDistance() {
        return busStopDistance;
    }

    public boolean isBikeRackAvailable() { return hasBikeRack; }
    public boolean isEasyAccessAvailable() { return hasEasyAccess; }

    public static final class TransitListItemBuilder {
        private String walkingDistance = "0";
        private int busNumber;
        private int busStopNumber;
        private String busStopName;
        private String destination;
        private String status;
        private List<String> allTimes;
        private boolean hasBikeRack;
        private boolean hasEasyAccess;

        public TransitListItemBuilder setWalkingDistance(String walkingDistance) {
            this.walkingDistance = walkingDistance;
            return this;
        }

        public TransitListItemBuilder setBusNumber(int busNumber) {
            this.busNumber = busNumber;
            return this;
        }

        public TransitListItemBuilder setBusStopNumber(int busStopNumber) {
            this.busStopNumber = busStopNumber;
            return this;
        }

        public TransitListItemBuilder setBusStopName(String busStopName) {
            this.busStopName = busStopName;
            return this;
        }

        public TransitListItemBuilder setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public TransitListItemBuilder setStatus(String status) {
            this.status = status;
            return this;
        }

        public TransitListItemBuilder setAllTimes(List<String> allTimes) {
            this.allTimes = allTimes;
            return this;
        }

        public TransitListItemBuilder setHasBikeRack(boolean hasBikeRack) {
            this.hasBikeRack = hasBikeRack;
            return this;
        }

        public TransitListItemBuilder setHasEasyAccess(boolean hasEasyAccess) {
            this.hasEasyAccess = hasEasyAccess;
            return this;
        }

        public TransitListItem createTransitListItem() {
            return new TransitListItem(walkingDistance, busNumber, busStopNumber, busStopName, destination, status, allTimes, hasBikeRack, hasEasyAccess);
        }
    }
}
