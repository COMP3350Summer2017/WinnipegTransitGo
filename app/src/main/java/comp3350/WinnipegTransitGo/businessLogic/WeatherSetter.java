package comp3350.WinnipegTransitGo.businessLogic;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * WeatherSetter
 *  interface for setting weather UI components
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public interface WeatherSetter {

    // set the text view to show the current temperature
    void setTemperature(TextView textView);

    // set the image view to show the current weather condition
    void setWeatherImage(ImageView imageView);
}
