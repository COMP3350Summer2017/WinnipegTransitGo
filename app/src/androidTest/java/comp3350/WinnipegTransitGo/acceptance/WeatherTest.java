package comp3350.WinnipegTransitGo.acceptance;

import com.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.TextView;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.businessLogic.WeatherProvider;
import comp3350.WinnipegTransitGo.objects.WeatherCondition;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPICallback;
import comp3350.WinnipegTransitGo.presentation.MainActivity;
import comp3350.WinnipegTransitGo.presentation.WeatherPresenter;

/**
 * Created by Unknown on 2017-07-02.
 */

public class WeatherTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private Solo solo;

    public WeatherTest() {
        super(MainActivity.class);
    }

    public void setUp() throws  Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testNormalWeatherShowing() {
        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady("16");
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(WeatherCondition.broken_clouds);
            }
        });

        // check that we have the right activity
        solo.assertCurrentActivity("wrong activity", MainActivity.class);

        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertTrue(solo.waitForText("16 C°"));
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(weatherIV.getTag().equals(WeatherCondition.broken_clouds.getWeatherCode()));
    }
}
