package comp3350.WinnipegTransitGo.tests.businessLogic;

import junit.framework.TestCase;

import comp3350.WinnipegTransitGo.businessLogic.ReminderTimeProcessing;
import comp3350.WinnipegTransitGo.objects.ReminderParams;

/**
 * Test Reminder Time Processing Methods
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 27/06/2017
 */

public class ReminderTimeProcessingTest extends TestCase {


    public void testGetReminderDetailsForDepartureTime() throws Exception {

        ReminderTimeProcessing processing = ReminderTimeProcessing.getInstance();
        final long MILLIS_PER_MINUTE = 60000;
        final long FIVE_SECOND_MILLIS = 5000;

        ReminderParams p = processing.getReminderDetailsForDepartureTime(0, "Due");
        assertTrue(p != null);
        assertTrue(p.minutesToDeparture == 0);
        assertTrue(p.reminderTimeMillis == FIVE_SECOND_MILLIS);

        p = processing.getReminderDetailsForDepartureTime(300, "56");
        assertTrue(p != null);
        assertTrue(p.minutesToDeparture == 5);
        assertTrue(p.reminderTimeMillis == 51*MILLIS_PER_MINUTE + FIVE_SECOND_MILLIS + 300);

        p = processing.getReminderDetailsForDepartureTime(20, "gibberish i love you");
        assertTrue(p != null);
        assertTrue(p.minutesToDeparture == 0);
        assertTrue(p.reminderTimeMillis == 20 + FIVE_SECOND_MILLIS);
    }
}
