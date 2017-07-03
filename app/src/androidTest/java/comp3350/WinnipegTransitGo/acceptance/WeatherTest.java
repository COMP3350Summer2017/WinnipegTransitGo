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
 * WeatherTest
 * Weather/Temperature acceptance tests
 * Testing original API provider (not faked)
 * Not much can be done with the original provider (weather is random)
 * so most of the tests here are using stubs.
 * interaction is short and only involves clicking on the weather to refresh it
 * most of the validation is of correct temperature and weather
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-07-02
 */

public class WeatherTest extends ActivityInstrumentationTestCase2<MainActivity> {
    private static final String TEMP_ERROR = "incorrect temperature showing";
    private static final String WEATHER_ERROR = "incorrect weather showing";
    private static final String ACTIVITY_ERROR = "wrong activity";
    private static final String REFRESH_ERROR = ":refresh not working";

    private Solo solo;
    private WeatherProvider normalWP;
    private WeatherProvider rainWP;
    private WeatherProvider nullWP;
    private WeatherProvider edgeCaseColdWP;
    private WeatherProvider edgeCaseHotWP;

    public WeatherTest() {
        super(MainActivity.class);
    }

    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
        setupWeatherStubs();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    /**
     * Test the weather provider used (original, not fake)
     * not much can be done here because weather is random (unfortunately)
     */
    public void testUsedWeatherProvider() {
        // check that we have the right activity
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertFalse(TEMP_ERROR, tempTV.getText().toString().isEmpty());
        assertNotNull(TEMP_ERROR, tempTV.getText());
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertNotNull(WEATHER_ERROR, weatherIV.getTag());
    }

    /**
     * Testing if normal weather is showing on the screen
     * normal weather: 16 degrees clear sky (not something crazy)
     */
    public void testNormalWeatherShowing() {
        // check that we have the right activity
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(normalWP);
        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertTrue(TEMP_ERROR, solo.waitForText("16 C°"));
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(WEATHER_ERROR, weatherIV.getTag().equals(WeatherCondition.broken_clouds.getWeatherCode()));
    }

    /**
     * Testing the refresh feature of the weather
     * making sure weather changes...
     */
    public void testWeatherRefresh() {
        // check that we have the right activity
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(normalWP);

        // injecting a different weather API dependency
        WeatherPresenter.setWeatherProvider(rainWP);

        // refreshing
        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);


        assertTrue(TEMP_ERROR + REFRESH_ERROR, solo.waitForText("10 C°"));
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(WEATHER_ERROR + REFRESH_ERROR,
                weatherIV.getTag().equals(WeatherCondition.shower_rain.getWeatherCode()));
    }

    /**
     * Testing if extreme weather is showing on the screen
     * extreme weather: 60/-60 degrees clear sky/snow
     * basically making sure big possible numbers are showing on screen
     */
    public void testExtremeWeatherShowing() {
        // check that we have the right activity
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(edgeCaseColdWP);

        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertTrue(TEMP_ERROR, solo.waitForText("-60 C°"));
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(WEATHER_ERROR, weatherIV.getTag().equals(WeatherCondition.snow.getWeatherCode()));

        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(edgeCaseHotWP);

        tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertTrue(TEMP_ERROR, solo.waitForText("60 C°"));
        weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(WEATHER_ERROR, weatherIV.getTag().equals(WeatherCondition.clear_sky_day.getWeatherCode()));
    }

    /**
     * making sure there are no crashes when invalid weather is received
     * keep showing previous weather when invalid weather is received
     */
    public void testInvalidWeather() {
        // check that we have the right activity
        solo.assertCurrentActivity(ACTIVITY_ERROR, MainActivity.class);
        solo.waitForActivity(MainActivity.class);

        // injecting test specific weather API dependency
        WeatherPresenter.setWeatherProvider(normalWP);
        TextView tempTV = (TextView) solo.getView(R.id.tempText);
        solo.clickOnView(tempTV);
        assertTrue(TEMP_ERROR, solo.waitForText("16 C°"));
        ImageView weatherIV = (ImageView) solo.getView(R.id.weatherImage);
        assertTrue(WEATHER_ERROR, weatherIV.getTag().equals(WeatherCondition.broken_clouds.getWeatherCode()));

        // try to inject invalid weather
        WeatherPresenter.setWeatherProvider(nullWP);
        solo.clickOnView(tempTV);
        assertTrue(TEMP_ERROR, solo.waitForText("16 C°"));
        assertTrue(WEATHER_ERROR, weatherIV.getTag().equals(WeatherCondition.broken_clouds.getWeatherCode()));
    }

    private void setupWeatherStubs() {
        normalWP = new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady("16");
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(WeatherCondition.broken_clouds);
            }
        };

        rainWP = new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady("10");
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(WeatherCondition.shower_rain);
            }
        };

        nullWP = new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady(null);
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(null);
            }
        };

        edgeCaseColdWP = new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady("-60");
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(WeatherCondition.snow);
            }
        };

        edgeCaseHotWP = new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady("60");
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(WeatherCondition.clear_sky_day);
            }
        };
    }
}
