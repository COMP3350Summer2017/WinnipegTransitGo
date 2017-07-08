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
 * Not testing original API (API is faked)
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
        normalWP = createWeatherStub("16", WeatherCondition.broken_clouds);
        rainWP = createWeatherStub("10", WeatherCondition.shower_rain);
        nullWP = createWeatherStub(null , null);
        edgeCaseColdWP = createWeatherStub("-60" , WeatherCondition.snow);
        edgeCaseHotWP = createWeatherStub("60", WeatherCondition.clear_sky_day);
    }

    private WeatherProvider createWeatherStub(final String temp, final WeatherCondition weatherCond) {
        return new WeatherProvider() {
            @Override
            public void getTemperature(WeatherAPICallback callback) {
                callback.temperatureReady(temp);
            }

            @Override
            public void getWeatherCondition(WeatherAPICallback callback) {
                callback.weatherReady(weatherCond);
            }
        };
    }
}
