package comp3350.WinnipegTransitGo.objects;

/**
 * Time
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-05-22
 */
public class Time {
    private ScheduledEstimatedTimes arrival;
    private ScheduledEstimatedTimes departure;

    public Time(ScheduledEstimatedTimes arrival, ScheduledEstimatedTimes departure)
    {
        this.arrival = arrival;
        this.departure = departure;
    }

    //region public getters
    public String getScheduledArrival() {
        return arrival.scheduled;
    }

    public String getEstimatedArrival() {
        return arrival.estimated;
    }

    public String getScheduledDeparture() {
        return departure.scheduled;
    }

    public String getEstimatedDeparture() {
        return departure.estimated;
    }
    //endregion

    public static class ScheduledEstimatedTimes {
        private String scheduled;
        private String estimated;
        public ScheduledEstimatedTimes( String scheduled, String estimated)
        {
            this.scheduled = scheduled;
            this.estimated = estimated;
        }
    }
}
