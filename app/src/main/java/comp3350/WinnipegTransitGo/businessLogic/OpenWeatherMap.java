package comp3350.WinnipegTransitGo.businessLogic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;

import comp3350.WinnipegTransitGo.R;
import comp3350.WinnipegTransitGo.objects.Temperature;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPI;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPIProvider;
import comp3350.WinnipegTransitGo.persistence.weatherAPI.WeatherAPIResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * OpenWeatherMap Business Logic
 *  accesses Open Weather Map api to get
 *  weather and temperature information
 *
 * implements WeatherSetter
 *  sets weather and temperature UI elements
 *
 * @author Dima Mukhin
 * @version 1.0
 * @since 2017-06-11
 */

public class OpenWeatherMap implements WeatherSetter {
    private final String IMG_BASE_URL = "http://openweathermap.org/img/w/";
    private final String IMG_EXT = ".png";
    private WeatherAPIProvider weatherAPI;

    public OpenWeatherMap(String apiKey) {
        weatherAPI = WeatherAPI.getAPI(apiKey);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void setTemperature(final TextView textView) {
        Call<WeatherAPIResponse> weatherResponse = weatherAPI.getWeather();
        weatherResponse.enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                if(response.errorBody() != null)
                    return;

                if (response.body().getTemperature() != null)
                {
                    Temperature temp = response.body().getTemperature();
                    textView.setText(Integer.toString(temp.getTemperature()));
                    textView.append(" C°");
                }
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                Log.w(this.getClass().getName(),
                        Resources.getSystem().getString(R.string.Weather_Connection_Error));
            }
        });
    }

    @Override
    public void setWeatherImage(final ImageView imageView) {
        Call<WeatherAPIResponse> weatherResponse = weatherAPI.getWeather();
        weatherResponse.enqueue(new Callback<WeatherAPIResponse>() {
            @Override
            public void onResponse(Call<WeatherAPIResponse> call, Response<WeatherAPIResponse> response) {
                if(response.errorBody() != null)
                    return;

                if (response.body().getWeather() != null)
                {
                    String iconCode = response.body().getWeather().getIconCode();
                    String iconURL = IMG_BASE_URL + iconCode + IMG_EXT;
                    Drawable weatherIcon = LoadImageFromWebOperations(iconURL);

                    if (weatherIcon != null)
                        imageView.setImageDrawable(weatherIcon);
                }
            }

            @Override
            public void onFailure(Call<WeatherAPIResponse> call, Throwable t) {
                Log.w(this.getClass().getName(),
                        Resources.getSystem().getString(R.string.Weather_Connection_Error));
            }
        });
    }

    // Loads image from URL, returns null on error
    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
