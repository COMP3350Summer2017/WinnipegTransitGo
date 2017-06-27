package comp3350.WinnipegTransitGo.businessLogic.reminders;

/**
 * OnReminderButtonClick
 *
 * Called when a user wants to add a reminder for
 * a bus arriving at a specified time.
 *
 * @author Abdul-Rasheed Audu
 * @version 1.0
 * @since 26/06/2017
 */

public interface OnReminderButtonClick {
    void setReminder(String minutes);
}
