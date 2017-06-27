package comp3350.WinnipegTransitGo.objects;

/**
 * ReminderParams
 *
 * Parameters for generating a bus departure reminder
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 27/06/2017
 */

public class ReminderParams {
    public long reminderTimeMillis; //notification fire time
    public int minutesToDeparture; //number of minutes from reminder firre until bus departs
}
