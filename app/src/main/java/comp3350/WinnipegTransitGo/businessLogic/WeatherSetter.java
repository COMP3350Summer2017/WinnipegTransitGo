package comp3350.WinnipegTransitGo.businessLogic;

import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Unknown on 2017-06-11.
 */

public interface WeatherSetter {

    // set the text view to show the current temperature
    void setTemperature(TextView textView);

    // set the image view to show the current weather condition
    void setWeatherImage(ImageView imageView);
}
